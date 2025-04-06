package org.pl.pcz.yevkov.botcore.application.event.factory;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.event.registry.RegisteredEvent;
import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;


public interface BotEventFactory {
    RegisteredEvent create(@NonNull Class<? extends BotEvent> eventClass);
}
