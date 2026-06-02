package com.quizquest.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * One question inside a {@link QuizSession}, plus the answer the student gave for it.
 *
 * It stores the question id (not a hard reference) and the position in the quiz, so the order is
 * stable. {@code selectedIndex} is null until the student answers.
 */
@Entity
@Table(name = "quiz_items")
public class QuizItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private QuizSession session;

    @Column(nullable = false)
    private Long questionId;

    @Column(nullable = false)
    private int position;

    private Integer selectedIndex;

    private boolean correct;

    protected QuizItem() {
        // for JPA
    }

    public QuizItem(Long questionId, int position) {
        this.questionId = questionId;
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public QuizSession getSession() {
        return session;
    }

    public void setSession(QuizSession session) {
        this.session = session;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public int getPosition() {
        return position;
    }

    public Integer getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(Integer selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
