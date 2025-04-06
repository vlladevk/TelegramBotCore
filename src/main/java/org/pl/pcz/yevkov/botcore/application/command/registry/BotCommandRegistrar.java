package org.pl.pcz.yevkov.botcore.application.command.registry;

import lombok.NonNull;

import java.lang.reflect.Method;

/**
 * Defines a contract for registering bot commands at runtime.
 *
 * <p>
 * Typically used during component scanning or application startup to register
 * annotated command handler methods.
 * </p>
 */
public interface BotCommandRegistrar {

    void registerCommand(@NonNull Object handler, @NonNull Method method);
}
