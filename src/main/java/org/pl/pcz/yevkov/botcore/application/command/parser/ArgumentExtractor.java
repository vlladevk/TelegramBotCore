package org.pl.pcz.yevkov.botcore.application.command.parser;

import lombok.NonNull;

import java.util.List;


public interface ArgumentExtractor {
    List<String> extract(@NonNull String text);
}
