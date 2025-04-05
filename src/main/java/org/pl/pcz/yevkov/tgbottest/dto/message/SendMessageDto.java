package org.pl.pcz.yevkov.tgbottest.dto.message;

import lombok.Builder;
import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.model.vo.ChatId;

@Builder
public record SendMessageDto(
        @NonNull ChatId chatId,
        @NonNull String text
) {
}