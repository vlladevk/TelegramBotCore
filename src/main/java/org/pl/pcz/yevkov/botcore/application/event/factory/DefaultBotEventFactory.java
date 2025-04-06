package org.pl.pcz.yevkov.botcore.application.event.factory;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.annotation.BotEventBinding;
import org.pl.pcz.yevkov.botcore.application.event.exception.InvalidBotEventDefinitionException;
import org.pl.pcz.yevkov.botcore.application.event.registry.RegisteredEvent;
import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;
import org.springframework.stereotype.Component;



@Log4j2
@Component
public class DefaultBotEventFactory implements BotEventFactory {


    @Override
    public RegisteredEvent create(@NonNull Class<? extends BotEvent> eventClass) {
        // Ensure event is annotated properly
        var annotation = eventClass.getAnnotation(BotEventBinding.class);
        if (annotation == null) {
            log.error("Missing @BotEventBinding on class: {}", eventClass.getName());
            throw new InvalidBotEventDefinitionException(
                    "@BotEventBinding annotation is missing on class: " + eventClass.getName()
            );
        }

        var dtoClass = annotation.eventDto();

        try {
            var constructor = eventClass.getConstructor(dtoClass);
            log.debug("Registered BotEvent: {} -> DTO: {}", eventClass.getSimpleName(), dtoClass.getSimpleName());
            return new RegisteredEvent(dtoClass, constructor);
        } catch (NoSuchMethodException e) {
            log.error("Constructor with parameter {} not found in {}", dtoClass.getName(), eventClass.getName(), e);
            throw new InvalidBotEventDefinitionException(
                    "Constructor with parameter " + dtoClass.getName() + " not found in " + eventClass.getName(), e
            );
        }
    }
}