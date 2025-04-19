package org.pl.pcz.yevkov.botcore.infrastructure.mapper.command;

import org.pl.pcz.yevkov.botcore.application.command.registry.RegisteredCommand;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.Mapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Component
public class BotCommandDtoMapper implements Mapper<RegisteredCommand, BotCommand> {
    @Override
    public BotCommand mapFrom(RegisteredCommand cmd) {
        return new BotCommand(cmd.name(), cmd.description());
    }
}