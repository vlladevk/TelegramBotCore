package org.pl.pcz.yevkov.tgbottest.application.command.registry;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.tgbottest.bot.adapter.BotApiAdapter;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class CommandRegistrar {
    private final BotApiAdapter telegramBot;
    private final BotCommandCatalog commandCatalog;

    @EventListener
    public void onApplicationReady(ContextRefreshedEvent ignore) {
        try {
            log.info("Registering {} Telegram commands...", commandCatalog.getAllRegisteredCommands()
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
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
