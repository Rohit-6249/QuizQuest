package com.quizquest.dto;

import com.quizquest.domain.Difficulty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * Payload for creating or updating a question (used by the Question Bank / admin screen).
 */
public record QuestionInput(
        @Min(1) @Max(12) int classLevel,
        @NotBlank @Size(max = 60) String subject,
        @NotNull Difficulty difficulty,
        @NotBlank @Size(max = 500) String text,
        @NotNull @Size(min = 2, max = 6) List<@NotBlank String> options,
        @Min(0) int correctIndex,
        @Size(max = 600) String explanation
) {
}
