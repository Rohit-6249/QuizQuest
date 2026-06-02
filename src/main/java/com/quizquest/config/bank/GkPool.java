package com.quizquest.config.bank;

import static com.quizquest.domain.Difficulty.EASY;
import static com.quizquest.domain.Difficulty.HARD;
import static com.quizquest.domain.Difficulty.MEDIUM;

import java.util.List;

/** Verified General Knowledge questions: 30 per difficulty (90 total). */
public final class GkPool {

    private GkPool() {
    }

    public static List<Curated> all() {
        return List.of(
                // ---------- EASY (30) ----------
                Curated.of("What is the capital of India?", List.of("Mumbai", "New Delhi", "Kolkata", "Chennai"), 1, EASY),
                Curated.of("What is the capital of France?", List.of("Berlin", "Paris", "Rome", "Madrid"), 1, EASY),
                Curated.of("What is the capital of Japan?", List.of("Beijing", "Seoul", "Tokyo", "Bangkok"), 2, EASY),
                Curated.of("What is the capital of the United Kingdom?", List.of("Manchester", "London", "Liverpool", "Leeds"), 1, EASY),
                Curated.of("What is the national animal of India?", List.of("Lion", "Tiger", "Elephant", "Leopard"), 1, EASY),
                Curated.of("What is the national bird of India?", List.of("Parrot", "Peacock", "Eagle", "Swan"), 1, EASY),
                Curated.of("How many continents are there on Earth?", List.of("5", "6", "7", "8"), 2, EASY),
                Curated.of("How many colours are there in a rainbow?", List.of("5", "6", "7", "8"), 2, EASY),
                Curated.of("Which is the largest planet in our solar system?", List.of("Earth", "Jupiter", "Saturn", "Mars"), 1, EASY),
                Curated.of("How many players are there in a cricket team?", List.of("9", "10", "11", "12"), 2, EASY),
                Curated.of("In which city is the Taj Mahal located?", List.of("Delhi", "Agra", "Jaipur", "Lucknow"), 1, EASY),
                Curated.of("Which animal is known as the 'Ship of the Desert'?", List.of("Horse", "Camel", "Elephant", "Donkey"), 1, EASY),
                Curated.of("What is the currency of India?", List.of("Rupee", "Dollar", "Pound", "Yen"), 0, EASY),
                Curated.of("Which festival is known as the 'festival of lights'?", List.of("Holi", "Diwali", "Eid", "Onam"), 1, EASY),
                Curated.of("How many days are there in a leap year?", List.of("365", "366", "364", "360"), 1, EASY),
                Curated.of("What is the national flower of India?", List.of("Rose", "Lotus", "Sunflower", "Tulip"), 1, EASY),
                Curated.of("The Sun is a?", List.of("Planet", "Star", "Moon", "Comet"), 1, EASY),
                Curated.of("Which is the largest ocean on Earth?", List.of("Atlantic", "Indian", "Pacific", "Arctic"), 2, EASY),
                Curated.of("Which shape has exactly three sides?", List.of("Square", "Triangle", "Circle", "Pentagon"), 1, EASY),
                Curated.of("How many months have 31 days?", List.of("5", "6", "7", "8"), 2, EASY),
                Curated.of("What is the capital of Spain?", List.of("Barcelona", "Madrid", "Seville", "Valencia"), 1, EASY),
                Curated.of("How many wheels does a bicycle have?", List.of("1", "2", "3", "4"), 1, EASY),
                Curated.of("Which is the fastest land animal?", List.of("Lion", "Cheetah", "Horse", "Leopard"), 1, EASY),
                Curated.of("How many sides does a square have?", List.of("3", "4", "5", "6"), 1, EASY),
                Curated.of("Which festival is celebrated with colours?", List.of("Diwali", "Holi", "Eid", "Onam"), 1, EASY),
                Curated.of("How many colours are there in the Indian national flag?", List.of("2", "3", "4", "5"), 1, EASY),
                Curated.of("Which is the largest land animal?", List.of("Elephant", "Rhino", "Hippo", "Giraffe"), 0, EASY),
                Curated.of("Which bird cannot fly?", List.of("Sparrow", "Penguin", "Eagle", "Parrot"), 1, EASY),
                Curated.of("Which planet is known as the Red Planet?", List.of("Venus", "Mars", "Mercury", "Saturn"), 1, EASY),
                Curated.of("How many legs does a spider have?", List.of("6", "8", "10", "4"), 1, EASY),
                // ---------- MEDIUM (30) ----------
                Curated.of("What is the capital of the United States?", List.of("New York", "Washington, D.C.", "Los Angeles", "Chicago"), 1, MEDIUM),
                Curated.of("What is the capital of China?", List.of("Shanghai", "Beijing", "Hong Kong", "Guangzhou"), 1, MEDIUM),
                Curated.of("What is the capital of Italy?", List.of("Venice", "Milan", "Rome", "Naples"), 2, MEDIUM),
                Curated.of("What is the capital of Germany?", List.of("Munich", "Berlin", "Frankfurt", "Hamburg"), 1, MEDIUM),
                Curated.of("What is the capital of Russia?", List.of("Moscow", "Kyiv", "Minsk", "St. Petersburg"), 0, MEDIUM),
                Curated.of("What is the currency of Japan?", List.of("Yuan", "Won", "Yen", "Ringgit"), 2, MEDIUM),
                Curated.of("What is the currency of the United States?", List.of("Dollar", "Pound", "Euro", "Peso"), 0, MEDIUM),
                Curated.of("What is the currency of the United Kingdom?", List.of("Euro", "Pound Sterling", "Dollar", "Franc"), 1, MEDIUM),
                Curated.of("Which is the longest river in the world?", List.of("Amazon", "Nile", "Ganga", "Yangtze"), 1, MEDIUM),
                Curated.of("Which is the tallest mountain on Earth?", List.of("K2", "Kangchenjunga", "Mount Everest", "Makalu"), 2, MEDIUM),
                Curated.of("Which is the largest desert in the world?", List.of("Sahara", "Gobi", "Thar", "Kalahari"), 0, MEDIUM),
                Curated.of("Which is the largest country by area?", List.of("Canada", "China", "Russia", "USA"), 2, MEDIUM),
                Curated.of("Which is the smallest continent by area?", List.of("Europe", "Australia", "Antarctica", "Asia"), 1, MEDIUM),
                Curated.of("Who is credited with inventing the telephone?", List.of("Thomas Edison", "Alexander Graham Bell", "Nikola Tesla", "Guglielmo Marconi"), 1, MEDIUM),
                Curated.of("Who wrote the play 'Romeo and Juliet'?", List.of("Charles Dickens", "William Shakespeare", "Mark Twain", "Leo Tolstoy"), 1, MEDIUM),
                Curated.of("The Great Wall is located in which country?", List.of("Japan", "China", "India", "Mongolia"), 1, MEDIUM),
                Curated.of("In which year did India gain independence?", List.of("1942", "1945", "1947", "1950"), 2, MEDIUM),
                Curated.of("In which city is the Statue of Liberty located?", List.of("London", "Paris", "New York", "Rome"), 2, MEDIUM),
                Curated.of("Which is the smallest ocean on Earth?", List.of("Indian", "Arctic", "Atlantic", "Southern"), 1, MEDIUM),
                Curated.of("Which planet is closest in size to Earth?", List.of("Mars", "Venus", "Mercury", "Jupiter"), 1, MEDIUM),
                Curated.of("What is the capital of Canada?", List.of("Toronto", "Ottawa", "Vancouver", "Montreal"), 1, MEDIUM),
                Curated.of("What is the capital of Australia?", List.of("Sydney", "Canberra", "Melbourne", "Perth"), 1, MEDIUM),
                Curated.of("What is the currency of China?", List.of("Yuan", "Yen", "Won", "Baht"), 0, MEDIUM),
                Curated.of("What is the currency of France?", List.of("Franc", "Euro", "Pound", "Lira"), 1, MEDIUM),
                Curated.of("Who was the first Prime Minister of India?", List.of("Mahatma Gandhi", "Jawaharlal Nehru", "Sardar Patel", "Subhas Chandra Bose"), 1, MEDIUM),
                Curated.of("In which city is the Eiffel Tower located?", List.of("London", "Paris", "Rome", "Berlin"), 1, MEDIUM),
                Curated.of("Which gas is used to fill balloons that float?", List.of("Oxygen", "Helium", "Nitrogen", "Carbon dioxide"), 1, MEDIUM),
                Curated.of("How many zeroes are there in one million?", List.of("3", "4", "6", "9"), 2, MEDIUM),
                Curated.of("Which is the hottest continent on Earth?", List.of("Asia", "Africa", "Australia", "Europe"), 1, MEDIUM),
                Curated.of("How many years are there in a decade?", List.of("5", "10", "20", "100"), 1, MEDIUM),
                // ---------- HARD (30) ----------
                Curated.of("What is the capital of Australia?", List.of("Sydney", "Melbourne", "Canberra", "Perth"), 2, HARD),
                Curated.of("What is the capital of Canada?", List.of("Toronto", "Ottawa", "Vancouver", "Montreal"), 1, HARD),
                Curated.of("What is the capital of Egypt?", List.of("Cairo", "Giza", "Alexandria", "Luxor"), 0, HARD),
                Curated.of("What is the capital of Brazil?", List.of("Rio de Janeiro", "São Paulo", "Brasília", "Salvador"), 2, HARD),
                Curated.of("What is the capital of Turkey?", List.of("Istanbul", "Ankara", "Izmir", "Bursa"), 1, HARD),
                Curated.of("Where are the United Nations headquarters located?", List.of("Geneva", "Paris", "New York", "London"), 2, HARD),
                Curated.of("What is the currency of South Korea?", List.of("Yen", "Won", "Yuan", "Baht"), 1, HARD),
                Curated.of("Who painted the Mona Lisa?", List.of("Pablo Picasso", "Leonardo da Vinci", "Vincent van Gogh", "Michelangelo"), 1, HARD),
                Curated.of("Which is the smallest country in the world by area?", List.of("Monaco", "Vatican City", "Malta", "Nauru"), 1, HARD),
                Curated.of("Which atmospheric layer protects us from the Sun's UV rays?", List.of("Oxygen layer", "Ozone layer", "Nitrogen layer", "Helium layer"), 1, HARD),
                Curated.of("Who was the first person to walk on the Moon?", List.of("Buzz Aldrin", "Neil Armstrong", "Yuri Gagarin", "Michael Collins"), 1, HARD),
                Curated.of("What is the largest mammal on Earth?", List.of("Elephant", "Blue whale", "Giraffe", "Hippopotamus"), 1, HARD),
                Curated.of("What is the currency of Russia?", List.of("Ruble", "Lira", "Euro", "Zloty"), 0, HARD),
                Curated.of("Which planet rotates on its side?", List.of("Neptune", "Uranus", "Saturn", "Venus"), 1, HARD),
                Curated.of("How many players are there in a football (soccer) team?", List.of("9", "10", "11", "12"), 2, HARD),
                Curated.of("Which is the deepest ocean on Earth?", List.of("Atlantic", "Pacific", "Indian", "Arctic"), 1, HARD),
                Curated.of("Where are the World Health Organization (WHO) headquarters?", List.of("Geneva", "New York", "Paris", "Vienna"), 0, HARD),
                Curated.of("Which country has the largest population in the world?", List.of("China", "India", "United States", "Indonesia"), 1, HARD),
                Curated.of("Who is known as the 'Father of the Nation' in India?", List.of("Jawaharlal Nehru", "Mahatma Gandhi", "Sardar Patel", "B. R. Ambedkar"), 1, HARD),
                Curated.of("What is the administrative capital of South Africa?", List.of("Cape Town", "Pretoria", "Johannesburg", "Durban"), 1, HARD),
                Curated.of("What is the capital of Argentina?", List.of("Lima", "Buenos Aires", "Santiago", "Bogotá"), 1, HARD),
                Curated.of("Which country gifted the Statue of Liberty to the USA?", List.of("United Kingdom", "France", "Spain", "Italy"), 1, HARD),
                Curated.of("Who developed the theory of evolution?", List.of("Isaac Newton", "Charles Darwin", "Gregor Mendel", "Albert Einstein"), 1, HARD),
                Curated.of("Which is the longest mountain range in the world?", List.of("Himalayas", "Andes", "Rockies", "Alps"), 1, HARD),
                Curated.of("The Great Barrier Reef is located near which country?", List.of("Brazil", "Australia", "India", "Egypt"), 1, HARD),
                Curated.of("Which planet has the most known moons?", List.of("Jupiter", "Saturn", "Uranus", "Mars"), 1, HARD),
                Curated.of("Who wrote India's national anthem?", List.of("Mahatma Gandhi", "Rabindranath Tagore", "Jawaharlal Nehru", "Bankim Chandra Chatterjee"), 1, HARD),
                Curated.of("In computing, what does 'CPU' stand for?", List.of("Central Process Unit", "Central Processing Unit", "Computer Personal Unit", "Central Power Unit"), 1, HARD),
                Curated.of("What is the capital of Japan?", List.of("Osaka", "Tokyo", "Kyoto", "Nagoya"), 1, HARD),
                Curated.of("Which is the largest country in South America?", List.of("Argentina", "Brazil", "Peru", "Chile"), 1, HARD));
    }
}
