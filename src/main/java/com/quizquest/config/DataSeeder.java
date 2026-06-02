package com.quizquest.config;

import com.quizquest.domain.Question;
import com.quizquest.repository.QuestionRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds the question bank on startup.
 *
 * The questions come from {@link QuestionGenerator} (≈360 per class across classes 6–10). The seeder
 * is self-updating: if the generated bank size differs from what's already stored — e.g. after the
 * bank is expanded in a new build — it replaces the old questions so you always get the latest bank
 * without having to delete the database by hand. (Quiz history is untouched.)
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
        List<Question> bank = new ArrayList<>();
        for (int classLevel = 6; classLevel <= 10; classLevel++) {
            bank.addAll(generator.generateForClass(classLevel));
        }
        long existing = questions.count();
        if (existing == bank.size()) {
            return; // bank already up to date
        }
        if (existing > 0) {
            questions.deleteAll(); // clear the stale bank (also clears each question's options)
        }
        questions.saveAll(bank);
    }
}
