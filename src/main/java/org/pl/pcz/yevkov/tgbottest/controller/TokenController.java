package org.pl.pcz.yevkov.tgbottest.controller;


import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.tgbottest.annotation.BotCommand;
import org.pl.pcz.yevkov.tgbottest.annotation.CommandController;
import org.pl.pcz.yevkov.tgbottest.application.helper.UserResolver;

import org.pl.pcz.yevkov.tgbottest.application.helper.UpdateHelper;
import org.pl.pcz.yevkov.tgbottest.model.vo.ChatId;
import org.pl.pcz.yevkov.tgbottest.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.tgbottest.model.vo.UserId;
import org.pl.pcz.yevkov.tgbottest.dto.userChat.UserChatReadDto;
import org.pl.pcz.yevkov.tgbottest.dto.userChat.UserChatUpdateDto;
import org.pl.pcz.yevkov.tgbottest.entity.ChatType;
import org.pl.pcz.yevkov.tgbottest.entity.UserRole;
import org.pl.pcz.yevkov.tgbottest.service.UserChatService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.Optional;

@CommandController
@RequiredArgsConstructor
public class TokenController {
    private final UserChatService userChatService;
    private final UpdateHelper updateHelper;
    private final UserResolver userResolver;

    @BotCommand(chatTypes = ChatType.GROUP,
            description = """
                    Displays how many tokens you currently have.
                    Показывает, сколько токенов у вас осталось.
                    Usage: /remaining_tokens
                    """
    )
    @SuppressWarnings("unused")
    public SendMessage remainingTokens(ChatMessageReceivedDto receivedMessage) {
        ChatId chatId = receivedMessage.chatId();
        UserId userId = receivedMessage.userId();
        var userChatOptional = userChatService.getUserChatBy(chatId, userId);
        if (userChatOptional.isEmpty()) {
            throw new IllegalStateException("User chat not found. Add the bot to the group first.");
        }
        var userChat = userChatOptional.get();
        String userName = userChat.userReadDto().name();
        Long tokensLeft = userChat.remainingTokens();
        return updateHelper.generateMessage(receivedMessage, userName + ", you have " + tokensLeft + " token(s) remaining.");
    }


    @BotCommand(chatTypes = ChatType.GROUP, showInMenu = false, userRole = UserRole.CHAT_ADMIN,
            description = """
                    Adds tokens to a specific user.
                    Добавляет токены конкретному пользователю.
                    Usage: /add_tokens @username 20
                    """)
    @SuppressWarnings("unused")
    public SendMessage addTokens(ChatMessageReceivedDto receivedMessage) {
        List<String> arguments = updateHelper.extractArguments(receivedMessage.text());
        if (arguments.size() != 2) {
            return updateHelper.generateMessage(receivedMessage, "Invalid number of arguments <userName> <count>. Expected 2 but found " + arguments.size());
        }
        ChatId chatId = receivedMessage.chatId();
        String input = arguments.getFirst();
        Optional<UserChatReadDto> userOpt = userResolver.resolveUserByNameOrUsername(userChatService, chatId, input);
        if (userOpt.isEmpty()) return userResolver.handleUserNotFound(receivedMessage, input);

        Long countAdd = Long.parseLong(arguments.get(1));
        UserChatReadDto user = userOpt.get();
        UserChatUpdateDto dto = new UserChatUpdateDto(user.remainingTokens() + countAdd, null);
        userChatService.updateUserChat(user.id(), dto);
        return updateHelper.generateMessage(receivedMessage, user.userReadDto().name() + ", you have " + countAdd + " token(s) added.");
    }

    @BotCommand(chatTypes = ChatType.GROUP, showInMenu = false, userRole = UserRole.CHAT_ADMIN,
            description = """
                    Gives tokens to all users in the chat.
                    Выдаёт токены всем пользователям чата.
                    Usage: /add_tokens_all 20
                    """)
    @SuppressWarnings("unused")
    public SendMessage addTokensForAll(ChatMessageReceivedDto receivedMessage) {
        ChatId chatId = receivedMessage.chatId();
        List<String> arguments = updateHelper.extractArguments(receivedMessage.text());

        if (arguments.size() != 1) {
            return updateHelper.generateMessage(receivedMessage, "Invalid number of arguments. Expected: <count>");
        }

        long tokensToAdd;
        try {
            tokensToAdd = Long.parseLong(arguments.getFirst());
        } catch (NumberFormatException e) {
            return updateHelper.generateMessage(receivedMessage, "Invalid number format. Example: /add_tokens_all 20");
        }

        List<UserChatReadDto> allUsers = userChatService.getUserChatsByChatId(chatId);
        if (allUsers.isEmpty()) {
            return updateHelper.generateMessage(receivedMessage, "No users found in this chat.");
        }

        for (UserChatReadDto userChat : allUsers) {
            long updatedTokens = userChat.remainingTokens() + tokensToAdd;
            UserChatUpdateDto dto = new UserChatUpdateDto(updatedTokens, null);
            userChatService.updateUserChat(userChat.id(), dto);
        }

        return updateHelper.generateMessage(receivedMessage, "Added " + tokensToAdd + " tokens to " + allUsers.size() + " users.");
    }



    @BotCommand(chatTypes = {ChatType.GROUP}, userRole = UserRole.USER,
            description = """
                    Shows token count for all users or a specific one.
                    Показывает количество токенов у всех или у конкретного пользователя.
                    Usage: /show_tokens [username]
                    """
    )
    @SuppressWarnings("unused")
    public SendMessage showTokens(ChatMessageReceivedDto receivedMessage) {
        ChatId chatId = receivedMessage.chatId();
        List<String> arguments = updateHelper.extractArguments(receivedMessage.text());

        if (!arguments.isEmpty()) {
            String input = arguments.getFirst();

            Optional<UserChatReadDto> userOpt = userResolver.resolveUserByNameOrUsername(userChatService,chatId, input);
            if (userOpt.isEmpty()) return userResolver.handleUserNotFound(receivedMessage, input);

            UserChatReadDto userChat = userOpt.get();
            String username = formatUsername(userChat);
            return updateHelper.generateMessage(receivedMessage, username + " has " + userChat.remainingTokens() + " token(s).");
        }

        List<UserChatReadDto> allUsers = userChatService.getUserChatsByChatId(chatId);
        if (allUsers.isEmpty()) {
            return updateHelper.generateMessage(receivedMessage, "No users found in this chat.");
        }

        StringBuilder builder = new StringBuilder("💬 Token List:\n");
        for (UserChatReadDto user : allUsers) {
            String username = formatUsername(user);
            builder.append("- ").append(username).append(": ").append(user.remainingTokens()).append(" token(s)\n");
        }

        return updateHelper.generateMessage(receivedMessage, builder.toString());
    }


    private String formatUsername(UserChatReadDto user) {
        String username = user.userReadDto().userName();
        return (username != null && !username.isBlank())
                ? "@" + username
                : user.userReadDto().name();
    }
}
