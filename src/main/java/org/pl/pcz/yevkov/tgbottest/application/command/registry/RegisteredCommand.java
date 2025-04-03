package org.pl.pcz.yevkov.tgbottest.application.command.registry;


import org.pl.pcz.yevkov.tgbottest.entity.UserRole;
import org.pl.pcz.yevkov.tgbottest.entity.ChatType;

import java.lang.reflect.Method;

public record RegisteredCommand(
        String name,
        String description,
        boolean showInMenu,
        UserRole userRole,
        ChatType[] chatTypes,
        Object handler,
        Method method
) {}
