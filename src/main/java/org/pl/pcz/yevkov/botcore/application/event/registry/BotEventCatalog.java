package org.pl.pcz.yevkov.botcore.application.event.registry;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.botcore.application.dto.event.EventDto;
import org.pl.pcz.yevkov.botcore.application.event.exception.DuplicateEventRegistrationException;
import org.pl.pcz.yevkov.botcore.application.event.factory.BotEventFactory;
import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class BotEventCatalog implements BotEventRegistrar, BotEventProvider {

    private final BotEventFactory botEventFactory;
    private final Map<Class<? extends EventDto>, RegisteredEvent> registeredEventsMap = new HashMap<>();

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

    @Override
    public Optional<RegisteredEvent> getRegisteredEvent(@NonNull Class<? extends EventDto> dtoEventClass) {
        return Optional.ofNullable(registeredEventsMap.get(dtoEventClass));
    }
}