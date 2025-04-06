package org.pl.pcz.yevkov.botcore.infrastructure.mapper.chat;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.dto.chat.ChatCreateDto;
import org.pl.pcz.yevkov.botcore.domain.entity.Chat;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatStatus;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChatCreateMapper implements Mapper<ChatCreateDto, Chat> {
    @Value("${bot.chat.limit}")
    private Long hourLimit;

    @Override
    public Chat mapFrom(@NonNull ChatCreateDto object) {
        return Chat.builder()
                .id(object.id().value())
                .title(object.title())
                .chatStatus(ChatStatus.INACTIVE)
                .hourLimit(hourLimit)
                .build();
    }
}
