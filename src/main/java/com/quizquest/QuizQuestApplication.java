package com.quizquest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the QuizQuest application.
 *
 * QuizQuest is a small quiz platform for students: questions are organised by class level,
 * subject and difficulty, and a quiz run is timed accurately on the server.
 */
@SpringBootApplication
public class QuizQuestApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuizQuestApplication.class, args);
    }
}
