package org.pl.pcz.yevkov.botcore.application.command.response;

import lombok.Builder;
import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;
import org.pl.pcz.yevkov.botcore.domain.vo.UserId;


@Builder
public record GetChatMemberRequest(
        @NonNull ChatId chatId,
        @NonNull UserId userId
) {
}