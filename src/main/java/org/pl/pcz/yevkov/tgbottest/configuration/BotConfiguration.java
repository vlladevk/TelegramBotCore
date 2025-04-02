package org.pl.pcz.yevkov.tgbottest.configuration;


import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.bot.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@EnableScheduling
public class BotConfiguration {
    @Bean
    public TelegramBotsApi telegramBotsApi(@NonNull TelegramBot telegramBot) throws TelegramApiException {
        var ApiBot = new TelegramBotsApi(DefaultBotSession.class);
        ApiBot.registerBot(telegramBot);
        return ApiBot;
    }
}
