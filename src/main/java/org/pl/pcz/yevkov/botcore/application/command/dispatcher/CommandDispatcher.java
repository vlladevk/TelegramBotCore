package org.pl.pcz.yevkov.botcore.application.command.dispatcher;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.command.access.CommandAccessResult;
import org.pl.pcz.yevkov.botcore.application.command.access.CommandPermissionChecker;
import org.pl.pcz.yevkov.botcore.application.command.error.CommandErrorHandler;
import org.pl.pcz.yevkov.botcore.application.command.exception.CommandExecutionException;
import org.pl.pcz.yevkov.botcore.application.command.executor.CommandExecutor;
import org.pl.pcz.yevkov.botcore.application.command.parser.Command;
import org.pl.pcz.yevkov.botcore.application.command.parser.CommandExtractor;
import org.pl.pcz.yevkov.botcore.application.command.registry.BotCommandProvider;
import org.pl.pcz.yevkov.botcore.application.command.registry.RegisteredCommand;
import org.pl.pcz.yevkov.botcore.application.command.response.TextResponse;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.botcore.domain.event.UnrecognizedCommandEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
@Log4j2
public class CommandDispatcher {
    private final BotCommandProvider botCommandProvider;
    private final CommandPermissionChecker commandPermissionChecker;
    private final CommandExecutor commandExecutor;
    private final CommandErrorHandler commandErrorHandler;
    private final CommandExtractor commandExtractor;
    private final ApplicationEventPublisher publisher;

    public Optional<TextResponse> dispatch(@NonNull ChatMessageReceivedDto message) {
        if (message.text().isEmpty()) {
            return Optional.of(commandErrorHandler.handleEmptyMessage(message));
        }

        Command command = commandExtractor.extract(message.text());

        log.debug("Received command: '{}'", command);

        Optional<RegisteredCommand> commandOpt = botCommandProvider.getRegisteredCommand(command.text());
        if (commandOpt.isEmpty()) {
            publisher.publishEvent(new UnrecognizedCommandEvent(message));
            if (command.isExplicitCommand()) {
                return Optional.of(commandErrorHandler.handleUnknownCommand(message, command.text()));
            }
            return Optional.empty();
        }

        RegisteredCommand registeredCommand = commandOpt.get();
        CommandAccessResult access = commandPermissionChecker.hasAccess(message, registeredCommand);
        if (!access.allowed()) {
            return Optional.of(commandErrorHandler.handleAccessDenied(message, access, command.text()));
        }

        return executeCommand(message, command.text(), registeredCommand);
    }


    private Optional<TextResponse> executeCommand(ChatMessageReceivedDto message, String command, RegisteredCommand cmd) {
        try {
            log.debug("Executing command: '{}' via method {} on {}",
                    command, cmd.method().getName(), cmd.handler().getClass().getSimpleName());

            log.debug("Dispatching command: '{}'",
                    command);

            return commandExecutor.execute(cmd, message);
        } catch (CommandExecutionException e) {
            return Optional.of(commandErrorHandler.handleExecutionError(message, command, e));
        } catch (Exception e) {
            log.error("Unexpected error during dispatching of command '{}': {}",
                    command, e.getMessage(), e);
            return Optional.of(commandErrorHandler.handleExecutionError(message, command, e));
        }
    }
}
