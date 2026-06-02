package com.quizquest.dto;

import com.quizquest.domain.Difficulty;
import com.quizquest.domain.QuizMode;
import java.time.Instant;
import java.util.List;

/**
 * The live quiz handed back when a session starts. It carries the server clock and the deadline so
 * the browser can show an accurate countdown that agrees with the server.
 */
public record QuizSessionDto(
        Long sessionId,
        String studentName,
        int classLevel,
        String subject,
        Difficulty difficulty,
        QuizMode mode,
        int durationSeconds,
        Instant startedAt,
        Instant deadline,
        Instant serverNow,
        int totalQuestions,
        List<QuizQuestionDto> questions
) {
}
