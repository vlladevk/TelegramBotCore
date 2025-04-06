package org.pl.pcz.yevkov.botcore.application.event.registry;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.dto.event.EventDto;
import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;

import java.lang.reflect.Constructor;


/**
 * Holds metadata about a registered {@link BotEvent} type,
 * including its associated {@link EventDto} class and the reflective constructor.
 */
public record RegisteredEvent(
        @NonNull Class<? extends EventDto> clazzDto,
        @NonNull Constructor<? extends BotEvent> eventConstructor
) {
}
