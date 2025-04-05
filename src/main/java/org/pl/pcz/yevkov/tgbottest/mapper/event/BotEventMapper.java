package org.pl.pcz.yevkov.tgbottest.mapper.event;

import org.pl.pcz.yevkov.tgbottest.mapper.Mapper;

public interface BotEventMapper<From, Too> extends Mapper<From, Too> {
    boolean supports(From input);
}
