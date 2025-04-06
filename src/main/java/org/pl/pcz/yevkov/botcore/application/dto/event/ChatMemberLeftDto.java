package org.pl.pcz.yevkov.botcore.application.dto.event;

import lombok.Builder;
import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;
import org.pl.pcz.yevkov.botcore.domain.vo.UserId;

@Builder
public record ChatMemberLeftDto(
        @NonNull ChatId chatId,
        @NonNull UserId userId,
        String username,
        @NonNull String firstName
) implements EventDto {}