package org.pl.pcz.yevkov.tgbottest.application.helper;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.bot.adapter.BotApiAdapter;
import org.pl.pcz.yevkov.tgbottest.entity.ChatType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


@Component
public class UpdateHelper {
    public boolean isBotAddedToChat(@NonNull Update update, @NonNull BotApiAdapter botApiAdapter) throws TelegramApiException {
        Long botId = botApiAdapter.getBotId();
        return update.hasMessage()
                && update.getMessage().getNewChatMembers() != null
                && update.getMessage().getNewChatMembers().stream()
                .anyMatch(user -> user.getId().equals(botId));
    }

    public boolean isBotRemovedFromChat(@NonNull Update update, @NonNull BotApiAdapter botApiAdapter) throws TelegramApiException {
        Long botId = botApiAdapter.getBotId();
        return update.hasMessage()
                && update.getMessage().getLeftChatMember() != null
                && update.getMessage().getLeftChatMember().getId().equals(botId);
    }

    public SendMessage generateMessage(@NonNull Update update, @NonNull String message) {
        var sendMessage = generateMessage(update);
        sendMessage.setText(message);
        return sendMessage;
    }

    public SendMessage generateMessage(@NonNull Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(extractChatId(update));
        message.setMessageThreadId(extractThreadId(update));
        return message;
    }

    public Long extractChatId(@NonNull Update update) {
        if (update.getMessage() == null) {
            throw new IllegalArgumentException("Update or message is null");
        }
        return update.getMessage().getChatId();
    }

    public Long extractUserId(@NonNull Update update) {
        if (update.getMessage() == null || update.getMessage().getFrom() == null) {
            throw new IllegalArgumentException("Update, message, or sender is null");
        }
        return update.getMessage().getFrom().getId();
    }

    public Integer extractThreadId(@NonNull Update update) {
        if (update.getMessage() == null) {
            throw new IllegalArgumentException("Update or message is null");
        }
        return update.getMessage().getMessageThreadId();
    }

    public User extractUser(@NonNull Update update) {
        return update.getMessage().getFrom();
    }

    public List<String> extractArguments(Update update) {
        String text = update.getMessage().getText().trim();
        String[] parts = text.split("\\s+"); // разбивает по пробелам

        if (parts.length <= 1) {
            return List.of();
        }

        return List.of(parts).subList(1, parts.length);
    }

    public boolean isPrivateMessage(@NonNull Update update) {
        return resolveChatType(update) == ChatType.PRIVATE;
    }

    public ChatType resolveChatType(@NonNull Update update) {
        if (update.hasMessage()
                && update.getMessage().getChat() != null
                && "private".equals(update.getMessage().getChat().getType())) {
            return ChatType.PRIVATE;
        }
        return ChatType.GROUP;
    }

    public void deleteUserMessage(@NonNull Update update, @NonNull BotApiAdapter botApiAdapter) throws TelegramApiException {
        Long chatId = extractChatId(update);
        Integer messageId = update.getMessage().getMessageId();
        botApiAdapter.execute(new DeleteMessage(chatId.toString(), messageId));
    }

    public boolean isUserAdmin(@NonNull Long chatId, @NonNull Long userId, @NonNull BotApiAdapter telegramApi)
            throws TelegramApiException {
        GetChatMember getChatMember = new GetChatMember();
        getChatMember.setChatId(chatId.toString());
        getChatMember.setUserId(userId);

        ChatMember member = telegramApi.execute(getChatMember);

        String status = member.getStatus();
        return "administrator".equals(status) || "creator".equals(status);
    }
}
