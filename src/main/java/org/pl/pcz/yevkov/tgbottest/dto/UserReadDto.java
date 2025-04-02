package org.pl.pcz.yevkov.tgbottest.dto;


import lombok.NonNull;

public record UserReadDto (@NonNull Long id, @NonNull String name, String userName) {
}
