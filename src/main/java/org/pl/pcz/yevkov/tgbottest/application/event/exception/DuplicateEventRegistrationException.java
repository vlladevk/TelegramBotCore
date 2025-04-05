package org.pl.pcz.yevkov.tgbottest.application.event.exception;

import lombok.NonNull;

/**
 * Thrown to indicate that a {@link org.pl.pcz.yevkov.tgbottest.dto.event.EventDto}
 * type has already been registered in the event catalog.
 *
 * <p>This exception is typically raised during duplicate attempts
 * to register a {@link org.pl.pcz.yevkov.tgbottest.event.BotEvent}
 * class for the same DTO type.</p>
 */
public class DuplicateEventRegistrationException extends RuntimeException {

  public DuplicateEventRegistrationException(@NonNull String message) {
    super(message);
  }

}