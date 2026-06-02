package com.quizquest.config;

import static com.quizquest.domain.Difficulty.EASY;
import static com.quizquest.domain.Difficulty.HARD;
import static com.quizquest.domain.Difficulty.MEDIUM;

import com.quizquest.domain.Difficulty;
import com.quizquest.domain.Question;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

/**
 * Builds a large starter bank: 200 questions for each class.
 *
 * Mathematics questions are generated and their answers are computed in code, so they are correct
 * by construction and naturally scale with the class level. Science, English and General Knowledge
 * questions are drawn from hand-checked pools. The seed is fixed per class so the bank is stable
 * across restarts.
 */
public class QuestionGenerator {

    private static final String MATH = "Mathematics";
    private static final String SCIENCE = "Science";
    private static final String ENGLISH = "English";
    private static final String GK = "General Knowledge";

    private static final int MATH_PER_CLASS = 120;
    private static final int SCIENCE_PER_CLASS = 28;
    private static final int ENGLISH_PER_CLASS = 28;
    private static final int GK_PER_CLASS = 24; // 120 + 28 + 28 + 24 = 200

    public List<Question> generateForClass(int classLevel) {
        Random rnd = new Random(classLevel * 1_000L + 7);
        List<Question> out = new ArrayList<>();
        out.addAll(generateMath(classLevel, MATH_PER_CLASS, rnd));
        out.addAll(pick(classLevel, SCIENCE, sciencePool(), SCIENCE_PER_CLASS));
        out.addAll(pick(classLevel, ENGLISH, englishPool(), ENGLISH_PER_CLASS));
        out.addAll(pick(classLevel, GK, gkPool(), GK_PER_CLASS));
        return out;
    }

    // ---------------- Mathematics (computed) ----------------

    private List<Question> generateMath(int cls, int count, Random rnd) {
        List<Question> questions = new ArrayList<>();
        LinkedHashSet<String> seenText = new LinkedHashSet<>();
        int i = 0;
        while (questions.size() < count && i < count * 20) {
            Question q = mathQuestion(cls, i++, rnd);
            if (q != null && seenText.add(q.getText())) {
                questions.add(q);
            }
        }
        return questions;
    }

    private Question mathQuestion(int s, int i, Random rnd) {
        switch (i % 10) {
            case 0: {
                long a = s * 6L + i, b = s * 3L + i / 2;
                return numeric(s, EASY, "What is " + a + " + " + b + "?", a + b, rnd);
            }
            case 1: {
                long a = s * 15L + i + 40, b = s * 2L + (i % 11);
                return numeric(s, EASY, "What is " + a + " − " + b + "?", a - b, rnd);
            }
            case 2: {
                long x = 2 + (i % 12), y = 2 + (i / 12);
                return numeric(s, (x * y > 50 ? MEDIUM : EASY),
                        "What is " + x + " × " + y + "?", x * y, rnd);
            }
            case 3: {
                long d = 2 + (i % 9), q = 3 + (i / 9), a = d * q;
                return numeric(s, MEDIUM, "What is " + a + " ÷ " + d + "?", q, rnd);
            }
            case 4: {
                int[] ps = {10, 20, 25, 50, 5};
                long p = ps[i % ps.length], n = 20L * (2 + (i / ps.length));
                return numeric(s, MEDIUM, "What is " + p + "% of " + n + "?", p * n / 100, rnd);
            }
            case 5: {
                long x = s + (i / 4);
                return numeric(s, (x >= 14 ? HARD : MEDIUM),
                        "What is the square of " + x + "?", x * x, rnd);
            }
            case 6: {
                long base = s * 2L + i, a = base, b = base + 3, c = base + 6;
                return numeric(s, MEDIUM,
                        "What is the average of " + a + ", " + b + " and " + c + "?",
                        (a + b + c) / 3, rnd);
            }
            case 7: {
                long coef = 2 + (i / 9) % 7, x = 1 + (i % 9), cst = 1 + (i % 6);
                long c = coef * x + cst;
                return numeric(s, HARD,
                        "If " + coef + "x + " + cst + " = " + c + ", what is x?", x, rnd);
            }
            case 8: {
                long side = s + i / 3;
                return numeric(s, EASY,
                        "What is the perimeter of a square with side " + side + " units?",
                        4 * side, rnd);
            }
            default: {
                long l = 3 + (i % 12), w = 2 + (i / 12);
                return numeric(s, MEDIUM,
                        "What is the area of a " + l + " × " + w + " rectangle (square units)?",
                        l * w, rnd);
            }
        }
    }

