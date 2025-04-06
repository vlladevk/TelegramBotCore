package org.pl.pcz.yevkov.tgbottest.mapper.event;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.model.vo.ChatId;
import org.pl.pcz.yevkov.tgbottest.dto.event.ChatMemberJoinedDto;
import org.pl.pcz.yevkov.tgbottest.model.vo.UserId;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;


import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChatMemberJoinedMapperEvent implements BotEventBulkMapper<Update, ChatMemberJoinedDto> {
    @Override
    public boolean supports(@NonNull Update update) {
        return update.hasMessage()
                && update.getMessage().getNewChatMembers() != null
                && !update.getMessage().getNewChatMembers().isEmpty();
    }

    @Override
    public List<ChatMemberJoinedDto> mapAll(@NonNull Update update) {
        if (!supports(update)) {
            throw new IllegalStateException("Update is not a ChatMemberJoined event");
        }

        Long chatId_long = update.getMessage().getChatId();
        ChatId chatId = new ChatId(chatId_long);
        String chatTitle = update.getMessage().getChat().getTitle();

        return update.getMessage().getNewChatMembers().stream()
                .map(user -> ChatMemberJoinedDto.builder()
                        .chatId(chatId)
                        .userId(new UserId(user.getId()))
                        .username(user.getUserName())
                        .firstName(user.getFirstName())
                        .chatTitle(chatTitle)
                        .build()
                )
                .collect(Collectors.toList());
    }
}
