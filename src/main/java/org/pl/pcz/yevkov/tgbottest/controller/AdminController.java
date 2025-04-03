package org.pl.pcz.yevkov.tgbottest.controller;


import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.tgbottest.annotation.BotCommand;
import org.pl.pcz.yevkov.tgbottest.annotation.CommandController;
import org.pl.pcz.yevkov.tgbottest.application.helper.CommandHelper;
import org.pl.pcz.yevkov.tgbottest.application.helper.UpdateHelper;
import org.pl.pcz.yevkov.tgbottest.dto.userChat.UserChatReadDto;
import org.pl.pcz.yevkov.tgbottest.dto.userChat.UserChatUpdateDto;
import org.pl.pcz.yevkov.tgbottest.entity.ChatType;
import org.pl.pcz.yevkov.tgbottest.entity.UserRole;

import org.pl.pcz.yevkov.tgbottest.service.UserChatService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

@CommandController
@RequiredArgsConstructor
public class AdminController {
    private final UserChatService userChatService;
    private final UpdateHelper updateHelper;
    private final CommandHelper commandHelper;


    @BotCommand(chatTypes = ChatType.GROUP, showInMenu = false, userRole = UserRole.CHAT_ADMIN,
            description = """
                    Grants admin rights to a user.
                    Выдаёт права администратора пользователю.
                    Usage: /add_admin @username
                    """)
    @SuppressWarnings("unused")
    public SendMessage addAdmin(Update update) {
        List<String> arguments = updateHelper.extractArguments(update);
        if (arguments.size() != 1) {
            return updateHelper.generateMessage(update, "Invalid number of arguments <userName> or <FirstName>. Expected 1 but found " + arguments.size());
        }
        Long chatId = updateHelper.extractChatId(update);
        Optional<UserChatReadDto> userOpt = commandHelper.resolveUserFromArgs(userChatService, chatId, arguments);
        if (userOpt.isEmpty()) return commandHelper.handleUserNotFound(update, arguments);

        UserChatUpdateDto dto = new UserChatUpdateDto(null, UserRole.CHAT_ADMIN);
        userChatService.updateUserChat(userOpt.get().id(), dto);
        return updateHelper.generateMessage(update, "User promoted to admin.");
    }

    @BotCommand(chatTypes = ChatType.GROUP, showInMenu = false, userRole = UserRole.CHAT_OWNER,
            description = """
                    Revokes admin rights from a user.
                    Убирает права администратора у пользователя.
                    Usage: /remove_admin @username
                    """
    )
    @SuppressWarnings("unused")
    public SendMessage removeAdmin(Update update) {
        Long chatId = updateHelper.extractChatId(update);
        List<String> arguments = updateHelper.extractArguments(update);

        if (arguments.size() != 1) {
            return updateHelper.generateMessage(update, "Invalid number of arguments. Expected: <userName> or <firstName>");
        }

        Optional<UserChatReadDto> resolved = commandHelper.resolveUserFromArgs(userChatService, chatId,arguments);
        if (resolved.isEmpty()) {
            return commandHelper.handleUserNotFound(update, arguments);
        }

        UserChatReadDto userChatReadDto = resolved.get();
        UserChatUpdateDto userChatUpdateDto = new UserChatUpdateDto(
                null,
                UserRole.USER
        );

        userChatService.updateUserChat(userChatReadDto.id(), userChatUpdateDto);
        return updateHelper.generateMessage(update, "Admin privileges removed successfully.");
    }


}
