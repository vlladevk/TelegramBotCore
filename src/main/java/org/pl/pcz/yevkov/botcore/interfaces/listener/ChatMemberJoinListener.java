package org.pl.pcz.yevkov.botcore.interfaces.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMemberJoinedDto;
import org.pl.pcz.yevkov.botcore.application.service.ChatJoinService;
import org.pl.pcz.yevkov.botcore.domain.event.ChatMemberJoinedEvent;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.exception.BotApiException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Log4j2
public class ChatMemberJoinListener {

    private final ChatJoinService chatJoinService;

    @EventListener
    public void onTelegramNewChat(ChatMemberJoinedEvent event) {
        ChatMemberJoinedDto chatMember = event.member();
        log.debug("Handling ChatMemberJoinedEvent: chatId={}, userId={}",
                chatMember.chatId(), chatMember.userId());
        try {
            chatJoinService.handleChatCreatedOrReactivated(chatMember);

        } catch (BotApiException e) {
            log.error("Error while processing bot join", e);
        }
    }
}
