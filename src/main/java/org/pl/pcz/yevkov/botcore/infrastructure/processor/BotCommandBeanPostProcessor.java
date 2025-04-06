package org.pl.pcz.yevkov.botcore.infrastructure.processor;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.botcore.annotation.BotCommand;
import org.pl.pcz.yevkov.botcore.annotation.CommandController;
import org.pl.pcz.yevkov.botcore.application.command.registry.BotCommandRegistrar;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;


/**
 * {@link BeanPostProcessor} that processes beans annotated with {@link CommandController}.
 * It scans methods for the {@link BotCommand} annotation and registers them with the {@link BotCommandRegistrar}
 * for later execution. This enables dynamic discovery and handling of bot commands.
 */
@Component
@RequiredArgsConstructor
public class BotCommandBeanPostProcessor implements BeanPostProcessor {
    private final ObjectProvider<BotCommandRegistrar> commandScanner;

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
