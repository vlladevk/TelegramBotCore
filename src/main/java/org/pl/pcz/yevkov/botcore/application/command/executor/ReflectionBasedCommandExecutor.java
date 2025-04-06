package org.pl.pcz.yevkov.botcore.application.command.executor;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.command.exception.CommandExecutionException;
import org.pl.pcz.yevkov.botcore.application.command.registry.RegisteredCommand;
import org.pl.pcz.yevkov.botcore.application.command.response.TextResponse;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Default implementation of {@link CommandExecutor} that uses Java Reflection
 * to invoke command handler methods.
 * <p>
 * If the method returns {@code null}, the result is treated as an empty optional.
 * If the method returns an invalid type, an exception is raised.
 * </p>
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class ReflectionBasedCommandExecutor implements CommandExecutor {

    /**
     * Executes the provided command by invoking its method using reflection.
     * @return an optional {@link TextResponse} returned by the method
     * @throws CommandExecutionException if invocation fails or the method returns an invalid type
     */
    @Override
    public Optional<TextResponse> execute(@NonNull RegisteredCommand command, @NonNull ChatMessageReceivedDto input) {
        Method method = command.method();
        Object handler = command.handler();

        try {
            Object result = method.invoke(handler, input);
            return validateResult(result, method);
        } catch (InvocationTargetException e) {
            throw wrapException(e.getTargetException(), method);
        } catch (Exception e) {
            throw new CommandExecutionException("Unexpected error during method invocation: " + method.getName(), e);
        }
    }


    private Optional<TextResponse> validateResult(Object result, Method method) {
        if (result == null) {
            return Optional.empty();
        }

        if (result instanceof TextResponse response) {
            return Optional.of(response);
        }

        throw new CommandExecutionException("Invalid return type from method '" +
                method.getName() + "'. Expected TextResponse, but got " +
                result.getClass().getSimpleName());
    }


    private CommandExecutionException wrapException(Throwable t, Method method) {
        return new CommandExecutionException("Error while executing command method: " + method.getName(), t);
    }
}