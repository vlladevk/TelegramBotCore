package org.pl.pcz.yevkov.tgbottest.dto.chat;

import lombok.Builder;
import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.model.vo.ChatId;

@Builder
public record ChatCreateDto(@NonNull ChatId id, @NonNull String title) {}
