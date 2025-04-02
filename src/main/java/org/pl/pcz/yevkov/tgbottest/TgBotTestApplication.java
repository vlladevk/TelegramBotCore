package org.pl.pcz.yevkov.tgbottest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;


@SpringBootApplication
@ConfigurationPropertiesScan
public class TgBotTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TgBotTestApplication.class, args);
    }
}
