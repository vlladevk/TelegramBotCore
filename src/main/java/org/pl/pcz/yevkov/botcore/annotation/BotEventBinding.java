package org.pl.pcz.yevkov.botcore.annotation;

import org.pl.pcz.yevkov.botcore.application.dto.event.EventDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BotEventBinding {
    Class<? extends EventDto> eventDto();
}