package org.pl.pcz.yevkov.botcore.application.command.parser;

import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultArgumentExtractor implements ArgumentExtractor {
    @Override
    public List<String> extract(@NonNull String text) {
        String[] parts = text.trim().split("\\s+");
        return parts.length <= 1 ? List.of() : List.of(parts).subList(1, parts.length);
    }
}
