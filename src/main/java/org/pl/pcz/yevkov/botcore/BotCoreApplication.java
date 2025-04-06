package org.pl.pcz.yevkov.botcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;


@SpringBootApplication
@ConfigurationPropertiesScan
public class BotCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(BotCoreApplication.class, args);
    }
}
