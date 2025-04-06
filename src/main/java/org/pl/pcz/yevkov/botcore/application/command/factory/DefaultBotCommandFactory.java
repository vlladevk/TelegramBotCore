package org.pl.pcz.yevkov.botcore.application.command.factory;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.annotation.BotCommand;
import org.pl.pcz.yevkov.botcore.application.command.registry.RegisteredCommand;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatType;
import org.pl.pcz.yevkov.botcore.domain.entity.UserRole;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;


@Log4j2
@Component
public class DefaultBotCommandFactory implements BotCommandFactory {

    @Override
    public RegisteredCommand create(@NonNull Object handler, @NonNull Method method) {
        BotCommand annotation = method.getAnnotation(BotCommand.class);

        String name = annotation.name().isEmpty()
                ? "/" + processMethodName(method.getName())
                : annotation.name();
        String description = annotation.description();
        boolean showInMenu = annotation.showInMenu();
        UserRole userRole = annotation.userRole();
        ChatType[] chatTypes = annotation.chatTypes();

        RegisteredCommand registeredCommand = RegisteredCommand.builder()
                .name(name)
                .description(description)
                .showInMenu(showInMenu)
                .userRole(userRole)
                .chatTypes(chatTypes)
                .handler(handler)
                .method(method)
                .build();

        log.info("Registered bot command: {} from {}#{} (role={}, chats={})",
                registeredCommand.name(),
                handler.getClass().getSimpleName(),
                method.getName(),
                userRole,
                Arrays.toString(chatTypes)
        );

        return registeredCommand;
    }

    /**
     * Converts a camelCase Java method name into a snake_case command name.
     *
     * <p>
     * Example: {@code startBot → start_bot}
     * </p>
     *
     * @param name the original method name
     * @return the converted command name in snake_case
     */
    private String processMethodName(String name) {
        return name.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
