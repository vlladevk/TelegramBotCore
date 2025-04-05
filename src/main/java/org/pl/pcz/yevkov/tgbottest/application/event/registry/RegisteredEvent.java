package org.pl.pcz.yevkov.tgbottest.application.event.registry;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.dto.event.EventDto;
import org.pl.pcz.yevkov.tgbottest.event.BotEvent;

import java.lang.reflect.Constructor;


/**
 * Holds metadata about a registered {@link BotEvent} type,
 * including its associated {@link EventDto} class and the reflective constructor.
 *
 * <p>This record is used by event factories and registries to instantiate
 * {@link BotEvent} instances based on incoming DTOs.</p>
 *
 * @param clazzDto         the {@link EventDto} type associated with the event
 * @param eventConstructor the reflective constructor that accepts the corresponding DTO
 */
public record RegisteredEvent(
        @NonNull Class<? extends EventDto> clazzDto,
        @NonNull Constructor<? extends BotEvent> eventConstructor
) {
}
