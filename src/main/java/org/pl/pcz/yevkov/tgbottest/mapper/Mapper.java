package org.pl.pcz.yevkov.tgbottest.mapper;

public interface Mapper<From, To> {
    To mapFrom(From object);
}