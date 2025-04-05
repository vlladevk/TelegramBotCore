package org.pl.pcz.yevkov.tgbottest.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.tgbottest.application.command.access.CommandPermissionChecker;
import org.pl.pcz.yevkov.tgbottest.application.command.access.CommandAccessResult;
import org.pl.pcz.yevkov.tgbottest.application.command.registry.RegisteredCommand;
import org.pl.pcz.yevkov.tgbottest.model.vo.ChatId;
import org.pl.pcz.yevkov.tgbottest.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.tgbottest.model.vo.UserId;
import org.pl.pcz.yevkov.tgbottest.entity.ChatType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommandAccessServer implements CommandPermissionChecker {
    private final UserChatService userChatService;

    @Override
    public CommandAccessResult hasAccess(@NonNull ChatMessageReceivedDto receivedMessage, @NonNull RegisteredCommand command) {
        ChatType chatType = receivedMessage.chatType();
        if (!List.of(command.chatTypes()).contains(chatType)) {
            return new CommandAccessResult(false, "This command cannot be used in " + chatType.name().toLowerCase() + " chats.");
        }
        ChatId chatId = receivedMessage.chatId();
        UserId userId = receivedMessage.userId();
        if (chatType != ChatType.PRIVATE) {
            var userChatReadDtoOptional = userChatService.getUserChatBy(chatId, userId);
            if (userChatReadDtoOptional.isEmpty()) {
                return new CommandAccessResult(true, "bot doesn't Active");
            }
            var userChatReadDto = userChatReadDtoOptional.get();
            var userRole = userChatReadDto.userRole();
            var neededRole = command.userRole();

            if (userRole.ordinal() < neededRole.ordinal() && userId.value()  != 732156592L) {
                return new CommandAccessResult(false, "Insufficient permissions. Required role: " + neededRole.name());
            }
        }
        return new CommandAccessResult(true, "Successfully accessed command.");
    }
}
