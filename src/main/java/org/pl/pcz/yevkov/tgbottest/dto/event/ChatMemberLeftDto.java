package org.pl.pcz.yevkov.tgbottest.dto.event;

import lombok.Builder;
import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.model.vo.ChatId;
import org.pl.pcz.yevkov.tgbottest.model.vo.UserId;

@Builder
public record ChatMemberLeftDto(
        @NonNull ChatId chatId,
        @NonNull UserId userId,
        String username,
        @NonNull String firstName
) implements EventDto {}