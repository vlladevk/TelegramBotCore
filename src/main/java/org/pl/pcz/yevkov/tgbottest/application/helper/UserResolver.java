package org.pl.pcz.yevkov.tgbottest.application.helper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.tgbottest.application.message.factory.MessageResponseFactory;
import org.pl.pcz.yevkov.tgbottest.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.tgbottest.application.command.response.TextResponse;
import org.pl.pcz.yevkov.tgbottest.dto.userChat.UserChatReadDto;
import org.pl.pcz.yevkov.tgbottest.model.vo.ChatId;
import org.pl.pcz.yevkov.tgbottest.service.UserChatService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserResolver {
    private final MessageResponseFactory messageFactory;

    public Optional<UserChatReadDto> resolveUserByNameOrUsername(
            @NonNull UserChatService userChatService,
            @NonNull ChatId chatId,
            @NonNull String input
    ) {
        String trimmed = input.trim();
        return isUsername(trimmed)
                ? findByUsername(userChatService, chatId, trimmed)
                : findByFirstName(userChatService, chatId, trimmed);
    }

    private boolean isUsername(String input) {
        return input.startsWith("@");
    }

    private Optional<UserChatReadDto> findByUsername(UserChatService service, ChatId chatId, String username) {
        return service.getUserChatByUserName(chatId, username.substring(1));
    }

    private Optional<UserChatReadDto> findByFirstName(UserChatService service, ChatId chatId, String firstName) {
        List<UserChatReadDto> matches = service.getUserChatsByFirstName(chatId, firstName);
        return matches.size() == 1 ? Optional.of(matches.getFirst()) : Optional.empty();
    }

    public TextResponse handleUserNotFound(@NonNull ChatMessageReceivedDto receivedMessage, @NonNull String input) {
        String trimmed = input.trim();
        String finalInput = trimmed.isEmpty() ? "<no input>" : trimmed;
        String message = String.format("User '%s' not found or is ambiguous.", finalInput);
        return messageFactory.generateResponse(receivedMessage, message);
    }
}
