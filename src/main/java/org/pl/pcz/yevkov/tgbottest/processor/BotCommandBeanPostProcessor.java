package org.pl.pcz.yevkov.tgbottest.processor;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.tgbottest.annotation.BotCommand;
import org.pl.pcz.yevkov.tgbottest.annotation.CommandController;
import org.pl.pcz.yevkov.tgbottest.application.CommandScanner;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;


@Component
@RequiredArgsConstructor
public class BotCommandBeanPostProcessor implements BeanPostProcessor {
    private final ObjectProvider<CommandScanner> commandScanner;

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String ignore) throws BeansException {
        Class<?> originalClass = AopUtils.getTargetClass(bean);
        if (originalClass.isAnnotationPresent(CommandController.class)) {
            for (var method : ReflectionUtils.getDeclaredMethods(originalClass)) {
                if (method.isAnnotationPresent(BotCommand.class)) {
                    try {
                        Method proxyMethod = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
                        commandScanner.getObject().registerCommand(bean, proxyMethod);
                    } catch (NoSuchMethodException e) {
                        throw new IllegalStateException("Failed to find method " + method.getName(), e);
                    }
                }
            }
        }
        return bean;
    }
}