    /** Build a numeric multiple-choice question: the right answer plus three near-miss distractors. */
    private Question numeric(int cls, Difficulty d, String text, long answer, Random rnd) {
        LinkedHashSet<Long> values = new LinkedHashSet<>();
        values.add(answer);
        long[] deltas = {1, -1, 2, -2, 3, -3, 5, 10, -5};
        for (long delta : deltas) {
            if (values.size() >= 4) {
                break;
            }
            long candidate = answer + delta;
            if (candidate >= 0) {
                values.add(candidate);
            }
        }
        long extra = answer + 4;
        while (values.size() < 4) {
            values.add(extra++);
        }
        List<Long> shuffled = new ArrayList<>(values);
        Collections.shuffle(shuffled, rnd);
        List<String> options = new ArrayList<>();
        int correctIndex = 0;
        for (int k = 0; k < shuffled.size(); k++) {
            options.add(Long.toString(shuffled.get(k)));
            if (shuffled.get(k) == answer) {
                correctIndex = k;
            }
        }
        return new Question(cls, MATH, d, text, options, correctIndex, null);
    }

    // ---------------- Curated pools ----------------

    private record Curated(String text, List<String> options, int correct, Difficulty difficulty) {
    }

    /** Take {@code count} items from a pool, starting at a class-dependent offset so classes differ. */
    private List<Question> pick(int cls, String subject, List<Curated> pool, int count) {
        List<Question> out = new ArrayList<>();
        int offset = ((cls - 6) * count) % pool.size();
        for (int i = 0; i < count; i++) {
            Curated c = pool.get((offset + i) % pool.size());
            out.add(new Question(cls, subject, c.difficulty(), c.text(),
                    new ArrayList<>(c.options()), c.correct(), null));
        }
        return out;
    }

    private Curated c(String text, List<String> options, int correct, Difficulty d) {
        return new Curated(text, options, correct, d);
    }

