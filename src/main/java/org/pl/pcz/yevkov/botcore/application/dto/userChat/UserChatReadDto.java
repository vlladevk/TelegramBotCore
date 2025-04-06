package org.pl.pcz.yevkov.botcore.application.dto.userChat;


import lombok.Builder;
import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.dto.chat.ChatReadDto;
import org.pl.pcz.yevkov.botcore.application.dto.user.UserReadDto;
import org.pl.pcz.yevkov.botcore.domain.entity.UserRole;

@Builder
public record UserChatReadDto(@NonNull Long id,
                              @NonNull UserRole userRole,
                              @NonNull Long remainingTokens,
                              @NonNull ChatReadDto chatReadDto,
                              @NonNull UserReadDto userReadDto) {}
