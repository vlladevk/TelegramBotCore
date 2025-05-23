package org.pl.pcz.yevkov.botcore.application.command.error;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.command.access.CommandAccessResult;
import org.pl.pcz.yevkov.botcore.application.command.response.TextResponse;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.botcore.application.message.factory.MessageResponseFactory;
import org.springframework.stereotype.Component;



/**
 * Default {@link CommandErrorHandler} implementation.
 *
 * <p>
 * Generates {@link TextResponse} messages for all error cases during command processing:
 * empty input, unknown commands, access denied, and execution failures.
 * Uses {@link MessageResponseFactory} to format responses.
 * </p>
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class DefaultCommandErrorHandler implements CommandErrorHandler {
    private final MessageResponseFactory messageResponseFactory;

    @Override
    public TextResponse handleEmptyMessage(@NonNull ChatMessageReceivedDto dto) {
        log.debug("Received text message is empty or null: {}", dto);
        return messageResponseFactory.generateResponse(dto, "Input message or text is empty.");
    }

    @Override
    public TextResponse handleUnknownCommand(@NonNull ChatMessageReceivedDto dto, @NonNull String command) {
        log.debug("Unknown command received: {} from {} in {}",
                command, dto.userId(), dto.chatId()
        );
        return messageResponseFactory.generateResponse(dto, "Unknown command: " + command);
    }

    @Override
    public TextResponse handleAccessDenied(@NonNull ChatMessageReceivedDto dto,
                                                     @NonNull CommandAccessResult result,
                                                     @NonNull String command) {
        log.debug("Command '{}' access denied by {} for userId={} in chatId={}",
                command, result.reason(), dto.userId(), dto.chatId());
        return messageResponseFactory.generateResponse(dto, result.reason());
    }

    @Override
    public TextResponse handleExecutionError(@NonNull ChatMessageReceivedDto dto,
                                                       @NonNull String command,
                                                       @NonNull Exception e) {
        log.error("Error while processing command '{}' for userId={} in chatId={}: {}",
                command, dto.userId(), dto.chatId(), e.getMessage(), e
        );
        String text = "Error while processing command: " + command + " Error: " + e.getMessage();
        return messageResponseFactory.generateResponse(dto, text);
    }
}
