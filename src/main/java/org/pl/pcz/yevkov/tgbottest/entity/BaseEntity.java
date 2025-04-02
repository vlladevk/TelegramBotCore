package org.pl.pcz.yevkov.tgbottest.entity;

import lombok.NonNull;

import java.io.Serializable;

public interface BaseEntity <T extends Serializable>{
    T getId();
    void setId(@NonNull T id);
}
