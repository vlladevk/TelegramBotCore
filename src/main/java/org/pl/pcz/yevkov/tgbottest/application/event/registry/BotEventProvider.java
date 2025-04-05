package org.pl.pcz.yevkov.tgbottest.application.event.registry;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.dto.event.EventDto;

import java.util.Optional;

/**
 * Defines a contract for retrieving metadata about registered {@code BotEvent} classes
 * based on a given {@link EventDto} type.
 *
 * <p>Implementations are expected to provide lookup functionality that returns
 * the associated {@link RegisteredEvent} definition, if available.</p>
 */
public interface BotEventProvider {

    /**
     * Retrieves the {@link RegisteredEvent} associated with the specified {@link EventDto} class.
     *
     * @param dtoEventClass the class of the {@link EventDto} to look up
     * @return an {@link Optional} containing the corresponding {@link RegisteredEvent},
     *         or empty if no mapping is registered
     */
    Optional<RegisteredEvent> getRegisteredEvent(@NonNull Class<? extends EventDto> dtoEventClass);
}
