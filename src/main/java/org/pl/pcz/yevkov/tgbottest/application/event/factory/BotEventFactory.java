package org.pl.pcz.yevkov.tgbottest.application.event.factory;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.application.event.registry.RegisteredEvent;

public interface BotEventFactory {
    RegisteredEvent create(@NonNull Class<?> handler);
}
