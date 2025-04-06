package org.pl.pcz.yevkov.botcore.application.event.registry;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;


/**
 * Defines a contract for components responsible for registering {@link BotEvent} classes.
 *
 * <p>Implementations should associate each registered event class with its corresponding
 * {@code EventDto} type and ensure uniqueness of registration.</p>
 */
public interface BotEventRegistrar {
    /**
     * Registers a {@link BotEvent} class for later instantiation and dispatching.
     *
     * @param eventClass the {@link BotEvent} class to register;
     *                must be annotated and properly configured
     */
    void registerEvent(@NonNull Class<? extends BotEvent> eventClass);
}
