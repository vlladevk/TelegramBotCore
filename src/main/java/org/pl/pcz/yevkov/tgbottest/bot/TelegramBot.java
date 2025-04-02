package org.pl.pcz.yevkov.tgbottest.bot;

import lombok.NonNull;

import lombok.extern.slf4j.Slf4j;
import org.pl.pcz.yevkov.tgbottest.event.TelegramUpdateEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;


import org.telegram.telegrambots.meta.api.objects.Update;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotProperties props;
    private final ApplicationEventPublisher publisher;


    public TelegramBot(@NonNull BotProperties props, @NonNull ApplicationEventPublisher publisher) {
        super(props.token());
        this.props = props;
        this.publisher = publisher;
        log.info("Telegram bot initialized: {}", props.name());
    }

    @Override
    public void onUpdateReceived(@NonNull Update update) {
        log.debug("Received update: {}", update);
        if (!update.hasMessage()) {
            log.debug("Skipping update, because Massage has empty");
            return;
        }
        publisher.publishEvent(new TelegramUpdateEvent(this, update));
    }


    @Override
    public String getBotUsername() {
        return props.name();
    }

}
