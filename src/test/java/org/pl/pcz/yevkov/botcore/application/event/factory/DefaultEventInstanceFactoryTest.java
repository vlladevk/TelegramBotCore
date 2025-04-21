package org.pl.pcz.yevkov.botcore.application.event.factory;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pl.pcz.yevkov.botcore.application.dto.event.EventDto;
import org.pl.pcz.yevkov.botcore.application.event.registry.BotEventProvider;
import org.pl.pcz.yevkov.botcore.application.event.registry.RegisteredEvent;
import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;

import java.lang.reflect.Constructor;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultEventInstanceFactoryTest {
    @Mock
    private BotEventProvider botEventProvider;

    @InjectMocks
    private DefaultEventInstanceFactory factory;

    static class DummyDto implements EventDto {}

    @AllArgsConstructor
    static class DummyEvent implements BotEvent {
        DummyDto dto;
    }

    static class FailingEvent implements BotEvent {
        public FailingEvent(DummyDto ignore) {
            throw new RuntimeException("fail");
        }
    }

    @Test
    void shouldCreateEventInstance_whenRegistered() throws Exception {
        DummyDto dto = new DummyDto();
        Constructor<? extends BotEvent> constructor = DummyEvent.class.getConstructor(DummyDto.class);

        RegisteredEvent registeredEvent = new RegisteredEvent(DummyDto.class, constructor);
        when(botEventProvider.getRegisteredEvent(DummyDto.class)).thenReturn(Optional.of(registeredEvent));

        Optional<BotEvent> result = factory.createEventInstance(dto);

        assertTrue(result.isPresent());
        assertInstanceOf(DummyEvent.class, result.get());
        assertEquals(dto, ((DummyEvent) result.get()).dto);
    }

    @Test
    void shouldReturnEmpty_whenNotRegistered() {
        DummyDto dto = new DummyDto();
        when(botEventProvider.getRegisteredEvent(DummyDto.class)).thenReturn(Optional.empty());

        Optional<BotEvent> result = factory.createEventInstance(dto);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmpty_whenConstructorFails() throws Exception {
        DummyDto dto = new DummyDto();
        Constructor<? extends BotEvent> constructor = FailingEvent.class.getConstructor(DummyDto.class);
        RegisteredEvent registeredEvent = new RegisteredEvent(DummyDto.class, constructor);
        when(botEventProvider.getRegisteredEvent(DummyDto.class)).thenReturn(Optional.of(registeredEvent));

        Optional<BotEvent> result = factory.createEventInstance(dto);

        assertTrue(result.isEmpty());
    }

}
