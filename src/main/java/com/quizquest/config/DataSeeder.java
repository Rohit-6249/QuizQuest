package com.quizquest.config;

import static com.quizquest.domain.Difficulty.EASY;
import static com.quizquest.domain.Difficulty.HARD;
import static com.quizquest.domain.Difficulty.MEDIUM;

import com.quizquest.domain.Difficulty;
import com.quizquest.domain.Question;
import com.quizquest.repository.QuestionRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds a starter question bank the first time the app runs (when the table is empty).
 *
 * Questions are spread across classes 6–10, four subjects (Mathematics, Science, English, General
 * Knowledge) and all three difficulty levels, so every combination can power a quiz out of the box.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final QuestionRepository questions;

    public DataSeeder(QuestionRepository questions) {
        this.questions = questions;
    }

    @Override
    public void run(String... args) {
        if (questions.count() > 0) {
            return; // already seeded
        }
        List<Question> bank = new ArrayList<>();
        seedClass6(bank);
        seedClass7(bank);
        seedClass8(bank);
        seedClass9(bank);
        seedClass10(bank);
        questions.saveAll(bank);
    }

    private Question q(int cls, String subject, Difficulty d, String text,
                       List<String> options, int correct, String explanation) {
        return new Question(cls, subject, d, text, options, correct, explanation);
    }

    private void seedClass6(List<Question> b) {
        b.add(q(6, "Mathematics", EASY, "What is 7 × 8?",
                List.of("54", "56", "64", "49"), 1, "7 × 8 = 56."));
        b.add(q(6, "Mathematics", EASY, "Which of these is an even number?",
                List.of("17", "23", "40", "9"), 2, "40 is divisible by 2, so it is even."));
        b.add(q(6, "Mathematics", MEDIUM, "What is the smallest prime number?",
                List.of("0", "1", "2", "3"), 2, "2 is the smallest (and only even) prime number."));
        b.add(q(6, "Mathematics", MEDIUM, "The perimeter of a square with side 5 cm is:",
                List.of("10 cm", "20 cm", "25 cm", "15 cm"), 1, "Perimeter = 4 × side = 4 × 5 = 20 cm."));
        b.add(q(6, "Mathematics", HARD, "What is the value of 3² + 4²?",
                List.of("25", "12", "49", "14"), 0, "9 + 16 = 25."));

        b.add(q(6, "Science", EASY, "Which gas do plants take in for photosynthesis?",
                List.of("Oxygen", "Carbon dioxide", "Nitrogen", "Hydrogen"), 1,
                "Plants absorb carbon dioxide and release oxygen during photosynthesis."));
        b.add(q(6, "Science", EASY, "How many legs does an insect have?",
                List.of("Four", "Six", "Eight", "Ten"), 1, "All insects have six legs."));
        b.add(q(6, "Science", MEDIUM, "Which part of the plant makes food?",
                List.of("Root", "Stem", "Leaf", "Flower"), 2, "Leaves contain chlorophyll and make food."));
        b.add(q(6, "Science", HARD, "Which of these is NOT a state of matter?",
                List.of("Solid", "Liquid", "Energy", "Gas"), 2, "Energy is not a state of matter."));

        b.add(q(6, "English", EASY, "Choose the correct plural of 'child'.",
                List.of("childs", "children", "childes", "child"), 1, "'Child' becomes 'children'."));
        b.add(q(6, "English", MEDIUM, "Pick the noun in: 'The dog ran quickly.'",
                List.of("ran", "quickly", "dog", "the"), 2, "'Dog' is a naming word — a noun."));
        b.add(q(6, "English", HARD, "Which word is an antonym of 'ancient'?",
                List.of("old", "modern", "historic", "antique"), 1, "'Modern' is the opposite of 'ancient'."));

        b.add(q(6, "General Knowledge", EASY, "How many days are there in a week?",
                List.of("5", "6", "7", "8"), 2, "There are seven days in a week."));
        b.add(q(6, "General Knowledge", MEDIUM, "Which is the largest planet in our solar system?",
                List.of("Earth", "Saturn", "Jupiter", "Mars"), 2, "Jupiter is the largest planet."));
        b.add(q(6, "General Knowledge", HARD, "Who is known as the 'Father of the Nation' in India?",
                List.of("Jawaharlal Nehru", "Mahatma Gandhi", "Sardar Patel", "B. R. Ambedkar"), 1,
                "Mahatma Gandhi is called the Father of the Nation in India."));
    }

    private void seedClass7(List<Question> b) {
        b.add(q(7, "Mathematics", EASY, "What is 15% of 200?",
                List.of("15", "20", "30", "40"), 2, "15% of 200 = 0.15 × 200 = 30."));
        b.add(q(7, "Mathematics", MEDIUM, "Solve: -5 + 8 = ?",
                List.of("-3", "3", "13", "-13"), 1, "-5 + 8 = 3."));
        b.add(q(7, "Mathematics", MEDIUM, "The angles of a triangle add up to:",
                List.of("90°", "180°", "270°", "360°"), 1, "The interior angles of a triangle sum to 180°."));
        b.add(q(7, "Mathematics", HARD, "If 3x = 21, what is x?",
                List.of("6", "7", "8", "9"), 1, "x = 21 ÷ 3 = 7."));

        b.add(q(7, "Science", EASY, "What is the boiling point of water at sea level?",
                List.of("50°C", "90°C", "100°C", "120°C"), 2, "Water boils at 100°C at sea level."));
        b.add(q(7, "Science", MEDIUM, "Which organ pumps blood through the body?",
                List.of("Lungs", "Heart", "Liver", "Kidney"), 1, "The heart pumps blood around the body."));
        b.add(q(7, "Science", HARD, "Acids turn blue litmus paper which colour?",
                List.of("Green", "Red", "Blue", "Yellow"), 1, "Acids turn blue litmus paper red."));

        b.add(q(7, "English", EASY, "Which is a verb? 'She sings beautifully.'",
                List.of("She", "sings", "beautifully", "none"), 1, "'Sings' is the action word — a verb."));
        b.add(q(7, "English", MEDIUM, "Choose the correctly spelled word.",
                List.of("recieve", "receive", "receeve", "receve"), 1, "The rule is 'i before e except after c': receive."));
        b.add(q(7, "English", HARD, "What is the past tense of 'go'?",
                List.of("goed", "gone", "went", "going"), 2, "The simple past tense of 'go' is 'went'."));

        b.add(q(7, "General Knowledge", EASY, "Which animal is known as the 'Ship of the Desert'?",
                List.of("Horse", "Camel", "Elephant", "Donkey"), 1, "The camel is called the Ship of the Desert."));
        b.add(q(7, "General Knowledge", MEDIUM, "How many continents are there on Earth?",
                List.of("5", "6", "7", "8"), 2, "There are seven continents."));
        b.add(q(7, "General Knowledge", HARD, "The Great Wall is located in which country?",
                List.of("Japan", "India", "China", "Mongolia"), 2, "The Great Wall is in China."));
    }

    private void seedClass8(List<Question> b) {
        b.add(q(8, "Mathematics", EASY, "What is the square of 9?",
                List.of("18", "72", "81", "99"), 2, "9 × 9 = 81."));
        b.add(q(8, "Mathematics", MEDIUM, "What is the value of π (pi) to two decimal places?",
                List.of("3.41", "3.14", "3.12", "3.16"), 1, "π is approximately 3.14."));
        b.add(q(8, "Mathematics", HARD, "The cube root of 64 is:",
                List.of("4", "6", "8", "16"), 0, "4 × 4 × 4 = 64, so the cube root is 4."));
        b.add(q(8, "Mathematics", HARD, "Simplify: (2/3) ÷ (4/9).",
                List.of("3/2", "8/27", "1/2", "2/9"), 0, "(2/3) × (9/4) = 18/12 = 3/2."));

        b.add(q(8, "Science", EASY, "What force pulls objects towards the Earth?",
                List.of("Friction", "Gravity", "Magnetism", "Tension"), 1, "Gravity pulls objects towards Earth."));
        b.add(q(8, "Science", MEDIUM, "Which gas makes up most of Earth's atmosphere?",
                List.of("Oxygen", "Carbon dioxide", "Nitrogen", "Argon"), 2, "About 78% of the air is nitrogen."));
        b.add(q(8, "Science", HARD, "What is the chemical symbol for Sodium?",
                List.of("So", "Sd", "Na", "Sn"), 2, "Sodium's symbol is Na (from Latin 'natrium')."));

        b.add(q(8, "English", EASY, "An 'adjective' describes a:",
                List.of("verb", "noun", "adverb", "sentence"), 1, "Adjectives describe nouns."));
        b.add(q(8, "English", MEDIUM, "Identify the conjunction: 'I stayed home because it rained.'",
                List.of("stayed", "home", "because", "rained"), 2, "'Because' joins the two clauses — a conjunction."));
        b.add(q(8, "English", HARD, "Choose the correct sentence.",
                List.of("She don't like tea.", "She doesn't likes tea.", "She doesn't like tea.", "She not like tea."), 2,
                "'She doesn't like tea.' is grammatically correct."));

        b.add(q(8, "General Knowledge", EASY, "What is the currency of Japan?",
                List.of("Yuan", "Won", "Yen", "Ringgit"), 2, "Japan's currency is the Yen."));
        b.add(q(8, "General Knowledge", MEDIUM, "Who invented the telephone?",
                List.of("Thomas Edison", "Alexander Graham Bell", "Nikola Tesla", "Guglielmo Marconi"), 1,
                "Alexander Graham Bell is credited with inventing the telephone."));
        b.add(q(8, "General Knowledge", HARD, "Which is the longest river in the world?",
                List.of("Amazon", "Nile", "Ganga", "Yangtze"), 1, "The Nile is generally considered the longest river."));
    }

    private void seedClass9(List<Question> b) {
        b.add(q(9, "Mathematics", EASY, "What is the value of 5 factorial (5!)?",
                List.of("25", "60", "120", "150"), 2, "5! = 5 × 4 × 3 × 2 × 1 = 120."));
        b.add(q(9, "Mathematics", MEDIUM, "In a right triangle, the longest side is called the:",
                List.of("base", "altitude", "hypotenuse", "median"), 2, "The side opposite the right angle is the hypotenuse."));
        b.add(q(9, "Mathematics", HARD, "Solve for x: 2x + 3 = 11.",
                List.of("3", "4", "5", "7"), 1, "2x = 8, so x = 4."));

        b.add(q(9, "Science", EASY, "What is the powerhouse of the cell?",
                List.of("Nucleus", "Ribosome", "Mitochondria", "Vacuole"), 2, "Mitochondria produce the cell's energy."));
        b.add(q(9, "Science", MEDIUM, "Newton's first law is also known as the law of:",
                List.of("Acceleration", "Inertia", "Gravitation", "Momentum"), 1, "The first law describes inertia."));
        b.add(q(9, "Science", HARD, "The speed of light in vacuum is approximately:",
                List.of("3 × 10⁶ m/s", "3 × 10⁸ m/s", "3 × 10¹⁰ m/s", "3 × 10⁵ m/s"), 1,
                "Light travels at about 3 × 10⁸ metres per second."));

        b.add(q(9, "English", EASY, "A group of words with a subject and verb is a:",
                List.of("phrase", "clause", "prefix", "syllable"), 1, "A clause has both a subject and a verb."));
        b.add(q(9, "English", MEDIUM, "Which figure of speech is 'as brave as a lion'?",
                List.of("Metaphor", "Simile", "Personification", "Hyperbole"), 1, "A simile compares using 'as' or 'like'."));
        b.add(q(9, "English", HARD, "Choose the correct passive voice: 'The cat chased the mouse.'",
                List.of("The mouse chased the cat.", "The mouse was chased by the cat.",
                        "The mouse is chasing the cat.", "The cat was chased by the mouse."), 1,
                "Passive: 'The mouse was chased by the cat.'"));

        b.add(q(9, "General Knowledge", EASY, "How many players are there in a cricket team?",
                List.of("9", "10", "11", "12"), 2, "A cricket team has eleven players."));
        b.add(q(9, "General Knowledge", MEDIUM, "Which planet is known as the 'Red Planet'?",
                List.of("Venus", "Mars", "Mercury", "Neptune"), 1, "Mars looks red due to iron oxide and is the Red Planet."));
        b.add(q(9, "General Knowledge", HARD, "In which year did India gain independence?",
                List.of("1942", "1945", "1947", "1950"), 2, "India became independent on 15 August 1947."));
    }

    private void seedClass10(List<Question> b) {
        b.add(q(10, "Mathematics", EASY, "What is the slope of the line y = 3x + 2?",
                List.of("2", "3", "5", "1"), 1, "In y = mx + c, the slope m is 3."));
        b.add(q(10, "Mathematics", MEDIUM, "The roots of x² - 5x + 6 = 0 are:",
                List.of("2 and 3", "1 and 6", "-2 and -3", "0 and 5"), 0, "x² - 5x + 6 = (x-2)(x-3), so roots are 2 and 3."));
        b.add(q(10, "Mathematics", HARD, "What is sin 30°?",
                List.of("0", "1/2", "√3/2", "1"), 1, "sin 30° = 1/2."));

        b.add(q(10, "Science", EASY, "What is the chemical formula of water?",
                List.of("CO₂", "H₂O", "O₂", "NaCl"), 1, "Water is H₂O — two hydrogen atoms and one oxygen."));
        b.add(q(10, "Science", MEDIUM, "Ohm's law relates voltage, current and:",
                List.of("Power", "Resistance", "Energy", "Charge"), 1, "Ohm's law: V = I × R, where R is resistance."));
        b.add(q(10, "Science", HARD, "Which part of the human eye controls the amount of light entering it?",
                List.of("Cornea", "Retina", "Iris", "Lens"), 2, "The iris adjusts the pupil to control light."));

        b.add(q(10, "English", EASY, "A word that replaces a noun is a:",
                List.of("verb", "pronoun", "adverb", "article"), 1, "Pronouns (he, she, it) replace nouns."));
        b.add(q(10, "English", MEDIUM, "'Break the ice' is an example of a(n):",
                List.of("idiom", "simile", "metaphor", "proverb"), 0, "'Break the ice' is an idiom meaning to ease tension."));
        b.add(q(10, "English", HARD, "Choose the correct reported speech: He said, \"I am tired.\"",
                List.of("He said he is tired.", "He said he was tired.",
                        "He says he was tired.", "He said I was tired."), 1,
                "Reported speech shifts 'am' to 'was': He said he was tired."));

        b.add(q(10, "General Knowledge", EASY, "Which is the smallest continent by area?",
                List.of("Europe", "Antarctica", "Australia", "Asia"), 2, "Australia is the smallest continent."));
        b.add(q(10, "General Knowledge", MEDIUM, "Who wrote the play 'Romeo and Juliet'?",
                List.of("Charles Dickens", "William Shakespeare", "Mark Twain", "Leo Tolstoy"), 1,
                "Shakespeare wrote 'Romeo and Juliet'."));
        b.add(q(10, "General Knowledge", HARD, "The headquarters of the United Nations is in:",
                List.of("Geneva", "Paris", "New York", "London"), 2, "The UN headquarters is in New York City."));
    }
}
