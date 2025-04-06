package org.pl.pcz.yevkov.botcore.infrastructure.mapper;

public interface Updater <From, To> {
    void updateFromDto(From dto, To entity);
}
