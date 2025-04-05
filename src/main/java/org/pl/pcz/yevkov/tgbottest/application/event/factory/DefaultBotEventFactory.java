package org.pl.pcz.yevkov.tgbottest.application.event.factory;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.tgbottest.annotation.BotEventBinding;
import org.pl.pcz.yevkov.tgbottest.application.event.exception.InvalidBotEventDefinitionException;
import org.pl.pcz.yevkov.tgbottest.application.event.registry.RegisteredEvent;
import org.pl.pcz.yevkov.tgbottest.event.BotEvent;
import org.springframework.stereotype.Component;


/**
 * Factory implementation that validates and constructs metadata for {@link BotEvent} classes.
 *
 * <p>This factory ensures that each {@link BotEvent} class:</p>
 * <p>
 * - Is annotated with {@link BotEventBinding}<br>
 * - Declares a constructor that accepts the corresponding EventDto type
 * </p>
 *
 * <p>
 * If any validation fails, an
 * {@link org.pl.pcz.yevkov.tgbottest.application.event.exception.InvalidBotEventDefinitionException}
 * is thrown immediately to prevent misconfigured event registration.
 * </p>
 */
@Log4j2
@Component
public class DefaultBotEventFactory implements BotEventFactory {

    /**
     * Creates a {@link RegisteredEvent} instance by extracting and validating event metadata
     * from the provided {@link BotEvent} class.
     *
     * @param eventClass the {@link BotEvent} class to analyze and register
     * @return a fully validated {@link RegisteredEvent} instance, ready for use
     *
     * @throws InvalidBotEventDefinitionException if the given class is misconfigured,
     *         for example, if it is missing the {@link BotEventBinding} annotation
     *         or does not declare a constructor that accepts the corresponding EventDto class
     */
    @Override
    public RegisteredEvent create(@NonNull Class<? extends BotEvent> eventClass ) {
        // Ensure event is annotated properly

        var annotation = eventClass .getAnnotation(BotEventBinding.class);
        if (annotation == null) {
            log.error("Missing @BotEventBinding on class: {}", eventClass .getName());
            throw new InvalidBotEventDefinitionException(
                    "@BotEventBinding annotation is missing on class: " + eventClass .getName()
            );
        }

        // Extract the DTO class from annotation
        var dtoClass = annotation.eventDto();

        try {
            // Verify that a matching constructor exists
            var constructor = eventClass .getConstructor(dtoClass);
            log.debug("Registered BotEvent: {} -> DTO: {}", eventClass .getSimpleName(), dtoClass.getSimpleName());
            return new RegisteredEvent(dtoClass, constructor);
        } catch (NoSuchMethodException e) {
            log.error("Constructor with parameter {} not found in {}", dtoClass.getName(), eventClass .getName(), e);
            throw new InvalidBotEventDefinitionException(
                    "Constructor with parameter " + dtoClass.getName() + " not found in " + eventClass .getName(), e
            );
        }
    }
}