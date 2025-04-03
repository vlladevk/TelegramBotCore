package org.pl.pcz.yevkov.tgbottest.dto.userChat;


import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.dto.chat.ChatReadDto;
import org.pl.pcz.yevkov.tgbottest.dto.user.UserReadDto;
import org.pl.pcz.yevkov.tgbottest.entity.UserRole;

public record UserChatReadDto(@NonNull Long id,
                              @NonNull UserRole userRole,
                              @NonNull Long remainingTokens,
                              @NonNull ChatReadDto chatReadDto,
                              @NonNull UserReadDto userReadDto) {}
