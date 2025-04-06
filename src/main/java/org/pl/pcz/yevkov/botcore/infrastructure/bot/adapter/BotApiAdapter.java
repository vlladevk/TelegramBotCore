package org.pl.pcz.yevkov.botcore.infrastructure.bot.adapter;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.command.response.ChatMemberInfo;
import org.pl.pcz.yevkov.botcore.application.command.response.DeleteMessageRequest;
import org.pl.pcz.yevkov.botcore.application.command.response.GetChatMemberRequest;
import org.pl.pcz.yevkov.botcore.application.command.response.TextResponse;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.exception.BotApiException;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;


public interface BotApiAdapter {
    Long getBotId() throws BotApiException;
    void execute(@NonNull TextResponse sendMessage) throws BotApiException;
    void execute (@NonNull SetMyCommands commands) throws BotApiException;
    ChatMemberInfo execute (@NonNull GetChatMemberRequest commands) throws BotApiException;
    void execute (@NonNull DeleteMessageRequest message) throws BotApiException;
}
