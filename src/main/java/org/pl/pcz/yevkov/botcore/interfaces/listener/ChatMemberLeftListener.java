package org.pl.pcz.yevkov.botcore.interfaces.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.service.ChatLeaveService;
import org.pl.pcz.yevkov.botcore.domain.event.ChatMemberLeftEvent;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.exception.BotApiException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Log4j2
public class ChatMemberLeftListener {

    private final ChatLeaveService chatLeaveService;

    @EventListener
    public void onBotRemovedFromChat(ChatMemberLeftEvent event) {
        Long botId = event.member().userId().value();
        var chatId = event.member().chatId();

        log.debug("Bot removal detected: chatId={}, botId={}", chatId, botId);

        try {
            chatLeaveService.handleBotRemovedFromChat(chatId, botId);
        } catch (BotApiException e) {
            log.error("Error while processing bot removal", e);
        }
    }
}