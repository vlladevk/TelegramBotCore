package org.pl.pcz.yevkov.botcore.application.command.executor;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.command.registry.RegisteredCommand;
import org.pl.pcz.yevkov.botcore.application.command.response.TextResponse;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;

import java.util.Optional;

/**
 * Defines a contract for invoking the underlying logic of a registered bot command.
 */
public interface CommandExecutor {
    Optional<TextResponse> execute(@NonNull RegisteredCommand command, @NonNull ChatMessageReceivedDto input);
}