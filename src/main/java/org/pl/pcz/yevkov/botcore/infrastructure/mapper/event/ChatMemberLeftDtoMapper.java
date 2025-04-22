package org.pl.pcz.yevkov.botcore.infrastructure.mapper.event;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMemberLeftDto;
import org.pl.pcz.yevkov.botcore.domain.vo.UserId;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;


@Component
public class ChatMemberLeftDtoMapper implements UpdateToEventDtoMapper<Update, ChatMemberLeftDto> {

    @Override
    public boolean supports(@NonNull Update update) {
        return update.hasMessage()
                && update.getMessage().getLeftChatMember() != null;
    }

    @Override
    public List<ChatMemberLeftDto> mapFrom(@NonNull Update update) {
        if (!supports(update)) {
            throw new IllegalStateException("Update is not a ChatMemberLeft event");
        }

        var message = update.getMessage();
        User user = message.getLeftChatMember();

        return List.of(ChatMemberLeftDto.builder()
                .chatId(new ChatId(message.getChatId()))
                .userId(new UserId(user.getId()))
                .username(user.getUserName())
                .firstName(user.getFirstName())
                .build());
    }
}
