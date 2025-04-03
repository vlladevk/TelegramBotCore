package org.pl.pcz.yevkov.tgbottest.mapper.chat;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.dto.chat.ChatReadDto;
import org.pl.pcz.yevkov.tgbottest.entity.Chat;
import org.pl.pcz.yevkov.tgbottest.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class ChatReadMapper implements Mapper<Chat, ChatReadDto> {
    @Override
    public ChatReadDto mapFrom(@NonNull Chat object) {
        return new ChatReadDto(object.getId(), object.getTitle(), object.getHourLimit(), object.getChatStatus());
    }
}
