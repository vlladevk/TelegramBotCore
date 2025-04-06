package org.pl.pcz.yevkov.botcore.application.event.registry;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.dto.event.EventDto;

import java.util.Optional;


public interface BotEventProvider {

    /**
     * Retrieves the {@link RegisteredEvent} associated with the specified {@link EventDto} class.
     *
     * @param dtoEventClass the class of the {@link EventDto} to look up
     * @return an {@link Optional} containing the corresponding {@link RegisteredEvent},
     * or empty if no mapping is registered
     */
    Optional<RegisteredEvent> getRegisteredEvent(@NonNull Class<? extends EventDto> dtoEventClass);
}
