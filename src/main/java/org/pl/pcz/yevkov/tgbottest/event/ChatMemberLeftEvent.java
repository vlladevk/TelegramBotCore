package org.pl.pcz.yevkov.tgbottest.event;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.annotation.BotEventBinding;
import org.pl.pcz.yevkov.tgbottest.dto.event.ChatMemberLeftDto;

@BotEventBinding(eventDto = ChatMemberLeftDto.class)
public record ChatMemberLeftEvent(ChatMemberLeftDto member) implements BotEvent {
    public ChatMemberLeftEvent(@NonNull ChatMemberLeftDto member) {
        this.member = member;
    }
}