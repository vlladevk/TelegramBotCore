package org.pl.pcz.yevkov.tgbottest.mapper.event;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.model.vo.ChatId;
import org.pl.pcz.yevkov.tgbottest.dto.event.ChatMemberLeftDto;
import org.pl.pcz.yevkov.tgbottest.model.vo.UserId;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;


@Component
public class ChatMemberLeftMapper implements BotEventMapper<Update, ChatMemberLeftDto> {

    @Override
    public boolean supports(@NonNull Update update) {
        return update.hasMessage()
                && update.getMessage().getLeftChatMember() != null;
    }

    @Override
    public ChatMemberLeftDto mapFrom(Update update) {
        if (!supports(update)) {
            throw new IllegalStateException("Update is not a ChatMemberLeft event");
        }

        var message = update.getMessage();
        User user = message.getLeftChatMember();

        ChatId chatId = new ChatId(message.getChatId());
        UserId userId = new UserId(user.getId());

        return new ChatMemberLeftDto(
                chatId,
                userId,
                user.getUserName(),
                user.getFirstName()
        );
    }
}
