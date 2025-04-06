package org.pl.pcz.yevkov.botcore.application.command.parser;

import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class DefaultCommandExtractor implements CommandExtractor {
    @Override
    public String extract(@NonNull String text) {
        return text.split(" ")[0].split("@")[0];
    }
}