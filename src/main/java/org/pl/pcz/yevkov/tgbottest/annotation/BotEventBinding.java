package org.pl.pcz.yevkov.tgbottest.annotation;

import org.pl.pcz.yevkov.tgbottest.dto.event.EventDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BotEventBinding {
    Class<? extends EventDto> eventDto();
}