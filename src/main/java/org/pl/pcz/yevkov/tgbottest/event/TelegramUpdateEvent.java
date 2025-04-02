package org.pl.pcz.yevkov.tgbottest.event;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
public class TelegramUpdateEvent extends ApplicationEvent {
    private final Update update;

    public TelegramUpdateEvent(@NonNull Object source, @NonNull Update update) {
        super(source);
        this.update = update;
    }
}
