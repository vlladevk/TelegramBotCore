package org.pl.pcz.yevkov.botcore.application.service;


import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CommandTargetCheckerService {
    @Value( "${bot.name}")
    private String botUsername;

    public boolean isForThisBot(@NonNull String text) {
        text = text.trim();
        if (!text.startsWith("/")) {
            return false;
        }

        String firstToken = text.split("\\s", 2)[0];
        int atIndex = firstToken.indexOf('@');

        if (atIndex > 0) {
            String mention = firstToken.substring(atIndex + 1);
            return botUsername.equalsIgnoreCase(mention);
        }

        return true;
    }
}
