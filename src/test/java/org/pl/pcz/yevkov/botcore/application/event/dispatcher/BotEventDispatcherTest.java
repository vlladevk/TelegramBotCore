package org.pl.pcz.yevkov.botcore.application.event.dispatcher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.event.UpdateToEventMapper;
import org.telegram.telegrambots.meta.api.objects.Update;
import static org.mockito.Mockito.mock;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BotEventDispatcherTest {

    @Mock
    private UpdateToEventMapper<BotEvent> mapper;

    @Mock
    private Update update;

    @Mock
    private BotEvent event;

    private BotEventDispatcher dispatcher;

    public interface BotEventMapper extends UpdateToEventMapper<BotEvent> {}

    @BeforeEach
    void setUp() {
        dispatcher = new BotEventDispatcher(List.of(mapper));
    }

    @Test
    void dispatch_withMatchingMapper_returnsEvent() {
        when(mapper.supports(update)).thenReturn(true);
        when(mapper.mapFrom(update)).thenReturn(singletonList(event));

        List<BotEvent> result = dispatcher.dispatch(update);

        assertEquals(1, result.size());
        assertSame(event, result.getFirst());

        verify(mapper).supports(update);
        verify(mapper).mapFrom(update);
    }

    @Test
    void dispatch_withNoSupportingMappers_returnsEmptyList() {
        when(mapper.supports(update)).thenReturn(false);

        List<BotEvent> result = dispatcher.dispatch(update);

        assertTrue(result.isEmpty());

        verify(mapper).supports(update);
        verify(mapper, never()).mapFrom(any());
    }

    @Test
    void dispatch_withMultipleMappers_filtersCorrectly() {
        UpdateToEventMapper<BotEvent> mapper2 = mock(BotEventMapper.class);

        when(mapper.supports(update)).thenReturn(false);
        when(mapper2.supports(update)).thenReturn(true);
        BotEvent event2 = mock(BotEvent.class);
        when(mapper2.mapFrom(update)).thenReturn(List.of(event2));

        dispatcher = new BotEventDispatcher(List.of(mapper, mapper2));

        List<BotEvent> result = dispatcher.dispatch(update);

        assertEquals(1, result.size());
        assertSame(event2, result.getFirst());

        verify(mapper).supports(update);
        verify(mapper2).supports(update);
        verify(mapper2).mapFrom(update);
        verify(mapper, never()).mapFrom(any());
    }
}
