package org.pl.pcz.yevkov.botcore.application.event.registry;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pl.pcz.yevkov.botcore.application.dto.event.EventDto;
import org.pl.pcz.yevkov.botcore.application.event.exception.DuplicateEventRegistrationException;
import org.pl.pcz.yevkov.botcore.application.event.factory.BotEventFactory;
import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;

import java.lang.reflect.Constructor;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BotEventCatalogTest {
    @Mock
    BotEventFactory factory;

    @InjectMocks
    BotEventCatalog catalog;

    static class DummyEvent implements BotEvent {
        public DummyEvent(DummyDto ignore) {}
    }
    static class DummyDto implements EventDto {}

    @Nested
    class RegisterEvent {

        @Test
        void register_newEvent_registersSuccessfully() throws Exception {
            Constructor<DummyEvent> ctor = DummyEvent.class.getConstructor(DummyDto.class);
            RegisteredEvent event = new RegisteredEvent(DummyDto.class, ctor);
            when(factory.create(DummyEvent.class)).thenReturn(event);

            catalog.registerEvent(DummyEvent.class);

            Optional<RegisteredEvent> result = catalog.getRegisteredEvent(DummyDto.class);
            assertTrue(result.isPresent());
            assertEquals(DummyDto.class, result.get().clazzDto());
            assertEquals(ctor, result.get().eventConstructor());
        }

        @Test
        void register_duplicateDto_throwsException() throws Exception {
            Constructor<DummyEvent> ctor = DummyEvent.class.getConstructor(DummyDto.class);
            RegisteredEvent event = new RegisteredEvent(DummyDto.class, ctor);
            when(factory.create(DummyEvent.class)).thenReturn(event);

            catalog.registerEvent(DummyEvent.class);

            DuplicateEventRegistrationException ex = assertThrows(
                    DuplicateEventRegistrationException.class,
                    () -> catalog.registerEvent(DummyEvent.class)
            );

            assertTrue(ex.getMessage().contains(DummyDto.class.getName()));
        }
    }

    @Nested
    class GetRegisteredEvent {

        @Test
        void get_unregistered_returnsEmpty() {
            assertTrue(catalog.getRegisteredEvent(DummyDto.class).isEmpty());
        }

        @Test
        void get_registered_returnsEvent() throws Exception {
            Constructor<DummyEvent> ctor = DummyEvent.class.getConstructor(DummyDto.class);
            RegisteredEvent event = new RegisteredEvent(DummyDto.class, ctor);
            when(factory.create(DummyEvent.class)).thenReturn(event);

            catalog.registerEvent(DummyEvent.class);

            Optional<RegisteredEvent> result = catalog.getRegisteredEvent(DummyDto.class);
            assertTrue(result.isPresent());
            assertEquals(DummyDto.class, result.get().clazzDto());
        }
    }
}