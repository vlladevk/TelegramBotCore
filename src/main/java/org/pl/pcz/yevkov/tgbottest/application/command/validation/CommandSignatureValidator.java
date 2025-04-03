package org.pl.pcz.yevkov.tgbottest.application.command.validation;

import lombok.NonNull;

import java.lang.reflect.Method;

public interface CommandSignatureValidator {
    void validate(@NonNull Method method);
}
