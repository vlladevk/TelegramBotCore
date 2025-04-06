package org.pl.pcz.yevkov.tgbottest.application.command.executor;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.tgbottest.application.command.registry.RegisteredCommand;
import org.pl.pcz.yevkov.tgbottest.application.command.response.TextResponse;
import org.pl.pcz.yevkov.tgbottest.application.message.factory.MessageResponseFactory;
import org.pl.pcz.yevkov.tgbottest.dto.event.ChatMessageReceivedDto;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;


@Log4j2
@Component
@RequiredArgsConstructor
public class ReflectionBasedCommandExecutor implements CommandExecutor {
    private final MessageResponseFactory messageResponseFactory;

    @Override
    public Optional<TextResponse> execute(@NonNull RegisteredCommand command, @NonNull ChatMessageReceivedDto input) {
        Method method = command.method();
        Object handler = command.handler();

        try {
            Object result = method.invoke(handler, input);
            if (result == null) {
                return Optional.empty();
            }
            if (result instanceof TextResponse response) {
                return Optional.of(response);
            }

            log.warn("Invalid return type from command '{}'. Expected TextResponse, got {}",
                    method.getName(), result.getClass().getName());
            var fallback = messageResponseFactory.generateResponse(
                    input, "Internal command error. Invalid return type."
            );
            return Optional.of(fallback);

        } catch (Exception e) {
            log.error("Command execution error in method {}: {}", method.getName(), e.getMessage(), e);
            var errorResponse = messageResponseFactory.generateResponse(
                    input, "Error while executing command: " + e.getMessage()
            );
            return Optional.of(errorResponse);
        }
    }
}
