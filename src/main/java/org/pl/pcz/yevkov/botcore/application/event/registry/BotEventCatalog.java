package org.pl.pcz.yevkov.botcore.application.event.registry;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.botcore.application.event.exception.DuplicateEventRegistrationException;
import org.pl.pcz.yevkov.botcore.application.event.factory.BotEventFactory;
import org.pl.pcz.yevkov.botcore.application.dto.event.EventDto;
import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Central registry for mapping {@link EventDto} types to their corresponding {@link BotEvent} definitions.
 *
 * <p>This component is responsible for:</p>
 * <p>
 * - Registering event classes via {@link BotEventFactory}<br>
 * - Providing lookup access to registered event metadata<br>
 * </p>
 *
 * <p>All registrations must be unique per {@link EventDto} type.
 * Duplicate registrations will result in a {@link DuplicateEventRegistrationException}.</p>
 */
@Component
@RequiredArgsConstructor
public class BotEventCatalog implements BotEventRegistrar, BotEventProvider {

    private final BotEventFactory botEventFactory;
    private final Map<Class<? extends EventDto>, RegisteredEvent> registeredEventsMap = new HashMap<>();

    /**
     * Registers a {@link BotEvent} class and binds it to its declared {@link EventDto} type.
     *
     * @param eventClass the {@link BotEvent} implementation to register
     * @throws DuplicateEventRegistrationException if the {@link EventDto} type is already registered
     */
    @Override
    public void registerEvent(@NonNull Class<? extends BotEvent> eventClass) {
        var registeredEvent = botEventFactory.create(eventClass);
        var dtoClass = registeredEvent.clazzDto();

        if (registeredEventsMap.containsKey(dtoClass)) {
            throw new DuplicateEventRegistrationException(
                    "DTO type already registered: " + dtoClass.getName()
            );
        }

        registeredEventsMap.put(dtoClass, registeredEvent);
    }

    /**
     * Retrieves the registered event metadata for the specified {@link EventDto} type.
     *
     * @param dtoEventClass the class of the {@link EventDto} to look up
     * @return an {@link Optional} containing the {@link RegisteredEvent}, or empty if not found
     */
    @Override
    public Optional<RegisteredEvent> getRegisteredEvent(@NonNull Class<? extends EventDto> dtoEventClass) {
        return Optional.ofNullable(registeredEventsMap.get(dtoEventClass));
    }
}