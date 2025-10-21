package com.incubyte;

/**
 * Exception thrown when negative numbers are provided to the calculator.
 */
public class NegativeNumberException extends RuntimeException {
    public NegativeNumberException(String message) {
        super(message);
    }
}
