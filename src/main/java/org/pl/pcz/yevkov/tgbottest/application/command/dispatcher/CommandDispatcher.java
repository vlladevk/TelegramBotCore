package org.pl.pcz.yevkov.tgbottest.application.command.dispatcher;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.pl.pcz.yevkov.tgbottest.application.command.access.CommandPermissionChecker;
import org.pl.pcz.yevkov.tgbottest.application.command.access.CommandAccessResult;
import org.pl.pcz.yevkov.tgbottest.application.command.registry.BotCommandProvider;
import org.pl.pcz.yevkov.tgbottest.application.helper.UpdateHelper;
import org.pl.pcz.yevkov.tgbottest.dto.event.ChatId;
import org.pl.pcz.yevkov.tgbottest.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.tgbottest.dto.event.UserId;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;


@Component
@RequiredArgsConstructor
@Log4j2
public class CommandDispatcher {
    private final BotCommandProvider botCommandProvider;
    private final CommandPermissionChecker commandPermissionChecker;
    private final UpdateHelper updateHelper;

    public Optional<SendMessage> handle(@NonNull ChatMessageReceivedDto receivedMessage) {
        if (receivedMessage.text().isEmpty()) {
            log.debug("received text message is empty or null {}", receivedMessage);
            return Optional.of(updateHelper.generateMessage(receivedMessage, "input message or text is Empty"));
        }

        ChatId chatId = receivedMessage.chatId();
        UserId userId = receivedMessage.userId();
        String command = extractCommand(receivedMessage.text());

        log.info("Received command: '{}' from {} in {}", command, userId, chatId);

        var registeredCommandOptional = botCommandProvider.getRegisteredCommand(command);

        if (registeredCommandOptional.isEmpty()) {
            log.debug("Unknown command received: {} from {} in {}", command, userId, chatId);
            return Optional.of( updateHelper.generateMessage(receivedMessage, "Unknown command: " + command));
        }
        try {
            var registeredCommand = registeredCommandOptional.get();
            var method = registeredCommand.method();
            log.debug("Executing command: '{}' via method {} on {}",
                    command, method.getName(), registeredCommand.handler().getClass().getSimpleName());
            CommandAccessResult access = commandPermissionChecker.hasAccess(receivedMessage, registeredCommand);
            if (!access.allowed()) {
                log.debug("Command '{}' access denied by {} for userId={} in chatId={}",
                        command, access.reason(), userId, chatId);
                return Optional.of(updateHelper.generateMessage(receivedMessage, access.reason()));
            }

            log.info("Dispatching command: '{}' for userId={} in chatId={}", command, userId, chatId);
            // Can be null, if return methods type is void
            Object result = method.invoke(registeredCommand.handler(), receivedMessage);
            return Optional.ofNullable((SendMessage) result);
        } catch (Exception e) {
            log.error("Error while processing command '{}' for userId={} in chatId={}: {}",
                    command, userId, chatId, e.getMessage(), e);
            return Optional.of(updateHelper.generateMessage(receivedMessage, "Error while processing command: " + command + " Error: " + e.getMessage()));
        }
    }

    public boolean isCommandAllowed(@NonNull String text) {
        String command = extractCommand(text);
        return botCommandProvider.getRegisteredCommand(command).isPresent();
    }

    private String extractCommand(@NonNull String text) {
        return text.split(" ")[0].split("@")[0];
    }
}
