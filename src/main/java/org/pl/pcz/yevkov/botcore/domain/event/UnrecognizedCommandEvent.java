package org.pl.pcz.yevkov.botcore.domain.event;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;

public record UnrecognizedCommandEvent(ChatMessageReceivedDto message) implements BotEvent {
    public UnrecognizedCommandEvent(@NonNull ChatMessageReceivedDto message) {
        this.message = message;
    }
}
