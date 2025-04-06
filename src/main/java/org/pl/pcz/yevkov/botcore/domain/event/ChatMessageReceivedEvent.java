package org.pl.pcz.yevkov.botcore.domain.event;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.annotation.BotEventBinding;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;

@BotEventBinding(eventDto = ChatMessageReceivedDto.class)
public record ChatMessageReceivedEvent(ChatMessageReceivedDto member) implements BotEvent {
    public ChatMessageReceivedEvent(@NonNull ChatMessageReceivedDto member) {
        this.member = member;
    }
}