package org.pl.pcz.yevkov.tgbottest.application.command.dispatcher;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.pl.pcz.yevkov.tgbottest.application.command.access.CommandPermissionChecker;
import org.pl.pcz.yevkov.tgbottest.application.command.access.CommandAccessResult;
import org.pl.pcz.yevkov.tgbottest.application.command.registry.BotCommandProvider;
import org.pl.pcz.yevkov.tgbottest.application.helper.UpdateHelper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;


@Component
@RequiredArgsConstructor
@Log4j2
public class CommandDispatcher {
    private final BotCommandProvider botCommandProvider;
    private final CommandPermissionChecker commandPermissionChecker;
    private final UpdateHelper updateHelper;

    public Optional<SendMessage> handle(@NonNull Update update) {
        if (!hasText(update)) {
            return Optional.of(updateHelper.generateMessage(update, "input message or text is Empty"));
        }
        Long chatId = updateHelper.extractChatId(update);
        Long userId = updateHelper.extractUserId(update);
        String command = extractCommand(update);

        log.info("Received command: '{}' from userId={} in chatId={}", command, userId, chatId);

        var registeredCommandOptional = botCommandProvider.getRegisteredCommand(command);

        if (registeredCommandOptional.isEmpty()) {
            log.debug("Unknown command received: {} from userId={} in chatId={}", command, userId, chatId);

            return Optional.of( updateHelper.generateMessage(update, "Unknown command: " + command));
        }
        try {
            var registeredCommand = registeredCommandOptional.get();
            var method = registeredCommand.method();
            log.debug("Executing command: '{}' via method {} on {}",
                    command, method.getName(), registeredCommand.handler().getClass().getSimpleName());
            CommandAccessResult access = commandPermissionChecker.hasAccess(update, registeredCommand);
            if (!access.allowed()) {
                log.debug("Command '{}' access denied by {} for userId={} in chatId={}",
                        command, access.reason(), userId, chatId);
                return Optional.of(updateHelper.generateMessage(update, access.reason()));
            }

            log.info("Dispatching command: '{}' for userId={} in chatId={}", command, userId, chatId);
            // Can be null, if return methods type is void
            Object result = method.invoke(registeredCommand.handler(), update);
            return Optional.ofNullable((SendMessage) result);
        } catch (Exception e) {
            log.error("Error while processing command '{}' for userId={} in chatId={}: {}",
                    command, userId, chatId, e.getMessage(), e);
            return Optional.of(updateHelper.generateMessage(update, "Error while processing command: " + command + " Error: " + e.getMessage()));
        }
    }

    public boolean isCommandAllowed(@NonNull Update update) {
        String command = extractCommand(update);
        return botCommandProvider.getRegisteredCommand(command).isPresent();
    }

    private boolean hasText(@NonNull Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    private String extractCommand(@NonNull Update update) {
        String text = update.getMessage().getText().trim();
        return text.split(" ")[0].split("@")[0];
    }
}
