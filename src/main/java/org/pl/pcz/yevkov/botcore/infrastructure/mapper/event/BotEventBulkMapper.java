package org.pl.pcz.yevkov.botcore.infrastructure.mapper.event;


import org.pl.pcz.yevkov.botcore.infrastructure.mapper.BulkMapper;

public interface BotEventBulkMapper<From, Too> extends BulkMapper<From, Too> {
    boolean supports(From input);
}
