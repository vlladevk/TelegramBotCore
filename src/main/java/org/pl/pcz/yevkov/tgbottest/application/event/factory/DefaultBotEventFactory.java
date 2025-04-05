package org.pl.pcz.yevkov.tgbottest.application.event.factory;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.annotation.BotEventBinding;
import org.pl.pcz.yevkov.tgbottest.application.event.registry.RegisteredEvent;
import org.springframework.stereotype.Component;

@Component
public class DefaultBotEventFactory implements BotEventFactory {
    @Override
    public RegisteredEvent create(@NonNull Class<?> handler) {
        BotEventBinding eventBindingAnnotation = handler.getAnnotation(BotEventBinding.class);
        if (eventBindingAnnotation == null) {
            throw new IllegalArgumentException("No @BotEventBinding annotation found on " + handler.getName());
        }
        var dto = eventBindingAnnotation.eventDto();
        try {
            var constructor = handler.getConstructor(dto);
            return new RegisteredEvent(dto, constructor);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("No constructor found for " + dto, e);
        }
    }
}
