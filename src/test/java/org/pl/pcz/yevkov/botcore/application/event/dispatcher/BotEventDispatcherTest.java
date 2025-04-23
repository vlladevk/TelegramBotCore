package org.pl.pcz.yevkov.botcore.application.event.dispatcher;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pl.pcz.yevkov.botcore.application.dto.event.EventDto;
import org.pl.pcz.yevkov.botcore.application.event.factory.EventInstanceFactory;
import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.event.UpdateToEventDtoMapper;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BotEventDispatcherTest {

    @Mock
    UpdateToEventDtoMapper<Update, EventDto> mapper;

    @Mock
    EventInstanceFactory eventFactory;

    @Mock
    Update update;

    @Mock
    EventDto dto;

    @Mock
    BotEvent event;

    @InjectMocks
    BotEventDispatcher dispatcher;

    @Test
    void dispatch_withMatchingMapperAndEventInstance_returnsEvent() {
        when(mapper.supports(update)).thenReturn(true);
        when(mapper.mapFrom(update)).thenReturn(singletonList(dto));
        when(eventFactory.createEventInstance(dto)).thenReturn(Optional.of(event));

        dispatcher = new BotEventDispatcher(List.of(mapper), eventFactory);

        List<BotEvent> result = dispatcher.dispatch(update);

        assertEquals(1, result.size());
        assertEquals(event, result.getFirst());

        verify(mapper).supports(update);
        verify(mapper).mapFrom(update);
        verify(eventFactory).createEventInstance(dto);
    }

    @Test
    void dispatch_withNoSupportingMappers_returnsEmptyList() {
        when(mapper.supports(update)).thenReturn(false);
        dispatcher = new BotEventDispatcher(List.of(mapper), eventFactory);

        List<BotEvent> result = dispatcher.dispatch(update);

        assertEquals(0, result.size());
        verify(mapper).supports(update);
        verify(mapper, never()).mapFrom(any());
        verify(eventFactory, never()).createEventInstance(any());
    }

    @Test
    void dispatch_withNoEventFromFactory_returnsEmptyList() {
        when(mapper.supports(update)).thenReturn(true);
        when(mapper.mapFrom(update)).thenReturn(singletonList(dto));
        when(eventFactory.createEventInstance(dto)).thenReturn(Optional.empty());

        dispatcher = new BotEventDispatcher(List.of(mapper), eventFactory);

        List<BotEvent> result = dispatcher.dispatch(update);

        assertEquals(0, result.size());
        verify(mapper).mapFrom(update);
        verify(eventFactory).createEventInstance(dto);
    }
}