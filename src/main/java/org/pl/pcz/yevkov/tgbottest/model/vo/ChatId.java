package org.pl.pcz.yevkov.tgbottest.model.vo;

import lombok.NonNull;

import java.io.Serializable;

public record ChatId(@NonNull Long value) implements Serializable {}

