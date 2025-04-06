package org.pl.pcz.yevkov.botcore.application.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.dto.chat.ChatCreateDto;
import org.pl.pcz.yevkov.botcore.application.dto.chat.ChatReadDto;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMemberJoinedDto;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatStatus;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.adapter.BotApiAdapter;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.exception.BotApiException;
import org.springframework.stereotype.Service;

import java.util.Optional;



@Service
@RequiredArgsConstructor
@Log4j2
public class ChatJoinService {

    private final BotApiAdapter telegramBot;


    private final ChatService chatService;


    public void handleChatCreatedOrReactivated(ChatMemberJoinedDto chatMember) throws BotApiException {
        if (!isBotAddedToChat(chatMember)) return;

        ChatId chatId = chatMember.chatId();
        String chatName = chatMember.chatTitle();

        Optional<ChatReadDto> readDto = chatService.findChatById(chatId);
        if (readDto.isPresent()) {
            chatService.markChatAsStatus(chatId, ChatStatus.INACTIVE);
            log.info("Chat status updated to INACTIVE: {}", readDto);
        } else {
            ChatCreateDto newChat = ChatCreateDto.builder()
                    .id(chatId)
                    .title(chatName)
                    .build();
            chatService.createChat(newChat);
            log.info("New chat created: {}", newChat);
        }
    }


    private boolean isBotAddedToChat(ChatMemberJoinedDto chatMember) throws BotApiException {
        Long botId = telegramBot.getBotId();
        return botId.equals(chatMember.userId().value());
    }
}
