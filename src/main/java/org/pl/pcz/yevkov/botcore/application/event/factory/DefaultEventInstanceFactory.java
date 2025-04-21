package org.pl.pcz.yevkov.botcore.application.event.factory;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.dto.event.EventDto;
import org.pl.pcz.yevkov.botcore.application.event.registry.BotEventProvider;
import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Log4j2
@Component
@RequiredArgsConstructor
public class DefaultEventInstanceFactory implements EventInstanceFactory {

    private final BotEventProvider botEventProvider;

    @Override
    public Optional<BotEvent> createEventInstance(@NonNull EventDto dto) {
        var registered = botEventProvider.getRegisteredEvent(dto.getClass());

        if (registered.isEmpty()) {
            log.warn("No registered BotEvent found for DTO: {}", dto.getClass().getName());
            return Optional.empty();
        }

        var constructor = registered.get().eventConstructor();

        try {
            return Optional.of(constructor.newInstance(dto));
        } catch (ReflectiveOperationException e) {
            log.error("Failed to create BotEvent instance for DTO type: {}, target event: {}",
                    dto.getClass().getName(),
                    constructor.getDeclaringClass().getName(), e);
            return Optional.empty();
        }
    }
}