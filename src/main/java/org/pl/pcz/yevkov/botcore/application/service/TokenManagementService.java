package org.pl.pcz.yevkov.botcore.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.dto.chat.ChatReadDto;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.botcore.application.dto.userChat.UserChatReadDto;
import org.pl.pcz.yevkov.botcore.application.dto.userChat.UserChatUpdateDto;
import org.pl.pcz.yevkov.botcore.application.message.factory.MessageResponseFactory;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatStatus;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatType;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;
import org.pl.pcz.yevkov.botcore.domain.vo.UserId;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.adapter.BotApiAdapter;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.exception.BotApiException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class TokenManagementService {


    private final UserChatService userChatService;
    private final MessageDeletionService messageDeletionService;
    private final BotApiAdapter telegramBot;
    private final MessageResponseFactory messageFactory;


    public void handleNonCommandMessage(ChatMessageReceivedDto receivedMessage) throws BotApiException {
        if (receivedMessage.chatType() == ChatType.PRIVATE) return;

        ChatId chatId = receivedMessage.chatId();
        UserId userId = receivedMessage.userId();

        Optional<UserChatReadDto> userChatOpt = userChatService.getUserChatBy(chatId, userId);
        if (userChatOpt.isEmpty()) {
            log.error("UserChat not found for userId={} in chatId={}", userId, chatId);
            return;
        }

        ChatReadDto chat = userChatOpt.get().chatReadDto();
        if (!chat.chatStatus().equals(ChatStatus.ACTIVE)) {
            log.debug("Chat = {} is not active, tokens will not change", chat.id());
            return;
        }

        UserChatReadDto userChat = userChatOpt.get();
        String messageText = receivedMessage.text();
        int messageLength = messageText.length();

        if (userChat.remainingTokens() < messageLength) {
            log.info("Deleting message from userId={} in chatId={} text='{}'", userId, chatId, messageText);
            messageDeletionService.deleteMessage(chatId, receivedMessage.messageId());
            notifyUserNoTokens(receivedMessage);
            return;
        }

        long updatedTokens = userChat.remainingTokens() - messageLength;
        UserChatUpdateDto updateDto = UserChatUpdateDto.builder()
                .remainingTokens(updatedTokens)
                .build();
        userChatService.updateUserChat(userChat.id(), updateDto);
    }

    private void notifyUserNoTokens(ChatMessageReceivedDto receivedMessage) throws BotApiException {
        UserId userId = receivedMessage.userId();
        String username = receivedMessage.username();
        String mention = (username != null) ? "@" + username : "User with ID " + userId.value();

        String message = """
                ❌ %s has run out of tokens and their message was not sent.
                To continue chatting, please ask an admin to recharge your tokens.
                """.formatted(mention);

        var sendMessage = messageFactory.generateResponse(
                receivedMessage,
                message
        );
        telegramBot.execute(sendMessage);
    }
}