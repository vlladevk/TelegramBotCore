package org.pl.pcz.yevkov.tgbottest.application;

import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pl.pcz.yevkov.tgbottest.annotation.BotCommand;
import org.pl.pcz.yevkov.tgbottest.annotation.CommandController;
import org.pl.pcz.yevkov.tgbottest.application.model.RegisteredCommand;
import org.pl.pcz.yevkov.tgbottest.entity.UserRole;
import org.pl.pcz.yevkov.tgbottest.entity.ChatType;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
    public class CommandRegistry {

    private final ApplicationContext applicationContext;

    private final Map<String, RegisteredCommand> commandMap = new LinkedHashMap<>();


    /** Scans @CommandController for methods annotated with @BotCommand,
     *  validates method signatures (must take Update and return void or SendMessage),
     *  and registers them by command name. Throws an exception on duplicates or invalid methods.
    **/
    @PostConstruct
    public void init() {
        var beans = applicationContext.getBeansWithAnnotation(CommandController.class);
        var commandControllers = beans.values();
        commandControllers.forEach(this::registerCommandController);
    }

    public Collection<RegisteredCommand> getAllRegisteredCommands() {
        return commandMap.values();
    }

    public Optional<RegisteredCommand> getRegisteredCommand(@NonNull String command) {
        RegisteredCommand registeredCommand = commandMap.get(command);
        return  Optional.ofNullable(registeredCommand);
    }


    public void registerCommandController(Object controller) {
        for (Method method : controller.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(BotCommand.class)) continue;

            validateCommandMethod(method);

            RegisteredCommand command = toRegisteredCommand(controller, method);

            if (commandMap.containsKey(command.name())) {
                throw new IllegalStateException("Command duplication: " + command.name());
            }

            commandMap.put(command.name(), command);
            log.debug("Registered command: {}", command.name());
        }
    }

    private void validateCommandMethod(@NonNull Method method) {
        if (method.getParameterCount() != 1 || !Update.class.equals(method.getParameterTypes()[0])) {
            throw new IllegalStateException("@" + BotCommand.class.getSimpleName() + " method '" + method.getName() +
                    "' must have exactly one parameter of type Update");
        }

        Class<?> returnType = method.getReturnType();
        if (!SendMessage.class.equals(returnType) && !void.class.equals(returnType)) {
            throw new IllegalStateException("@" + BotCommand.class.getSimpleName() + " method '" + method.getName() +
                    "' must return either SendMessage or void");
        }
    }

    // startBot -> start_bot
    private String processMethodName(String name) {
        return name.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    private RegisteredCommand toRegisteredCommand(@NonNull Object handler, @NonNull Method method) {
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
}
