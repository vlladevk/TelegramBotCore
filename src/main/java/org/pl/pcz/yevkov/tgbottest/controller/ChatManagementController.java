package org.pl.pcz.yevkov.tgbottest.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.tgbottest.annotation.BotCommand;
import org.pl.pcz.yevkov.tgbottest.annotation.CommandController;
import org.pl.pcz.yevkov.tgbottest.application.command.parser.ArgumentExtractor;
import org.pl.pcz.yevkov.tgbottest.service.AdminCheckService;
import org.pl.pcz.yevkov.tgbottest.application.message.factory.MessageResponseFactory;
import org.pl.pcz.yevkov.tgbottest.bot.exception.BotApiException;
import org.pl.pcz.yevkov.tgbottest.dto.chat.ChatReadDto;
import org.pl.pcz.yevkov.tgbottest.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.tgbottest.application.command.response.TextResponse;
import org.pl.pcz.yevkov.tgbottest.dto.user.UserCreateDto;
import org.pl.pcz.yevkov.tgbottest.dto.user.UserReadDto;
import org.pl.pcz.yevkov.tgbottest.dto.userChat.UserChatCreateDto;
import org.pl.pcz.yevkov.tgbottest.dto.userChat.UserChatReadDto;
import org.pl.pcz.yevkov.tgbottest.entity.ChatStatus;
import org.pl.pcz.yevkov.tgbottest.entity.ChatType;
import org.pl.pcz.yevkov.tgbottest.entity.UserRole;
import org.pl.pcz.yevkov.tgbottest.model.vo.ChatId;
import org.pl.pcz.yevkov.tgbottest.model.vo.UserId;
import org.pl.pcz.yevkov.tgbottest.service.ChatService;
import org.pl.pcz.yevkov.tgbottest.service.UserChatService;
import org.pl.pcz.yevkov.tgbottest.service.UserService;

import java.util.List;
import java.util.Optional;

@Log4j2
@CommandController
@RequiredArgsConstructor
public class ChatManagementController {
    private final ChatService chatService;
    private final UserService userService;
    private final UserChatService userChatService;
    private final AdminCheckService adminCheckService;
    private final ArgumentExtractor argumentExtractor;
    private final MessageResponseFactory messageFactory;

    @BotCommand(chatTypes = ChatType.GROUP,
            description = """
                    Activates the chat if the bot was added but not yet registered.
                    Активирует чат, если бот был добавлен, но не зарегистрирован.
                    Usage: /registration_chat [optional_limit_hours]
                    """
    )
    @SuppressWarnings("unused")
    public TextResponse registerChat(ChatMessageReceivedDto receivedMessage) {
        ChatId chatId = receivedMessage.chatId();
        UserId userId = receivedMessage.userId();
        var chatOpt = chatService.findChatById(chatId);

        if (chatOpt.isEmpty()) {
            return messageFactory.generateResponse(
                    receivedMessage,
                    "Chat not found. Add the bot to the group first."
            );
        }


        ChatReadDto chat = chatOpt.get();

        List<String> args = argumentExtractor.extract(receivedMessage.text());
        if (!args.isEmpty()) {
            Long hourLimit = Long.parseLong(args.getFirst());
            chat = chatService.changeLimit(chatId, hourLimit).orElseThrow(
                    () -> new IllegalStateException("Cannot change limit of chat. Chat don't found" + chatId));
        }

        return switch (chat.chatStatus()) {
            case INACTIVE -> handleInactiveChat(receivedMessage, chat, chatId, userId);
            case ACTIVE -> {
                log.debug("registrationChat: chat is already active: {}", chat);

                yield messageFactory.generateResponse(
                        receivedMessage,
                        "Chat is already active!"
                );
            }
            case DELETED -> {
                log.debug("registrationChat: bot was previously deleted from chat: {}", chat);

                yield messageFactory.generateResponse(
                        receivedMessage,
                        "Bot was deleted from this chat."
                );
            }
        };
    }


    @BotCommand(
            chatTypes = ChatType.GROUP,
            showInMenu = false,
            userRole = UserRole.CHAT_ADMIN,
            description = """
                    Marks the chat as inactive.
                    Деактивирует чат.
                    Usage: /deactivate_chat
                    """
    )
    @SuppressWarnings("unused")
    public TextResponse deactivateChat(ChatMessageReceivedDto receivedMessage) {
        ChatId chatId = receivedMessage.chatId();
        UserId userId = receivedMessage.userId();

        Optional<ChatReadDto> chatOpt = chatService.findChatById(chatId);
        if (chatOpt.isEmpty()) {
            return messageFactory.generateResponse(
                    receivedMessage,
                    "Chat not found."
            );
        }

        ChatReadDto chat = chatOpt.get();

        if (chat.chatStatus() == ChatStatus.INACTIVE) {
            return messageFactory.generateResponse(
                    receivedMessage,
                    "Chat is already inactive."
            );
        }

        try {
            boolean isAdmin = adminCheckService.isUserAdmin(chatId, userId);
            if (!isAdmin) {
                return messageFactory.generateResponse(
                        receivedMessage,
                        "You don't have permission to deactivate this chat."
                );
            }

            chatService.markChatAsStatus(chatId, ChatStatus.INACTIVE);
            log.info("Chat {} marked as INACTIVE by user {}", chatId, userId);

            return messageFactory.generateResponse(
                    receivedMessage,
                    "Chat successfully marked as INACTIVE."
            );
        } catch (BotApiException e) {
            log.error("Failed to deactivate chat {} by user {}", chatId, userId, e);

            return messageFactory.generateResponse(
                    receivedMessage,
                    "An error occurred while trying to deactivate the chat."
            );
        }
    }

    private TextResponse handleInactiveChat(ChatMessageReceivedDto receivedMessage, ChatReadDto chat, ChatId chatId, UserId userId) {
        try {
            boolean isAdmin = adminCheckService.isUserAdmin(chatId, userId);
            if (isAdmin) {
                chatService.markChatAsStatus(chat.id(), ChatStatus.ACTIVE);
                Optional<UserReadDto> userOpt = userService.findUserById(userId);
                if (userOpt.isEmpty()) {
                    UserCreateDto userCreateDto = UserCreateDto.builder()
                            .id(userId)
                            .name(receivedMessage.firstName())
                            .userName(receivedMessage.username())
                            .build();
                    userService.createUser(userCreateDto);
                }
                Optional<UserChatReadDto> userChatReadDto = userChatService.getUserChatBy(chatId, userId);
                if (userChatReadDto.isEmpty()) {
                    UserChatCreateDto userChatCreateDto = UserChatCreateDto.builder()
                            .chatId(chatId)
                            .userId(userId)
                            .userRole(UserRole.CHAT_OWNER)
                            .build();
                    userChatService.createUserChat(userChatCreateDto);
                } else {
                    userChatService.updateChatStatus(chatId, userId, UserRole.CHAT_OWNER);
                }
                log.info("Chat {} successfully activated by user {}", chat.id(), userId);

                return messageFactory.generateResponse(
                        receivedMessage,
                        "Operation Successful!"
                );
            } else {
                log.debug("registrationChat: user is not admin, registration denied: {}", chat);

                return messageFactory.generateResponse(
                        receivedMessage,
                        "Operation Failed! You are not allowed to register this chat."
                );
            }
        } catch (BotApiException e) {
            log.error("registrationChat: Telegram API error while checking admin rights", e);

            return messageFactory.generateResponse(
                    receivedMessage,
                    "An error has occurred while checking permissions."
            );
        }
    }

}
