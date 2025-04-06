package org.pl.pcz.yevkov.tgbottest.application.message.factory;

import org.pl.pcz.yevkov.tgbottest.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.tgbottest.application.command.response.TextResponse;

public interface MessageResponseFactory {
    TextResponse generateResponse(ChatMessageReceivedDto chatMessageReceivedDto, String responseText);
}
