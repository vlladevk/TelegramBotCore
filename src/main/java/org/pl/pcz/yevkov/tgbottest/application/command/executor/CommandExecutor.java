package org.pl.pcz.yevkov.tgbottest.application.command.executor;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.application.command.registry.RegisteredCommand;
import org.pl.pcz.yevkov.tgbottest.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.tgbottest.application.command.response.TextResponse;

import java.util.Optional;

public interface CommandExecutor {
    Optional<TextResponse> execute(@NonNull RegisteredCommand command, @NonNull ChatMessageReceivedDto input);
}