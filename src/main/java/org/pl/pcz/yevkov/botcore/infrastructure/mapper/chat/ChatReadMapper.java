package org.pl.pcz.yevkov.botcore.infrastructure.mapper.chat;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.dto.chat.ChatReadDto;
import org.pl.pcz.yevkov.botcore.domain.entity.Chat;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.Mapper;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;
import org.springframework.stereotype.Component;

@Component
public class ChatReadMapper implements Mapper<Chat, ChatReadDto> {
    @Override
    public ChatReadDto mapFrom(@NonNull Chat entity) {
        return ChatReadDto.builder()
                .id(new ChatId(entity.getId()))
                .title(entity.getTitle())
                .hourLimit(entity.getHourLimit())
                .chatStatus(entity.getChatStatus())
                .build();
    }
}
