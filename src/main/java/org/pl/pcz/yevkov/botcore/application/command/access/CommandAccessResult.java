package org.pl.pcz.yevkov.botcore.application.command.access;

import lombok.NonNull;


public record CommandAccessResult(boolean allowed, @NonNull String reason) {
}
