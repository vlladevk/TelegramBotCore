package org.pl.pcz.yevkov.tgbottest.dto.userChat;


import lombok.Builder;
import org.pl.pcz.yevkov.tgbottest.entity.UserRole;

@Builder
public record UserChatUpdateDto(Long remainingTokens, UserRole userRole) {}
