package org.pl.pcz.yevkov.botcore.application.service;

import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.botcore.application.command.response.DeleteMessageRequest;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.adapter.BotApiAdapter;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;
import org.pl.pcz.yevkov.botcore.domain.vo.MessageId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageDeletionService {
    private final BotApiAdapter botApiAdapter;

    public void deleteMessage(ChatId chatId, MessageId messageId) {
        DeleteMessageRequest request = DeleteMessageRequest.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build();

        botApiAdapter.execute(request);
    }
}
