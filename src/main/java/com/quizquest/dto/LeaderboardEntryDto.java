package com.quizquest.dto;

import com.quizquest.domain.Difficulty;
import java.time.Instant;

/**
 * One row on the leaderboard.
 */
public record LeaderboardEntryDto(
        int rank,
        Long sessionId,
        String studentName,
        int classLevel,
        String subject,
        Difficulty difficulty,
        int score,
        int correctCount,
        int totalQuestions,
        double accuracy,
        int elapsedSeconds,
        Instant submittedAt
) {
}
