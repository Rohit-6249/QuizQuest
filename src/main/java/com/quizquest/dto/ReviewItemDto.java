package com.quizquest.dto;

import com.quizquest.domain.Difficulty;
import java.util.List;

/**
 * One line in the post-quiz review: the question, what the student picked, the right answer and why.
 */
public record ReviewItemDto(
        int position,
        Long questionId,
        String subject,
        Difficulty difficulty,
        String text,
        List<String> options,
        int correctIndex,
        Integer selectedIndex,
        boolean correct,
        String explanation
) {
}
