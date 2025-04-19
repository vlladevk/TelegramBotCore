package org.pl.pcz.yevkov.botcore.infrastructure.config;

import org.pl.pcz.yevkov.botcore.BotCoreApplication;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ReflectionScannerConfig {
    @Bean
    public Reflections reflections() {
        return new Reflections(BotCoreApplication.class.getPackageName(), Scanners.TypesAnnotated);
    }
}