    private List<Curated> sciencePool() {
        return List.of(
                c("What is the chemical symbol for Hydrogen?", List.of("H", "He", "Hy", "Ho"), 0, EASY),
                c("What is the chemical symbol for Oxygen?", List.of("O", "Ox", "Og", "On"), 0, EASY),
                c("What is the chemical symbol for Carbon?", List.of("C", "Ca", "Cr", "Co"), 0, EASY),
                c("What is the chemical symbol for Sodium?", List.of("So", "Na", "Sd", "S"), 1, MEDIUM),
                c("What is the chemical symbol for Potassium?", List.of("P", "Po", "K", "Pt"), 2, HARD),
                c("What is the chemical symbol for Iron?", List.of("Ir", "Fe", "In", "I"), 1, MEDIUM),
                c("What is the chemical symbol for Gold?", List.of("Go", "Gd", "Au", "Ag"), 2, MEDIUM),
                c("What is the chemical symbol for Silver?", List.of("Si", "Ag", "Sv", "Au"), 1, MEDIUM),
                c("What is the chemical symbol for Nitrogen?", List.of("Ni", "N", "Nt", "Na"), 1, EASY),
                c("What is the chemical symbol for Helium?", List.of("H", "He", "Hl", "Hm"), 1, EASY),
                c("What is the SI unit of force?", List.of("Joule", "Newton", "Watt", "Pascal"), 1, MEDIUM),
                c("What is the SI unit of energy?", List.of("Newton", "Watt", "Joule", "Volt"), 2, MEDIUM),
                c("What is the SI unit of power?", List.of("Watt", "Joule", "Ampere", "Ohm"), 0, MEDIUM),
                c("What is the SI unit of electric current?", List.of("Volt", "Ohm", "Ampere", "Watt"), 2, MEDIUM),
                c("What is the SI unit of temperature?", List.of("Celsius", "Kelvin", "Fahrenheit", "Joule"), 1, HARD),
                c("What is the SI unit of pressure?", List.of("Pascal", "Newton", "Bar", "Watt"), 0, HARD),
                c("What is the unit of frequency?", List.of("Hertz", "Decibel", "Watt", "Newton"), 0, MEDIUM),
                c("What is the powerhouse of the cell?", List.of("Nucleus", "Mitochondria", "Ribosome", "Vacuole"), 1, EASY),
                c("Which gas do plants absorb for photosynthesis?", List.of("Oxygen", "Carbon dioxide", "Nitrogen", "Hydrogen"), 1, EASY),
                c("Which gas do humans breathe in to survive?", List.of("Carbon dioxide", "Oxygen", "Helium", "Methane"), 1, EASY),
                c("How many bones are in the adult human body?", List.of("201", "206", "210", "196"), 1, HARD),
                c("What is the largest organ of the human body?", List.of("Heart", "Liver", "Skin", "Lungs"), 2, MEDIUM),
                c("Which organ filters waste from the blood?", List.of("Heart", "Kidney", "Lung", "Brain"), 1, MEDIUM),
                c("What is the approximate speed of light?", List.of("3×10⁸ m/s", "3×10⁶ m/s", "3×10⁵ m/s", "3×10¹⁰ m/s"), 0, HARD),
                c("Which planet is closest to the Sun?", List.of("Venus", "Mercury", "Earth", "Mars"), 1, EASY),
                c("Which is the largest planet in our solar system?", List.of("Saturn", "Jupiter", "Neptune", "Earth"), 1, EASY),
                c("Which planet is called the Red Planet?", List.of("Mars", "Venus", "Jupiter", "Mercury"), 0, EASY),
                c("At what temperature does water freeze?", List.of("0°C", "10°C", "-10°C", "5°C"), 0, EASY),
                c("At what temperature does water boil at sea level?", List.of("90°C", "100°C", "110°C", "80°C"), 1, EASY),
                c("What is the chemical formula of water?", List.of("CO₂", "H₂O", "O₂", "NaCl"), 1, EASY),
                c("What is the chemical formula of common salt?", List.of("NaCl", "KCl", "HCl", "NaOH"), 0, MEDIUM),
                c("Which process do plants use to make food?", List.of("Respiration", "Photosynthesis", "Digestion", "Transpiration"), 1, MEDIUM),
                c("Which force pulls objects towards the Earth?", List.of("Friction", "Gravity", "Magnetism", "Tension"), 1, EASY),
                c("Which instrument measures temperature?", List.of("Barometer", "Thermometer", "Ammeter", "Hygrometer"), 1, EASY),
                c("Acids turn blue litmus paper which colour?", List.of("Green", "Red", "Yellow", "Blue"), 1, MEDIUM),
                c("Which gas is most abundant in Earth's atmosphere?", List.of("Oxygen", "Nitrogen", "Carbon dioxide", "Argon"), 1, MEDIUM),
                c("Which vitamin do we get from sunlight?", List.of("Vitamin A", "Vitamin C", "Vitamin D", "Vitamin K"), 2, MEDIUM),
                c("How many teeth does an adult human have?", List.of("28", "30", "32", "34"), 2, HARD),
                c("Which part of the plant makes food?", List.of("Root", "Stem", "Leaf", "Flower"), 2, EASY),
                c("How many legs does an insect have?", List.of("Four", "Six", "Eight", "Ten"), 1, EASY));
    }

