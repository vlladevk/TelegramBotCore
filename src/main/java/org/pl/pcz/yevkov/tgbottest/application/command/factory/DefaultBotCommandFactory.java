package org.pl.pcz.yevkov.tgbottest.application.command.factory;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.tgbottest.annotation.BotCommand;
import org.pl.pcz.yevkov.tgbottest.application.command.registry.RegisteredCommand;
import org.pl.pcz.yevkov.tgbottest.entity.ChatType;
import org.pl.pcz.yevkov.tgbottest.entity.UserRole;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Log4j2
@Component
public class DefaultBotCommandFactory implements BotCommandFactory {
    @Override
    public RegisteredCommand create(@NonNull Object handler, @NonNull Method method) {
        BotCommand annotation = method.getAnnotation(BotCommand.class);
        String name = annotation.name().isEmpty() ? "/" + processMethodName(method.getName()) : annotation.name();
        String description = annotation.description();
        boolean showInMenu = annotation.showInMenu();
        UserRole userRole = annotation.userRole();
        ChatType[] typyChats = annotation.chatTypes();
        RegisteredCommand registeredCommand = new RegisteredCommand(name, description, showInMenu, userRole, typyChats,handler, method);
        log.info("Registered bot command: {} from {}#{}",
                registeredCommand.name(),
                handler.getClass().getSimpleName(),
                method.getName());
        return registeredCommand;
    }
    // startBot -> start_bot
    private String processMethodName(String name) {
        return name.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
