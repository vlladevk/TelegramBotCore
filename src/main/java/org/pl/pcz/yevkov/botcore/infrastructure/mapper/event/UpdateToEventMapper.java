package org.pl.pcz.yevkov.botcore.infrastructure.mapper.event;

import lombok.NonNull;

import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface UpdateToEventMapper<To extends BotEvent> {
    boolean supports(@NonNull Update input);

    List<To> mapFrom(@NonNull Update input);
}
