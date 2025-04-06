package org.pl.pcz.yevkov.botcore.application.event.exception;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.dto.event.EventDto;
import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;

/**
 * Thrown to indicate that a {@link EventDto}
 * type has already been registered in the event catalog.
 *
 * <p>This exception is typically raised during duplicate attempts
 * to register a {@link BotEvent}
 * class for the same DTO type.</p>
 * <p>
 * This exception is typically raised during application startup or manual registration
 * if a duplicate mapping is detected in the bot event catalog.
 */
public class DuplicateEventRegistrationException extends RuntimeException {

    public DuplicateEventRegistrationException(@NonNull String message) {
        super(message);
    }

}