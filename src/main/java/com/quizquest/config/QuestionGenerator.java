package com.quizquest.config;

import com.quizquest.config.bank.Curated;
import com.quizquest.config.bank.EnglishPool;
import com.quizquest.config.bank.GkPool;
import com.quizquest.config.bank.MathGenerator;
import com.quizquest.config.bank.SciencePool;
import com.quizquest.domain.Question;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds the starter bank for a class.
 *
 * Mathematics is generated (answers computed in code, scaled by class), and Science, English and
 * General Knowledge come from verified pools — each with 20+ questions per difficulty, so any
 * (class, subject, difficulty) can fill a 20-question quiz without repeating.
 */
public class QuestionGenerator {

    private final MathGenerator math = new MathGenerator();

    public List<Question> generateForClass(int classLevel) {
        List<Question> out = new ArrayList<>();
        stamp(out, classLevel, "Mathematics", math.generate(classLevel));
        stamp(out, classLevel, "Science", SciencePool.all());
        stamp(out, classLevel, "English", EnglishPool.all());
        stamp(out, classLevel, "General Knowledge", GkPool.all());
        return out;
    }

    private void stamp(List<Question> out, int classLevel, String subject, List<Curated> items) {
        for (Curated c : items) {
            out.add(new Question(classLevel, subject, c.difficulty(), c.text(),
                    new ArrayList<>(c.options()), c.correct(), null));
        }
    }
}
