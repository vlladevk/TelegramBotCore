package org.pl.pcz.yevkov.tgbottest.controller;


import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.tgbottest.annotation.BotCommand;
import org.pl.pcz.yevkov.tgbottest.annotation.CommandController;
import org.pl.pcz.yevkov.tgbottest.application.command.parser.ArgumentExtractor;
import org.pl.pcz.yevkov.tgbottest.application.command.response.TextResponse;
import org.pl.pcz.yevkov.tgbottest.application.helper.UserResolver;
import org.pl.pcz.yevkov.tgbottest.application.message.factory.MessageResponseFactory;
import org.pl.pcz.yevkov.tgbottest.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.tgbottest.dto.userChat.UserChatReadDto;
import org.pl.pcz.yevkov.tgbottest.dto.userChat.UserChatUpdateDto;
import org.pl.pcz.yevkov.tgbottest.entity.ChatType;
import org.pl.pcz.yevkov.tgbottest.entity.UserRole;
import org.pl.pcz.yevkov.tgbottest.model.vo.ChatId;
import org.pl.pcz.yevkov.tgbottest.service.UserChatService;

import java.util.List;
import java.util.Optional;

@CommandController
@RequiredArgsConstructor
public class AdminController {
    private final UserChatService userChatService;
    private final ArgumentExtractor argumentExtractor;
    private final UserResolver userResolver;
    private final MessageResponseFactory messageFactory;


    @BotCommand(chatTypes = ChatType.GROUP, showInMenu = false, userRole = UserRole.CHAT_ADMIN,
            description = """
                    Grants admin rights to a user.
                    Выдаёт права администратора пользователю.
                    Usage: /add_admin @username
                    """)
    @SuppressWarnings("unused")
    public TextResponse addAdmin(ChatMessageReceivedDto receivedMessage) {
        List<String> arguments = argumentExtractor.extract(receivedMessage.text());
        if (arguments.size() != 1) {

            var sendText = "Invalid number of arguments <userName> or <FirstName>. Expected 1 but found "
                    + arguments.size();

            return messageFactory.generateResponse(
                    receivedMessage,
                    sendText
            );
        }
        ChatId chatId = receivedMessage.chatId();
        String input = arguments.getFirst();

        Optional<UserChatReadDto> userOpt = userResolver.resolveUserByNameOrUsername(userChatService, chatId, input);
        if (userOpt.isEmpty()) return userResolver.handleUserNotFound(receivedMessage, input);

        UserChatUpdateDto dto = UserChatUpdateDto.builder()
                .userRole(UserRole.CHAT_ADMIN)
                .build();
        userChatService.updateUserChat(userOpt.get().id(), dto);

        return messageFactory.generateResponse(
                receivedMessage,
                "User promoted to admin."
        );
    }

    @BotCommand(chatTypes = ChatType.GROUP, showInMenu = false, userRole = UserRole.CHAT_OWNER,
            description = """
                    Revokes admin rights from a user.
                    Убирает права администратора у пользователя.
                    Usage: /remove_admin @username
                    """
    )
    @SuppressWarnings("unused")
    public TextResponse removeAdmin(ChatMessageReceivedDto receivedMessage) {
        ChatId chatId = receivedMessage.chatId();
        List<String> arguments = argumentExtractor.extract(receivedMessage.text());

        if (arguments.size() != 1) {
            return messageFactory.generateResponse(
                    receivedMessage,
                    "Invalid number of arguments. Expected: <userName> or <firstName>"
            );
        }
        String input = arguments.getFirst();
        Optional<UserChatReadDto> resolved = userResolver.resolveUserByNameOrUsername(userChatService, chatId, input);
        if (resolved.isEmpty()) {
            return userResolver.handleUserNotFound(receivedMessage, input);
        }

        UserChatReadDto userChatReadDto = resolved.get();
        UserChatUpdateDto userChatUpdateDto = UserChatUpdateDto.builder()
                .userRole(UserRole.USER)
                .build();

        userChatService.updateUserChat(userChatReadDto.id(), userChatUpdateDto);

        return messageFactory.generateResponse(
                receivedMessage,
                "Admin privileges removed successfully."
        );
    }


}
