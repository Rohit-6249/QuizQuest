package com.quizquest.config.bank;

import static com.quizquest.domain.Difficulty.EASY;
import static com.quizquest.domain.Difficulty.HARD;
import static com.quizquest.domain.Difficulty.MEDIUM;

import java.util.List;

/** Verified English questions: 30 per difficulty (90 total). */
public final class EnglishPool {

    private EnglishPool() {
    }

    public static List<Curated> all() {
        return List.of(
                // ---------- EASY (30) ----------
                Curated.of("What is the plural of 'cat'?", List.of("cat", "cats", "cates", "catt"), 1, EASY),
                Curated.of("What is the plural of 'box'?", List.of("boxs", "boxes", "boxen", "box"), 1, EASY),
                Curated.of("What is the plural of 'child'?", List.of("childs", "children", "childes", "child"), 1, EASY),
                Curated.of("What is the plural of 'man'?", List.of("mans", "mens", "men", "man"), 2, EASY),
                Curated.of("What is the past tense of 'play'?", List.of("play", "played", "playd", "plaid"), 1, EASY),
                Curated.of("What is the past tense of 'go'?", List.of("goed", "gone", "went", "going"), 2, EASY),
                Curated.of("What is the past tense of 'eat'?", List.of("eated", "ate", "eaten", "eats"), 1, EASY),
                Curated.of("What is the opposite of 'big'?", List.of("tall", "small", "huge", "wide"), 1, EASY),
                Curated.of("What is the opposite of 'hot'?", List.of("warm", "cold", "boiling", "mild"), 1, EASY),
                Curated.of("What is the opposite of 'up'?", List.of("left", "down", "top", "high"), 1, EASY),
                Curated.of("Which word is a noun in 'The dog barks.'?", List.of("The", "dog", "barks", "loudly"), 1, EASY),
                Curated.of("Which of these is an action word (verb)?", List.of("happy", "run", "blue", "slowly"), 1, EASY),
                Curated.of("Choose the correct article: '___ orange'.", List.of("A", "An", "The", "No"), 1, EASY),
                Curated.of("Choose the correct word: 'She ___ happy.'", List.of("is", "are", "am", "be"), 0, EASY),
                Curated.of("What is a baby dog called?", List.of("kitten", "puppy", "calf", "cub"), 1, EASY),
                Curated.of("Which word rhymes with 'cat'?", List.of("dog", "hat", "cup", "pen"), 1, EASY),
                Curated.of("A capital letter is used at the ___ of a sentence.", List.of("end", "middle", "beginning", "anywhere"), 2, EASY),
                Curated.of("'I', 'you' and 'he' are examples of?", List.of("nouns", "pronouns", "verbs", "adjectives"), 1, EASY),
                Curated.of("Choose a synonym of 'happy'.", List.of("sad", "glad", "angry", "tired"), 1, EASY),
                Curated.of("What is the plural of 'baby'?", List.of("babys", "babies", "babyes", "babie"), 1, EASY),
                Curated.of("What is the plural of 'dog'?", List.of("dog", "dogs", "doges", "dogz"), 1, EASY),
                Curated.of("What is the plural of 'bus'?", List.of("buss", "buses", "busies", "bus"), 1, EASY),
                Curated.of("What is the past tense of 'jump'?", List.of("jump", "jumped", "jumpd", "jumpt"), 1, EASY),
                Curated.of("What is the opposite of 'fast'?", List.of("quick", "slow", "rapid", "swift"), 1, EASY),
                Curated.of("What is the opposite of 'day'?", List.of("noon", "night", "evening", "dawn"), 1, EASY),
                Curated.of("Which of these is a colour?", List.of("run", "blue", "jump", "sing"), 1, EASY),
                Curated.of("Choose the correct word: 'He ___ a book.'", List.of("have", "has", "having", "haves"), 1, EASY),
                Curated.of("What is a baby cat called?", List.of("puppy", "kitten", "cub", "foal"), 1, EASY),
                Curated.of("Choose a synonym of 'big'.", List.of("small", "large", "tiny", "short"), 1, EASY),
                Curated.of("What is the opposite of 'open'?", List.of("shut", "wide", "free", "empty"), 0, EASY),
                // ---------- MEDIUM (30) ----------
                Curated.of("What is the plural of 'mouse'?", List.of("mouses", "mice", "mouse", "mices"), 1, MEDIUM),
                Curated.of("What is the plural of 'leaf'?", List.of("leafs", "leaves", "leafes", "leave"), 1, MEDIUM),
                Curated.of("What is the past tense of 'swim'?", List.of("swimmed", "swam", "swum", "swims"), 1, MEDIUM),
                Curated.of("What is the past tense of 'buy'?", List.of("buyed", "bought", "buyt", "buys"), 1, MEDIUM),
                Curated.of("What is the past tense of 'write'?", List.of("writed", "wrote", "written", "writes"), 1, MEDIUM),
                Curated.of("Choose a synonym of 'begin'.", List.of("end", "start", "stop", "close"), 1, MEDIUM),
                Curated.of("Choose a synonym of 'brave'.", List.of("coward", "bold", "weak", "shy"), 1, MEDIUM),
                Curated.of("Choose an antonym of 'ancient'.", List.of("old", "modern", "antique", "historic"), 1, MEDIUM),
                Curated.of("Choose an antonym of 'increase'.", List.of("rise", "decrease", "grow", "expand"), 1, MEDIUM),
                Curated.of("Which word is an adjective in 'A red apple fell.'?", List.of("apple", "red", "fell", "a"), 1, MEDIUM),
                Curated.of("Which word is an adverb in 'He runs quickly.'?", List.of("He", "runs", "quickly", "fast"), 2, MEDIUM),
                Curated.of("Which is spelled correctly?", List.of("recieve", "receive", "receeve", "receve"), 1, MEDIUM),
                Curated.of("'Break the ice' is an example of a(n):", List.of("idiom", "simile", "metaphor", "proverb"), 0, MEDIUM),
                Curated.of("Choose the correct word: 'They ___ playing.'", List.of("is", "are", "am", "was"), 1, MEDIUM),
                Curated.of("Identify the conjunction: 'I stayed home because it rained.'", List.of("stayed", "home", "because", "rained"), 2, MEDIUM),
                Curated.of("What is the comparative form of 'good'?", List.of("gooder", "better", "best", "more good"), 1, MEDIUM),
                Curated.of("Choose a synonym of 'fast'.", List.of("slow", "quick", "late", "dull"), 1, MEDIUM),
                Curated.of("What is the past tense of 'teach'?", List.of("teached", "taught", "teacht", "teaches"), 1, MEDIUM),
                Curated.of("What is the plural of 'knife'?", List.of("knifes", "knives", "knifs", "knive"), 1, MEDIUM),
                Curated.of("Choose an antonym of 'victory'.", List.of("win", "defeat", "success", "triumph"), 1, MEDIUM),
                Curated.of("What is the plural of 'tooth'?", List.of("tooths", "teeth", "toothes", "teeths"), 1, MEDIUM),
                Curated.of("What is the plural of 'foot'?", List.of("foots", "feets", "feet", "foot"), 2, MEDIUM),
                Curated.of("What is the past tense of 'run'?", List.of("runned", "ran", "run", "running"), 1, MEDIUM),
                Curated.of("Choose a synonym of 'smart'.", List.of("dull", "clever", "slow", "lazy"), 1, MEDIUM),
                Curated.of("Choose an antonym of 'difficult'.", List.of("hard", "easy", "tough", "complex"), 1, MEDIUM),
                Curated.of("Choose the correct word: 'There ___ many books.'", List.of("is", "are", "was", "has"), 1, MEDIUM),
                Curated.of("Identify the preposition: 'The cat is on the mat.'", List.of("cat", "is", "on", "mat"), 2, MEDIUM),
                Curated.of("What is the comparative form of 'tall'?", List.of("tallest", "taller", "more tall", "tall"), 1, MEDIUM),
                Curated.of("Which of these is a proper noun?", List.of("country", "river", "India", "city"), 2, MEDIUM),
                Curated.of("What is the past tense of 'sing'?", List.of("singed", "sang", "sung", "sings"), 1, MEDIUM),
                // ---------- HARD (30) ----------
                Curated.of("Which is spelled correctly?", List.of("definately", "definitely", "definitly", "definatly"), 1, HARD),
                Curated.of("Which is spelled correctly?", List.of("seperate", "separate", "seperete", "separete"), 1, HARD),
                Curated.of("Which is spelled correctly?", List.of("neccessary", "necessary", "necesary", "neccesary"), 1, HARD),
                Curated.of("What is the past participle of 'go'?", List.of("went", "gone", "goed", "going"), 1, HARD),
                Curated.of("Choose the passive voice of: 'The cat chased the mouse.'", List.of("The mouse chased the cat.", "The mouse was chased by the cat.", "The mouse is chasing the cat.", "The cat was chased by the mouse."), 1, HARD),
                Curated.of("Reported speech: He said, \"I am tired.\"", List.of("He said he is tired.", "He said he was tired.", "He says he was tired.", "He said I am tired."), 1, HARD),
                Curated.of("'As busy as a bee' is an example of a:", List.of("metaphor", "simile", "idiom", "pun"), 1, HARD),
                Curated.of("Choose an antonym of 'generous'.", List.of("kind", "stingy", "giving", "lavish"), 1, HARD),
                Curated.of("Choose a synonym of 'enormous'.", List.of("tiny", "huge", "average", "narrow"), 1, HARD),
                Curated.of("Which sentence is grammatically correct?", List.of("She don't like tea.", "She doesn't likes tea.", "She doesn't like tea.", "She not like tea."), 2, HARD),
                Curated.of("What is the superlative form of 'bad'?", List.of("baddest", "worse", "worst", "more bad"), 2, HARD),
                Curated.of("What does the word 'hardly' mean?", List.of("strongly", "barely", "quickly", "fully"), 1, HARD),
                Curated.of("A word that joins two sentences is called a?", List.of("noun", "conjunction", "adverb", "article"), 1, HARD),
                Curated.of("What is the plural of 'crisis'?", List.of("crisises", "crises", "crisis", "crisi"), 1, HARD),
                Curated.of("Choose an antonym of 'transparent'.", List.of("clear", "opaque", "glassy", "bright"), 1, HARD),
                Curated.of("A 'bibliophile' is a lover of?", List.of("music", "books", "food", "travel"), 1, HARD),
                Curated.of("Which figure of speech is in 'The wind whispered'?", List.of("simile", "metaphor", "personification", "hyperbole"), 2, HARD),
                Curated.of("Choose a synonym of 'reluctant'.", List.of("eager", "unwilling", "happy", "ready"), 1, HARD),
                Curated.of("Choose the correct word: 'Neither of them ___ here.'", List.of("is", "are", "were", "have"), 0, HARD),
                Curated.of("Choose a synonym of 'abundant'.", List.of("scarce", "plentiful", "rare", "empty"), 1, HARD),
                Curated.of("Which is spelled correctly?", List.of("acommodate", "accommodate", "accomodate", "acomodate"), 1, HARD),
                Curated.of("Choose an antonym of 'expand'.", List.of("grow", "contract", "widen", "stretch"), 1, HARD),
                Curated.of("Choose a synonym of 'rapid'.", List.of("slow", "swift", "late", "calm"), 1, HARD),
                Curated.of("What is the past participle of 'eat'?", List.of("ate", "eaten", "eated", "eat"), 1, HARD),
                Curated.of("The idiom 'a piece of cake' means something is?", List.of("very hard", "very easy", "tasty", "expensive"), 1, HARD),
                Curated.of("What is the plural of 'phenomenon'?", List.of("phenomenons", "phenomena", "phenomenon", "phenomenas"), 1, HARD),
                Curated.of("Which sentence uses the correct tense?", List.of("She have gone home.", "She has went home.", "She has gone home.", "She gone home."), 2, HARD),
                Curated.of("Choose an antonym of 'optimist'.", List.of("dreamer", "pessimist", "realist", "idealist"), 1, HARD),
                Curated.of("The idiom 'once in a blue moon' means?", List.of("often", "rarely", "never", "daily"), 1, HARD),
                Curated.of("Choose a synonym of 'courage'.", List.of("fear", "bravery", "doubt", "worry"), 1, HARD));
    }
}
