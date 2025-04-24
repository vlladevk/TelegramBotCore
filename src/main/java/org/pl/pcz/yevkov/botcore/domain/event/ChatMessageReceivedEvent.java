package org.pl.pcz.yevkov.botcore.domain.event;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;

public record ChatMessageReceivedEvent(ChatMessageReceivedDto message) implements BotEvent {
    public ChatMessageReceivedEvent(@NonNull ChatMessageReceivedDto message) {
        this.message = message;
    }

    public boolean isCommand() {
        return message.text().trim().startsWith("/");
    }
}