package org.pl.pcz.yevkov.botcore.application.event.factory;

import org.junit.jupiter.api.Test;
import org.pl.pcz.yevkov.botcore.annotation.BotEventBinding;
import org.pl.pcz.yevkov.botcore.application.dto.event.EventDto;
import org.pl.pcz.yevkov.botcore.application.event.exception.InvalidBotEventDefinitionException;
import org.pl.pcz.yevkov.botcore.application.event.registry.RegisteredEvent;
import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;

import static org.junit.jupiter.api.Assertions.*;

class DefaultBotEventFactoryTest {

    DefaultBotEventFactory factory = new DefaultBotEventFactory();

    static class NoAnnotationEvent implements BotEvent {
        public NoAnnotationEvent(DummyDto ignore) {}
    }

    static class DummyDto implements EventDto {}

    @BotEventBinding(eventDto = DummyDto.class)
    static class NoConstructorEvent implements BotEvent {
        public NoConstructorEvent() {}
    }

    @BotEventBinding(eventDto = DummyDto.class)
    static class ValidEvent implements BotEvent {
        public ValidEvent(DummyDto ignore) {}
    }

    @Test
    void create_missingAnnotation_throwsException() {
        InvalidBotEventDefinitionException ex = assertThrows(
                InvalidBotEventDefinitionException.class,
                () -> factory.create(NoAnnotationEvent.class)
        );
        assertTrue(ex.getMessage().contains("annotation is missing"));
    }

    @Test
    void create_missingConstructor_throwsException() {
        InvalidBotEventDefinitionException ex = assertThrows(
                InvalidBotEventDefinitionException.class,
                () -> factory.create(NoConstructorEvent.class)
        );
        assertTrue(ex.getMessage().contains("Constructor with parameter"));
    }

    @Test
    void create_validEvent_returnsRegisteredEvent() {
        RegisteredEvent result = factory.create(ValidEvent.class);
        assertEquals(DummyDto.class, result.clazzDto());
        assertEquals(ValidEvent.class, result.eventConstructor().getDeclaringClass());
    }
}
