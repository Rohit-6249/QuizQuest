package com.quizquest.dto;

import com.quizquest.domain.Difficulty;
import com.quizquest.domain.Question;
import java.util.List;

/**
 * A question as the student sees it during a quiz: the correct answer is deliberately left out.
 */
public record QuizQuestionDto(
        int position,
        Long questionId,
        String subject,
        Difficulty difficulty,
        int points,
        String text,
        List<String> options
) {
    public static QuizQuestionDto from(Question q, int position) {
        return new QuizQuestionDto(
                position, q.getId(), q.getSubject(), q.getDifficulty(), q.getPoints(),
                q.getText(), List.copyOf(q.getOptions()));
    }
}
