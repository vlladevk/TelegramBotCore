package org.pl.pcz.yevkov.botcore.application.command.registry;

import lombok.NonNull;

import java.util.Collection;
import java.util.Optional;

/**
 * Defines a contract for retrieving registered bot commands.
 *
 * <p>
 * Implementations of this interface provide access to {@link RegisteredCommand}
 * metadata, typically used during command dispatching, permission checks,
 * or for generating help documentation.
 * </p>
 */
public interface BotCommandProvider {

    Collection<RegisteredCommand> getAllRegisteredCommands();

    Optional<RegisteredCommand> getRegisteredCommand(@NonNull String command);
}
