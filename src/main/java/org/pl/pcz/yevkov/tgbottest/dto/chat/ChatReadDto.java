package org.pl.pcz.yevkov.tgbottest.dto.chat;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.entity.ChatStatus;
import org.pl.pcz.yevkov.tgbottest.model.vo.ChatId;


public record ChatReadDto(@NonNull ChatId id,
                          @NonNull String title,
                          @NonNull Long hourLimit,
                          @NonNull ChatStatus chatStatus) {}
