package org.pl.pcz.yevkov.tgbottest.application.event.factory;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.tgbottest.application.event.registry.BotEventProvider;
import org.pl.pcz.yevkov.tgbottest.dto.event.EventDto;
import org.pl.pcz.yevkov.tgbottest.event.BotEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Default implementation of {@link EventInstanceFactory} that creates {@link BotEvent}
 * instances based on the type of {@link EventDto}.
 *
 * <p>This factory looks up a registered event class using {@link BotEventProvider} and attempts
 * to instantiate it using a constructor that accepts the provided DTO.</p>
 *
 * <p>If no corresponding event is registered or instantiation fails, an empty {@link Optional}
 * is returned. Failures are logged but not propagated.</p>
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class DefaultEventInstanceFactory implements EventInstanceFactory {

    private final BotEventProvider botEventProvider;

    /**
     * Attempts to create a {@link BotEvent} instance from the given {@link EventDto}.
     *
     * @param dto the source {@link EventDto} used to construct the event
     * @return an {@link Optional} containing the created {@link BotEvent},
     *         or an empty optional if the event is not registered or cannot be instantiated
     */
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