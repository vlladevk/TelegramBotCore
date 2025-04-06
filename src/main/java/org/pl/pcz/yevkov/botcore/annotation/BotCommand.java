package org.pl.pcz.yevkov.botcore.annotation;

import org.pl.pcz.yevkov.botcore.domain.entity.UserRole;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BotCommand {
    String name() default "";
    String description() default "description";
    UserRole userRole() default UserRole.USER;
    ChatType[] chatTypes() default {ChatType.GROUP, ChatType.PRIVATE};
    boolean showInMenu() default true;
}
