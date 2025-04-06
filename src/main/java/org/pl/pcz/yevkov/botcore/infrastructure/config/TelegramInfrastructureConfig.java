package org.pl.pcz.yevkov.botcore.infrastructure.config;


import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.core.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


@Configuration
@EnableScheduling
public class TelegramInfrastructureConfig {
    @Bean
    public TelegramBotsApi telegramBotsApi(@NonNull TelegramBot telegramBot) throws TelegramApiException {
        var apiBot = new TelegramBotsApi(DefaultBotSession.class);
        apiBot.registerBot(telegramBot);
        return apiBot;
    }

}
