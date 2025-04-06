package org.pl.pcz.yevkov.botcore.application.command.exception;

import lombok.NonNull;

/**
 * Thrown to indicate that a bot command with the same name
 * has already been registered.
 *
 * <p>
 * This exception is typically thrown by implementations of
 * {@link org.pl.pcz.yevkov.botcore.application.command.registry.BotCommandRegistrar}
 * such as {@link org.pl.pcz.yevkov.botcore.application.command.registry.BotCommandCatalog}
 * during the command registration phase.
 * </p>
 *
 * <p>
 * All command names must be unique within the registry. Attempting to register
 * a second command with an existing name will trigger this exception.
 * </p>
 */
public class DuplicateCommandRegistrationException extends RuntimeException {
    public DuplicateCommandRegistrationException(@NonNull String message) {
        super(message);
    }

}
