package com.quizquest.domain;

/**
 * TIMED quizzes count down and lock when the clock runs out.
 * PRACTICE quizzes have no time pressure (the timer just measures how long you took).
 */
public enum QuizMode {
    TIMED,
    PRACTICE
}
