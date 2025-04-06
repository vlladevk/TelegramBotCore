package org.pl.pcz.yevkov.botcore.interfaces.controller;


import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.botcore.annotation.BotCommand;
import org.pl.pcz.yevkov.botcore.annotation.CommandController;
import org.pl.pcz.yevkov.botcore.application.command.registry.BotCommandProvider;
import org.pl.pcz.yevkov.botcore.application.command.registry.RegisteredCommand;
import org.pl.pcz.yevkov.botcore.application.message.factory.MessageResponseFactory;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.botcore.application.command.response.TextResponse;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@CommandController
@RequiredArgsConstructor
public class HelpController {
    private final BotCommandProvider botCommandProvider;
    private final MessageResponseFactory messageFactory;


    @BotCommand(description = """
             Lists all available bot commands.      \s
             Показывает список всех доступных команд.
             Usage: /help
            \s""")
    @SuppressWarnings("unused")
    public TextResponse help(ChatMessageReceivedDto receivedMessage) {
        Collection<RegisteredCommand> commands = botCommandProvider.getAllRegisteredCommands();

        if (commands.isEmpty()) {
            return messageFactory.generateResponse(
                    receivedMessage,
                    "No commands available."
            );
        }

        int maxNameLength = commands.stream()
                .mapToInt(cmd -> cmd.name().length())
                .max()
                .orElse(0);

        String message = commands.stream()
                .sorted(Comparator.comparing(RegisteredCommand::name))
                .map(cmd -> String.format("%-" + maxNameLength + "s — %s", cmd.name(), cmd.description()))
                .collect(Collectors.joining("\n\n", "Available commands:\n\n", ""));

        return messageFactory.generateResponse(
                receivedMessage,
                message
        );
    }
}
