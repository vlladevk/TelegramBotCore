package org.pl.pcz.yevkov.botcore.application.command.registry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatType;
import org.pl.pcz.yevkov.botcore.domain.entity.UserRole;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.adapter.BotApiAdapter;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.exception.BotApiException;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.command.BotCommandDtoMapper;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.lang.reflect.Method;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CommandRegistrarTest {
    @Mock
    private BotApiAdapter botApiAdapter;

    @Mock
    private BotCommandDtoMapper commandDtoMapper;

    @Mock
    private BotCommandCatalog commandCatalog;

    @InjectMocks
    private CommandRegistrar registrar;

    @SuppressWarnings("unused")
    public static class DummyController {
        public void testCommand1(ChatMessageReceivedDto ignore) {}

        public void testCommand2(ChatMessageReceivedDto ignore) {}

        public void testCommand3(ChatMessageReceivedDto ignore) {}
    }

    @Test
    void onApplicationReady_registersVisibleCommands() throws NoSuchMethodException {
        Object handler = new DummyController();
        Method method1 = DummyController.class.getMethod("testCommand1", ChatMessageReceivedDto.class);
        Method method2 = DummyController.class.getMethod("testCommand2", ChatMessageReceivedDto.class);
        Method method3 = DummyController.class.getMethod("testCommand3", ChatMessageReceivedDto.class);

        RegisteredCommand cmd1 = createMockCommand("test1", true, method1, handler);
        RegisteredCommand cmd2 = createMockCommand("test2", true, method2, handler);
        RegisteredCommand cmd3 = createMockCommand("test3", false, method3, handler);

        List<RegisteredCommand> allCommands = List.of(cmd1, cmd2, cmd3);
        Mockito.when(commandCatalog.getAllRegisteredCommands()).thenReturn(allCommands);

        BotCommand botCommand1 = new BotCommand("test1", "desc");
        BotCommand botCommand2 = new BotCommand("test2", "desc");

        Mockito.when(commandDtoMapper.mapFrom(cmd1)).thenReturn(botCommand1);
        Mockito.when(commandDtoMapper.mapFrom(cmd2)).thenReturn(botCommand2);

        Mockito.doNothing().when(botApiAdapter).execute(Mockito.any(SetMyCommands.class));
        registrar.onApplicationReady();

        Mockito.verify(botApiAdapter, Mockito.times(1))
                .execute(Mockito.any(SetMyCommands.class));

        Mockito.verify(commandDtoMapper, Mockito.never()).mapFrom(cmd3);
    }

    @Test
    void onApplicationReady_botApiThrowsException_throwsRuntimeException() throws NoSuchMethodException {
        Object handler = new DummyController();
        Method method = DummyController.class.getMethod("testCommand1", ChatMessageReceivedDto.class);
        RegisteredCommand cmd = createMockCommand("test1", true, method, handler);

        Mockito.when(commandCatalog.getAllRegisteredCommands()).thenReturn(List.of(cmd));
        Mockito.when(commandDtoMapper.mapFrom(cmd)).thenReturn(new BotCommand("test1", "desc"));

        Mockito.doThrow(new BotApiException("Telegram error"))
                .when(botApiAdapter)
                .execute(Mockito.any(SetMyCommands.class));

        Assertions.assertThrows(BotApiException.class, () -> registrar.onApplicationReady());
    }


    private RegisteredCommand createMockCommand(String name, boolean isShowInMenu, Method method, Object handler) {
        return RegisteredCommand.builder()
                .name(name)
                .description("desc")
                .handler(handler)
                .method(method)
                .userRole(UserRole.USER)
                .chatTypes(new ChatType[]{ChatType.GROUP, ChatType.PRIVATE})
                .showInMenu(isShowInMenu)
                .build();
    }
}