package org.pl.pcz.yevkov.tgbottest.mapper;

public interface Updater <From, To> {
    void updateFromDto(From dto, To entity);
}
