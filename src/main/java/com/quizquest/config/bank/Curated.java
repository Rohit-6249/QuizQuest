package com.quizquest.config.bank;

import com.quizquest.domain.Difficulty;
import java.util.List;

/**
 * A ready-made question (independent of class level). Pools return these; the generator stamps each
 * one with a class level when seeding.
 */
public record Curated(String text, List<String> options, int correct, Difficulty difficulty) {

    public static Curated of(String text, List<String> options, int correct, Difficulty d) {
        return new Curated(text, options, correct, d);
    }
}
