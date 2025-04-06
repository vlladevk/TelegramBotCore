package org.pl.pcz.yevkov.tgbottest.application.command.response;

import lombok.Builder;
import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.model.vo.ChatId;
import org.pl.pcz.yevkov.tgbottest.model.vo.UserId;

@Builder
public record GetChatMemberRequest(
        @NonNull ChatId chatId,
        @NonNull UserId userId
) {
}