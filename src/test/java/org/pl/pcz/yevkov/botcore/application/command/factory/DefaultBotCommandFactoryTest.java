package org.pl.pcz.yevkov.botcore.application.command.factory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.pl.pcz.yevkov.botcore.annotation.BotCommand;
import org.pl.pcz.yevkov.botcore.application.command.registry.RegisteredCommand;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatType;
import org.pl.pcz.yevkov.botcore.domain.entity.UserRole;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DefaultBotCommandFactoryTest {
    static class DummyController {
        @BotCommand(description = "desc", userRole = UserRole.CHAT_ADMIN, chatTypes = {ChatType.GROUP})
        public void someCommand() {}

        @BotCommand(name = "/explicit", description = "explicit name", showInMenu = false, userRole = UserRole.USER,
                chatTypes = {ChatType.PRIVATE})
        public void command() {}

        public void method() {}
    }

    private final DefaultBotCommandFactory factory = new DefaultBotCommandFactory();

    static Stream<Arguments> provideCommands() throws NoSuchMethodException {
        DummyController handler = new DummyController();

        Class<? extends DummyController> clazz = handler.getClass();

        Method method1 = clazz.getDeclaredMethod("someCommand");
        Method method2 = clazz.getDeclaredMethod("command");

        RegisteredCommand command1 = RegisteredCommand.builder()
                .name("/some_command")
                .description("desc")
                .showInMenu(true)
                .userRole(UserRole.CHAT_ADMIN)
                .chatTypes(List.of(ChatType.GROUP))
                .handler(handler)
                .method(method1)
                .build();

        RegisteredCommand command2 = RegisteredCommand.builder()
                .name("/explicit")
                .description("explicit name")
                .showInMenu(false)
                .userRole(UserRole.USER)
                .chatTypes(List.of(ChatType.PRIVATE))
                .handler(handler)
                .method(method2)
                .build();

        return Stream.of(
                Arguments.of(handler, method1, command1),
                Arguments.of(handler, method2, command2)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCommands")
    void create(Object handler, Method method, RegisteredCommand expectedResult) {
        RegisteredCommand command = factory.create(handler, method);
        assertEquals(expectedResult, command);
    }

    @Test
    void create_withOutAnnotationBotCommand() throws NoSuchMethodException {
        DummyController handler = new DummyController();
        Method method1 = DummyController.class.getDeclaredMethod("method");
        assertThrows(AssertionError.class, () -> factory.create(handler, method1));
    }
}