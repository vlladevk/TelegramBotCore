package org.pl.pcz.yevkov.botcore.infrastructure.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.command.registry.BotCommandProvider;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class CommandRegistrationLogger {

    private final BotCommandProvider botCommandCatalog;

    @EventListener(ApplicationReadyEvent.class)
    public void logCommandCount() {
        int total = botCommandCatalog.getAllRegisteredCommands().size();
        log.info("Total registered bot commands: {}", total);
    }
}