package org.pl.pcz.yevkov.botcore.application.command.validation;

import org.junit.jupiter.api.Test;
import org.pl.pcz.yevkov.botcore.annotation.BotCommand;
import org.pl.pcz.yevkov.botcore.application.command.response.TextResponse;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BotCommandSignatureValidatorTest {

    private final BotCommandSignatureValidator validator = new BotCommandSignatureValidator();

    static class ValidController {
        @BotCommand
        public TextResponse valid(ChatMessageReceivedDto ignore) {
            return null;
        }

        @BotCommand
        public void validVoid(ChatMessageReceivedDto ignore) {}

        @BotCommand
        public String invalidReturn(ChatMessageReceivedDto ignore) {
            return "";
        }

        @BotCommand
        public TextResponse tooManyParams(ChatMessageReceivedDto ignore, String ignoreExtra) {
            return null;
        }

        @BotCommand
        public TextResponse wrongParamType(String ignoreBad) {
            return null;
        }
    }

    static class UnrelatedController {
        public TextResponse notDeclaredHere(ChatMessageReceivedDto ignore) {
            return null;
        }
    }

    @Test
    void validMethod_passesValidation() throws Exception {
        Object handler = new ValidController();
        Method method = handler.getClass().getDeclaredMethod("valid", ChatMessageReceivedDto.class);
        assertDoesNotThrow(() -> validator.validate(handler, method));
    }

    @Test
    void validVoidMethod_passesValidation() throws Exception {
        Object handler = new ValidController();
        Method method = handler.getClass().getDeclaredMethod("validVoid", ChatMessageReceivedDto.class);
        assertDoesNotThrow(() -> validator.validate(handler, method));
    }

    @Test
    void invalidReturnType_throwsException() throws Exception {
        Object handler = new ValidController();
        Method method = handler.getClass().getDeclaredMethod("invalidReturn", ChatMessageReceivedDto.class);
        assertThrows(IllegalStateException.class, () -> validator.validate(handler, method));
    }

    @Test
    void tooManyParameters_throwsException() throws Exception {
        Object handler = new ValidController();
        Method method = handler.getClass().getDeclaredMethod("tooManyParams", ChatMessageReceivedDto.class, String.class);
        assertThrows(IllegalStateException.class, () -> validator.validate(handler, method));
    }

    @Test
    void wrongParameterType_throwsException() throws Exception {
        Object handler = new ValidController();
        Method method = handler.getClass().getDeclaredMethod("wrongParamType", String.class);
        assertThrows(IllegalStateException.class, () -> validator.validate(handler, method));
    }

    @Test
    void methodNotBelongingToHandler_throwsException() throws Exception {
        Object handler = new ValidController();
        Method method = UnrelatedController.class.getDeclaredMethod("notDeclaredHere", ChatMessageReceivedDto.class);
        assertThrows(IllegalStateException.class, () -> validator.validate(handler, method));
    }

    @Test
    void withoutBotCommandAnnotation_throwsException() throws Exception {
        Object handler = new UnrelatedController();
        Method method = UnrelatedController.class.getDeclaredMethod("notDeclaredHere", ChatMessageReceivedDto.class);
        assertThrows(IllegalStateException.class, () -> validator.validate(handler, method));
    }
}