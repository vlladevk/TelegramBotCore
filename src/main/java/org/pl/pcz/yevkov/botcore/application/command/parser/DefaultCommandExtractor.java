package org.pl.pcz.yevkov.botcore.application.command.parser;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class DefaultCommandExtractor implements CommandExtractor {
    @Value("${bot.name}")
    private String botUserName;

    @Override
    public Command extract(@NonNull String text) {
        String firstPart = text.trim().split("\\s", 2)[0];
        String commandText = firstPart.split("@", 2)[0];
        boolean isExplicitCommand = firstPart.contains("@" + botUserName);
        return Command.builder()
                .isExplicitCommand(isExplicitCommand)
                .text(commandText)
                .build();
    }
}