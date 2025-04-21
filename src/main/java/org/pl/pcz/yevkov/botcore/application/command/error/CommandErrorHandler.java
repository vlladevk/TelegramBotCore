package org.pl.pcz.yevkov.botcore.application.command.error;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.command.access.CommandAccessResult;
import org.pl.pcz.yevkov.botcore.application.command.response.TextResponse;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;



public interface CommandErrorHandler {

    TextResponse handleEmptyMessage(@NonNull ChatMessageReceivedDto dto);

    TextResponse handleUnknownCommand(@NonNull ChatMessageReceivedDto dto, @NonNull String command);

    TextResponse handleAccessDenied(@NonNull ChatMessageReceivedDto dto,
                                              @NonNull CommandAccessResult result,
                                              @NonNull String command);

    TextResponse handleExecutionError(@NonNull ChatMessageReceivedDto dto,
                                                @NonNull String command,
                                                @NonNull Exception e);
}
