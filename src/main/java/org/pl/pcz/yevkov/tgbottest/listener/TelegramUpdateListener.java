package org.pl.pcz.yevkov.tgbottest.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.tgbottest.bot.adapter.BotApiAdapter;
import org.pl.pcz.yevkov.tgbottest.application.command.dispatcher.CommandDispatcher;
import org.pl.pcz.yevkov.tgbottest.application.helper.UpdateHelper;
import org.pl.pcz.yevkov.tgbottest.dto.chat.ChatCreateDto;
import org.pl.pcz.yevkov.tgbottest.dto.chat.ChatReadDto;
import org.pl.pcz.yevkov.tgbottest.dto.event.*;
import org.pl.pcz.yevkov.tgbottest.dto.user.UserCreateDto;
import org.pl.pcz.yevkov.tgbottest.dto.userChat.UserChatCreateDto;
import org.pl.pcz.yevkov.tgbottest.dto.userChat.UserChatReadDto;
import org.pl.pcz.yevkov.tgbottest.dto.userChat.UserChatUpdateDto;
import org.pl.pcz.yevkov.tgbottest.entity.ChatStatus;
import org.pl.pcz.yevkov.tgbottest.entity.ChatType;
import org.pl.pcz.yevkov.tgbottest.entity.UserRole;
import org.pl.pcz.yevkov.tgbottest.event.ChatMemberJoinedEvent;
import org.pl.pcz.yevkov.tgbottest.event.ChatMemberLeftEvent;
import org.pl.pcz.yevkov.tgbottest.event.ChatMessageReceivedEvent;
import org.pl.pcz.yevkov.tgbottest.service.ChatService;
import org.pl.pcz.yevkov.tgbottest.service.UserChatService;
import org.pl.pcz.yevkov.tgbottest.service.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Log4j2
public class TelegramUpdateListener {
    private final CommandDispatcher dispatcher;
    private final BotApiAdapter telegramBot;
    private final ChatService chatService;
    private final UserService userService;
    private final UserChatService userChatService;
    private final UpdateHelper updateHelper;

    /**
     * Main listener for Telegram updates.
     * Determines whether the update is a command or message,
     * and delegates accordingly.
     */
    @EventListener
    public void onTelegramUpdate(@NonNull ChatMessageReceivedEvent event) {
        ChatMessageReceivedDto receivedMessage = event.member();
        String text = receivedMessage.text();
        boolean isCommand = isCommand(text);
        if (isCommand) {
            onTelegramCommand(receivedMessage);
        }
        if (!isCommand || !dispatcher.isCommandAllowed(text)) {
            onTelegramMessage(receivedMessage);
        }
    }


    /**
     * Triggered when the bot is added to a new group.
     * Registers the chat or updates its status to INACTIVE.
     */
    @EventListener
        public void onTelegramNewChat(ChatMemberJoinedEvent event) {
        ChatMemberJoinedDto chatMember = event.member();
        try {
            if (!isBotAddedToChat(chatMember)) return;
            ChatId chatId = chatMember.chatId();
            String chatName = chatMember.chatTitle();
            Optional<ChatReadDto> readDto = chatService.findChatById(chatId.value());
            if (readDto.isPresent()) {
                chatService.markChatAsStatus(chatId.value(), ChatStatus.INACTIVE);
                log.info("Chat Status Updated on INACTIVE: {}", readDto);
            } else {
                ChatCreateDto newChat = new ChatCreateDto(chatId.value(), chatName);
                chatService.createChat(newChat);
                log.info("New chat created: {}", newChat);
            }
        } catch (TelegramApiException e) {
            log.error("Unexpected error", e);
        }
    }


    /**
     * Triggered when the bot is removed from a group.
     * Marks the chat as DELETED in the system.
     */
    @EventListener
    public void onBotRemovedFromChat(ChatMemberLeftEvent event) {
        ChatMemberLeftDto memberLeftDto = event.member();
        try {
            Long botId = telegramBot.getBotId();
            if (botId.equals(memberLeftDto.userId().value())) {
                ChatId chatId = memberLeftDto.chatId();
                chatService.markChatAsStatus(chatId.value(), ChatStatus.DELETED);
                log.info("Bot was removed from chat {}", chatId);
            }
        } catch (TelegramApiException e) {
            log.error("Unexpected error", e);
        }
    }

