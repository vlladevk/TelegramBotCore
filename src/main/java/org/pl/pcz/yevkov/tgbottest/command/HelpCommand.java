package org.pl.pcz.yevkov.tgbottest.command;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.tgbottest.annotation.BotCommand;
import org.pl.pcz.yevkov.tgbottest.application.CommandRegistry;
import org.pl.pcz.yevkov.tgbottest.application.model.RegisteredCommand;
import org.pl.pcz.yevkov.tgbottest.application.helper.UpdateHelper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collection;
import java.util.Comparator;

@Component
@RequiredArgsConstructor
public class HelpCommand {
    private final CommandRegistry commandRegistry;
    private final UpdateHelper updateHelper;
    @PostConstruct
    public void init() {
        commandRegistry.registerCommandController(this);
    }

    @BotCommand(description = """
            Lists all available bot commands.
            Показывает список всех доступных команд.
            Usage: /help
            """)
    @SuppressWarnings("unused")
    public SendMessage help(Update update) {
        Collection<RegisteredCommand> commands = commandRegistry.getAllRegisteredCommands();

        if (commands.isEmpty()) {
            return updateHelper.generateMessage(update, "No commands available.");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Available commands:\n\n");

        int maxNameLength = commands.stream()
                .mapToInt(cmd -> cmd.name().length())
                .max()
                .orElse(0);

        commands.stream()
                .sorted(Comparator.comparing(RegisteredCommand::name))
                .forEach(cmd -> {
                    String paddedName = String.format("%-" + maxNameLength + "s", cmd.name());
                    sb.append(paddedName)
                            .append(" — ")
                            .append(cmd.description())
                            .append("\n\n");
                });

        return updateHelper.generateMessage(update, sb.toString());
    }
}
