package org.pl.pcz.yevkov.botcore.application.command.dispatcher;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.command.access.CommandAccessResult;
import org.pl.pcz.yevkov.botcore.application.command.access.CommandPermissionChecker;
import org.pl.pcz.yevkov.botcore.application.command.error.CommandErrorHandler;
import org.pl.pcz.yevkov.botcore.application.command.executor.CommandExecutor;
import org.pl.pcz.yevkov.botcore.application.command.parser.CommandExtractor;
import org.pl.pcz.yevkov.botcore.application.command.registry.BotCommandProvider;
import org.pl.pcz.yevkov.botcore.application.command.response.TextResponse;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;
import org.pl.pcz.yevkov.botcore.domain.vo.UserId;
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


    public Optional<TextResponse> handle(@NonNull ChatMessageReceivedDto receivedMessage) {
        if (receivedMessage.text().isEmpty()) {
            return commandErrorHandler.handleEmptyMessage(receivedMessage);
        }

        ChatId chatId = receivedMessage.chatId();
        UserId userId = receivedMessage.userId();
        String command = commandExtractor.extract(receivedMessage.text());

        log.info("Received command: '{}' from {} in {}", command, userId, chatId);

        var registeredCommandOptional = botCommandProvider.getRegisteredCommand(command);

        if (registeredCommandOptional.isEmpty()) {
            return commandErrorHandler.handleUnknownCommand(receivedMessage, command);
        }
        try {
            var registeredCommand = registeredCommandOptional.get();
            var method = registeredCommand.method();
            log.debug("Executing command: '{}' via method {} on {}",
                    command, method.getName(), registeredCommand.handler().getClass().getSimpleName());
            CommandAccessResult access = commandPermissionChecker.hasAccess(receivedMessage, registeredCommand);
            if (!access.allowed()) {
                return commandErrorHandler.handleAccessDenied(receivedMessage, access, command);
            }

            log.info("Dispatching command: '{}' for userId={} in chatId={}", command, userId, chatId);

            return commandExecutor.execute(registeredCommand, receivedMessage);
        } catch (Exception e) {
            return commandErrorHandler.handleExecutionError(receivedMessage, command, e);
        }
    }

    public boolean isCommandAllowed(@NonNull String text) {
        String command = commandExtractor.extract(text);
        return botCommandProvider.getRegisteredCommand(command).isPresent();
    }
}
