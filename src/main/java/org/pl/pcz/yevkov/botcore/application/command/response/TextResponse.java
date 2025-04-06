package org.pl.pcz.yevkov.botcore.application.command.response;

import lombok.Builder;
import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;

@Builder
public record TextResponse(
        @NonNull ChatId chatId,
        @NonNull String text
) {
}