package org.pl.pcz.yevkov.botcore.infrastructure.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


import org.pl.pcz.yevkov.botcore.infrastructure.mapper.event.UpdateToEventMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;


@Log4j2
@Component
@RequiredArgsConstructor
public class MapperRegistrationLogger {

    private final List<UpdateToEventMapper<?>> mappers;

    @EventListener(ApplicationReadyEvent.class)
    public void logMapperCount() {
        int total = mappers.size();
        log.info("Total registered UpdateToEventMappers: {}", total);
        mappers.forEach(mapper ->
                log.debug(" • Mapper bean: {}", mapper.getClass().getSimpleName())
        );
    }
}