package org.pl.pcz.yevkov.botcore.application.command.dispatcher;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.pl.pcz.yevkov.botcore.application.command.access.CommandAccessResult;
import org.pl.pcz.yevkov.botcore.application.command.access.CommandPermissionChecker;
import org.pl.pcz.yevkov.botcore.application.command.error.CommandErrorHandler;
import org.pl.pcz.yevkov.botcore.application.command.exception.CommandExecutionException;
import org.pl.pcz.yevkov.botcore.application.command.executor.CommandExecutor;
import org.pl.pcz.yevkov.botcore.application.command.parser.CommandExtractor;
import org.pl.pcz.yevkov.botcore.application.command.registry.BotCommandProvider;
import org.pl.pcz.yevkov.botcore.application.command.registry.RegisteredCommand;
import org.pl.pcz.yevkov.botcore.application.command.response.TextResponse;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatType;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;
import org.pl.pcz.yevkov.botcore.domain.vo.MessageId;
import org.pl.pcz.yevkov.botcore.domain.vo.ThreadId;
import org.pl.pcz.yevkov.botcore.domain.vo.UserId;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CommandDispatcherTest {

    @Mock BotCommandProvider provider;
    @Mock CommandPermissionChecker permission;
    @Mock CommandExecutor executor;
    @Mock CommandErrorHandler error;
    @Mock CommandExtractor extractor;

    @InjectMocks CommandDispatcher dispatcher;

    private static ChatMessageReceivedDto msg(String text) {
        return new ChatMessageReceivedDto(
                new ChatId(1L),
                new UserId(2L),
                new ThreadId(3),
                new MessageId(4),
                "userName",
                "first Name",
                text,
                ChatType.PRIVATE
        );
    }

    private static RegisteredCommand rc() throws NoSuchMethodException {
        Method m = Object.class.getDeclaredMethod("toString");
        return RegisteredCommand.builder()
                .name("/test")
                .handler(new Object())
                .method(m)
                .build();
    }

    private final TextResponse ok = new TextResponse(new ChatId(1L), "ok");

    @Nested
    class EmptyMessage {
        @Test
        void dispatch_emptyMessage_returnsErrorResponse() {
            ChatMessageReceivedDto m = msg("");
            when(error.handleEmptyMessage(m)).thenReturn(ok);

            assertSame(ok, dispatcher.dispatch(m).orElseThrow());

            verify(error).handleEmptyMessage(m);
            verify(executor, never()).execute(any(), any());
        }
    }

    @Nested
    class Unknown {
        @Test
        void dispatch_unknownCommand_returnsUnknownCommandError() {
            ChatMessageReceivedDto m = msg("/oops");
            when(extractor.extract("/oops")).thenReturn("oops");
            when(provider.getRegisteredCommand("oops")).thenReturn(Optional.empty());
            when(error.handleUnknownCommand(m, "oops")).thenReturn(ok);

            assertSame(ok, dispatcher.dispatch(m).orElseThrow());

            verify(error).handleUnknownCommand(m, "oops");
            verify(executor, never()).execute(any(), any());
        }
    }

    @Nested
    class Access {
        @Test
        void dispatch_accessDenied_returnsAccessDeniedResponse() throws Exception {
            ChatMessageReceivedDto m = msg("/test");
            RegisteredCommand cmd = rc();

            givenCommon(m, cmd);

            CommandAccessResult denied = new CommandAccessResult(false, "role");
            when(permission.hasAccess(m, cmd)).thenReturn(denied);
            when(error.handleAccessDenied(m, denied, "test")).thenReturn(ok);

            assertSame(ok, dispatcher.dispatch(m).orElseThrow());

            verify(error).handleAccessDenied(m, denied, "test");
            verify(executor, never()).execute(any(), any());
        }

        @Test
        void dispatch_accessGranted_executesCommand() throws Exception {
            ChatMessageReceivedDto m = msg("/test");
            RegisteredCommand cmd = rc();

            givenCommon(m, cmd);

            when(permission.hasAccess(m, cmd)).thenReturn(new CommandAccessResult(true, "role"));
            when(executor.execute(cmd, m)).thenReturn(Optional.of(ok));

            assertSame(ok, dispatcher.dispatch(m).orElseThrow());

            verify(executor).execute(cmd, m);
            verify(error, never()).handleAccessDenied(any(), any(), any());
        }

        private void givenCommon(ChatMessageReceivedDto m, RegisteredCommand cmd) {
            when(extractor.extract(m.text())).thenReturn("test");
            when(provider.getRegisteredCommand("test")).thenReturn(Optional.of(cmd));
        }
    }

    @Nested
    class ExecuteErrors {
        @Test
        void dispatch_commandExecutionThrowsHandledException_returnsHandledError() throws Exception {
            ChatMessageReceivedDto m = msg("/test");
            RegisteredCommand cmd = rc();

            when(extractor.extract("/test")).thenReturn("test");
            when(provider.getRegisteredCommand("test")).thenReturn(Optional.of(cmd));
            when(permission.hasAccess(m, cmd)).thenReturn(new CommandAccessResult(true, "role"));

            CommandExecutionException boom = new CommandExecutionException("boom");
            when(executor.execute(cmd, m)).thenThrow(boom);
            when(error.handleExecutionError(m, "test", boom)).thenReturn(ok);

            assertSame(ok, dispatcher.dispatch(m).orElseThrow());

            verify(error).handleExecutionError(m, "test", boom);
        }

        @Test
        void dispatch_commandExecutionThrowsRuntimeException_returnsHandledError() throws Exception {
            ChatMessageReceivedDto m = msg("/test");
            RegisteredCommand cmd = rc();

            when(extractor.extract("/test")).thenReturn("test");
            when(provider.getRegisteredCommand("test")).thenReturn(Optional.of(cmd));
            when(permission.hasAccess(m, cmd)).thenReturn(new CommandAccessResult(true, "role"));

            RuntimeException boom = new RuntimeException("boom");
            when(executor.execute(cmd, m)).thenThrow(boom);
            when(error.handleExecutionError(m, "test", boom)).thenReturn(ok);

            assertSame(ok, dispatcher.dispatch(m).orElseThrow());

            verify(error).handleExecutionError(m, "test", boom);
        }
    }
}
