package org.pl.pcz.yevkov.botcore.application.command.error;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.command.access.CommandAccessResult;
import org.pl.pcz.yevkov.botcore.application.command.response.TextResponse;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;

import java.util.Optional;


public interface CommandErrorHandler {

    Optional<TextResponse> handleEmptyMessage(@NonNull ChatMessageReceivedDto dto);

    Optional<TextResponse> handleUnknownCommand(@NonNull ChatMessageReceivedDto dto, @NonNull String command);

    Optional<TextResponse> handleAccessDenied(@NonNull ChatMessageReceivedDto dto,
                                              @NonNull CommandAccessResult result,
                                              @NonNull String command);

    Optional<TextResponse> handleExecutionError(@NonNull ChatMessageReceivedDto dto,
                                                @NonNull String command,
                                                @NonNull Exception e);
}
