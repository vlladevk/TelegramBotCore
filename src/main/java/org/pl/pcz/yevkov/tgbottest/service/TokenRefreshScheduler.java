package org.pl.pcz.yevkov.tgbottest.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.tgbottest.dto.UserChatUpdateDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Log4j2
@Service
@RequiredArgsConstructor
public class TokenRefreshScheduler {
    private final UserChatService userChatService;

    @Scheduled(cron = "0 0 * * * *")
    public void refreshTokens() {
        log.info("Starting hourly token refresh...");
        long start = System.currentTimeMillis();
        var userChats = userChatService.getAllUserChats();
        userChats.forEach(userChatReadDto -> {
                    var chat = userChatReadDto.chatReadDto();
                    long newRemainingTokens = userChatReadDto.remainingTokens();
                    if (newRemainingTokens < chat.hourLimit()) {
                        UserChatUpdateDto updateInfo = new UserChatUpdateDto(chat.hourLimit(), null);
                        userChatService.updateUserChat(userChatReadDto.id(), updateInfo);
                    }
                }
        );
        log.info("Hourly token refresh completed: {} user chats updated in {} ms",
                userChats.size(),
                System.currentTimeMillis() - start);
    }
}
