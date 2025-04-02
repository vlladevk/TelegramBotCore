package org.pl.pcz.yevkov.tgbottest.dto;


import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.entity.UserRole;

public record UserChatReadDto(@NonNull Long id,
                              @NonNull UserRole userRole,
                              @NonNull Long remainingTokens,
                              @NonNull ChatReadDto chatReadDto,
                              @NonNull UserReadDto userReadDto) {}