    private List<Curated> englishPool() {
        return List.of(
                c("What is the plural of 'child'?", List.of("childs", "children", "childes", "child"), 1, EASY),
                c("What is the plural of 'mouse'?", List.of("mouses", "mice", "mouse", "mices"), 1, MEDIUM),
                c("What is the plural of 'tooth'?", List.of("tooths", "teeth", "toothes", "teeths"), 1, MEDIUM),
                c("What is the plural of 'foot'?", List.of("foots", "feets", "feet", "foot"), 2, EASY),
                c("What is the plural of 'leaf'?", List.of("leafs", "leaves", "leafes", "leave"), 1, MEDIUM),
                c("What is the plural of 'man'?", List.of("mans", "mens", "men", "man"), 2, EASY),
                c("What is the plural of 'city'?", List.of("citys", "cities", "cityes", "citie"), 1, EASY),
                c("What is the plural of 'baby'?", List.of("babys", "babies", "babyes", "babie"), 1, EASY),
                c("What is the plural of 'knife'?", List.of("knifes", "knives", "knifs", "knive"), 1, MEDIUM),
                c("What is the past tense of 'go'?", List.of("goed", "gone", "went", "going"), 2, EASY),
                c("What is the past tense of 'eat'?", List.of("eated", "ate", "eaten", "eats"), 1, EASY),
                c("What is the past tense of 'run'?", List.of("runned", "ran", "run", "running"), 1, EASY),
                c("What is the past tense of 'swim'?", List.of("swimmed", "swam", "swum", "swims"), 1, MEDIUM),
                c("What is the past tense of 'buy'?", List.of("buyed", "bought", "buyt", "buys"), 1, MEDIUM),
                c("What is the past tense of 'teach'?", List.of("teached", "taught", "teacht", "teaches"), 1, MEDIUM),
                c("What is the past tense of 'write'?", List.of("writed", "wrote", "written", "writes"), 1, MEDIUM),
                c("Choose a synonym of 'happy'.", List.of("sad", "joyful", "angry", "tired"), 1, EASY),
                c("Choose a synonym of 'big'.", List.of("tiny", "large", "small", "weak"), 1, EASY),
                c("Choose a synonym of 'fast'.", List.of("slow", "quick", "late", "dull"), 1, EASY),
                c("Choose a synonym of 'brave'.", List.of("coward", "bold", "weak", "shy"), 1, MEDIUM),
                c("Choose a synonym of 'begin'.", List.of("end", "start", "stop", "close"), 1, EASY),
                c("Choose an antonym of 'hot'.", List.of("warm", "cold", "boiling", "mild"), 1, EASY),
                c("Choose an antonym of 'happy'.", List.of("glad", "sad", "merry", "jolly"), 1, EASY),
                c("Choose an antonym of 'ancient'.", List.of("old", "modern", "antique", "historic"), 1, MEDIUM),
                c("Choose an antonym of 'increase'.", List.of("rise", "decrease", "grow", "expand"), 1, MEDIUM),
                c("Choose an antonym of 'victory'.", List.of("win", "defeat", "success", "triumph"), 1, MEDIUM),
                c("Which word is a noun? 'The dog ran fast.'", List.of("ran", "fast", "dog", "the"), 2, EASY),
                c("Which word is a verb? 'She sings well.'", List.of("She", "sings", "well", "nicely"), 1, EASY),
                c("Which word is an adjective? 'A red apple fell.'", List.of("apple", "red", "fell", "a"), 1, EASY),
                c("Which word is an adverb? 'He runs quickly.'", List.of("He", "runs", "quickly", "fast"), 2, MEDIUM),
                c("Choose the correct article: '___ apple a day.'", List.of("A", "An", "The", "No"), 1, EASY),
                c("Choose the correct verb: 'She ___ to school daily.'", List.of("go", "goes", "going", "gone"), 1, EASY),
                c("Which is spelled correctly?", List.of("recieve", "receive", "receeve", "receve"), 1, MEDIUM),
                c("Which is spelled correctly?", List.of("definately", "definitely", "definitly", "definatly"), 1, HARD),
                c("Which is spelled correctly?", List.of("seperate", "separate", "seperete", "separete"), 1, HARD),
                c("'Break the ice' is an example of a(n):", List.of("idiom", "simile", "metaphor", "proverb"), 0, MEDIUM),
                c("'As busy as a bee' is an example of a:", List.of("metaphor", "simile", "idiom", "pun"), 1, MEDIUM),
                c("Choose an antonym of 'arrive'.", List.of("reach", "depart", "come", "enter"), 1, MEDIUM));
    }

