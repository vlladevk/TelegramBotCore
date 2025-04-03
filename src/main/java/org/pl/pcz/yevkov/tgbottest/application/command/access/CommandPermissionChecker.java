package org.pl.pcz.yevkov.tgbottest.application.command.access;

import org.pl.pcz.yevkov.tgbottest.application.command.registry.RegisteredCommand;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandPermissionChecker {
    CommandAccessResult hasAccess(Update update, RegisteredCommand command);
}
