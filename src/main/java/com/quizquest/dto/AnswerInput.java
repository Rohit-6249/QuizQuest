package com.quizquest.dto;

import jakarta.validation.constraints.NotNull;

/**
 * One answer in a submission. {@code selectedIndex} may be null if the student left it blank.
 */
public record AnswerInput(
        @NotNull Long questionId,
        Integer selectedIndex
) {
}
