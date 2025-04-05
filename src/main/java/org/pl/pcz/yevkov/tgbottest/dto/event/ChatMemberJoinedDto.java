package org.pl.pcz.yevkov.tgbottest.dto.event;

import lombok.NonNull;

public record ChatMemberJoinedDto(
        @NonNull ChatId chatId,
        @NonNull UserId userId,
        String username,
        @NonNull String firstName,
        @NonNull String chatTitle
) implements EventDto {}