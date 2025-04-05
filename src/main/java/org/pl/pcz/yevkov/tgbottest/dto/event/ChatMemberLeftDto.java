package org.pl.pcz.yevkov.tgbottest.dto.event;

import lombok.NonNull;

public record ChatMemberLeftDto(
        @NonNull ChatId chatId,
        @NonNull UserId userId,
        String username,
        @NonNull String firstName
) implements EventDto {}