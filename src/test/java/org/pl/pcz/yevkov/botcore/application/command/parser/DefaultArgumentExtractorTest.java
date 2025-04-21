package org.pl.pcz.yevkov.botcore.application.command.parser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class DefaultArgumentExtractorTest {
    private final DefaultArgumentExtractor extractor = new DefaultArgumentExtractor();

    private static Stream<Arguments> provideTestArguments() {
        return Stream.of(
                Arguments.of("command arg1 arg2", List.of("arg1", "arg2")),
                Arguments.of("command", List.of()),
                Arguments.of("", List.of()),
                Arguments.of("     ", List.of()),
                Arguments.of("command    arg1    arg2", List.of("arg1", "arg2")),
                Arguments.of("\tcommand\targ1\targ2", List.of("arg1", "arg2"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestArguments")
    void extract_testArguments_returnArguments(String input, List<String> expectedResult) {
        List<String> result = extractor.extract(input);
        assertEquals(expectedResult, result);
    }
}