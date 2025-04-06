package org.pl.pcz.yevkov.botcore.infrastructure.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


import org.pl.pcz.yevkov.botcore.application.event.registry.BotEventRegistrar;
import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;
import org.pl.pcz.yevkov.botcore.infrastructure.scanner.BotEventScanner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Responsible for dynamically registering all discovered bot event classes during application startup.
 * <p>
 * This component listens for the {@link ApplicationReadyEvent}, which is published once the Spring
 * application context has been fully initialized and all beans are ready to be used.
 * <p>
 * It uses the {@link BotEventScanner} to locate all valid bot event classes that are annotated with
 * {@link org.pl.pcz.yevkov.botcore.annotation.BotEventBinding} and implement the {@link BotEvent} interface.
 * These are then registered via the {@link BotEventRegistrar}.
 *
 * <p><b>Why not use {@code @PostConstruct}?</b></p>
 * <ul>
 *     <li>{@code @PostConstruct} is called immediately after dependency injection is completed — which can be too early.</li>
 *     <li>At {@code @PostConstruct} time, the application context may not be fully ready (e.g., other components
 *         depending on events, messaging systems, or other post-processors may not be initialized yet).</li>
 *     <li>{@code ApplicationReadyEvent} guarantees that the Spring container is fully initialized and stable,
 *         making it the ideal place for dynamic, runtime registration logic like this.</li>
 * </ul>
 *
 * <p>This ensures that all bot event types are reliably registered only when the application is fully prepared to handle them.</p>
 *
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class BotEventRegistrarConfigurer {
    private final BotEventRegistrar registrar;
    private final BotEventScanner eventScanner;

    @EventListener(ApplicationReadyEvent.class)
    public void registerEvents() {
        try {
            Set<Class<? extends BotEvent>> events = eventScanner.scanAnnotatedEvents();
            events.forEach(registrar::registerEvent);
            log.info("Registered {} bot events", events.size());
        } catch (Exception e) {
            log.error("Bot event registration failed", e);
            throw new IllegalStateException("Event registration failed", e);
        }
    }
}