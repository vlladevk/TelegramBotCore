package org.pl.pcz.yevkov.botcore.application.command.executor;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pl.pcz.yevkov.botcore.application.command.exception.CommandExecutionException;
import org.pl.pcz.yevkov.botcore.application.command.registry.RegisteredCommand;
import org.pl.pcz.yevkov.botcore.application.command.response.TextResponse;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class ReflectionBasedCommandExecutorTest {
    private final ReflectionBasedCommandExecutor executor = new ReflectionBasedCommandExecutor();

    @SuppressWarnings("unused")
    static class TestHandler {
        public TextResponse valid(ChatMessageReceivedDto ignore) {
            return new TextResponse(new ChatId(1L),"ok");
        }

        public void returnsNull(ChatMessageReceivedDto ignore) {
        }

        public String invalid(ChatMessageReceivedDto ignore) {
            return "not allowed";
        }

        public TextResponse throwsError(ChatMessageReceivedDto ignore) {
            throw new RuntimeException("Failure");
        }
    }

    @Test
    void execute_validMethod_returnsResponse() throws Exception {
        var command = makeCommand("valid");
        ChatMessageReceivedDto dto = Mockito.mock(ChatMessageReceivedDto.class);
        var result = executor.execute(command, dto);

        assertTrue(result.isPresent());
        assertEquals("ok", result.get().text());
    }

    @Test
    void execute_methodReturnsNull_returnsEmpty() throws Exception {
        var command = makeCommand("returnsNull");
        ChatMessageReceivedDto dto = Mockito.mock(ChatMessageReceivedDto.class);
        var result = executor.execute(command, dto);

        assertTrue(result.isEmpty());
    }

    @Test
    void execute_invalidReturnType_throwsException() throws Exception {
        var command = makeCommand("invalid");
        ChatMessageReceivedDto dto = Mockito.mock(ChatMessageReceivedDto.class);

        assertThrows(CommandExecutionException.class, () -> executor.execute(command, dto));
    }

    @Test
    void execute_methodThrows_throwsException() throws Exception {
        var command = makeCommand("throwsError");
        ChatMessageReceivedDto dto = Mockito.mock(ChatMessageReceivedDto.class);

         assertThrows(CommandExecutionException.class, () -> executor.execute(command, dto));
    }

    private RegisteredCommand makeCommand(String methodName) throws NoSuchMethodException {
        TestHandler handler = new TestHandler();
        Method method = TestHandler.class.getMethod(methodName, ChatMessageReceivedDto.class);
        return RegisteredCommand.builder()
                .name("/test")
                .handler(handler)
                .method(method)
                .build();
    }

}