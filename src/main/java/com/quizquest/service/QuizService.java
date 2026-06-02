package com.quizquest.service;

import com.quizquest.domain.Difficulty;
import com.quizquest.domain.Question;
import com.quizquest.domain.QuizItem;
import com.quizquest.domain.QuizMode;
import com.quizquest.domain.QuizSession;
import com.quizquest.domain.SessionStatus;
import com.quizquest.dto.AnswerInput;
import com.quizquest.dto.LeaderboardEntryDto;
import com.quizquest.dto.QuizQuestionDto;
import com.quizquest.dto.QuizSessionDto;
import com.quizquest.dto.ResultDto;
import com.quizquest.dto.ReviewItemDto;
import com.quizquest.dto.StartQuizRequest;
import com.quizquest.dto.SubmitRequest;
import com.quizquest.repository.QuestionRepository;
import com.quizquest.repository.QuizSessionRepository;
import com.quizquest.web.BadRequestException;
import com.quizquest.web.NotFoundException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Runs a quiz from start to finish.
 *
 * The server owns the clock: it records when a quiz started and how long it may run, decides on
 * submit whether the deadline was missed, and grades the answers. The browser only displays a
 * countdown — it can never change the real result.
 */
@Service
public class QuizService {

    /** Default seconds allowed per question when the caller doesn't specify a duration. */
    private static final int SECONDS_PER_QUESTION = 30;
    private static final int MIN_DURATION = 10;
    private static final int MAX_DURATION = 2 * 60 * 60;

    private final QuestionRepository questions;
    private final QuizSessionRepository sessions;
    private final Clock clock;

    public QuizService(QuestionRepository questions, QuizSessionRepository sessions, Clock clock) {
        this.questions = questions;
        this.sessions = sessions;
        this.clock = clock;
    }

    @Transactional
    public QuizSessionDto start(StartQuizRequest request) {
        String subject = blankToNull(request.subject());
        List<Question> pool = questions.findForQuiz(request.classLevel(), subject, request.difficulty());
        if (pool.isEmpty()) {
            throw new BadRequestException(
                    "No questions are available for that class / subject / difficulty yet.");
        }

        Collections.shuffle(pool);
        int count = Math.min(request.count(), pool.size());
        List<Question> chosen = pool.subList(0, count);

        int duration = resolveDuration(request.durationSeconds(), count);
        QuizSession session = new QuizSession(
                request.studentName().trim(), request.classLevel(), subject,
                request.difficulty(), request.mode(), duration);
        session.setTotalQuestions(count);

        int position = 0;
        for (Question q : chosen) {
            session.addItem(new QuizItem(q.getId(), position++));
        }
        sessions.save(session);

        List<QuizQuestionDto> questionDtos = new ArrayList<>();
        for (int i = 0; i < chosen.size(); i++) {
            questionDtos.add(QuizQuestionDto.from(chosen.get(i), i));
        }

        return new QuizSessionDto(
                session.getId(), session.getStudentName(), session.getClassLevel(),
                session.getSubject(), session.getDifficulty(), session.getMode(),
                session.getDurationSeconds(), session.getStartedAt(), session.getDeadline(),
                clock.instant(), count, questionDtos);
    }

    /** Re-fetch a running quiz (e.g. after a page refresh) so the countdown can resync. */
    @Transactional(readOnly = true)
    public QuizSessionDto live(Long sessionId) {
        QuizSession session = findSession(sessionId);
        if (session.getStatus() != SessionStatus.IN_PROGRESS) {
            throw new BadRequestException("This quiz has already been submitted.");
        }
        Map<Long, Question> byId = loadQuestions(session);
        List<QuizQuestionDto> questionDtos = new ArrayList<>();
        for (QuizItem item : session.getItems()) {
            Question q = byId.get(item.getQuestionId());
            if (q != null) {
                questionDtos.add(QuizQuestionDto.from(q, item.getPosition()));
            }
        }
        return new QuizSessionDto(
                session.getId(), session.getStudentName(), session.getClassLevel(),
                session.getSubject(), session.getDifficulty(), session.getMode(),
                session.getDurationSeconds(), session.getStartedAt(), session.getDeadline(),
                clock.instant(), session.getTotalQuestions(), questionDtos);
    }

