package org.pl.pcz.yevkov.botcore.application.command.parser;

import lombok.NonNull;


public interface CommandExtractor {
    Command extract(@NonNull String text);
}
