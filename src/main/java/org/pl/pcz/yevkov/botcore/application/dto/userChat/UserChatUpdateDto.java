package org.pl.pcz.yevkov.botcore.application.dto.userChat;


import lombok.Builder;
import org.pl.pcz.yevkov.botcore.domain.entity.UserRole;

@Builder
public record UserChatUpdateDto(Long remainingTokens, UserRole userRole) {}
