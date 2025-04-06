package org.pl.pcz.yevkov.botcore.infrastructure.bot.core;

import lombok.NonNull;

import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.event.dispatcher.BotEventDispatcher;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import org.telegram.telegrambots.meta.api.objects.Update;


@Log4j2
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotProperties props;
    private final ApplicationEventPublisher publisher;
    private final BotEventDispatcher botEventDispatcher;

    public TelegramBot( @NonNull BotProperties props,
                        @NonNull ApplicationEventPublisher publisher,
                        @NonNull BotEventDispatcher botEventDispatcher) {
        super(props.token());
        this.props = props;
        this.publisher = publisher;
        this.botEventDispatcher = botEventDispatcher;
        log.info("Telegram bot initialized: {}", props.name());
    }

    @Override
    public void onUpdateReceived(@NonNull Update update) {
        log.info("Received update: {}", update);
        try {
           var events =  botEventDispatcher.handle(update);
           for (var event : events) {
               log.info("Event received: {}", event);
           }
           events.forEach(publisher::publishEvent);
        } catch (Exception e) {
            log.error("Unhandled exception while processing update: {}", update, e);
        }
    }

    @Override
    public String getBotUsername() {
        return props.name();
    }
}
