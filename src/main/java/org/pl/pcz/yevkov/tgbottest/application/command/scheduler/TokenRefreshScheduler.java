package org.pl.pcz.yevkov.tgbottest.application.command.scheduler;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.tgbottest.dto.userChat.UserChatUpdateDto;
import org.pl.pcz.yevkov.tgbottest.service.UserChatService;
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
                        UserChatUpdateDto updateInfo = UserChatUpdateDto.builder()
                                .remainingTokens(chat.hourLimit())
                                .build();
                        userChatService.updateUserChat(userChatReadDto.id(), updateInfo);
                    }
                }
        );
        log.info("Hourly token refresh completed: {} user chats updated in {} ms",
                userChats.size(),
                System.currentTimeMillis() - start);
    }
}
