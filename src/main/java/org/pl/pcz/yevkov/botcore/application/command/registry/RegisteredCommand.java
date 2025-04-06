package org.pl.pcz.yevkov.botcore.application.command.registry;


import org.pl.pcz.yevkov.botcore.domain.entity.UserRole;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatType;

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
