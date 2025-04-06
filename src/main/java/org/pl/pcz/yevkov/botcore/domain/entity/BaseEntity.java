package org.pl.pcz.yevkov.botcore.domain.entity;

import lombok.NonNull;

import java.io.Serializable;

public interface BaseEntity <T extends Serializable>{
    T getId();
    void setId(@NonNull T id);
}
