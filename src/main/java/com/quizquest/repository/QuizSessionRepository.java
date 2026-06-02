package com.quizquest.repository;

import com.quizquest.domain.QuizSession;
import com.quizquest.domain.SessionStatus;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuizSessionRepository extends JpaRepository<QuizSession, Long> {

    /**
     * Leaderboard: finished attempts ordered by score (then by speed). Class level and subject are
     * optional filters.
     */
    @Query("""
            select s from QuizSession s
            where s.status <> com.quizquest.domain.SessionStatus.IN_PROGRESS
              and (:classLevel is null or s.classLevel = :classLevel)
              and (:subject is null or s.subject = :subject)
            order by s.score desc, s.correctCount desc, s.submittedAt asc
            """)
    List<QuizSession> leaderboard(@Param("classLevel") Integer classLevel,
                                  @Param("subject") String subject,
                                  Pageable pageable);

    List<QuizSession> findTop10ByStatusNotOrderBySubmittedAtDesc(SessionStatus status);

    long countByStatusNot(SessionStatus status);
}
