package org.pl.pcz.yevkov.tgbottest.mapper.event;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.dto.event.*;
import org.pl.pcz.yevkov.tgbottest.entity.ChatType;

import org.pl.pcz.yevkov.tgbottest.model.vo.ChatId;
import org.pl.pcz.yevkov.tgbottest.model.vo.MessageId;
import org.pl.pcz.yevkov.tgbottest.model.vo.ThreadId;
import org.pl.pcz.yevkov.tgbottest.model.vo.UserId;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
public class ChatMessageReceivedMapper implements BotEventMapper<Update, ChatMessageReceivedDto> {

    @Override
    public boolean supports(@NonNull Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    @Override
    public ChatMessageReceivedDto mapFrom(@NonNull Update update) {
        if (!supports(update)) {
            throw new IllegalStateException("Update is not a ChatMessageReceived event");
        }

        var message = update.getMessage();
        Chat chat = message.getChat();
        User user = message.getFrom();

        ChatId chatId = new ChatId(chat.getId());
        UserId userId = new UserId(user.getId());
        ThreadId threadId = new ThreadId( message.getMessageThreadId());
        MessageId messageId = new MessageId( message.getMessageId());

        String text = message.getText() == null ? "" : message.getText().trim();

        ChatType chatType = "private".equals(chat.getType()) ? ChatType.PRIVATE : ChatType.GROUP;

        return new ChatMessageReceivedDto(
                chatId,
                userId,
                threadId,
                messageId,
                user.getUserName(),
                user.getFirstName(),
                text,
                chatType
        );
    }
}
