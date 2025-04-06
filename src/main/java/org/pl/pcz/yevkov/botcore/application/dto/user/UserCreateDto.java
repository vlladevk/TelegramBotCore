package org.pl.pcz.yevkov.botcore.application.dto.user;

import lombok.Builder;
import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.domain.vo.UserId;

@Builder
public record UserCreateDto(@NonNull UserId id, @NonNull String name, String userName) { }
