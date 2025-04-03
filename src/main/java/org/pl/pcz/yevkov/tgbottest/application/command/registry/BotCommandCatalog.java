package org.pl.pcz.yevkov.tgbottest.application.command.registry;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.tgbottest.application.command.factory.BotCommandFactory;
import org.pl.pcz.yevkov.tgbottest.application.command.validation.CommandSignatureValidator;
import org.springframework.stereotype.Component;


import java.lang.reflect.Method;
import java.util.*;

@Log4j2
@Component
@RequiredArgsConstructor
public class BotCommandCatalog implements BotCommandRegistrar, BotCommandProvider {
    private final CommandSignatureValidator commandSignatureValidator;
    private final BotCommandFactory botCommandFactory;

    private final Map<String, RegisteredCommand> commandMap = new LinkedHashMap<>();

    @Override
    public void registerCommand(@NonNull Object handler, @NonNull Method method) {
        commandSignatureValidator.validate(method);
        RegisteredCommand command = botCommandFactory.create(handler, method);
        if (commandMap.containsKey(command.name())) {
            throw new IllegalStateException("Command duplication: " + command.name());
        }
        commandMap.put(command.name(), command);
        log.debug("Registered command: {}", command.name());
    }

    @Override
    public Collection<RegisteredCommand> getAllRegisteredCommands() {
        return commandMap.values();
    }

    @Override
    public Optional<RegisteredCommand> getRegisteredCommand(@NonNull String command) {
        RegisteredCommand registeredCommand = commandMap.get(command);
        return  Optional.ofNullable(registeredCommand);
    }
}
