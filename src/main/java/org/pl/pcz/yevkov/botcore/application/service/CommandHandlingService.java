package org.pl.pcz.yevkov.botcore.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.command.dispatcher.CommandDispatcher;
import org.pl.pcz.yevkov.botcore.application.command.response.TextResponse;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.adapter.BotApiAdapter;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.exception.BotApiException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Log4j2
public class CommandHandlingService {

    private final CommandDispatcher dispatcher;
    private final BotApiAdapter telegramBot;
    private final CommandTargetCheckerService commandTargetChecker;

    public void handleCommand(ChatMessageReceivedDto receivedMessage) throws BotApiException {
        if (!commandTargetChecker.isForThisBot(receivedMessage.text())) return;
        Optional<TextResponse> message = dispatcher.dispatch(receivedMessage);
        message.ifPresent(telegramBot::execute);
    }
}