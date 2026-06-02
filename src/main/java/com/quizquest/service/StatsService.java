package com.quizquest.service;

import com.quizquest.domain.Difficulty;
import com.quizquest.domain.QuizSession;
import com.quizquest.domain.SessionStatus;
import com.quizquest.dto.MetaDto;
import com.quizquest.dto.StatsDto;
import com.quizquest.repository.QuestionRepository;
import com.quizquest.repository.QuizSessionRepository;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Read-only lookups for the UI: form metadata and the home-dashboard headline numbers.
 */
@Service
public class StatsService {

    private final QuestionRepository questions;
    private final QuizSessionRepository sessions;

    public StatsService(QuestionRepository questions, QuizSessionRepository sessions) {
        this.questions = questions;
        this.sessions = sessions;
    }

    @Transactional(readOnly = true)
    public MetaDto meta() {
        List<MetaDto.DifficultyInfo> difficulties = Arrays.stream(Difficulty.values())
                .map(d -> new MetaDto.DifficultyInfo(d.name(), d.getPoints()))
                .toList();
        return new MetaDto(
                questions.findDistinctClassLevels(),
                questions.findDistinctSubjects(),
                difficulties,
                questions.countByActiveTrue(),
                List.of(5, 10, 15, 20));
    }

    @Transactional(readOnly = true)
    public StatsDto stats() {
        long totalQuizzes = sessions.countByStatusNot(SessionStatus.IN_PROGRESS);
        List<StatsDto.RecentResult> recent = sessions
                .findTop10ByStatusNotOrderBySubmittedAtDesc(SessionStatus.IN_PROGRESS).stream()
                .map(s -> new StatsDto.RecentResult(
                        s.getId(), s.getStudentName(), s.getClassLevel(), s.getSubject(),
                        s.getScore(), s.getCorrectCount(), s.getTotalQuestions()))
                .toList();
        int topScore = recent.stream()
                .mapToInt(StatsDto.RecentResult::score)
                .max()
                .orElse(0);
        // The true top score may be older than the recent window, so check the leaderboard head too.
        topScore = Math.max(topScore, sessions
                .leaderboard(null, null, org.springframework.data.domain.PageRequest.of(0, 1))
                .stream().mapToInt(QuizSession::getScore).findFirst().orElse(0));

        return new StatsDto(questions.countByActiveTrue(), totalQuizzes, topScore, recent);
    }
}
