package org.pl.pcz.yevkov.botcore.application.command.validation;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.annotation.BotCommand;
import org.pl.pcz.yevkov.botcore.application.command.response.TextResponse;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;

import java.lang.reflect.Method;

/**
 * Validates the signature of methods annotated with {@link BotCommand}.
 *
 * <p>
 * This validator ensures that the annotated method adheres to the following requirements:
 * </p>
 * <ul>
 *   <li>It must have exactly one parameter of type {@link ChatMessageReceivedDto}</li>
 *   <li>The return type must be either {@link TextResponse} or {@code void}</li>
 * </ul>
 *
 * <p>
 * Methods that do not meet these criteria will trigger an {@link IllegalStateException}.
 * This validation is performed at the time of registration during the bot's startup.
 * </p>
 */
public interface CommandSignatureValidator {
    void validate(@NonNull Method method);
}
