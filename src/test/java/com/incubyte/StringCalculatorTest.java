package com.incubyte;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class StringCalculatorTest {

    private final StringCalculator calc = new StringCalculator();

    @Test
    void emptyStringReturnsZero() {
        assertEquals(0, calc.add(""), "Empty string should return 0");
    }

    @Test
    void singleNumberReturnsValue() {
        assertEquals(1, calc.add("1"));
        assertEquals(5, calc.add("5"));
    }

    @Test
    void twoNumbersCommaSeparated() {
        assertEquals(6, calc.add("1,5"));
    }

    @Test
    void anyAmountOfNumbers() {
        assertEquals(15, calc.add("1,2,3,4,5"));
    }

    @Test
    void newlinesAsSeparators() {
        assertEquals(6, calc.add("1\n2,3"));
        assertEquals(10, calc.add("1\n2\n3,4"));
    }

    @Test
    void customSingleCharDelimiter() {
        assertEquals(3, calc.add("//;\n1;2"));
    }

    @Test
    void negativeNumberThrowsException() {
        NegativeNumberException ex = assertThrows(NegativeNumberException.class, () -> calc.add("1,-2,3"));
        assertTrue(ex.getMessage().contains("negative numbers not allowed"));
        assertTrue(ex.getMessage().contains("-2"));
    }

    @Test
    void multipleNegativesReported() {
        NegativeNumberException ex = assertThrows(NegativeNumberException.class, () -> calc.add("-1,-2,3,-5"));
        assertTrue(ex.getMessage().contains("-1"));
        assertTrue(ex.getMessage().contains("-2"));
        assertTrue(ex.getMessage().contains("-5"));
        assertTrue(ex.getMessage().contains(" -1") || ex.getMessage().contains("-1,") || ex.getMessage().contains("-1,-2"));
    }

    @Test
    void multiCharDelimiterSupport() {
        assertEquals(6, calc.add("//[***]\n1***2***3"));
    }

    @Test
    void multipleDelimitersSupport() {
        assertEquals(6, calc.add("//[*][%]\n1*2%3"));
    }
}