package org.pl.pcz.yevkov.botcore.application.dto.chat;

import lombok.Builder;
import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;

@Builder
public record ChatCreateDto(@NonNull ChatId id, @NonNull String title) {}
