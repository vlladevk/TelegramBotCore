package org.pl.pcz.yevkov.botcore.application.dto.chat;

import lombok.Builder;
import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatStatus;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;

@Builder
public record ChatReadDto(@NonNull ChatId id,
                          @NonNull String title,
                          @NonNull Long hourLimit,
                          @NonNull ChatStatus chatStatus) {}
