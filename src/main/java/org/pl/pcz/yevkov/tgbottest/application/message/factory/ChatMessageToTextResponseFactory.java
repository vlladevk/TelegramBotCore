package org.pl.pcz.yevkov.tgbottest.application.message.factory;

import org.pl.pcz.yevkov.tgbottest.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.tgbottest.application.command.response.TextResponse;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageToTextResponseFactory implements MessageResponseFactory {
    @Override
    public TextResponse generateResponse(ChatMessageReceivedDto chatMessageReceivedDto, String responseText) {
        return TextResponse.builder()
                .chatId(chatMessageReceivedDto.chatId())
                .text(responseText)
                .build();
    }
}
