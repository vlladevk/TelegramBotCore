package org.pl.pcz.yevkov.botcore.annotation;

import org.pl.pcz.yevkov.botcore.application.dto.event.EventDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation used to declare a bot event class as discoverable and eligible
 * for registration in the event handling system.
 * <p>
 * This annotation is intended to be placed on classes that implement {@code BotEvent}
 * and represent domain-level Telegram events (e.g., user joined, message received).
 *
 * <p>When applied, this annotation allows the class to be automatically discovered
 * at runtime via reflection (e.g., using {@link org.reflections.Reflections}),
 * without the need for manual registration in the application configuration.</p>
 *
 * <p>The {@code eventDto()} attribute specifies the associated {@link EventDto}
 * class that represents the external, serializable form of the event (usually used
 * for mapping to APIs, storage, or messaging).</p>
 *
 * <p>This approach decouples the internal event representation from the
 * infrastructure, while still providing enough metadata to register and map
 * event types dynamically.</p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>
 * &#64;BotEventBinding(eventDto = ChatMessageReceivedDto.class)
 * public class ChatMessageReceivedEvent implements BotEvent {
 *     ...
 * }
 * </pre>
 *
 * @see org.pl.pcz.yevkov.botcore.domain.event.BotEvent
 * @see org.pl.pcz.yevkov.botcore.application.dto.event.EventDto
 * @see org.pl.pcz.yevkov.botcore.infrastructure.scanner.BotEventScanner
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BotEventBinding {
    Class<? extends EventDto> eventDto();
}