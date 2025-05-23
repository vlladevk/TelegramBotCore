package org.pl.pcz.yevkov.botcore.interfaces.controller;


import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.botcore.annotation.BotCommand;
import org.pl.pcz.yevkov.botcore.annotation.CommandController;
import org.pl.pcz.yevkov.botcore.application.command.parser.ArgumentExtractor;
import org.pl.pcz.yevkov.botcore.application.command.response.TextResponse;
import org.pl.pcz.yevkov.botcore.application.helper.UserResolver;
import org.pl.pcz.yevkov.botcore.application.message.factory.MessageResponseFactory;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.botcore.application.dto.userChat.UserChatReadDto;
import org.pl.pcz.yevkov.botcore.application.dto.userChat.UserChatUpdateDto;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatType;
import org.pl.pcz.yevkov.botcore.domain.entity.UserRole;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;
import org.pl.pcz.yevkov.botcore.domain.vo.UserId;
import org.pl.pcz.yevkov.botcore.application.service.UserChatService;

import java.util.List;
import java.util.Optional;

@CommandController
@RequiredArgsConstructor
public class TokenController {
    private final UserChatService userChatService;
    private final ArgumentExtractor argumentExtractor;
    private final UserResolver userResolver;
    private final MessageResponseFactory messageFactory;

    @BotCommand(chatTypes = ChatType.GROUP,
            description = """
                    Displays how many tokens you currently have.
                    Показывает, сколько токенов у вас осталось.
                    Usage: /remaining_tokens
                    """
    )
    @SuppressWarnings("unused")
    public TextResponse remainingTokens(ChatMessageReceivedDto receivedMessage) {
        ChatId chatId = receivedMessage.chatId();
        UserId userId = receivedMessage.userId();

        var userChatOptional = userChatService.getUserChatBy(chatId, userId);
        if (userChatOptional.isEmpty()) {
            throw new IllegalStateException("User chat not found. Add the bot to the group first.");
        }

        var userChat = userChatOptional.get();
        String userName = userChat.userReadDto().name();
        Long tokensLeft = userChat.remainingTokens();

        return messageFactory.generateResponse(
                receivedMessage,
                userName + ", you have " + tokensLeft + " token(s) remaining."
        );
    }


    @BotCommand(chatTypes = ChatType.GROUP, userRole = UserRole.CHAT_ADMIN,
            description = """
                    Adds tokens to a specific user.
                    Добавляет токены конкретному пользователю.
                    Usage: /add_tokens @username 20
                    """)
    @SuppressWarnings("unused")
    public TextResponse addTokens(ChatMessageReceivedDto receivedMessage) {
        List<String> arguments = argumentExtractor.extract(receivedMessage.text());
        if (arguments.size() != 2) {
            return messageFactory.generateResponse(
                    receivedMessage,
                    "Invalid number of arguments <userName> <count>. Expected 2 but found " + arguments.size()
            );
        }
        ChatId chatId = receivedMessage.chatId();
        String input = arguments.getFirst();
        Optional<UserChatReadDto> userOpt = userResolver.resolveUserByNameOrUsername(userChatService, chatId, input);
        if (userOpt.isEmpty()) return userResolver.handleUserNotFound(receivedMessage, input);

        Long countAdd = Long.parseLong(arguments.get(1));
        UserChatReadDto user = userOpt.get();
        UserChatUpdateDto dto = UserChatUpdateDto.builder()
                .remainingTokens(user.remainingTokens() + countAdd)
                .build();
        userChatService.updateUserChat(user.id(), dto);

        return messageFactory.generateResponse(
                receivedMessage,
                user.userReadDto().name() + ", you have " + countAdd + " token(s) added."
        );
    }

    @BotCommand(chatTypes = ChatType.GROUP, showInMenu = false, userRole = UserRole.CHAT_ADMIN,
            description = """
                    Gives tokens to all users in the chat.
                    Выдаёт токены всем пользователям чата.
                    Usage: /add_tokens_all 20
                    """)
    @SuppressWarnings("unused")
    public TextResponse addTokensForAll(ChatMessageReceivedDto receivedMessage) {
        ChatId chatId = receivedMessage.chatId();
        List<String> arguments = argumentExtractor.extract(receivedMessage.text());

        if (arguments.size() != 1) {
            return messageFactory.generateResponse(
                    receivedMessage,
                    "Invalid number of arguments. Expected: <count>"
            );
        }

        long tokensToAdd;
        try {
            tokensToAdd = Long.parseLong(arguments.getFirst());
        } catch (NumberFormatException e) {
            return messageFactory.generateResponse(
                    receivedMessage,
                    "Invalid number format. Example: /add_tokens_all 20"
            );
        }

        List<UserChatReadDto> allUsers = userChatService.getUserChatsByChatId(chatId);
        if (allUsers.isEmpty()) {
            return messageFactory.generateResponse(
                    receivedMessage,
                    "No users found in this chat."
            );
        }

        for (UserChatReadDto userChat : allUsers) {
            long updatedTokens = userChat.remainingTokens() + tokensToAdd;
            UserChatUpdateDto dto = UserChatUpdateDto.builder()
                    .remainingTokens(updatedTokens)
                    .build();
            userChatService.updateUserChat(userChat.id(), dto);
        }

        return messageFactory.generateResponse(
                receivedMessage,
                "Added " + tokensToAdd + " tokens to " + allUsers.size() + " users."
        );
    }


    @BotCommand(chatTypes = {ChatType.GROUP}, userRole = UserRole.USER,
            description = """
                    Shows token count for all users or a specific one.
                    Показывает количество токенов у всех или у конкретного пользователя.
                    Usage: /show_tokens [username]
                    """
    )
    @SuppressWarnings("unused")
    public TextResponse showTokens(ChatMessageReceivedDto receivedMessage) {
        ChatId chatId = receivedMessage.chatId();
        List<String> arguments = argumentExtractor.extract(receivedMessage.text());

        if (!arguments.isEmpty()) {
            String input = arguments.getFirst();

            Optional<UserChatReadDto> userOpt = userResolver.resolveUserByNameOrUsername(userChatService, chatId, input);
            if (userOpt.isEmpty()) return userResolver.handleUserNotFound(receivedMessage, input);

            UserChatReadDto userChat = userOpt.get();
            String username = formatUsername(userChat);

            return messageFactory.generateResponse(
                    receivedMessage,
                    username + " has " + userChat.remainingTokens() + " token(s)."
            );
        }

        List<UserChatReadDto> allUsers = userChatService.getUserChatsByChatId(chatId);
        if (allUsers.isEmpty()) {
            return messageFactory.generateResponse(
                    receivedMessage,
                    "No users found in this chat."
            );
        }

        StringBuilder builder = new StringBuilder("💬 Token List:\n");
        for (UserChatReadDto user : allUsers) {
            String username = formatUsername(user);
            builder.append("- ").append(username).append(": ").append(user.remainingTokens()).append(" token(s)\n");
        }

        return messageFactory.generateResponse(
                receivedMessage,
                builder.toString()
        );
    }


    private String formatUsername(UserChatReadDto user) {
        String username = user.userReadDto().userName();
        return (username != null && !username.isBlank())
                ? "@" + username
                : user.userReadDto().name();
    }
}
