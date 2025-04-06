package org.pl.pcz.yevkov.tgbottest.application.command.response;

import lombok.Builder;
import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.model.vo.ChatId;
import org.pl.pcz.yevkov.tgbottest.model.vo.MessageId;

@Builder
public record DeleteMessageRequest(
        @NonNull ChatId chatId,
        @NonNull MessageId messageId
) {
}