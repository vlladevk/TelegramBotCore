package org.pl.pcz.yevkov.botcore.infrastructure.mapper.event;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.domain.event.ChatMemberJoinedEvent;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMemberJoinedDto;
import org.pl.pcz.yevkov.botcore.domain.vo.UserId;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;


import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChatMemberJoinedEventMapper implements UpdateToEventMapper<ChatMemberJoinedEvent> {
    @Override
    public boolean supports(@NonNull Update update) {
        return update.hasMessage()
                && update.getMessage().getNewChatMembers() != null
                && !update.getMessage().getNewChatMembers().isEmpty();
    }

    private List<ChatMemberJoinedDto> mapToDto(Update update) {
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

    @Override
    public List<ChatMemberJoinedEvent> mapFrom(@NonNull Update update) {
        if (!supports(update)) {
            throw new IllegalStateException("Update is not a ChatMemberJoined event");
        }
        return mapToDto(update).stream().map(ChatMemberJoinedEvent::new).toList();
    }

}
