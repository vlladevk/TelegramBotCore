package org.pl.pcz.yevkov.botcore.application.dto.event;

import lombok.Builder;
import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatType;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;
import org.pl.pcz.yevkov.botcore.domain.vo.MessageId;
import org.pl.pcz.yevkov.botcore.domain.vo.ThreadId;
import org.pl.pcz.yevkov.botcore.domain.vo.UserId;

@Builder
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