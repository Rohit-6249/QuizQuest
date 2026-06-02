package com.quizquest.dto;

import jakarta.validation.Valid;
import java.util.List;

/**
 * The full set of answers a student submits for a quiz. Missing questions are treated as blank.
 */
public record SubmitRequest(
        @Valid List<AnswerInput> answers
) {
    public List<AnswerInput> answers() {
        return answers == null ? List.of() : answers;
    }
}
