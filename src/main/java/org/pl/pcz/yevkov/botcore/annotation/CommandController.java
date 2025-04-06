package org.pl.pcz.yevkov.botcore.annotation;

import org.springframework.stereotype.Controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation used to mark a class as a command controller for the bot.
 * This annotation combines the functionality of the {@link Controller} annotation from Spring
 * to indicate that the class serves as a controller that handles bot commands.
 * <p>
 * Classes annotated with {@link CommandController} are typically responsible for handling incoming
 * bot commands and managing the logic for executing those commands. These classes are discovered and
 * processed by the application to register command handlers, ensuring that each method annotated with
 * {@link BotCommand} inside the controller is properly mapped to a corresponding bot command.
 * </p>
 * <p>
 * This annotation is intended to be used on classes that contain bot command handlers, simplifying the
 * configuration and integration of commands within the Spring context.
 * </p>
 * <p>
 * Example:
 * <pre>
 * {@code
 * @CommandController
 * public class AdminController {
 *     @BotCommand(name = "kick", description = "Kick a user from the group")
 *     public void kickUser(ChatMessageReceivedDto message) {
 *         // Command logic to kick a user
 *     }
 * }
 * </pre>
 * </p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Controller
public @interface CommandController {
}
