package org.pl.pcz.yevkov.tgbottest.application;

import lombok.NonNull;

import java.lang.reflect.Method;

public interface CommandScanner {
     void registerCommand(@NonNull Object handler, @NonNull Method method);
}
