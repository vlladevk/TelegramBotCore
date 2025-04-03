package org.pl.pcz.yevkov.tgbottest.application.command.registry;

import lombok.NonNull;

import java.util.Collection;
import java.util.Optional;

public interface BotCommandProvider {
     Collection<RegisteredCommand> getAllRegisteredCommands();
     Optional<RegisteredCommand> getRegisteredCommand(@NonNull String command);
}
