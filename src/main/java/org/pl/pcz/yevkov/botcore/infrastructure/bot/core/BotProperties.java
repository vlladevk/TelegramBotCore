package org.pl.pcz.yevkov.botcore.infrastructure.bot.core;


import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bot")
public record BotProperties(@NonNull String name, @NonNull String token) {}
