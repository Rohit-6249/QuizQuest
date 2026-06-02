package com.quizquest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;

/**
 * Pure unit tests for the time logic on a quiz session — no Spring, no database.
 */
class QuizSessionTest {

    private final Instant start = Instant.parse("2026-06-02T10:00:00Z");

    private QuizSession timedSession(int durationSeconds) {
        QuizSession session = new QuizSession(
                "Asha", 7, "Science", Difficulty.EASY, QuizMode.TIMED, durationSeconds);
        return session;
    }

    @Test
    void deadlineIsStartPlusDuration() {
        QuizSession session = timedSession(60);
        // startedAt is set at construction; deadline must be exactly duration later.
        assertThat(session.getDeadline())
                .isEqualTo(session.getStartedAt().plusSeconds(60));
    }

    @Test
    void timedSessionExpiresAfterDeadlinePlusGrace() {
        QuizSession session = timedSession(60);
        Instant justAfter = session.getDeadline().plusSeconds(2);   // within grace
        Instant wellAfter = session.getDeadline().plusSeconds(10);  // past grace

        assertThat(session.isExpiredAt(justAfter)).isFalse();
        assertThat(session.isExpiredAt(wellAfter)).isTrue();
    }

    @Test
    void practiceSessionNeverExpires() {
        QuizSession session = new QuizSession(
                "Asha", 7, "Science", Difficulty.EASY, QuizMode.PRACTICE, 30);
        assertThat(session.isExpiredAt(session.getStartedAt().plusSeconds(10_000))).isFalse();
    }

    @Test
    void elapsedSecondsNeverNegative() {
        QuizSession session = timedSession(60);
        assertThat(session.getElapsedSeconds(session.getStartedAt().minusSeconds(5))).isZero();
        assertThat(session.getElapsedSeconds(session.getStartedAt().plusSeconds(42))).isEqualTo(42);
    }
}
