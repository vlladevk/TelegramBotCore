package org.pl.pcz.yevkov.botcore.application.event.dispatcher;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.dto.event.EventDto;
import org.pl.pcz.yevkov.botcore.application.event.factory.EventInstanceFactory;
import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.event.BotEventBulkMapper;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.event.BotEventMapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;


@Log4j2
@Component
@RequiredArgsConstructor
public class BotEventDispatcher {

    private final List<BotEventMapper<Update, ? extends EventDto>> singleDtoMappers;
    private final List<BotEventBulkMapper<Update, ? extends EventDto>> bulkMappers;
    private final EventInstanceFactory eventInstanceFactory;

    /**
     * Entry point for processing an incoming Telegram {@link Update}.
     *
     * @param update the raw {@link Update} received from Telegram
     * @return a list of {@link BotEvent} instances ready for business-level handling
     */
    public List<BotEvent> handle(@NonNull Update update) {
        return dispatch(update);
    }


    /**
     * Performs the full dispatching process for a given {@link Update}.
     * - Passes the update through all registered event mappers
     * - Maps matched {@link EventDto} instances to {@link BotEvent} objects
     * - Collects all successfully instantiated events into a list
     *
     * @param update the {@link Update} to process
     * @return a list of mapped {@link BotEvent} instances
     */
    private List<BotEvent> dispatch(@NonNull Update update) {
        ArrayList<BotEvent> events = new ArrayList<>();
        dispatchSingleEvents(update, events);
        dispatchBulkEvents(update, events);
        logDispatchResult(update, events);
        return events;
    }


    private void dispatchSingleEvents(@NonNull Update update, @NonNull List<BotEvent> events) {
        for (var mapper : singleDtoMappers) {
            log.debug("Trying single event mapper: {}", mapper.getClass().getSimpleName());

            if (mapper.supports(update)) {
                var dto = mapper.mapFrom(update);
                processEventDto(dto, events);
            }
        }
    }


    private void dispatchBulkEvents(@NonNull Update update, @NonNull List<BotEvent> events) {
        for (var mapper : bulkMappers) {
            log.debug("Trying bulk event mapper: {}", mapper.getClass().getSimpleName());

            if (mapper.supports(update)) {
                var dtos = mapper.mapAll(update);
                for (var dto : dtos) {
                    processEventDto(dto, events);
                }
            }
        }
    }


    private void processEventDto(@NonNull EventDto dto, @NonNull List<BotEvent> events) {
        eventInstanceFactory.createEventInstance(dto).ifPresentOrElse(
                event -> {
                    events.add(event);
                    log.debug("Mapped event: {} from DTO: {}", event.getEventName(),
                            dto.getClass().getSimpleName());
                },
                () -> log.warn("No registered event found for DTO: {}", dto.getClass().getName())
        );
    }


    private void logDispatchResult(@NonNull Update update, @NonNull List<BotEvent> events) {
        if (events.isEmpty()) {
            log.info("No event matched for update with ID: {}", update.getUpdateId());
        } else {
            log.debug("Total events mapped for update {}: {}", update.getUpdateId(), events.size());
        }
    }
}