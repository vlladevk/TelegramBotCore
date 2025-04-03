package org.pl.pcz.yevkov.tgbottest.dto.userChat;


import org.pl.pcz.yevkov.tgbottest.entity.UserRole;

public record UserChatUpdateDto(Long remainingTokens, UserRole userRole) {}
