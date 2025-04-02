package org.pl.pcz.yevkov.tgbottest.dto;


import org.pl.pcz.yevkov.tgbottest.entity.UserRole;

public record UserChatUpdateDto(Long remainingTokens, UserRole userRole) {}
