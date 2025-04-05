package org.pl.pcz.yevkov.tgbottest.application.event.registry;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.dto.event.EventDto;

import java.lang.reflect.Constructor;

public record RegisteredEvent(
        @NonNull Class<? extends EventDto> clazzDto,
        @NonNull Constructor<?> eventConstructor) {
}
