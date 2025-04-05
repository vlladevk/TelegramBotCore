package org.pl.pcz.yevkov.tgbottest.mapper.event;


import org.pl.pcz.yevkov.tgbottest.mapper.BulkMapper;

public interface BotEventBulkMapper<From, Too> extends BulkMapper<From, Too> {
    boolean supports(From input);
}
