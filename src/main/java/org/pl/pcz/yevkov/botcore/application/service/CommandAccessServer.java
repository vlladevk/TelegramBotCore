package org.pl.pcz.yevkov.botcore.application.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.botcore.application.command.access.CommandAccessResult;
import org.pl.pcz.yevkov.botcore.application.command.access.CommandPermissionChecker;
import org.pl.pcz.yevkov.botcore.application.command.registry.RegisteredCommand;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatType;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;
import org.pl.pcz.yevkov.botcore.domain.vo.UserId;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommandAccessServer implements CommandPermissionChecker {
    private final UserChatService userChatService;

    @Override
    public CommandAccessResult hasAccess(@NonNull ChatMessageReceivedDto receivedMessage, @NonNull RegisteredCommand command) {
        ChatType chatType = receivedMessage.chatType();
        if (!command.chatTypes().contains(chatType)) {
            return new CommandAccessResult(false, "This command cannot be used in " + chatType.name().toLowerCase() + " chats.");
        }
        ChatId chatId = receivedMessage.chatId();
        UserId userId = receivedMessage.userId();
        if (chatType != ChatType.PRIVATE) {
            var userChatReadDtoOptional = userChatService.getUserChatBy(chatId, userId);
            if (userChatReadDtoOptional.isEmpty()) {
                return new CommandAccessResult(true, "Bot is not configured for this chat");
            }
            var userChatReadDto = userChatReadDtoOptional.get();
            var userRole = userChatReadDto.userRole();
            var neededRole = command.userRole();

            if (userRole.ordinal() < neededRole.ordinal()) {
                return new CommandAccessResult(false, "Insufficient permissions. Required role: " + neededRole.name());
            }
        }
        return new CommandAccessResult(true, "Successfully accessed command.");
    }
}
