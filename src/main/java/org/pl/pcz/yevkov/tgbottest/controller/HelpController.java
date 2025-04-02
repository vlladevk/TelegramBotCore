package org.pl.pcz.yevkov.tgbottest.controller;


import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.tgbottest.annotation.BotCommand;
import org.pl.pcz.yevkov.tgbottest.annotation.CommandController;
import org.pl.pcz.yevkov.tgbottest.application.CommandCatalog;
import org.pl.pcz.yevkov.tgbottest.application.model.RegisteredCommand;
import org.pl.pcz.yevkov.tgbottest.application.helper.UpdateHelper;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@CommandController
@RequiredArgsConstructor
public class HelpController {
    private final CommandCatalog commandCatalog;
    private final UpdateHelper updateHelper;


    @BotCommand(description = """
            Lists all available bot commands.      \s
            Показывает список всех доступных команд.
            Usage: /help
           \s""")
    @SuppressWarnings("unused")
    public SendMessage help(Update update) {
        Collection<RegisteredCommand> commands = commandCatalog.getAllRegisteredCommands();

        if (commands.isEmpty()) {
            return updateHelper.generateMessage(update, "No commands available.");
        }

        int maxNameLength = commands.stream()
                .mapToInt(cmd -> cmd.name().length())
                .max()
                .orElse(0);

        String message = commands.stream()
                .sorted(Comparator.comparing(RegisteredCommand::name))
                .map(cmd -> String.format("%-" + maxNameLength + "s — %s", cmd.name(), cmd.description()))
                .collect(Collectors.joining("\n\n", "Available commands:\n\n", ""));

        return updateHelper.generateMessage(update, message);
    }
}
