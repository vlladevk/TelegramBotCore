package org.pl.pcz.yevkov.botcore.interfaces.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.service.TokenManagementService;
import org.pl.pcz.yevkov.botcore.domain.event.UnrecognizedCommandEvent;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.exception.BotApiException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class CommandNotFoundEventListener {
    private final TokenManagementService tokenManagementService;

    @EventListener
    public void commandNotFound(@NonNull UnrecognizedCommandEvent event) {
        try {
            tokenManagementService.handleNonCommandMessage(event.message());
        } catch (BotApiException e) {
            log.error("Error processing command: {}", event.message().text(), e);
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
    }
}
