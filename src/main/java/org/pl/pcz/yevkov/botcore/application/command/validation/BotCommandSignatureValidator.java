package org.pl.pcz.yevkov.botcore.application.command.validation;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.annotation.BotCommand;
import org.pl.pcz.yevkov.botcore.application.command.response.TextResponse;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Component
public class BotCommandSignatureValidator implements CommandSignatureValidator {

    @Override
    public void validate(@NonNull Object handler, @NonNull Method method) {
        validateHandlerConsistency(handler, method);
        validateParameterSignature(method);
        validateReturnType(method);
        validateAnnotationPresent(method);
    }

    // Ensures the method belongs to the handler's class hierarchy.
    private void validateHandlerConsistency(Object handler, Method method) {
        if (!method.getDeclaringClass().isAssignableFrom(handler.getClass())) {
            throw new IllegalStateException(
                    "Method " + method + " is not declared in " + handler.getClass()
            );
        }
    }

    // Ensures the method has exactly one parameter of type ChatMessageReceivedDto.
    private void validateParameterSignature(Method method) {
        if (method.getParameterCount() != 1 || !ChatMessageReceivedDto.class.equals(method.getParameterTypes()[0])) {
            throw new IllegalStateException("@" + BotCommand.class.getSimpleName() +
                    " method '" + method.getName() + "' must have exactly one parameter of type ChatMessageReceivedDto");
        }
    }

    // Ensures the method returns either TextResponse or void.
    private void validateReturnType(Method method) {
        Class<?> returnType = method.getReturnType();
        if (!TextResponse.class.equals(returnType) && !void.class.equals(returnType)) {
            throw new IllegalStateException("@" + BotCommand.class.getSimpleName() +
                    " method '" + method.getName() + "' must return either TextResponse or void");
        }
    }

    // Ensures the method is annotated with @BotCommand.
    private void validateAnnotationPresent(Method method) {
        if (!method.isAnnotationPresent(BotCommand.class)) {
            throw new IllegalStateException("Method " + method.getName() +
                    " must be annotated with @BotCommand");
        }
    }
}
