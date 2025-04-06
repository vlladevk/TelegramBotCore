package org.pl.pcz.yevkov.tgbottest.mapper.message;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.application.command.response.TextResponse;
import org.pl.pcz.yevkov.tgbottest.mapper.Mapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class SendMessageMapper implements Mapper<TextResponse, SendMessage> {

    @Override
    public SendMessage mapFrom(@NonNull TextResponse dto) {
        SendMessage message = new SendMessage();
        message.setChatId(dto.chatId().value().toString());
        message.setText(dto.text());

        return message;
    }
}