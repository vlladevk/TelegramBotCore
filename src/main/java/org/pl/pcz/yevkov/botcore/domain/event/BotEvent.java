package org.pl.pcz.yevkov.botcore.domain.event;

public interface BotEvent {
    default String getEventName() {
        return this.getClass().getSimpleName();
    }
}
