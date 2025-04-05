package org.pl.pcz.yevkov.tgbottest.model.vo;

import lombok.NonNull;

import java.io.Serializable;

public record UserId(@NonNull Long value) implements Serializable {}
