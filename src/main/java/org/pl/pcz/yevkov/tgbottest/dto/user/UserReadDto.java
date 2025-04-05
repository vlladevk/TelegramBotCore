package org.pl.pcz.yevkov.tgbottest.dto.user;


import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.model.vo.UserId;

public record UserReadDto (@NonNull UserId id, @NonNull String name, String userName) {
}
