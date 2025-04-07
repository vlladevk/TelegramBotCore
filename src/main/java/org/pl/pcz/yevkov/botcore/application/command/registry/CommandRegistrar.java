package org.pl.pcz.yevkov.botcore.application.command.registry;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.adapter.BotApiAdapter;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.exception.BotApiException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;

import java.util.List;

/**
 * Application-level component responsible for publishing bot command metadata
 * to the Telegram platform during application startup.
 *
 * <p>
 * This component listens for the Spring {@link ApplicationReadyEvent} and
 * pushes all registered {@link RegisteredCommand} instances (marked with
 * {@code showInMenu=true}) to Telegram using {@link BotApiAdapter}.
 * </p>
 *
 * <p>
 * Any failure in communication with the Telegram Bot API results in a fatal
 * application error.
 * </p>
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class CommandRegistrar {
    private final BotApiAdapter telegramBot;
    private final BotCommandCatalog commandCatalog;

    /**
     * Initializes Telegram command menu after the Spring application context is fully initialized.
     *
     * <p>
     * Filters all registered commands from {@link BotCommandCatalog} that are marked
     * as visible in the menu ({@code showInMenu=true}) and registers them using
     * {@link SetMyCommands}.
     * </p>
     *
     * @throws RuntimeException if command registration with Telegram fails
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        try {
            log.info("Registering {} Telegram commands in bot's menu", commandCatalog.getAllRegisteredCommands()
                    .stream().filter(RegisteredCommand::showInMenu).count());
            List<BotCommand> commands = commandCatalog.getAllRegisteredCommands().stream()
                    .filter(RegisteredCommand::showInMenu)
                    .map(cmd -> new BotCommand(cmd.name(), cmd.description()))
                    .toList();
            SetMyCommands setMyCommands = new SetMyCommands(
                    commands,
                    new BotCommandScopeDefault(),
                    null);
            telegramBot.execute(setMyCommands);
            log.info("Telegram commands registered successfully");
        } catch (BotApiException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
