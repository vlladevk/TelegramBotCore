package org.pl.pcz.yevkov.botcore.application.command.factory;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.command.registry.RegisteredCommand;

import java.lang.reflect.Method;

public interface BotCommandFactory {
    RegisteredCommand create(@NonNull Object handler, @NonNull Method method);
}
