package org.pl.pcz.yevkov.tgbottest.application.command.access;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.application.command.registry.RegisteredCommand;
import org.pl.pcz.yevkov.tgbottest.dto.event.ChatMessageReceivedDto;

public interface CommandPermissionChecker {
    CommandAccessResult hasAccess(@NonNull ChatMessageReceivedDto receivedMessage, @NonNull RegisteredCommand command);
}
