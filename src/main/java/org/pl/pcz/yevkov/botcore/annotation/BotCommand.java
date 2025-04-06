package org.pl.pcz.yevkov.botcore.annotation;

import org.pl.pcz.yevkov.botcore.domain.entity.UserRole;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation used to define a bot command.
 * This annotation can be applied to methods to indicate that the method is a handler for a specific bot command.
 * It provides metadata about the command, such as its name, description, user role requirements,
 * the types of chats in which the command can be used, and whether the command should appear in a menu.
 * <p>
 * This annotation is processed at runtime to configure command behavior and enforce access control based on user roles
 * and chat types. It can also be used to dynamically populate command menus or define the bot's functionality in
 * various chat environments (e.g., group, private).
 * </p>
 * <p>
 * Example:
 * <pre>
 * {@code
 * @BotCommand(
 *     name = "kick",
 *     description = "Kicks a user from the group",
 *     userRole = UserRole.CHAT_ADMIN,
 *     chatTypes = {ChatType.GROUP},
 *     showInMenu = false
 * )
 * public void kickUser(ChatMessageReceivedDto message) {
 *     // Command logic to kick a user
 * }
 * </pre>
 * </p>
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BotCommand {
    /**
     * The name of the bot command.
     * If not provided, the method name will be used as the command name by default.
     *
     * @return the name of the command
     */
    String name() default "";

    /**
     * A description of the command that provides context for its usage.
     *
     * @return the description of the command
     */
    String description() default "description";

    /**
     * The required user role to execute the command.
     * The default value is {@link UserRole#USER}, which means any user can execute the command.
     *
     * @return the required user role for executing the command
     */
    UserRole userRole() default UserRole.USER;

    /**
     * The types of chats where the command can be executed.
     * By default, the command can be used in both group and private chats.
     *
     * @return the allowed chat types for the command
     */
    ChatType[] chatTypes() default {ChatType.GROUP, ChatType.PRIVATE};

    /**
     * A flag that determines whether the command should be visible in the bot's command menu.
     * By default, the command will be shown in the menu.
     *
     * @return {@code true} if the command should appear in the menu, {@code false} otherwise
     */
    boolean showInMenu() default true;
}
