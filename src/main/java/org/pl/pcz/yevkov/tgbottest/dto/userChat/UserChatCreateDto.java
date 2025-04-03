package org.pl.pcz.yevkov.tgbottest.dto.userChat;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.entity.UserRole;

public record UserChatCreateDto(
        @NonNull Long chatId,
        @NonNull Long userId,
        @NonNull UserRole userRole
) {}