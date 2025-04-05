package org.pl.pcz.yevkov.tgbottest.event;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.annotation.BotEventBinding;
import org.pl.pcz.yevkov.tgbottest.dto.event.ChatMemberJoinedDto;


@BotEventBinding(eventDto = ChatMemberJoinedDto.class)
public record ChatMemberJoinedEvent(ChatMemberJoinedDto member) implements BotEvent {
    public ChatMemberJoinedEvent(@NonNull ChatMemberJoinedDto member) {
        this.member = member;
    }
}