package org.pl.pcz.yevkov.botcore.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatStatus;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.adapter.BotApiAdapter;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.exception.BotApiException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Log4j2
public class ChatLeaveService {

    private final BotApiAdapter telegramBot;
    private final ChatService chatService;


    public void handleBotRemovedFromChat(ChatId chatId, Long botId) throws BotApiException {
        if (botId.equals(telegramBot.getBotId())) {
            chatService.markChatAsStatus(chatId, ChatStatus.DELETED);
            log.info("Bot was removed from chat {}", chatId);
        }
    }
}