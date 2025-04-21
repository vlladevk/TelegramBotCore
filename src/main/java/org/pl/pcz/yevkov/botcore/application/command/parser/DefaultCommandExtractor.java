package org.pl.pcz.yevkov.botcore.application.command.parser;

import lombok.NonNull;
import org.springframework.stereotype.Component;


@Component
public class DefaultCommandExtractor implements CommandExtractor {
    @Override
    public String extract(@NonNull String text) {
        String firstPart = text.trim().split("\\s", 2)[0];
        return firstPart.split("@", 2)[0];
    }
}