package org.pl.pcz.yevkov.tgbottest.event;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.annotation.BotEventBinding;
import org.pl.pcz.yevkov.tgbottest.dto.event.ChatMessageReceivedDto;

@BotEventBinding(eventDto = ChatMessageReceivedDto.class)
public record ChatMessageReceivedEvent(ChatMessageReceivedDto member) implements BotEvent {
    public ChatMessageReceivedEvent(@NonNull ChatMessageReceivedDto member) {
        this.member = member;
    }
}