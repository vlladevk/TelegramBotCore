package org.pl.pcz.yevkov.botcore.application.command.registry;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.adapter.BotApiAdapter;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.exception.BotApiException;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.command.BotCommandDtoMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;


@Component
@RequiredArgsConstructor
@Log4j2
public class CommandRegistrar {
    private final BotApiAdapter telegramBot;
    private final BotCommandCatalog commandCatalog;
    private final BotCommandDtoMapper commandDtoMapper;

    // Registers visible commands (showInMenu = true) in Telegram bot menu on startup
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        try {
            var allCommands = commandCatalog.getAllRegisteredCommands();
            var visibleCommands = allCommands.stream()
                    .filter(RegisteredCommand::showInMenu)
                    .map(commandDtoMapper::mapFrom)
                    .toList();

            log.info("Registering {} Telegram commands in bot's menu", commandCatalog.getAllRegisteredCommands()
                    .stream().filter(RegisteredCommand::showInMenu).count());

            SetMyCommands setMyCommands = new SetMyCommands(
                    visibleCommands,
                    new BotCommandScopeDefault(),
                    null);
            telegramBot.execute(setMyCommands);
            log.info("Telegram commands registered successfully");
        } catch (BotApiException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}
