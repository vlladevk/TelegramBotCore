package org.pl.pcz.yevkov.botcore.application.command.dispatcher;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.command.access.CommandAccessResult;
import org.pl.pcz.yevkov.botcore.application.command.access.CommandPermissionChecker;
import org.pl.pcz.yevkov.botcore.application.command.error.CommandErrorHandler;
import org.pl.pcz.yevkov.botcore.application.command.exception.CommandExecutionException;
import org.pl.pcz.yevkov.botcore.application.command.executor.CommandExecutor;
import org.pl.pcz.yevkov.botcore.application.command.parser.CommandExtractor;
import org.pl.pcz.yevkov.botcore.application.command.registry.BotCommandProvider;
import org.pl.pcz.yevkov.botcore.application.command.registry.RegisteredCommand;
import org.pl.pcz.yevkov.botcore.application.command.response.TextResponse;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;
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

    public Optional<TextResponse> dispatch(@NonNull ChatMessageReceivedDto message) {
        if (message.text().isEmpty()) {
            return commandErrorHandler.handleEmptyMessage(message);
        }

        String command = commandExtractor.extract(message.text());
        log.info("Received command: '{}' from {} in {}", command, message.userId(), message.chatId());

        Optional<RegisteredCommand> commandOpt = botCommandProvider.getRegisteredCommand(command);
        if (commandOpt.isEmpty()) {
            return commandErrorHandler.handleUnknownCommand(message, command);
        }

        RegisteredCommand registeredCommand = commandOpt.get();
        CommandAccessResult access = commandPermissionChecker.hasAccess(message, registeredCommand);
        if (!access.allowed()) {
            return commandErrorHandler.handleAccessDenied(message, access, command);
        }

        return executeCommand(message, command, registeredCommand);
    }


    private Optional<TextResponse> executeCommand(ChatMessageReceivedDto message, String command, RegisteredCommand cmd) {
        try {
            log.debug("Executing command: '{}' via method {} on {}",
                    command, cmd.method().getName(), cmd.handler().getClass().getSimpleName());

            log.info("Dispatching command: '{}' for userId={} in chatId={}",
                    command, message.userId(), message.chatId());

            return commandExecutor.execute(cmd, message);
        } catch (CommandExecutionException e) {
            return commandErrorHandler.handleExecutionError(message, command, e);
        } catch (Exception e) {
            log.error("Unexpected error during dispatching of command '{}': {}",
                    command, e.getMessage(), e);
            return commandErrorHandler.handleExecutionError(message, command, e);
        }
    }
}