    @Transactional
    public ResultDto submit(Long sessionId, SubmitRequest request) {
        QuizSession session = findSession(sessionId);

        // Submitting again just returns the same graded result (safe for retries / timeouts).
        if (session.getStatus() != SessionStatus.IN_PROGRESS) {
            return buildResult(session);
        }

        Instant now = clock.instant();
        boolean expired = session.isExpiredAt(now);

        Map<Long, Integer> chosen = new HashMap<>();
        for (AnswerInput answer : request.answers()) {
            chosen.put(answer.questionId(), answer.selectedIndex());
        }
        Map<Long, Question> byId = loadQuestions(session);

        int correctCount = 0;
        int score = 0;
        for (QuizItem item : session.getItems()) {
            Question question = byId.get(item.getQuestionId());
            Integer selected = normaliseSelection(chosen.get(item.getQuestionId()), question);
            boolean correct = question != null && question.isCorrect(selected);
            item.setSelectedIndex(selected);
            item.setCorrect(correct);
            if (correct) {
                correctCount++;
                score += question.getPoints();
            }
        }

        session.setCorrectCount(correctCount);
        session.setScore(score);
        session.setSubmittedAt(now);
        session.setStatus(expired ? SessionStatus.EXPIRED : SessionStatus.SUBMITTED);
        sessions.save(session);

        return buildResult(session, byId);
    }

    @Transactional(readOnly = true)
    public ResultDto result(Long sessionId) {
        QuizSession session = findSession(sessionId);
        if (session.getStatus() == SessionStatus.IN_PROGRESS) {
            throw new BadRequestException("This quiz hasn't been submitted yet.");
        }
        return buildResult(session);
    }

    @Transactional(readOnly = true)
    public List<LeaderboardEntryDto> leaderboard(Integer classLevel, String subject, int limit) {
        List<QuizSession> top = sessions.leaderboard(
                classLevel, blankToNull(subject), PageRequest.of(0, limit));
        List<LeaderboardEntryDto> entries = new ArrayList<>();
        int rank = 1;
        for (QuizSession s : top) {
            entries.add(new LeaderboardEntryDto(
                    rank++, s.getId(), s.getStudentName(), s.getClassLevel(), s.getSubject(),
                    s.getDifficulty(), s.getScore(), s.getCorrectCount(), s.getTotalQuestions(),
                    accuracy(s.getCorrectCount(), s.getTotalQuestions()),
                    elapsedSeconds(s), s.getSubmittedAt()));
        }
        return entries;
    }

    // --- internals ---

    private ResultDto buildResult(QuizSession session) {
        return buildResult(session, loadQuestions(session));
    }

    private ResultDto buildResult(QuizSession session, Map<Long, Question> byId) {
        List<ReviewItemDto> review = new ArrayList<>();
        int maxScore = 0;
        for (QuizItem item : session.getItems()) {
            Question q = byId.get(item.getQuestionId());
            if (q != null) {
                maxScore += q.getPoints();
                review.add(new ReviewItemDto(
                        item.getPosition(), q.getId(), q.getSubject(), q.getDifficulty(),
                        q.getText(), List.copyOf(q.getOptions()), q.getCorrectIndex(),
                        item.getSelectedIndex(), item.isCorrect(), q.getExplanation()));
            } else {
                review.add(new ReviewItemDto(
                        item.getPosition(), item.getQuestionId(), session.getSubject(), null,
                        "(this question was removed from the bank)", List.of(), -1,
                        item.getSelectedIndex(), item.isCorrect(), null));
            }
        }
        return new ResultDto(
                session.getId(), session.getStudentName(), session.getClassLevel(),
                session.getSubject(), session.getDifficulty(), session.getMode(),
                session.getStatus(), session.getTotalQuestions(), session.getCorrectCount(),
                session.getScore(), maxScore,
                accuracy(session.getCorrectCount(), session.getTotalQuestions()),
                session.getDurationSeconds(), elapsedSeconds(session),
                session.getStartedAt(), session.getSubmittedAt(), review);
    }

    private Map<Long, Question> loadQuestions(QuizSession session) {
        List<Long> ids = session.getItems().stream().map(QuizItem::getQuestionId).toList();
        return questions.findAllById(ids).stream()
                .collect(Collectors.toMap(Question::getId, Function.identity()));
    }

    private QuizSession findSession(Long id) {
        return sessions.findById(id)
                .orElseThrow(() -> new NotFoundException("Quiz session " + id + " not found"));
    }

    private int resolveDuration(Integer requested, int count) {
        if (requested != null) {
            return Math.max(MIN_DURATION, Math.min(MAX_DURATION, requested));
        }
        return Math.max(MIN_DURATION, count * SECONDS_PER_QUESTION);
    }

    /** Keep a chosen option only if it points at a real option on that question. */
    private Integer normaliseSelection(Integer selected, Question question) {
        if (selected == null || question == null) {
            return null;
        }
        return (selected >= 0 && selected < question.getOptions().size()) ? selected : null;
    }

    private int elapsedSeconds(QuizSession session) {
        Instant end = session.getSubmittedAt() != null ? session.getSubmittedAt() : clock.instant();
        return (int) Math.max(0, Duration.between(session.getStartedAt(), end).getSeconds());
    }

    private double accuracy(int correct, int total) {
        if (total <= 0) {
            return 0.0;
        }
        return Math.round((double) correct / total * 10000.0) / 10000.0;
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
