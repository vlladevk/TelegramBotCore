package org.pl.pcz.yevkov.tgbottest.dto;

import lombok.NonNull;

public record UserCreateDto(@NonNull Long Id, @NonNull String Name, String UserName) { }
