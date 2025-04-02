package org.pl.pcz.yevkov.tgbottest.dto;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.entity.ChatStatus;


public record ChatReadDto(@NonNull Long id,
                          @NonNull String title,
                          @NonNull Long hourLimit,
                          @NonNull ChatStatus chatStatus) {}
