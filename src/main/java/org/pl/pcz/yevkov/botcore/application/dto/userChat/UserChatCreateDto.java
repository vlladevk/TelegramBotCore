package org.pl.pcz.yevkov.botcore.application.dto.userChat;

import lombok.Builder;
import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.domain.entity.UserRole;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;
import org.pl.pcz.yevkov.botcore.domain.vo.UserId;

@Builder
public record UserChatCreateDto(
        @NonNull ChatId chatId,
        @NonNull UserId userId,
        @NonNull UserRole userRole
) {}