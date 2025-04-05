package org.pl.pcz.yevkov.tgbottest.application.event.exception;

import lombok.NonNull;

/**
 * Thrown to indicate that a {@link org.pl.pcz.yevkov.tgbottest.event.BotEvent} class is
 * misconfigured or does not meet the required registration criteria.
 *
 * <p>Common causes include:</p>
 * <p>
 * - Missing {@link org.pl.pcz.yevkov.tgbottest.annotation.BotEventBinding} annotation<br>
 * - Missing constructor that accepts the associated EventDto class
 * </p>
 */
public class InvalidBotEventDefinitionException extends RuntimeException {
    public InvalidBotEventDefinitionException(@NonNull String message) {
        super(message);
    }

    public InvalidBotEventDefinitionException(@NonNull String message,@NonNull Throwable cause) {
        super(message, cause);
    }
}