package org.pl.pcz.yevkov.botcore.application.command.registry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pl.pcz.yevkov.botcore.application.command.exception.DuplicateCommandRegistrationException;
import org.pl.pcz.yevkov.botcore.application.command.factory.BotCommandFactory;
import org.pl.pcz.yevkov.botcore.application.command.validation.CommandSignatureValidator;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatType;
import org.pl.pcz.yevkov.botcore.domain.entity.UserRole;

import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BotCommandCatalogTest {
    @Mock
    private CommandSignatureValidator signatureValidator;

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

        Mockito.verify(signatureValidator).validate(method);
        Mockito.verify(commandFactory).create(handler, method);
    }

    @Test
    void registerCommand_throwsExceptionOnDuplicateName() throws NoSuchMethodException {
        Object handler = new DummyController();
        Method method1 = getHandlerMethod("testCommand1");
        Method method2 = getHandlerMethod("testCommand2");

        RegisteredCommand command = createMockCommand("test", method1, handler);

        Mockito.when(commandFactory.create(handler, method1)).thenReturn(command);
        Mockito.when(commandFactory.create(handler, method2)).thenReturn(command);
        Mockito.doNothing().when(signatureValidator).validate(method1);
        Mockito.doNothing().when(signatureValidator).validate(method2);

        catalog.registerCommand(handler, method1);

        assertThrows(DuplicateCommandRegistrationException.class, () ->
                catalog.registerCommand(handler, method2));

        Mockito.verify(signatureValidator, Mockito.times(1)).validate(method1);
        Mockito.verify(signatureValidator, Mockito.times(1)).validate(method2);

        Mockito.verify(commandFactory, Mockito.times(1)).create(handler, method1);
        Mockito.verify(commandFactory, Mockito.times(1)).create(handler, method2);
    }

    @Test
    void registerCommand_throwsOnInvalidSignature() throws NoSuchMethodException {
        Object handler = new DummyController();
        Method method = getHandlerMethod("testCommand1");

        Mockito.doThrow(new IllegalArgumentException("Invalid")).when(signatureValidator).validate(method);

        assertThrows(IllegalArgumentException.class, () -> catalog.registerCommand(handler, method));
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

        Mockito.when(commandFactory.create(handler, method1)).thenReturn(command1);
        Mockito.when(commandFactory.create(handler, method2)).thenReturn(command2);
        Mockito.doNothing().when(signatureValidator).validate(method1);
        Mockito.doNothing().when(signatureValidator).validate(method2);

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
                .chatTypes(new ChatType[]{ChatType.GROUP, ChatType.PRIVATE})
                .showInMenu(true)
                .build();
    }

    private void prepareMocking(Object handler, Method method, RegisteredCommand command) {
        Mockito.doNothing().when(signatureValidator).validate(method);
        Mockito.when(commandFactory.create(handler, method)).thenReturn(command);
    }

    private Method getHandlerMethod(String methodName) throws NoSuchMethodException {
        return DummyController.class.getMethod(methodName, ChatMessageReceivedDto.class);
    }

}