package org.pl.pcz.yevkov.botcore.application.message.factory;

import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.botcore.application.command.response.TextResponse;

public interface MessageResponseFactory {
    TextResponse generateResponse(ChatMessageReceivedDto chatMessageReceivedDto, String responseText);
}
