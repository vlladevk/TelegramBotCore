package org.pl.pcz.yevkov.botcore.infrastructure.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.event.registry.BotEventRegistrar;

import org.pl.pcz.yevkov.botcore.domain.event.ChatMemberJoinedEvent;
import org.pl.pcz.yevkov.botcore.domain.event.ChatMemberLeftEvent;
import org.pl.pcz.yevkov.botcore.domain.event.ChatMessageReceivedEvent;
import org.springframework.stereotype.Component;


@Log4j2
@Component
@RequiredArgsConstructor
public class BotEventRegistrarConfigurer {
    private final BotEventRegistrar registrar;

    @PostConstruct
    public void registerEvents() {
        try {
            registrar.registerEvent(ChatMemberJoinedEvent.class);
            registrar.registerEvent(ChatMemberLeftEvent.class);
            registrar.registerEvent(ChatMessageReceivedEvent.class);
            log.info("All bot events registered successfully");
        } catch (Exception e) {
            log.error("Bot event registration failed", e);
            throw new IllegalStateException("Event registration failed", e);
        }
    }
}
