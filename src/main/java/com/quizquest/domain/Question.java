package com.quizquest.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * A single multiple-choice question in the bank.
 *
 * A question belongs to one class level (e.g. 6), one subject (e.g. "Science") and one
 * difficulty. It has a list of options and the index of the correct one.
 */
@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int classLevel;

    @Column(nullable = false, length = 60)
    private String subject;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Difficulty difficulty;

    @Column(nullable = false, length = 500)
    private String text;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @OrderColumn(name = "option_index")
    @Column(name = "option_text", length = 300)
    private List<String> options = new ArrayList<>();

    /** Zero-based index into {@link #options} that is the correct answer. */
    @Column(nullable = false)
    private int correctIndex;

    @Column(length = 600)
    private String explanation;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected Question() {
        // for JPA
    }

    public Question(int classLevel, String subject, Difficulty difficulty, String text,
                    List<String> options, int correctIndex, String explanation) {
        this.classLevel = classLevel;
        this.subject = subject;
        this.difficulty = difficulty;
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
        this.explanation = explanation;
    }

    /** How many points this question is worth, based on its difficulty. */
    public int getPoints() {
        return difficulty.getPoints();
    }

    public boolean isCorrect(Integer selectedIndex) {
        return selectedIndex != null && selectedIndex == correctIndex;
    }

    // --- getters & setters ---

    public Long getId() {
        return id;
    }

    public int getClassLevel() {
        return classLevel;
    }

    public void setClassLevel(int classLevel) {
        this.classLevel = classLevel;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public void setCorrectIndex(int correctIndex) {
        this.correctIndex = correctIndex;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
