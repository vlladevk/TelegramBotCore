package org.pl.pcz.yevkov.botcore.application.command.response;

import lombok.Builder;
import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;
import org.pl.pcz.yevkov.botcore.domain.vo.MessageId;

@Builder
public record DeleteMessageRequest(
        @NonNull ChatId chatId,
        @NonNull MessageId messageId
) {
}