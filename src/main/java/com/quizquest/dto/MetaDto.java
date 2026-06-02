package com.quizquest.dto;

import java.util.List;

/**
 * Lookup data the UI needs to build the "start a quiz" and admin forms: which classes and subjects
 * exist, the difficulty levels (with their point values) and a couple of handy totals.
 */
public record MetaDto(
        List<Integer> classLevels,
        List<String> subjects,
        List<DifficultyInfo> difficulties,
        long totalQuestions,
        List<Integer> suggestedCounts
) {
    public record DifficultyInfo(String name, int points) {
    }
}
