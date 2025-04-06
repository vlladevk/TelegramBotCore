package org.pl.pcz.yevkov.botcore.interfaces.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;

import org.pl.pcz.yevkov.botcore.application.service.CommandHandlingService;
import org.pl.pcz.yevkov.botcore.application.service.TokenManagementService;
import org.pl.pcz.yevkov.botcore.application.service.UserManagementService;
import org.pl.pcz.yevkov.botcore.domain.event.ChatMessageReceivedEvent;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.exception.BotApiException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Log4j2
public class ChatMessageEventListener {

    private final CommandHandlingService commandHandlingService;
    private final UserManagementService userManagementService;
    private final TokenManagementService tokenManagementService;

    @EventListener
    public void onTelegramUpdate(@NonNull ChatMessageReceivedEvent event) {
        ChatMessageReceivedDto receivedMessage = event.member();
        try {
            commandHandlingService.handleCommand(receivedMessage);
            tokenManagementService.handleNonCommandMessage(receivedMessage);
            userManagementService.ensureUserRegisteredInChat(receivedMessage);
        } catch (BotApiException e) {
            log.error("Error processing message: {}", receivedMessage.text(), e);
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
    }
}