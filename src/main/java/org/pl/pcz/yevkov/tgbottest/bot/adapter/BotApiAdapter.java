package org.pl.pcz.yevkov.tgbottest.bot.adapter;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface BotApiAdapter {
    Long getBotId() throws TelegramApiException;
    void execute(@NonNull SendMessage sendMessage) throws TelegramApiException;
    void execute (@NonNull SetMyCommands commands) throws TelegramApiException;
    ChatMember execute (@NonNull GetChatMember commands) throws TelegramApiException;
    void execute (@NonNull DeleteMessage message) throws TelegramApiException;
}
