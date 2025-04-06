package org.pl.pcz.yevkov.botcore.application.command.error;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.command.access.CommandAccessResult;
import org.pl.pcz.yevkov.botcore.application.command.response.TextResponse;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.botcore.application.message.factory.MessageResponseFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 * Default implementation of {@link CommandErrorHandler} responsible for generating
 * error responses during various failure stages of command processing.
 *
 * <p>
 * This handler covers the following error scenarios:
 * </p>
 * <ul>
 *   <li>Empty or null message text</li>
 *   <li>Unrecognized command name</li>
 *   <li>Access denied due to insufficient permissions</li>
 *   <li>Runtime errors during command execution</li>
 * </ul>
 *
 * <p>
 * All error messages are formatted and returned as {@link TextResponse} objects
 * using {@link MessageResponseFactory}.
 * </p>
 */
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
