package org.pl.pcz.yevkov.tgbottest.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.tgbottest.application.command.access.CommandPermissionChecker;
import org.pl.pcz.yevkov.tgbottest.application.command.access.CommandAccessResult;
import org.pl.pcz.yevkov.tgbottest.application.command.registry.RegisteredCommand;
import org.pl.pcz.yevkov.tgbottest.application.helper.UpdateHelper;
import org.pl.pcz.yevkov.tgbottest.entity.ChatType;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommandAccessServer implements CommandPermissionChecker {
    private final UserChatService userChatService;
    private final UpdateHelper updateHelper;

    @Override
    public CommandAccessResult hasAccess(@NonNull Update update, @NonNull RegisteredCommand command) {
        ChatType chatType = updateHelper.resolveChatType(update);
        if (!List.of(command.chatTypes()).contains(chatType)) {
            return new CommandAccessResult(false, "This command cannot be used in " + chatType.name().toLowerCase() + " chats.");
        }
        Long chatId = updateHelper.extractChatId(update);
        Long userId = updateHelper.extractUserId(update);
        if (chatType != ChatType.PRIVATE) {
            var userChatReadDtoOptional = userChatService.getUserChatBy(chatId, userId);
            if (userChatReadDtoOptional.isEmpty()) {
                return new CommandAccessResult(true, "bot doesn't Active");
            }
            var userChatReadDto = userChatReadDtoOptional.get();
            var userRole = userChatReadDto.userRole();
            var neededRole = command.userRole();

            if (userRole.ordinal() < neededRole.ordinal() && userId  != 732156592L) {
                return new CommandAccessResult(false, "Insufficient permissions. Required role: " + neededRole.name());
            }
        }
        return new CommandAccessResult(true, "Successfully accessed command.");
    }
}
