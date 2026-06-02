package com.quizquest.dto;

import java.util.List;

/**
 * Headline numbers for the home dashboard.
 */
public record StatsDto(
        long totalQuestions,
        long totalQuizzes,
        int topScore,
        List<RecentResult> recent
) {
    public record RecentResult(
            Long sessionId,
            String studentName,
            int classLevel,
            String subject,
            int score,
            int correctCount,
            int totalQuestions
    ) {
    }
}
