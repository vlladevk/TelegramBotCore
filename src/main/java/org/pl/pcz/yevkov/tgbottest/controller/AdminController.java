package org.pl.pcz.yevkov.tgbottest.controller;


import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.tgbottest.annotation.BotCommand;
import org.pl.pcz.yevkov.tgbottest.annotation.CommandController;
import org.pl.pcz.yevkov.tgbottest.application.helper.UpdateHelper;
import org.pl.pcz.yevkov.tgbottest.application.helper.UserResolver;
import org.pl.pcz.yevkov.tgbottest.application.message.facrory.MessageDtoFactory;
import org.pl.pcz.yevkov.tgbottest.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.tgbottest.dto.message.SendMessageDto;
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
    private final UpdateHelper updateHelper;
    private final UserResolver userResolver;
    private final MessageDtoFactory messageFactory;


    @BotCommand(chatTypes = ChatType.GROUP, showInMenu = false, userRole = UserRole.CHAT_ADMIN,
            description = """
                    Grants admin rights to a user.
                    Выдаёт права администратора пользователю.
                    Usage: /add_admin @username
                    """)
    @SuppressWarnings("unused")
    public SendMessageDto addAdmin(ChatMessageReceivedDto receivedMessage) {
        List<String> arguments = updateHelper.extractArguments(receivedMessage.text());
        if (arguments.size() != 1) {

            var sendText = "Invalid number of arguments <userName> or <FirstName>. Expected 1 but found "
                    + arguments.size();

            return messageFactory.generateMessage(
                    receivedMessage,
                    sendText
            );
        }
        ChatId chatId = receivedMessage.chatId();
        String input = arguments.getFirst();

        Optional<UserChatReadDto> userOpt = userResolver.resolveUserByNameOrUsername(userChatService, chatId, input);
        if (userOpt.isEmpty()) return userResolver.handleUserNotFound(receivedMessage, input);

        UserChatUpdateDto dto = new UserChatUpdateDto(null, UserRole.CHAT_ADMIN);
        userChatService.updateUserChat(userOpt.get().id(), dto);

        return messageFactory.generateMessage(
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
    public SendMessageDto removeAdmin(ChatMessageReceivedDto receivedMessage) {
        ChatId chatId = receivedMessage.chatId();
        List<String> arguments = updateHelper.extractArguments(receivedMessage.text());

        if (arguments.size() != 1) {
            return messageFactory.generateMessage(
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
        UserChatUpdateDto userChatUpdateDto = new UserChatUpdateDto(
                null,
                UserRole.USER
        );

        userChatService.updateUserChat(userChatReadDto.id(), userChatUpdateDto);

        return messageFactory.generateMessage(
                receivedMessage,
                "Admin privileges removed successfully."
        );
    }


}
