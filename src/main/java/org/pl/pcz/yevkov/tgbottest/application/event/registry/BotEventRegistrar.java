package org.pl.pcz.yevkov.tgbottest.application.event.registry;

import lombok.NonNull;

public interface BotEventRegistrar {
    void registerEvent(@NonNull Class<?> handler);
}
