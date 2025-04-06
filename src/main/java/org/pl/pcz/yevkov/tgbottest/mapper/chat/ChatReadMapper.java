package org.pl.pcz.yevkov.tgbottest.mapper.chat;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.dto.chat.ChatReadDto;
import org.pl.pcz.yevkov.tgbottest.entity.Chat;
import org.pl.pcz.yevkov.tgbottest.mapper.Mapper;
import org.pl.pcz.yevkov.tgbottest.model.vo.ChatId;
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
