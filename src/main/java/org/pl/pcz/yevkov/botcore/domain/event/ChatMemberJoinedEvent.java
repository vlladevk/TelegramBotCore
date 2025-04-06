package org.pl.pcz.yevkov.botcore.domain.event;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.annotation.BotEventBinding;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMemberJoinedDto;


@BotEventBinding(eventDto = ChatMemberJoinedDto.class)
public record ChatMemberJoinedEvent(ChatMemberJoinedDto member) implements BotEvent {
    public ChatMemberJoinedEvent(@NonNull ChatMemberJoinedDto member) {
        this.member = member;
    }
}