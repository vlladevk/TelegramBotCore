package org.pl.pcz.yevkov.tgbottest.controller;


import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.tgbottest.annotation.BotCommand;
import org.pl.pcz.yevkov.tgbottest.annotation.CommandController;
import org.pl.pcz.yevkov.tgbottest.application.command.registry.BotCommandProvider;
import org.pl.pcz.yevkov.tgbottest.application.command.registry.RegisteredCommand;
import org.pl.pcz.yevkov.tgbottest.application.message.facrory.MessageDtoFactory;
import org.pl.pcz.yevkov.tgbottest.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.tgbottest.dto.message.SendMessageDto;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@CommandController
@RequiredArgsConstructor
public class HelpController {
    private final BotCommandProvider botCommandProvider;
    private final MessageDtoFactory messageFactory;


    @BotCommand(description = """
             Lists all available bot commands.      \s
             Показывает список всех доступных команд.
             Usage: /help
            \s""")
    @SuppressWarnings("unused")
    public SendMessageDto help(ChatMessageReceivedDto receivedMessage) {
        Collection<RegisteredCommand> commands = botCommandProvider.getAllRegisteredCommands();

        if (commands.isEmpty()) {
            return messageFactory.generateMessage(
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

        return messageFactory.generateMessage(
                receivedMessage,
                message
        );
    }
}
