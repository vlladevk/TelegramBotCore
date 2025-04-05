package org.pl.pcz.yevkov.tgbottest.application.event.dispatcher;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.tgbottest.application.event.factory.EventInstanceFactory;
import org.pl.pcz.yevkov.tgbottest.dto.event.EventDto;
import org.pl.pcz.yevkov.tgbottest.event.BotEvent;
import org.pl.pcz.yevkov.tgbottest.mapper.event.BotEventBulkMapper;
import org.pl.pcz.yevkov.tgbottest.mapper.event.BotEventMapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;



/**
 * Central dispatcher responsible for mapping incoming Telegram {@link Update} objects
 * into corresponding business-level {@link BotEvent} instances.
 *
 * <p>This component delegates the following responsibilities:</p>
 * <p>
 * - Extraction of event DTOs from updates using {@link BotEventMapper} and {@link BotEventBulkMapper}<br>
 * - Instantiation of {@link BotEvent} objects via {@link EventInstanceFactory}
 * </p>
 *
 * <p>
 * Note: This class only performs mapping and instantiation.
 * Actual event handling should be performed by a separate processing layer.
 * </p>
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class BotEventDispatcher {

    // Handles single EventDTO extraction from Update (e.g. one-to-one mapping)
    private final List<BotEventMapper<Update, ? extends EventDto>> singleDtoMappers;

    // Handles multiple EventDTOs from a single Update (e.g. message with multiple entities)
    private final List<BotEventBulkMapper<Update, ? extends EventDto>> bulkMappers;

    // Factory that converts EventDto to a BotEvent instance
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
     * Performs the full dispatching process for a given {@link Update}:
     * <p>
     * - Passes the update through all registered event mappers<br>
     * - Maps matched {@link EventDto} instances to {@link BotEvent} objects<br>
     * - Collects all successfully instantiated events into a list
     * </p>
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

    /**
     * Iterates over all registered {@link BotEventMapper} instances and processes
     * single {@link EventDto} mappings from the given {@link Update}.
     *
     * @param update the update to process
     * @param events the list to collect resulting {@link BotEvent} instances
     */
    private void dispatchSingleEvents(@NonNull Update update, @NonNull List<BotEvent> events) {
        for (var mapper : singleDtoMappers) {
            log.debug("Trying single event mapper: {}", mapper.getClass().getSimpleName());

            if (!mapper.supports(update)) continue;

            var dto = mapper.mapFrom(update);
            processEventDto(dto, events);
        }
    }

    /**
     * Iterates over all registered {@link BotEventBulkMapper} instances and processes
     * multiple {@link EventDto} objects returned from each mapper.
     *
     * @param update the update to process
     * @param events the list to collect resulting {@link BotEvent} instances
     */
    private void dispatchBulkEvents(@NonNull Update update, @NonNull List<BotEvent> events) {
        for (var mapper : bulkMappers) {
            log.debug("Trying bulk event mapper: {}", mapper.getClass().getSimpleName());

            if (!mapper.supports(update)) continue;

            var dtos = mapper.mapAll(update);
            for (var dto : dtos) {
                processEventDto(dto, events);
            }
        }
    }

    /**
     * Attempts to create a {@link BotEvent} instance from the given {@link EventDto}
     * using the {@link EventInstanceFactory}. If no event is registered for the DTO,
     * the method skips it.
     *
     * @param dto the source {@link EventDto} to convert
     * @param events the list to collect the created {@link BotEvent}, if available
     */
    private void processEventDto(@NonNull EventDto dto, @NonNull List<BotEvent> events) {
        eventInstanceFactory.createEventInstance(dto).ifPresentOrElse(
                event -> {
                    events.add(event);
                    log.debug("Mapped event: {} from DTO: {}", event.getClass().getSimpleName(),
                            dto.getClass().getSimpleName());
                },
                () -> log.warn("No registered event found for DTO: {}", dto.getClass().getName())
        );
    }

    /**
     * Logs the outcome of the dispatching process for the given {@link Update}.
     * Emits a debug log if events were mapped, or an info log if no matches were found.
     *
     * @param update the processed {@link Update}
     * @param events the list of resulting {@link BotEvent} instances
     */
    private void logDispatchResult(@NonNull Update update, @NonNull List<BotEvent> events) {
        if (events.isEmpty()) {
            log.info("No event matched for update with ID: {}", update.getUpdateId());
        } else {
            log.debug("Total events mapped for update {}: {}", update.getUpdateId(), events.size());
        }
    }
}