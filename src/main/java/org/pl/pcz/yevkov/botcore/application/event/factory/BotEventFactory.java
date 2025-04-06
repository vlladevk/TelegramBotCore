package org.pl.pcz.yevkov.botcore.application.event.factory;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.event.registry.RegisteredEvent;
import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;

/**
 * Defines a contract for factories that produce {@link RegisteredEvent} instances
 * based on a given {@link BotEvent} class.
 *
 * <p>Implementations of this interface are responsible for:</p>
 * <p>
 * - Validating the structure of the {@code BotEvent} class<br>
 * - Extracting required metadata such as the associated EventDto type and constructor<br>
 * - Failing fast if the class is misconfigured or incomplete
 * </p>
 */
public interface BotEventFactory {
    /**
     * Creates a {@link RegisteredEvent} based on the given {@link BotEvent} class.
     *
     * @param eventClass the class to analyze and register; must be annotated with
     *                   {@link org.pl.pcz.yevkov.botcore.annotation.BotEventBinding}
     * @return a fully validated {@link RegisteredEvent} containing constructor and DTO metadata
     *
     * @throws org.pl.pcz.yevkov.botcore.application.event.exception.InvalidBotEventDefinitionException
     *         if the class lacks the required annotation or does not define a matching constructor
     */
    RegisteredEvent create(@NonNull Class<? extends BotEvent> eventClass );
}
