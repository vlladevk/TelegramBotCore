package org.pl.pcz.yevkov.tgbottest.application;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pl.pcz.yevkov.tgbottest.bot.TelegramBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBotApiAdapter implements BotApiAdapter {
    private final TelegramBot telegramBot;

    @Override
    public Long getBotId() throws TelegramApiException {
        log.debug("Getting bot ID via getMe()");
        return telegramBot.getMe().getId();
    }

    @Override
    public void execute(@NonNull SendMessage sendMessage) throws TelegramApiException {
        log.debug("Sending message to chatId={}, text='{}'",
                sendMessage.getChatId(), sendMessage.getText());
        telegramBot.execute(sendMessage);
    }

    @Override
    public void execute(@NonNull SetMyCommands commands) throws TelegramApiException {
        log.debug("Setting bot commands: {}", commands.getCommands().stream()
                .map(c -> c.getCommand() + " -> " + c.getDescription())
                .toList());
        telegramBot.execute(commands);
    }

    @Override
    public ChatMember execute(@NonNull GetChatMember commands) throws TelegramApiException {
        log.debug("Getting chat member info for chatId={}, userId={}",
                commands.getChatId(), commands.getUserId());
        return telegramBot.execute(commands);
    }

    @Override
    public void execute(@NonNull DeleteMessage message) throws TelegramApiException {
        telegramBot.execute(message);
    }
}
