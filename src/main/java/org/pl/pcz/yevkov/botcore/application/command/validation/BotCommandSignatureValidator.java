package org.pl.pcz.yevkov.botcore.application.command.validation;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.annotation.BotCommand;
import org.pl.pcz.yevkov.botcore.application.command.response.TextResponse;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;



@Component
public class BotCommandSignatureValidator implements CommandSignatureValidator {

    @Override
    public void validate(@NonNull Method method) {
        if (method.getParameterCount() != 1 || !ChatMessageReceivedDto.class.equals(method.getParameterTypes()[0])) {
            throw new IllegalStateException("@" + BotCommand.class.getSimpleName() + " method '" + method.getName() +
                    "' must have exactly one parameter of type Update");
        }

        Class<?> returnType = method.getReturnType();
        if (!TextResponse.class.equals(returnType) && !void.class.equals(returnType)) {
            throw new IllegalStateException("@" + BotCommand.class.getSimpleName() + " method '" + method.getName() +
                    "' must return either SendMessage or void");
        }
    }
}
