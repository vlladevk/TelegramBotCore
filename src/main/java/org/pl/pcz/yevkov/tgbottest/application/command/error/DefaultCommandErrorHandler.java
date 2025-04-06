package org.pl.pcz.yevkov.tgbottest.application.command.error;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.tgbottest.application.command.access.CommandAccessResult;
import org.pl.pcz.yevkov.tgbottest.application.command.response.TextResponse;
import org.pl.pcz.yevkov.tgbottest.application.message.factory.MessageResponseFactory;
import org.pl.pcz.yevkov.tgbottest.dto.event.ChatMessageReceivedDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Log4j2
@Component
@RequiredArgsConstructor
public class DefaultCommandErrorHandler implements CommandErrorHandler {
    private final MessageResponseFactory messageResponseFactory;

    @Override
    public Optional<TextResponse> handleEmptyMessage(@NonNull ChatMessageReceivedDto dto) {
        log.debug("Received text message is empty or null: {}", dto);
        return Optional.of(messageResponseFactory.generateResponse(dto, "Input message or text is empty."));
    }

    @Override
    public Optional<TextResponse> handleUnknownCommand(@NonNull ChatMessageReceivedDto dto, @NonNull String command) {
        log.debug("Unknown command received: {} from {} in {}",
                command, dto.userId(), dto.chatId()
        );
        return Optional.of(messageResponseFactory.generateResponse(
                dto,
                "Unknown command: " + command)
        );
    }

    @Override
    public Optional<TextResponse> handleAccessDenied(@NonNull ChatMessageReceivedDto dto,
                                                     @NonNull CommandAccessResult result,
                                                     @NonNull String command) {
        log.debug("Command '{}' access denied by {} for userId={} in chatId={}",
                command, result.reason(), dto.userId(), dto.chatId());
        return Optional.of(messageResponseFactory.generateResponse(dto, result.reason()));
    }

    @Override
    public Optional<TextResponse> handleExecutionError(@NonNull ChatMessageReceivedDto dto,
                                                       @NonNull String command,
                                                       @NonNull Exception e) {
        log.error("Error while processing command '{}' for userId={} in chatId={}: {}",
                command, dto.userId(), dto.chatId(), e.getMessage(), e
        );
        String text = "Error while processing command: " + command + " Error: " + e.getMessage();
        return Optional.of(messageResponseFactory.generateResponse(dto, text));
    }
}
