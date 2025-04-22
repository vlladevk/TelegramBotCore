package org.pl.pcz.yevkov.botcore.infrastructure.mapper.event;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.dto.event.EventDto;

import java.util.List;

/**
 * Maps an Update into zero or more EventDto instances.
 */
public interface UpdateToEventDtoMapper<From, To extends EventDto> {
    boolean supports(@NonNull From input);

    List<To> mapFrom(@NonNull From input);
}
