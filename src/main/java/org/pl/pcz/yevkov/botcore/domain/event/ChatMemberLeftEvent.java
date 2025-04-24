package org.pl.pcz.yevkov.botcore.domain.event;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMemberLeftDto;

public record ChatMemberLeftEvent(ChatMemberLeftDto member) implements BotEvent {
    public ChatMemberLeftEvent(@NonNull ChatMemberLeftDto member) {
        this.member = member;
    }
}