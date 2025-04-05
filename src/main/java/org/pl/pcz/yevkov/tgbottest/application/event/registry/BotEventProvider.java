package org.pl.pcz.yevkov.tgbottest.application.event.registry;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.dto.event.EventDto;

import java.util.Optional;

public interface BotEventProvider {
    Optional<RegisteredEvent> getRegisteredEvent(@NonNull Class<? extends EventDto> clazzDto);
}
