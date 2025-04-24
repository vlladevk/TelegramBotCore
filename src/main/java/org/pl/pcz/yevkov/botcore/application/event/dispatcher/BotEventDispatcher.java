package org.pl.pcz.yevkov.botcore.application.event.dispatcher;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.event.UpdateToEventMapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;


@Log4j2
@Component
@RequiredArgsConstructor
public class BotEventDispatcher {

    private final List<UpdateToEventMapper<? extends BotEvent>> eventMappers;

    public List<BotEvent> dispatch(@NonNull Update update) {
        ArrayList<BotEvent> events = new ArrayList<>();
        dispatchEvents(update, events);
        logDispatchResult(update, events);
        return events;
    }

    private void dispatchEvents(Update update, List<BotEvent> events) {
        for (var mapper : eventMappers) {
            log.debug("Trying bulk event mapper: {}", mapper.getClass().getSimpleName());
            if (mapper.supports(update)) {
                mapper.mapFrom(update).forEach(evt -> addAndLog(evt, events));
            }
        }
    }

    private void addAndLog(BotEvent evt, List<BotEvent> events) {
        events.add(evt);
        log.debug("Mapped event: {} added to events list", evt.getEventName());
    }

    private void logDispatchResult(Update update, List<BotEvent> events) {
        if (events.isEmpty()) {
            log.info("No event matched for update with ID: {}", update.getUpdateId());
        } else {
            log.debug("Total events mapped for update {}: {}", update.getUpdateId(), events.size());
        }
    }
}