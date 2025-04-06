package org.pl.pcz.yevkov.botcore.infrastructure.mapper;

public interface Mapper<From, To> {
    To mapFrom(From object);
}