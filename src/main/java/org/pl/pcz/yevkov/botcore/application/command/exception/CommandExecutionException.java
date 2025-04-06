package org.pl.pcz.yevkov.botcore.application.command.exception;

/**
 * Exception thrown to indicate that an error occurred during
 * the execution of a bot command.
 *
 * <p>
 * This exception is typically thrown by the command handler method or
 * {@link org.pl.pcz.yevkov.botcore.application.command.executor.CommandExecutor}
 * when an error occurs during method invocation.
 * </p>
 */
public class CommandExecutionException extends RuntimeException {
    public CommandExecutionException(String message) {
        super(message);
    }

    public CommandExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

}