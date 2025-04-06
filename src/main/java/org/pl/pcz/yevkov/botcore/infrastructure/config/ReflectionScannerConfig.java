package org.pl.pcz.yevkov.botcore.infrastructure.config;

import org.pl.pcz.yevkov.botcore.BotCoreApplication;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReflectionScannerConfig {
    @Bean
    public Reflections reflections() {
        return new Reflections(BotCoreApplication.class.getPackageName(), Scanners.TypesAnnotated);
    }
}