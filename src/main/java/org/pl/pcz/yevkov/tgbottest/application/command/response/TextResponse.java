package org.pl.pcz.yevkov.tgbottest.application.command.response;

import lombok.Builder;
import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.model.vo.ChatId;

@Builder
public record TextResponse(
        @NonNull ChatId chatId,
        @NonNull String text
) {
}