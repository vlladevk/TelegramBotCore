package org.pl.pcz.yevkov.tgbottest.mapper.message;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.dto.message.SendMessageDto;
import org.pl.pcz.yevkov.tgbottest.mapper.Mapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class SendMessageMapper implements Mapper<SendMessageDto, SendMessage> {

    @Override
    public SendMessage mapFrom(@NonNull SendMessageDto dto) {
        SendMessage message = new SendMessage();
        message.setChatId(dto.chatId().value().toString());
        message.setText(dto.text());

        return message;
    }
}