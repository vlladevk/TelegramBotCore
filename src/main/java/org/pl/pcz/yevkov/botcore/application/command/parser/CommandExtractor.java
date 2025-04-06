package org.pl.pcz.yevkov.botcore.application.command.parser;

import lombok.NonNull;

public interface CommandExtractor {
    String extract(@NonNull String text);
}
