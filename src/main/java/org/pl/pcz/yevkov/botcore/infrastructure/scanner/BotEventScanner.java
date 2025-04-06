package org.pl.pcz.yevkov.botcore.infrastructure.scanner;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.annotation.BotEventBinding;
import org.pl.pcz.yevkov.botcore.domain.event.BotEvent;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Scans the classpath for classes annotated with {@link BotEventBinding} that implement {@link BotEvent}.
 * It uses {@link Reflections} for runtime classpath scanning, enabling dynamic event discovery without manual registration.
 * This allows bot events to be registered flexibly and modularly, without requiring them to be Spring beans.
 *
 * <p><b>Why use both annotation and interface checks?</b></p>
 * <ul>
 *     <li>{@code @BotEventBinding} marks a class for registration as a bot event.</li>
 *     <li>{@code BotEvent.class.isAssignableFrom(clazz)} ensures type safety.</li>
 *     <li>{@code clazz.asSubclass(BotEvent.class)} prevents unchecked warnings.</li>
 * </ul>
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class BotEventScanner {
    private final Reflections reflections;


    public Set<Class<? extends BotEvent>> scanAnnotatedEvents() {
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(BotEventBinding.class);

        for (Class<?> clazz : annotatedClasses) {
            if (!BotEvent.class.isAssignableFrom(clazz)) {
                log.error("Class {} is annotated with @BotEventBinding but does NOT implement BotEvent",
                        clazz.getName());
                throw new IllegalStateException("Invalid @BotEventBinding: " +
                        clazz.getName() + " must implement BotEvent");
            }
        }

        Set<Class<? extends BotEvent>> validEvents = annotatedClasses.stream()
                .map(this::castToBotEvent)
                .collect(Collectors.toSet());

        log.info("Found {} valid bot events: {}", validEvents.size(),
                validEvents.stream().map(Class::getSimpleName).toList());

        return validEvents;
    }

    private Class<? extends BotEvent> castToBotEvent(Class<?> clazz) {
        return clazz.asSubclass(BotEvent.class);
    }
}