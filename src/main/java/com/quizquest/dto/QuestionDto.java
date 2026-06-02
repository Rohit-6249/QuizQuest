package com.quizquest.dto;

import com.quizquest.domain.Difficulty;
import com.quizquest.domain.Question;
import java.util.List;

/**
 * Full view of a question, including the correct answer. Used by the admin screen and the
 * post-quiz review — never sent while a quiz is still being taken.
 */
public record QuestionDto(
        Long id,
        int classLevel,
        String subject,
        Difficulty difficulty,
        String text,
        List<String> options,
        int correctIndex,
        String explanation,
        int points,
        boolean active
) {
    public static QuestionDto from(Question q) {
        return new QuestionDto(
                q.getId(), q.getClassLevel(), q.getSubject(), q.getDifficulty(), q.getText(),
                List.copyOf(q.getOptions()), q.getCorrectIndex(), q.getExplanation(),
                q.getPoints(), q.isActive());
    }
}
