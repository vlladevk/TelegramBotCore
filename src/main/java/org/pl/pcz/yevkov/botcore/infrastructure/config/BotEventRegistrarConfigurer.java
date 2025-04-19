package org.pl.pcz.yevkov.botcore.infrastructure.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


import org.pl.pcz.yevkov.botcore.application.event.registry.BotEventRegistrar;
import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;
import org.pl.pcz.yevkov.botcore.infrastructure.scanner.BotEventScanner;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Registers all bot event classes annotated with @BotEventBinding.
 * Executed after bean construction via @PostConstruct.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class BotEventRegistrarConfigurer {
    private final BotEventRegistrar registrar;
    private final BotEventScanner eventScanner;

    @PostConstruct
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