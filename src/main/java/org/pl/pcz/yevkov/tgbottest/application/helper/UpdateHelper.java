package org.pl.pcz.yevkov.tgbottest.application.helper;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.bot.adapter.BotApiAdapter;
import org.pl.pcz.yevkov.tgbottest.model.vo.ChatId;
import org.pl.pcz.yevkov.tgbottest.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.tgbottest.model.vo.UserId;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


@Component
public class UpdateHelper {
    public List<String> extractArguments(String text) {
        String[] parts = text.split("\\s+");

        if (parts.length <= 1) {
            return List.of();
        }

        return List.of(parts).subList(1, parts.length);
    }


    public void deleteUserMessage(@NonNull ChatMessageReceivedDto receivedMessage, @NonNull BotApiAdapter botApiAdapter) throws TelegramApiException {
        ChatId chatId = receivedMessage.chatId();
        Integer messageId = receivedMessage.messageId().value();
        botApiAdapter.execute(new DeleteMessage(chatId.value().toString(), messageId));
    }

    public boolean isUserAdmin(@NonNull ChatId chatId, @NonNull UserId userId, @NonNull BotApiAdapter telegramApi) throws TelegramApiException {
        GetChatMember getChatMember = new GetChatMember();
        getChatMember.setChatId(chatId.value().toString());
        getChatMember.setUserId(userId.value());

        ChatMember member = telegramApi.execute(getChatMember);

        String status = member.getStatus();
        return "administrator".equals(status) || "creator".equals(status);
    }
}
