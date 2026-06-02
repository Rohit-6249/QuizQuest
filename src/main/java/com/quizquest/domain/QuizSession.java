package com.quizquest.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * One quiz attempt by one student.
 *
 * The session remembers which questions were picked (as {@link QuizItem}s), when it started and
 * how long it is allowed to run. The deadline and grading are decided on the server so the timer
 * cannot be cheated from the browser.
 */
@Entity
@Table(name = "quiz_sessions")
public class QuizSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String studentName;

    @Column(nullable = false)
    private int classLevel;

    /** Null means "mixed subjects". */
    @Column(length = 60)
    private String subject;

    /** Null means "mixed difficulty". */
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private QuizMode mode;

    @Column(nullable = false)
    private int durationSeconds;

    @Column(nullable = false, updatable = false)
    private Instant startedAt = Instant.now();

    private Instant submittedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private SessionStatus status = SessionStatus.IN_PROGRESS;

    private int totalQuestions;
    private int correctCount;
    private int score;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<QuizItem> items = new ArrayList<>();

    protected QuizSession() {
        // for JPA
    }

    public QuizSession(String studentName, int classLevel, String subject, Difficulty difficulty,
                       QuizMode mode, int durationSeconds) {
        this.studentName = studentName;
        this.classLevel = classLevel;
        this.subject = subject;
        this.difficulty = difficulty;
        this.mode = mode;
        this.durationSeconds = durationSeconds;
    }

    public void addItem(QuizItem item) {
        item.setSession(this);
        this.items.add(item);
    }

    /** The instant after which a TIMED quiz no longer accepts answers. */
    public Instant getDeadline() {
        return startedAt.plusSeconds(durationSeconds);
    }

    /**
     * Whether the deadline has passed (with a few seconds of grace for network lag).
     * Practice mode never expires.
     */
    public boolean isExpiredAt(Instant now) {
        if (mode == QuizMode.PRACTICE) {
            return false;
        }
        return now.isAfter(getDeadline().plusSeconds(3));
    }

    public int getElapsedSeconds(Instant now) {
        long seconds = Duration.between(startedAt, now).getSeconds();
        return (int) Math.max(0, seconds);
    }

    // --- getters & setters ---

    public Long getId() {
        return id;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getClassLevel() {
        return classLevel;
    }

    public String getSubject() {
        return subject;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public QuizMode getMode() {
        return mode;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<QuizItem> getItems() {
        return items;
    }
}
