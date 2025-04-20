package org.pl.pcz.yevkov.botcore.application.command.validation;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.annotation.BotCommand;
import org.pl.pcz.yevkov.botcore.application.command.response.TextResponse;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;

import java.lang.reflect.Method;

/**
 * Validates that a given {@link BotCommand}-annotated method:
 * <ul>
 *   <li>Belongs to the specified {@code handler}</li>
 *   <li>Has one {@link ChatMessageReceivedDto} parameter</li>
 *   <li>returns {@link TextResponse} or {@code void}</li>
 * </ul>
 *
 * <p>
 * Fails with {@link IllegalStateException} if any rule is violated.
 * </p>
 */
public interface CommandSignatureValidator {
    void validate(@NonNull Object handler, @NonNull Method method);
}
