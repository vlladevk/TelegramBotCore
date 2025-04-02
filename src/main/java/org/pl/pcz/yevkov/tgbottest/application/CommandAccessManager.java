package org.pl.pcz.yevkov.tgbottest.application;

import org.pl.pcz.yevkov.tgbottest.application.model.CommandAccessResult;
import org.pl.pcz.yevkov.tgbottest.application.model.RegisteredCommand;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandAccessManager {
    CommandAccessResult hasAccess(Update update, RegisteredCommand command);
}
