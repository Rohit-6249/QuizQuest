package com.quizquest.config;

import com.quizquest.domain.Question;
import com.quizquest.repository.QuestionRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds the question bank the first time the app runs (when the table is empty).
 *
 * The actual questions come from {@link QuestionGenerator}, which produces 200 questions per class
 * across classes 6–10 and all four subjects.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final QuestionRepository questions;
    private final QuestionGenerator generator = new QuestionGenerator();

    public DataSeeder(QuestionRepository questions) {
        this.questions = questions;
    }

    @Override
    public void run(String... args) {
        if (questions.count() > 0) {
            return; // already seeded
        }
        List<Question> bank = new ArrayList<>();
        for (int classLevel = 6; classLevel <= 10; classLevel++) {
            bank.addAll(generator.generateForClass(classLevel));
        }
        questions.saveAll(bank);
    }
}
