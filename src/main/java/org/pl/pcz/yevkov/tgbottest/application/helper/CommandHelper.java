package org.pl.pcz.yevkov.tgbottest.application.helper;

import lombok.NonNull;

import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.tgbottest.dto.UserChatReadDto;
import org.pl.pcz.yevkov.tgbottest.service.UserChatService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommandHelper {
    private final UpdateHelper updateHelper;
    public Optional<UserChatReadDto> resolveUserFromArgs(
            @NonNull UserChatService userChatService,
            @NonNull Long chatId,
            @NonNull List<String> args
    ) {
        if (args.isEmpty()) return Optional.empty();
        String find = args.getFirst();

        // Try by @username first
        Optional<UserChatReadDto> byUsername = userChatService.getUserChatByUserName(chatId, find);
        if (byUsername.isPresent()) return byUsername;

        // Fallback to firstName (must be unique in this chat)
        List<UserChatReadDto> byFirstName = userChatService.getUserChatsByFirstName(chatId, find);
        if (byFirstName.size() == 1) return Optional.of(byFirstName.getFirst());

        return Optional.empty();
    }

    public SendMessage handleUserNotFound( @NonNull Update update, @NonNull List<String> args) {
        return updateHelper.generateMessage(update, args.getFirst() + " not found or ambiguous.");
    }
}
