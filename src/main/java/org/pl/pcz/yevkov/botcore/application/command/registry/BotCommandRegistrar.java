package org.pl.pcz.yevkov.botcore.application.command.registry;

import lombok.NonNull;

import java.lang.reflect.Method;

public interface BotCommandRegistrar {
     void registerCommand(@NonNull Object handler, @NonNull Method method);
}
