package org.pl.pcz.yevkov.tgbottest.dto.event;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.model.vo.ChatId;
import org.pl.pcz.yevkov.tgbottest.model.vo.UserId;

public record ChatMemberJoinedDto(
        @NonNull ChatId chatId,
        @NonNull UserId userId,
        String username,
        @NonNull String firstName,
        @NonNull String chatTitle
) implements EventDto {}