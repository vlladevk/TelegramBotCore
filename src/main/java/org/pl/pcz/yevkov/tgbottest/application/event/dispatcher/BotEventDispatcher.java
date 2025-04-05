package org.pl.pcz.yevkov.tgbottest.application.event.dispatcher;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.tgbottest.application.event.registry.BotEventProvider;
import org.pl.pcz.yevkov.tgbottest.dto.event.EventDto;
import org.pl.pcz.yevkov.tgbottest.mapper.event.BotEventBulkMapper;
import org.pl.pcz.yevkov.tgbottest.mapper.event.BotEventMapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class BotEventDispatcher {
    private final BotEventProvider botEventProvider;

    private final List<BotEventMapper<Update, ? extends EventDto>> singleEventMappers;
    private final List<BotEventBulkMapper<Update, ? extends EventDto>> bulkEventMappers;

    public ArrayList<Object> handle(@NonNull Update update) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        ArrayList<Object> events = new ArrayList<>();
        for (var singleMapper : singleEventMappers) {
            boolean isSupport = singleMapper.supports(update);
            if (isSupport) {
                EventDto eventDto = singleMapper.mapFrom(update);
                var eventClazzDto = eventDto.getClass();
                var registeredEvent = botEventProvider.getRegisteredEvent(eventClazzDto);
                if (registeredEvent.isPresent()) {
                    Object event = registeredEvent.get().eventConstructor().newInstance(eventDto);
                    events.add(event);
                }
            }
        }

        for (var bulkMapper : bulkEventMappers) {
            boolean isSupport = bulkMapper.supports(update);
            if (isSupport) {
                List<? extends EventDto> eventsDto = bulkMapper.mapAll(update);
                if (!eventsDto.isEmpty()) {
                    var eventClazzDto = eventsDto.getFirst().getClass();
                    for (var eventDto : eventsDto) {
                        var registeredEvent = botEventProvider.getRegisteredEvent(eventClazzDto);
                        if (registeredEvent.isPresent()) {
                            Object event = registeredEvent.get().eventConstructor().newInstance(eventDto);
                            events.add(event);
                        }
                    }
                }
            }
        }
        return events;
    }
}
