package org.pl.pcz.yevkov.tgbottest.mapper;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.dto.ChatCreateDto;
import org.pl.pcz.yevkov.tgbottest.entity.Chat;
import org.pl.pcz.yevkov.tgbottest.entity.ChatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChatCreateMapper implements Mapper<ChatCreateDto, Chat> {
    @Value("${bot.chat.limit}")
    private Long hourLimit;

    @Override
    public Chat mapFrom(@NonNull ChatCreateDto object) {
        return Chat.builder()
                .id(object.id())
                .title(object.title())
                .chatStatus(ChatStatus.INACTIVE)
                .hourLimit(hourLimit)
                .build();
    }
}