    private List<Curated> gkPool() {
        return List.of(
                c("What is the capital of India?", List.of("Mumbai", "New Delhi", "Kolkata", "Chennai"), 1, EASY),
                c("What is the capital of France?", List.of("Berlin", "Paris", "Rome", "Madrid"), 1, EASY),
                c("What is the capital of Japan?", List.of("Beijing", "Seoul", "Tokyo", "Bangkok"), 2, EASY),
                c("What is the capital of the United States?", List.of("New York", "Washington, D.C.", "Los Angeles", "Chicago"), 1, MEDIUM),
                c("What is the capital of the United Kingdom?", List.of("Manchester", "London", "Liverpool", "Leeds"), 1, EASY),
                c("What is the capital of Australia?", List.of("Sydney", "Melbourne", "Canberra", "Perth"), 2, HARD),
                c("What is the capital of China?", List.of("Shanghai", "Beijing", "Hong Kong", "Guangzhou"), 1, EASY),
                c("What is the capital of Italy?", List.of("Venice", "Milan", "Rome", "Naples"), 2, EASY),
                c("What is the capital of Germany?", List.of("Munich", "Berlin", "Frankfurt", "Hamburg"), 1, EASY),
                c("What is the capital of Canada?", List.of("Toronto", "Ottawa", "Vancouver", "Montreal"), 1, HARD),
                c("What is the capital of Russia?", List.of("Moscow", "Kyiv", "Minsk", "St. Petersburg"), 0, MEDIUM),
                c("What is the capital of Egypt?", List.of("Cairo", "Giza", "Alexandria", "Luxor"), 0, MEDIUM),
                c("What is the currency of Japan?", List.of("Yuan", "Won", "Yen", "Ringgit"), 2, MEDIUM),
                c("What is the currency of the United States?", List.of("Dollar", "Pound", "Euro", "Peso"), 0, EASY),
                c("What is the currency of the United Kingdom?", List.of("Euro", "Pound Sterling", "Dollar", "Franc"), 1, MEDIUM),
                c("What is the currency of India?", List.of("Rupee", "Taka", "Rupiah", "Ringgit"), 0, EASY),
                c("Which is the largest ocean on Earth?", List.of("Atlantic", "Indian", "Pacific", "Arctic"), 2, MEDIUM),
                c("Which is the longest river in the world?", List.of("Amazon", "Nile", "Ganga", "Yangtze"), 1, HARD),
                c("Which is the tallest mountain on Earth?", List.of("K2", "Kangchenjunga", "Mount Everest", "Makalu"), 2, EASY),
                c("Which is the largest desert in the world?", List.of("Sahara", "Gobi", "Thar", "Kalahari"), 0, MEDIUM),
                c("Which is the largest country by area?", List.of("Canada", "China", "Russia", "USA"), 2, MEDIUM),
                c("Which is the smallest continent by area?", List.of("Europe", "Australia", "Antarctica", "Asia"), 1, MEDIUM),
                c("How many continents are there on Earth?", List.of("5", "6", "7", "8"), 2, EASY),
                c("What is the national animal of India?", List.of("Lion", "Tiger", "Elephant", "Leopard"), 1, EASY),
                c("What is the national bird of India?", List.of("Parrot", "Peacock", "Eagle", "Swan"), 1, EASY),
                c("Who is credited with inventing the telephone?", List.of("Thomas Edison", "Alexander Graham Bell", "Nikola Tesla", "Guglielmo Marconi"), 1, MEDIUM),
                c("Who wrote the play 'Romeo and Juliet'?", List.of("Charles Dickens", "William Shakespeare", "Mark Twain", "Leo Tolstoy"), 1, MEDIUM),
                c("The Great Wall is located in which country?", List.of("Japan", "China", "India", "Mongolia"), 1, EASY),
                c("The Taj Mahal is located in which city?", List.of("Delhi", "Agra", "Jaipur", "Lucknow"), 1, EASY),
                c("How many players are there in a cricket team?", List.of("9", "10", "11", "12"), 2, EASY),
                c("How many colours are there in a rainbow?", List.of("5", "6", "7", "8"), 2, EASY),
                c("Where are the United Nations headquarters located?", List.of("Geneva", "Paris", "New York", "London"), 2, HARD),
                c("Which animal is known as the 'Ship of the Desert'?", List.of("Horse", "Camel", "Elephant", "Donkey"), 1, EASY),
                c("In which year did India gain independence?", List.of("1942", "1945", "1947", "1950"), 2, MEDIUM));
    }
}
