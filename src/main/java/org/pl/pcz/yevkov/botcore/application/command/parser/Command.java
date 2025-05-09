package org.pl.pcz.yevkov.botcore.application.command.parser;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record Command(@NonNull String text, boolean isExplicitCommand) {}
