package org.pl.pcz.yevkov.tgbottest.dto.user;

import lombok.NonNull;

public record UserCreateDto(@NonNull Long Id, @NonNull String Name, String UserName) { }
