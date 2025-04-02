package org.pl.pcz.yevkov.tgbottest.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pl.pcz.yevkov.tgbottest.annotation.BotCommand;
import org.pl.pcz.yevkov.tgbottest.annotation.CommandController;
import org.pl.pcz.yevkov.tgbottest.application.BotApiAdapter;
import org.pl.pcz.yevkov.tgbottest.application.helper.UpdateHelper;
import org.pl.pcz.yevkov.tgbottest.dto.*;
import org.pl.pcz.yevkov.tgbottest.entity.ChatStatus;
import org.pl.pcz.yevkov.tgbottest.entity.ChatType;
import org.pl.pcz.yevkov.tgbottest.entity.UserRole;
import org.pl.pcz.yevkov.tgbottest.service.ChatService;
import org.pl.pcz.yevkov.tgbottest.service.UserChatService;
import org.pl.pcz.yevkov.tgbottest.service.UserService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Slf4j
@CommandController
public class ChatManagementController {
    private final ChatService chatService;
    private final UserService userService;
    private final UserChatService userChatService;
    private final BotApiAdapter bot;
    private final UpdateHelper updateHelper;

    @BotCommand(chatTypes = ChatType.GROUP,
            description = """
                    Activates the chat if the bot was added but not yet registered.
                    Активирует чат, если бот был добавлен, но не зарегистрирован.
                    Usage: /registration_chat [optional_limit_hours]
                    """
    )
    @SuppressWarnings("unused")
    public SendMessage registrationChat(Update update) {
        Long chatId = updateHelper.extractChatId(update);
        Long userId = updateHelper.extractUserId(update);
        var chatOpt = chatService.findChatById(chatId);

        if (chatOpt.isEmpty()) {
            return updateHelper.generateMessage(update, "Chat not found. Add the bot to the group first.");
        }


        ChatReadDto chat = chatOpt.get();

        List<String> args = updateHelper.extractArguments(update);
        if (!args.isEmpty()) {
            Long hourLimit = Long.parseLong(args.getFirst());
            chat = chatService.changeLimit(chatId, hourLimit).orElseThrow(
                    () -> new IllegalStateException("Cannot change limit of chat. Chat don't found" + chatId));
        }

        return switch (chat.chatStatus()) {
            case INACTIVE -> handleInactiveChat(update, chat, chatId, userId);
            case ACTIVE -> {
                log.debug("registrationChat: chat is already active: {}", chat);
                yield updateHelper.generateMessage(update, "Chat is already active!");
            }
            case DELETED -> {
                log.debug("registrationChat: bot was previously deleted from chat: {}", chat);
                yield updateHelper.generateMessage(update, "Bot was deleted from this chat.");
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
    public SendMessage deactivateChat(Update update) {
        Long chatId = updateHelper.extractChatId(update);
        Long userId = updateHelper.extractUserId(update);

        Optional<ChatReadDto> chatOpt = chatService.findChatById(chatId);
        if (chatOpt.isEmpty()) {
            return updateHelper.generateMessage(update, "Chat not found.");
        }

        ChatReadDto chat = chatOpt.get();

        if (chat.chatStatus() == ChatStatus.INACTIVE) {
            return updateHelper.generateMessage(update, "Chat is already inactive.");
        }

        try {
            boolean isAdmin = updateHelper.isUserAdmin(chatId, userId, bot);
            if (!isAdmin) {
                return updateHelper.generateMessage(update, "You don't have permission to deactivate this chat.");
            }

            chatService.markChatAsStatus(chatId, ChatStatus.INACTIVE);
            log.info("Chat {} marked as INACTIVE by user {}", chatId, userId);
            return updateHelper.generateMessage(update, "Chat successfully marked as INACTIVE.");
        } catch (TelegramApiException e) {
            log.error("Failed to deactivate chat {} by user {}", chatId, userId, e);
            return updateHelper.generateMessage(update, "An error occurred while trying to deactivate the chat.");
        }
    }

    private SendMessage handleInactiveChat(Update update, ChatReadDto chat, Long chatId, Long userId) {
        try {
            boolean isAdmin = updateHelper.isUserAdmin(chatId, userId, bot);
            if (isAdmin) {
                chatService.markChatAsStatus(chat.id(), ChatStatus.ACTIVE);
                Optional<UserReadDto> userOpt = userService.findUserById(userId);
                if (userOpt.isEmpty()) {
                    var user = updateHelper.extractUser(update);
                    UserCreateDto userCreateDto = new UserCreateDto(
                            userId, user.getFirstName(), user.getUserName()
                    );
                    userService.createUser(userCreateDto);
                }
                Optional<UserChatReadDto> userChatReadDto = userChatService.getUserChatBy(chatId, userId);
                if (userChatReadDto.isEmpty()) {
                    UserChatCreateDto userChatCreateDto = new UserChatCreateDto(
                            chatId, userId, UserRole.CHAT_OWNER
                    );
                    userChatService.createUserChat(userChatCreateDto);
                } else {
                    userChatService.updateChatStatus(chatId, userId, UserRole.CHAT_OWNER);
                }
                log.info("Chat {} successfully activated by user {}", chat.id(), userId);
                return updateHelper.generateMessage(update, "Operation Successful!");
            } else {
                log.debug("registrationChat: user is not admin, registration denied: {}", chat);
                return updateHelper.generateMessage(update, "Operation Failed! You are not allowed to register this chat.");
            }
        } catch (TelegramApiException e) {
            log.error("registrationChat: Telegram API error while checking admin rights", e);
            return updateHelper.generateMessage(update, "An error has occurred while checking permissions.");
        }
    }

}
