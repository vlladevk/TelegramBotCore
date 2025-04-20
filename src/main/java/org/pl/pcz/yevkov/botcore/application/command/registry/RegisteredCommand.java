package org.pl.pcz.yevkov.botcore.application.command.registry;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatType;
import org.pl.pcz.yevkov.botcore.domain.entity.UserRole;

import java.lang.reflect.Method;
import java.util.List;


@Builder
public record RegisteredCommand(
        @NotNull String name,
        @NotNull String description,
        boolean showInMenu,
        @NotNull UserRole userRole,
        @NotNull List<ChatType> chatTypes,
        @NotNull Object handler,
        @NotNull Method method
) {
}
