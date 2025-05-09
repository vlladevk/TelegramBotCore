package org.pl.pcz.yevkov.botcore.application.command.parser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DefaultCommandExtractorTest {
    private final DefaultCommandExtractor extractor = new DefaultCommandExtractor();

    private static Stream<Arguments> provideTestArguments() {
        return Stream.of(
                Arguments.of("command arg1 arg2", "command"),
                Arguments.of("command", "command"),
                Arguments.of("   command   ", "command"),
                Arguments.of("command@bot arg1", "command"),
                Arguments.of("command@bot", "command"),
                Arguments.of("command@bot@extra arg1", "command"),
                Arguments.of("\tcommand\targ1", "command"),
                Arguments.of("   command@bot    arg1 arg2", "command")
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestArguments")
    void extract_testCommandExtraction_returnCommand(String input, String expectedResult) {
        Command result = extractor.extract(input);
        assertEquals(expectedResult, result.text());
    }
}