package org.pl.pcz.yevkov.tgbottest.application.message.facrory;

import org.pl.pcz.yevkov.tgbottest.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.tgbottest.dto.message.SendMessageDto;

public interface MessageDtoFactory {
    SendMessageDto generateMessage(ChatMessageReceivedDto chatMessageReceivedDto, String responseText);
}
