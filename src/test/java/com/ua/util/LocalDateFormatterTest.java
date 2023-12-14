package com.ua.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalDateFormatterTest {

    @Test
    void formatIsSuccess() {
        String date = "2023-12-13";

        LocalDate actualResult = LocalDateFormatter.format(date);

        assertThat(actualResult).isEqualTo(LocalDate.of(2023, 12, 13));
    }

    @Test
    void throwExceptionIfFormatInvalid() {
        String date = "12-13-2023";

        Assertions.assertThrows(DateTimeParseException.class, () -> LocalDateFormatter.format(date));
    }

    @ParameterizedTest
    @MethodSource("getValidationArguments")
    void isValid(String date, boolean expectedResult) {
        boolean actualResult = LocalDateFormatter.isValid(date);

        assertEquals(expectedResult, actualResult);
    }

    static Stream<Arguments> getValidationArguments() {
        return Stream.of(
                Arguments.of("2023-12-13", true),
                Arguments.of("12-13-2023", false),
                Arguments.of("2023-12-13 23:30", false),
                Arguments.of(null, false)
        );
    }
}