package org.pl.pcz.yevkov.botcore.application.command.access;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.command.registry.RegisteredCommand;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;


public interface CommandPermissionChecker {
    CommandAccessResult hasAccess(@NonNull ChatMessageReceivedDto receivedMessage, @NonNull RegisteredCommand command);
}
