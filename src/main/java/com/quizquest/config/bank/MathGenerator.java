package com.quizquest.config.bank;

import static com.quizquest.domain.Difficulty.EASY;
import static com.quizquest.domain.Difficulty.HARD;
import static com.quizquest.domain.Difficulty.MEDIUM;

import com.quizquest.domain.Difficulty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

/**
 * Generates Mathematics questions whose answers are computed in code, so they are always correct.
 *
 * Each difficulty mixes several different question shapes (so a single-difficulty quiz is varied,
 * not the same template repeated) and the numbers scale with the class level. There are ~32 distinct
 * questions per difficulty, comfortably more than a 20-question quiz needs, so quizzes never repeat
 * and vary between attempts.
 */
public class MathGenerator {

    /** Pythagorean triples (a² + b² = c²), scaled per class for variety. */
    private static final int[][] TRIPLES = {
            {3, 4, 5}, {6, 8, 10}, {5, 12, 13}, {8, 15, 17},
            {9, 12, 15}, {7, 24, 25}, {20, 21, 29}, {12, 16, 20}
    };
    private static final long[][] POWERS = {
            {2, 5}, {2, 6}, {2, 7}, {3, 3}, {3, 4}, {3, 5}, {5, 3}, {6, 2}
    };
    private static final long[] PERFECT_SQUARES = {144, 169, 196, 225, 256, 289, 324, 400};
    private static final int[] PERCENTS = {10, 20, 25, 50, 5, 15, 40, 75};

    public List<Curated> generate(int cls) {
        Random rnd = new Random(cls * 7919L + 13);
        List<Curated> out = new ArrayList<>();
        out.addAll(easy(cls, rnd));
        out.addAll(medium(cls, rnd));
        out.addAll(hard(cls, rnd));
        return out;
    }

    private List<Curated> easy(int cls, Random rnd) {
        List<Curated> q = new ArrayList<>();
        for (int k = 1; k <= 8; k++) {
            long a = cls * 8L + k * 9 + 13, b = cls * 3L + k * 4 + 7;
            q.add(numeric(EASY, "What is " + a + " + " + b + "?", a + b, rnd));
        }
        for (int k = 1; k <= 8; k++) {
            long a = cls * 14L + k * 11 + 60, b = cls * 2L + k * 5 + 3;
            q.add(numeric(EASY, "What is " + a + " − " + b + "?", a - b, rnd));
        }
        for (int k = 1; k <= 8; k++) {
            long a = 6 + k, b = cls + k * 2L;
            q.add(numeric(EASY, "What is " + a + " × " + b + "?", a * b, rnd));
        }
        for (int k = 1; k <= 8; k++) {
            long divisor = 3 + k, quotient = 4 + k + cls / 3L, dividend = divisor * quotient;
            q.add(numeric(EASY, "What is " + dividend + " ÷ " + divisor + "?", quotient, rnd));
        }
        return q;
    }

    private List<Curated> medium(int cls, Random rnd) {
        List<Curated> q = new ArrayList<>();
        for (int k = 0; k < 8; k++) {
            long p = PERCENTS[k], n = (cls + k + 1) * 40L;
            q.add(numeric(MEDIUM, "What is " + p + "% of " + n + "?", p * n / 100, rnd));
        }
        for (int k = 1; k <= 8; k++) {
            long l = 5 + k + cls, w = 3 + k;
            q.add(numeric(MEDIUM, "What is the area of a " + l + " × " + w
                    + " rectangle (square units)?", l * w, rnd));
        }
        for (int k = 1; k <= 8; k++) {
            long base = cls * 3L + k * 5;
            q.add(numeric(MEDIUM, "What is the average of " + base + ", " + (base + 3)
                    + " and " + (base + 6) + "?", base + 3, rnd));
        }
        for (int k = 1; k <= 8; k++) {
            long x = 10 + cls + k;
            q.add(numeric(MEDIUM, "What is the square of " + x + "?", x * x, rnd));
        }
        return q;
    }

    private List<Curated> hard(int cls, Random rnd) {
        List<Curated> q = new ArrayList<>();
        for (int k = 1; k <= 8; k++) {
            long coef = 2 + (k % 4), x = 3 + k + cls / 2L, cst = 1 + k, c = coef * x + cst;
            q.add(numeric(HARD, "If " + coef + "x + " + cst + " = " + c + ", what is x?", x, rnd));
        }
        int factor = cls - 5; // 1..5, scales the triangle with the class level
        for (int[] t : TRIPLES) {
            long a = (long) t[0] * factor, b = (long) t[1] * factor, h = (long) t[2] * factor;
            q.add(numeric(HARD, "A right-angled triangle has legs " + a + " and " + b
                    + ". What is the length of the hypotenuse?", h, rnd));
        }
        for (long[] pw : POWERS) {
            long value = (long) Math.pow(pw[0], pw[1]);
            q.add(numeric(HARD, "What is " + pw[0] + " raised to the power " + pw[1]
                    + " (" + pw[0] + "^" + pw[1] + ")?", value, rnd));
        }
        for (long sq : PERFECT_SQUARES) {
            q.add(numeric(HARD, "What is the square root of " + sq + "?",
                    (long) Math.sqrt(sq), rnd));
        }
        return q;
    }

    /** Build a numeric multiple-choice question: the right answer plus three near-miss distractors. */
    private Curated numeric(Difficulty d, String text, long answer, Random rnd) {
        LinkedHashSet<Long> values = new LinkedHashSet<>();
        values.add(answer);
        long[] deltas = {1, -1, 2, -2, 3, -3, 5, 10, -5, 4};
        for (long delta : deltas) {
            if (values.size() >= 4) {
                break;
            }
            long candidate = answer + delta;
            if (candidate >= 0) {
                values.add(candidate);
            }
        }
        long extra = answer + 6;
        while (values.size() < 4) {
            values.add(extra++);
        }
        List<Long> shuffled = new ArrayList<>(values);
        Collections.shuffle(shuffled, rnd);
        List<String> options = new ArrayList<>();
        int correct = 0;
        for (int i = 0; i < shuffled.size(); i++) {
            options.add(Long.toString(shuffled.get(i)));
            if (shuffled.get(i) == answer) {
                correct = i;
            }
        }
        return Curated.of(text, options, correct, d);
    }
}
