package com.quizquest.dto;

import com.quizquest.domain.Difficulty;
import com.quizquest.domain.QuizMode;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request to begin a quiz. {@code subject} and {@code difficulty} may be null for a "mixed" quiz.
 * {@code durationSeconds} may be null — the server then picks a sensible default for the question
 * count.
 */
public record StartQuizRequest(
        @NotBlank @Size(max = 60) String studentName,
        @Min(1) @Max(12) int classLevel,
        String subject,
        Difficulty difficulty,
        @Min(1) @Max(50) int count,
        @NotNull QuizMode mode,
        Integer durationSeconds
) {
}
