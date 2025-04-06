package org.pl.pcz.yevkov.tgbottest.service;

import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.tgbottest.application.command.response.DeleteMessageRequest;
import org.pl.pcz.yevkov.tgbottest.bot.adapter.BotApiAdapter;
import org.pl.pcz.yevkov.tgbottest.model.vo.ChatId;
import org.pl.pcz.yevkov.tgbottest.model.vo.MessageId;
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