    /**
     * Handles command messages (starting with '/').
     * Sends the response from the dispatcher if any.
     */
    private void onTelegramCommand(@NonNull ChatMessageReceivedDto receivedMessage) {
        Optional<SendMessage> message = dispatcher.handle(receivedMessage);
        if (message.isPresent()) {
            SendMessage sendMessage = message.get();
            try {
                 telegramBot.execute(sendMessage);
            } catch (TelegramApiException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * Handles regular messages (non-commands).
     * Currently used to auto-register users.
     */
    private void onTelegramMessage(@NonNull ChatMessageReceivedDto receivedMessage) {
        if (receivedMessage.chatType() == ChatType.PRIVATE) return;

        ensureUserRegisteredInChat(receivedMessage);

        ChatId chatId = receivedMessage.chatId();
        UserId userId = receivedMessage.userId();

        Optional<UserChatReadDto> userChatOpt = userChatService.getUserChatBy(chatId.value(), userId.value());
        if (userChatOpt.isEmpty()) {
            log.error("UserChat not found for userId={} in chatId={}", userId, chatId);
            return;
        }

        ChatReadDto chat = userChatOpt.get().chatReadDto();
        if (!chat.chatStatus().equals(ChatStatus.ACTIVE)) {
            log.debug("UserChat = {} is not active, tokens will not change", chat.id());
            return;
        }

        UserChatReadDto userChat = userChatOpt.get();
        String messageText = receivedMessage.text();
        int messageLength = messageText.length();

        if (userChat.remainingTokens() < messageLength) {
            try {
                log.info("Deleting message from userId={} in chatId={} text='{}'", userId, chatId, messageText);
                updateHelper.deleteUserMessage(receivedMessage, telegramBot);
            } catch (TelegramApiException e) {
                log.error("Error delete message", e);
            }
            notifyUserNoTokens(receivedMessage);
            log.info("Blocked message from user {} due to insufficient tokens", userId);
            return;
        }

        long updatedTokens = userChat.remainingTokens() - messageLength;
        UserChatUpdateDto updateDto = new UserChatUpdateDto(updatedTokens, null);
        userChatService.updateUserChat(userChat.id(), updateDto);

        log.debug("Deducted {} tokens from user {} ({} tokens remaining)", messageLength, userId, updatedTokens);
    }


    /**
     * If the user is not registered in the active chat,
     * creates User and UserChat records as needed.
     */
    private void ensureUserRegisteredInChat(@NonNull ChatMessageReceivedDto receivedMessage) {
        UserId userId = receivedMessage.userId();
        ChatId chatId = receivedMessage.chatId();

        var chatOptional = chatService.findChatById(chatId.value());
        if (chatOptional.isEmpty() || chatOptional.get().chatStatus() != ChatStatus.ACTIVE) return;

        var userOptional = userService.findUserById(userId.value());
        if (userOptional.isEmpty()) {
            registrationUser(userId.value(), receivedMessage.firstName(), receivedMessage.username());
            registrationUserChat(chatId.value(), userId.value());
            return;
        }

        Optional<UserChatReadDto> userChatReadDto = userChatService.getUserChatBy(chatId.value(), userId.value());
        if (userChatReadDto.isEmpty()) registrationUserChat(chatId.value(), userId.value());
    }

    /**
     * Detects whether the message is a command (starts with '/').
     */
    private boolean isCommand(@NonNull String string) {
        return string.startsWith("/");
    }


    /**
     * Checks if the bot was just added to a group chat.
     */
    private boolean isBotAddedToChat(@NonNull ChatMemberJoinedDto chatMember) throws TelegramApiException {
        Long botId = telegramBot.getBotId();
        return botId.equals(chatMember.userId().value());
    }

    /**
     * Registers a new user in the database.
     */
    private void registrationUser(@NonNull Long userId, @NonNull String firstName, String userName) {
        UserCreateDto userCreateDto = new UserCreateDto(userId, firstName, userName);
        log.debug("Attempting to register user: {}", userCreateDto);
        try {
            userService.createUser(userCreateDto);
        } catch (Exception exception) {
            log.error("Error registration User : {} ", userCreateDto, exception);
        }
    }

    /**
     * Registers a user-chat relation with default role USER.
     */
    private void registrationUserChat(@NonNull Long chatId, @NonNull Long userId) {
        UserChatCreateDto userChatCreateDto = new UserChatCreateDto(
                chatId, userId, UserRole.USER
        );
        log.debug("Attempting to register UserChat: {}", userChatCreateDto);
        try {
            userChatService.createUserChat(userChatCreateDto);
        } catch (Exception exception) {
            log.error("Error registration UserChat : {} ", userChatCreateDto, exception);
        }
    }

    private void notifyUserNoTokens(@NonNull ChatMessageReceivedDto receivedMessage) {
        UserId userId = receivedMessage.userId();
        String username = receivedMessage.username();

        String mention = (username != null) ? "@" + username : "User with ID " + userId.value();

        String warningMessage = """
            ❌ %s has run out of tokens and their message was not sent.
            To continue chatting, please ask an admin to recharge your tokens.
            """.formatted(mention);

        SendMessage sendMessage =  updateHelper.generateMessage(receivedMessage, warningMessage);
        try {
            telegramBot.execute(sendMessage);
            log.info("Sent public token depletion warning for user {}", userId);
        } catch (TelegramApiException e) {
            log.error("Failed to send public token warning for user {}", userId, e);
        }
    }

}
