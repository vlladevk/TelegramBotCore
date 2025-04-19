package org.pl.pcz.yevkov.botcore.infrastructure.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.core.TelegramBot;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Log4j2
@Component
@RequiredArgsConstructor
public class TelegramBotInitializer {

    private final TelegramBot telegramBot;

    @EventListener(ApplicationReadyEvent.class)
    public void initBot() {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(telegramBot);
            log.info("Telegram bot registered");
        } catch (TelegramApiException e) {
            log.error("Failed to register Telegram bot", e);
            throw new IllegalStateException("Telegram bot startup failed", e);
        }
    }
}
