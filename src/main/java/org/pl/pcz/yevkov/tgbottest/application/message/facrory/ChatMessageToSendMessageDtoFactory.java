package org.pl.pcz.yevkov.tgbottest.application.message.facrory;

import org.pl.pcz.yevkov.tgbottest.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.tgbottest.dto.message.SendMessageDto;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageToSendMessageDtoFactory implements MessageDtoFactory {
    @Override
    public SendMessageDto generateMessage(ChatMessageReceivedDto chatMessageReceivedDto, String responseText) {
        return SendMessageDto.builder()
                .chatId(chatMessageReceivedDto.chatId())
                .text(responseText)
                .build();
    }
}
