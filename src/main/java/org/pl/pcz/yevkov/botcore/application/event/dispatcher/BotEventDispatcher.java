package org.pl.pcz.yevkov.botcore.application.event.dispatcher;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.dto.event.EventDto;
import org.pl.pcz.yevkov.botcore.application.event.factory.EventInstanceFactory;
import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.event.UpdateToEventDtoMapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * Dispatches an incoming Telegram {@link Update} through all registered
 * mappers and the event instance factory, returning the resulting list
 * of domain {@link BotEvent} objects.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class BotEventDispatcher {

    private final List<UpdateToEventDtoMapper<Update, ? extends EventDto>> dtoMappers;
    private final EventInstanceFactory eventInstanceFactory;

    public   List<BotEvent> dispatch(@NonNull Update update) {
        ArrayList<BotEvent> events = new ArrayList<>();
        dispatchEvents(update, events);
        logDispatchResult(update, events);
        return events;
    }


    private void dispatchEvents(@NonNull Update update, @NonNull List<BotEvent> events) {
        for (var mapper : dtoMappers) {
            log.debug("Trying bulk event mapper: {}", mapper.getClass().getSimpleName());

            if (mapper.supports(update)) {
                var dtos = mapper.mapFrom(update);
                dtos.forEach(dto -> processEventDto(dto, events));
            }
        }
    }


    private void processEventDto(@NonNull EventDto dto, @NonNull List<BotEvent> events) {
        var maybeEvent = eventInstanceFactory.createEventInstance(dto);
        if (maybeEvent.isPresent()) {
            var event = maybeEvent.get();
            events.add(event);
            log.debug("Mapped event: {} from DTO: {}", event.getEventName(), dto.getClass().getSimpleName());
        } else {
            log.warn("No registered event found for DTO: {}", dto.getClass().getName());
        }
    }


    private void logDispatchResult(@NonNull Update update, @NonNull List<BotEvent> events) {
        if (events.isEmpty()) {
            log.info("No event matched for update with ID: {}", update.getUpdateId());
        } else {
            log.debug("Total events mapped for update {}: {}", update.getUpdateId(), events.size());
        }
    }
}