package org.pl.pcz.yevkov.botcore.application.event.factory;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.dto.event.EventDto;
import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;

import java.util.Optional;

/**
 * Defines a contract for factories that create {@link BotEvent} instances
 * based on the type of {@link EventDto}.
 *
 * <p>Implementations are expected to resolve the appropriate event class
 * and instantiate it using the provided DTO as a constructor argument.</p>
 */
public interface EventInstanceFactory {

    /**
     * Attempts to create a {@link BotEvent} instance from the specified {@link EventDto}.
     *
     * @param dto the {@link EventDto} to use as input for instantiation
     * @return an {@link Optional} containing the created {@link BotEvent},
     *         or an empty optional if no corresponding event is registered or instantiation fails
     */
    Optional<BotEvent> createEventInstance(@NonNull EventDto dto);
}
