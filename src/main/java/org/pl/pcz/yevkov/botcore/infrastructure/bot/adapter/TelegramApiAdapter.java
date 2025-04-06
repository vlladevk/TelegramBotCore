package org.pl.pcz.yevkov.botcore.infrastructure.bot.adapter;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.command.response.ChatMemberInfo;
import org.pl.pcz.yevkov.botcore.application.command.response.DeleteMessageRequest;
import org.pl.pcz.yevkov.botcore.application.command.response.GetChatMemberRequest;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.core.TelegramBot;
import org.pl.pcz.yevkov.botcore.application.command.response.TextResponse;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.exception.BotApiException;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.message.ChatMemberMapper;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.message.DeleteMessageMapper;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.message.GetChatMemberMapper;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.message.SendMessageMapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Log4j2
@Component
@RequiredArgsConstructor
public class TelegramApiAdapter implements BotApiAdapter {
    private final TelegramBot telegramBot;
    private final SendMessageMapper sendMessageMapper;
    private final DeleteMessageMapper deleteMessageMapper;
    private final GetChatMemberMapper getChatMemberMapper;
    private final ChatMemberMapper chatMemberMapper;


    @Override
    public Long getBotId() throws BotApiException {
        try {
            log.debug("Getting bot ID via getMe()");
            return telegramBot.getMe().getId();
        } catch (TelegramApiException e) {
            throw new BotApiException("Failed to get bot ID", e);
        }
    }


    @Override
    public void execute(@NonNull TextResponse sendMessage) throws BotApiException {
        try {
            log.debug("Sending message to chatId={}, text='{}'", sendMessage.chatId(), sendMessage.text());
            telegramBot.execute(sendMessageMapper.mapFrom(sendMessage));
        } catch (TelegramApiException e) {
            throw new BotApiException("Failed to send message", e);
        }
    }


    @Override
    public void execute(@NonNull SetMyCommands commands) throws BotApiException {
        try {
            log.debug("Setting bot commands: {}", commands.getCommands().stream()
                    .map(c -> c.getCommand() + " -> " + c.getDescription())
                    .toList());
            telegramBot.execute(commands);
        } catch (TelegramApiException e) {
            throw new BotApiException("Failed to set bot commands", e);
        }
    }

    @Override
    public ChatMemberInfo execute(@NonNull GetChatMemberRequest command) throws BotApiException {
        try {
            log.debug("Getting chat member info for chatId={}, userId={}",
                    command.chatId(), command.userId());
            var chatMember = telegramBot.execute(getChatMemberMapper.mapFrom(command));
            return chatMemberMapper.mapFrom(chatMember);
        } catch (TelegramApiException e) {
            throw new BotApiException("Failed to get chat member info", e);
        }
    }

    @Override
    public void execute(@NonNull DeleteMessageRequest message) throws BotApiException {
        try {
            log.debug("Deleting message with chatId={}, messageId={}", message.chatId(), message.messageId());
            telegramBot.execute(deleteMessageMapper.mapFrom(message));
        } catch (TelegramApiException e) {
            throw new BotApiException("Failed to delete message", e);
        }
    }
}
