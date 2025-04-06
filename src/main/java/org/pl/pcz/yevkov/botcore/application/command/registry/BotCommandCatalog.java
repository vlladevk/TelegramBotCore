package org.pl.pcz.yevkov.botcore.application.command.registry;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.command.exception.DuplicateCommandRegistrationException;
import org.pl.pcz.yevkov.botcore.application.command.factory.BotCommandFactory;
import org.pl.pcz.yevkov.botcore.application.command.validation.CommandSignatureValidator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Central registry for mapping command names to their corresponding {@link RegisteredCommand} metadata.
 *
 * <p>
 * Note: All command names must be unique. Attempting to register a duplicate name
 * results in a {@link DuplicateCommandRegistrationException}.
 * </p>
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class BotCommandCatalog implements BotCommandRegistrar, BotCommandProvider {

    private final CommandSignatureValidator signatureValidator;
    private final BotCommandFactory commandFactory;
    private final Map<String, RegisteredCommand> registeredCommandsMap = new LinkedHashMap<>();


    @Override
    public void registerCommand(@NonNull Object handler, @NonNull Method method) {
        signatureValidator.validate(method);
        var command = commandFactory.create(handler, method);
        var commandName = command.name();

        if (registeredCommandsMap.containsKey(commandName)) {
            throw new DuplicateCommandRegistrationException(
                    "Command name already registered: " + commandName
            );
        }

        registeredCommandsMap.put(commandName, command);

        log.info("Registered command: {} → {}.{}()",
                commandName,
                handler.getClass().getSimpleName(),
                method.getName());
    }


    @Override
    public Optional<RegisteredCommand> getRegisteredCommand(@NonNull String commandName) {
        return Optional.ofNullable(registeredCommandsMap.get(commandName));
    }

    @Override
    public Collection<RegisteredCommand> getAllRegisteredCommands() {
        return registeredCommandsMap.values();
    }
}
