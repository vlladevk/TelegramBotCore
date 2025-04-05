package org.pl.pcz.yevkov.tgbottest.dto.event;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.entity.ChatType;
import org.pl.pcz.yevkov.tgbottest.model.vo.ChatId;
import org.pl.pcz.yevkov.tgbottest.model.vo.MessageId;
import org.pl.pcz.yevkov.tgbottest.model.vo.ThreadId;
import org.pl.pcz.yevkov.tgbottest.model.vo.UserId;

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