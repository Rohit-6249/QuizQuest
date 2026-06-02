package com.quizquest.web;

/**
 * Thrown for invalid operations that aren't simple field-validation errors — for example, asking
 * for more questions than the bank holds, or submitting a quiz twice. Mapped to HTTP 400.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
