package org.pl.pcz.yevkov.tgbottest.application.event.registry;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.tgbottest.application.event.factory.BotEventFactory;
import org.pl.pcz.yevkov.tgbottest.dto.event.EventDto;

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
    public void registerEvent(@NonNull Class<?> handler) {
        var registeredEvent =  botEventFactory.create(handler);
        registeredEventsMap.put(registeredEvent.clazzDto(), registeredEvent);
    }

    @Override
    public Optional<RegisteredEvent> getRegisteredEvent(@NonNull Class<? extends EventDto> clazzDto) {
        RegisteredEvent registeredEvent = registeredEventsMap.get(clazzDto);
        return Optional.ofNullable(registeredEvent);
    }
}