package org.pl.pcz.yevkov.botcore.application.event.factory;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.dto.event.EventDto;
import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;

import java.util.Optional;


public interface EventInstanceFactory {
    Optional<BotEvent> createEventInstance(@NonNull EventDto dto);
}
