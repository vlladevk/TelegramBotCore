package org.pl.pcz.yevkov.tgbottest.dto.user;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.model.vo.UserId;

public record UserCreateDto(@NonNull UserId Id, @NonNull String Name, String UserName) { }
