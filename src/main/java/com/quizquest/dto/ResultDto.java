package com.quizquest.dto;

import com.quizquest.domain.Difficulty;
import com.quizquest.domain.QuizMode;
import com.quizquest.domain.SessionStatus;
import java.time.Instant;
import java.util.List;

/**
 * The graded outcome of a quiz, including a per-question review.
 */
public record ResultDto(
        Long sessionId,
        String studentName,
        int classLevel,
        String subject,
        Difficulty difficulty,
        QuizMode mode,
        SessionStatus status,
        int totalQuestions,
        int correctCount,
        int score,
        int maxScore,
        double accuracy,
        int durationSeconds,
        int elapsedSeconds,
        Instant startedAt,
        Instant submittedAt,
        List<ReviewItemDto> review
) {
}
