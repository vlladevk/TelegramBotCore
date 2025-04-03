package org.pl.pcz.yevkov.tgbottest.dto.user;


import lombok.NonNull;

public record UserReadDto (@NonNull Long id, @NonNull String name, String userName) {
}
