package com.quizquest.web;

/**
 * Thrown when a requested entity (question, quiz session, …) does not exist. Mapped to HTTP 404.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
