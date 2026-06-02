package com.quizquest.domain;

/**
 * How hard a question is. Each level is worth a different number of points,
 * so a harder quiz rewards more.
 */
public enum Difficulty {
    EASY(1),
    MEDIUM(2),
    HARD(3);

    private final int points;

    Difficulty(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
