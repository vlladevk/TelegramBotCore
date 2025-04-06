package org.pl.pcz.yevkov.tgbottest.dto.user;


import lombok.Builder;
import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.model.vo.UserId;

@Builder
public record UserReadDto (@NonNull UserId id, @NonNull String name, String userName) {
}
