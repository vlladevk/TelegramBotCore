package org.pl.pcz.yevkov.tgbottest.dto.event;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.entity.ChatType;

public record ChatMessageReceivedDto(
        @NonNull ChatId chatId,
        @NonNull UserId userId,
        ThreadId threadId,
        @NonNull MessageId messageId,
        String username,
        @NonNull String firstName,
        @NonNull String text,
        @NonNull ChatType chatType
) implements EventDto {}