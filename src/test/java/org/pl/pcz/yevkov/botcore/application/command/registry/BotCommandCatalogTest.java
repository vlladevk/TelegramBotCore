package org.pl.pcz.yevkov.botcore.application.command.registry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pl.pcz.yevkov.botcore.application.command.exception.DuplicateCommandRegistrationException;
import org.pl.pcz.yevkov.botcore.application.command.factory.BotCommandFactory;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatType;
import org.pl.pcz.yevkov.botcore.domain.entity.UserRole;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BotCommandCatalogTest {

    @Mock
    private BotCommandFactory commandFactory;

    @InjectMocks
    private BotCommandCatalog catalog;

    @SuppressWarnings("unused")
    public static class DummyController {
        public void testCommand1(ChatMessageReceivedDto ignore) {}
        public void testCommand2(ChatMessageReceivedDto ignore) {}
    }

    @Test
    void registerCommand_addsCommandSuccessfully() throws Exception {
        Object handler = new DummyController();
        Method method = getHandlerMethod("testCommand1");
        RegisteredCommand command = createMockCommand("test", method, handler);

        prepareMocking(handler, method, command);
        catalog.registerCommand(handler, method);

        var result = catalog.getRegisteredCommand(command.name());

        assertTrue(result.isPresent());
        assertEquals(command, result.get());

        Mockito.verify(commandFactory).create(handler, method);
    }

    @Test
    void registerCommand_detectsDuplicateByName() throws Exception {
        Object handler = new DummyController();
        Method m1 = getHandlerMethod("testCommand1");
        Method m2 = getHandlerMethod("testCommand2");

        RegisteredCommand c1 = createMockCommand("dup", m1, handler);
        RegisteredCommand c2 = createMockCommand("dup", m2, handler);

        prepareMocking(handler, m1, c1);
        prepareMocking(handler, m2, c2);

        catalog.registerCommand(handler, m1);
        assertThrows(DuplicateCommandRegistrationException.class,
                () -> catalog.registerCommand(handler, m2));
    }

    @Test
    void getRegisteredCommand_commandNotFound() {
        var result = catalog.getRegisteredCommand("test");
        assertFalse(result.isPresent());
    }

    @Test
    void getAllRegisteredCommands_returnsAllCommands() throws NoSuchMethodException {
        Object handler = new DummyController();
        Method method1 = getHandlerMethod("testCommand1");
        Method method2 = getHandlerMethod("testCommand2");

        RegisteredCommand command1 = createMockCommand("test1", method1, handler);
        RegisteredCommand command2 = createMockCommand("test2", method2, handler);

        prepareMocking(handler, method1, command1);
        prepareMocking(handler, method2, command2);

        catalog.registerCommand(handler, method1);
        catalog.registerCommand(handler, method2);

        var result = catalog.getAllRegisteredCommands();
        assertEquals(2, result.size());

        assertTrue(result.contains(command1));
        assertTrue(result.contains(command2));
    }

    private RegisteredCommand createMockCommand(String name, Method method, Object handler) {
        return RegisteredCommand.builder()
                .name(name)
                .description("desc")
                .handler(handler)
                .method(method)
                .userRole(UserRole.USER)
                .chatTypes(List.of(ChatType.GROUP, ChatType.PRIVATE))
                .showInMenu(true)
                .build();
    }

    private void prepareMocking(Object handler, Method method, RegisteredCommand command) {
        Mockito.when(commandFactory.create(handler, method)).thenReturn(command);
    }

    private Method getHandlerMethod(String methodName) throws NoSuchMethodException {
        return DummyController.class.getMethod(methodName, ChatMessageReceivedDto.class);
    }

}