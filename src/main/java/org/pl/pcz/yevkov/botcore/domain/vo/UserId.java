package org.pl.pcz.yevkov.botcore.domain.vo;

import lombok.NonNull;

import java.io.Serializable;

public record UserId(@NonNull Long value) implements Serializable {}
