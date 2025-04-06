package org.pl.pcz.yevkov.botcore.infrastructure.mapper.event;

import org.pl.pcz.yevkov.botcore.infrastructure.mapper.Mapper;

public interface BotEventMapper<From, Too> extends Mapper<From, Too> {
    boolean supports(From input);
}
