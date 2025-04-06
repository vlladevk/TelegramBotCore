package org.pl.pcz.yevkov.tgbottest.dto.userChat;

import lombok.Builder;
import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.entity.UserRole;
import org.pl.pcz.yevkov.tgbottest.model.vo.ChatId;
import org.pl.pcz.yevkov.tgbottest.model.vo.UserId;

@Builder
public record UserChatCreateDto(
        @NonNull ChatId chatId,
        @NonNull UserId userId,
        @NonNull UserRole userRole
) {}