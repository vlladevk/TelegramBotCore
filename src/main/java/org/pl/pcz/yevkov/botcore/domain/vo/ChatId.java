package org.pl.pcz.yevkov.botcore.domain.vo;

import lombok.NonNull;

import java.io.Serializable;

public record ChatId(@NonNull Long value) implements Serializable {}

