package com.quizquest.config.bank;

import static com.quizquest.domain.Difficulty.EASY;
import static com.quizquest.domain.Difficulty.HARD;
import static com.quizquest.domain.Difficulty.MEDIUM;

import java.util.List;

/** Verified Science questions: 30 per difficulty (90 total). */
public final class SciencePool {

    private SciencePool() {
    }

    public static List<Curated> all() {
        return List.of(
                // ---------- EASY (30) ----------
                Curated.of("Which gas do plants absorb for photosynthesis?", List.of("Oxygen", "Carbon dioxide", "Nitrogen", "Hydrogen"), 1, EASY),
                Curated.of("Which gas do humans need to breathe to survive?", List.of("Carbon dioxide", "Oxygen", "Helium", "Nitrogen"), 1, EASY),
                Curated.of("How many legs does an insect have?", List.of("Four", "Six", "Eight", "Ten"), 1, EASY),
                Curated.of("Which part of a plant usually makes food?", List.of("Root", "Stem", "Leaf", "Flower"), 2, EASY),
                Curated.of("At what temperature does water freeze?", List.of("0°C", "10°C", "100°C", "-100°C"), 0, EASY),
                Curated.of("At what temperature does water boil at sea level?", List.of("50°C", "90°C", "100°C", "120°C"), 2, EASY),
                Curated.of("In which direction does the Sun rise?", List.of("West", "East", "North", "South"), 1, EASY),
                Curated.of("Which organ pumps blood around the body?", List.of("Lungs", "Heart", "Liver", "Brain"), 1, EASY),
                Curated.of("Which sense organ do we use to see?", List.of("Ears", "Nose", "Eyes", "Skin"), 2, EASY),
                Curated.of("What is the young one of a frog called?", List.of("Puppy", "Tadpole", "Calf", "Kitten"), 1, EASY),
                Curated.of("Plants make food using sunlight in a process called?", List.of("Respiration", "Photosynthesis", "Digestion", "Evaporation"), 1, EASY),
                Curated.of("Which is a natural source of light?", List.of("Moon", "Sun", "Mirror", "Cloud"), 1, EASY),
                Curated.of("Ice is the ___ state of water.", List.of("Solid", "Liquid", "Gas", "Plasma"), 0, EASY),
                Curated.of("Which gas is needed for burning?", List.of("Nitrogen", "Oxygen", "Carbon dioxide", "Hydrogen"), 1, EASY),
                Curated.of("Fish breathe using their?", List.of("Lungs", "Gills", "Skin", "Nose"), 1, EASY),
                Curated.of("The force that pulls objects towards the ground is?", List.of("Friction", "Gravity", "Magnetism", "Push"), 1, EASY),
                Curated.of("Which instrument measures temperature?", List.of("Barometer", "Thermometer", "Telescope", "Microscope"), 1, EASY),
                Curated.of("How many days does the Earth take to orbit the Sun?", List.of("30", "100", "365", "24"), 2, EASY),
                Curated.of("Which animal commonly gives us milk?", List.of("Lion", "Cow", "Tiger", "Eagle"), 1, EASY),
                Curated.of("Which body part helps us to smell?", List.of("Ears", "Nose", "Eyes", "Tongue"), 1, EASY),
                Curated.of("Which planet do we live on?", List.of("Mars", "Earth", "Venus", "Moon"), 1, EASY),
                Curated.of("How many hours are there in a day?", List.of("12", "24", "36", "48"), 1, EASY),
                Curated.of("Which is the largest land animal?", List.of("Lion", "Elephant", "Horse", "Bear"), 1, EASY),
                Curated.of("What do bees make?", List.of("Milk", "Honey", "Silk", "Butter"), 1, EASY),
                Curated.of("Which part of the body do we use to think?", List.of("Heart", "Brain", "Lungs", "Liver"), 1, EASY),
                Curated.of("Plants mainly need ___ to make food.", List.of("Darkness", "Sunlight", "Salt", "Plastic"), 1, EASY),
                Curated.of("Which of these is a fruit?", List.of("Carrot", "Apple", "Potato", "Onion"), 1, EASY),
                Curated.of("The gas we breathe out is mostly?", List.of("Oxygen", "Carbon dioxide", "Hydrogen", "Helium"), 1, EASY),
                Curated.of("How many basic senses does a human have?", List.of("Three", "Four", "Five", "Six"), 2, EASY),
                Curated.of("Which animal is known as man's best friend?", List.of("Cat", "Dog", "Cow", "Goat"), 1, EASY),
                // ---------- MEDIUM (30) ----------
                Curated.of("What is the powerhouse of the cell?", List.of("Nucleus", "Mitochondria", "Ribosome", "Vacuole"), 1, MEDIUM),
                Curated.of("What is the largest organ of the human body?", List.of("Heart", "Liver", "Skin", "Lungs"), 2, MEDIUM),
                Curated.of("Which gas is most abundant in Earth's atmosphere?", List.of("Oxygen", "Nitrogen", "Carbon dioxide", "Argon"), 1, MEDIUM),
                Curated.of("What is the chemical formula of water?", List.of("CO₂", "H₂O", "O₂", "NaCl"), 1, MEDIUM),
                Curated.of("Which vitamin do we get from sunlight?", List.of("Vitamin A", "Vitamin C", "Vitamin D", "Vitamin K"), 2, MEDIUM),
                Curated.of("Which organ filters waste from the blood?", List.of("Heart", "Kidney", "Lung", "Stomach"), 1, MEDIUM),
                Curated.of("How many planets are there in our solar system?", List.of("7", "8", "9", "10"), 1, MEDIUM),
                Curated.of("Which planet is closest to the Sun?", List.of("Venus", "Mercury", "Earth", "Mars"), 1, MEDIUM),
                Curated.of("Which planet is called the Red Planet?", List.of("Mars", "Venus", "Jupiter", "Saturn"), 0, MEDIUM),
                Curated.of("Acids turn blue litmus paper which colour?", List.of("Green", "Red", "Blue", "Yellow"), 1, MEDIUM),
                Curated.of("Which blood cells help fight infection?", List.of("Red blood cells", "White blood cells", "Platelets", "Plasma"), 1, MEDIUM),
                Curated.of("What is the chemical symbol for sodium?", List.of("So", "Na", "Sd", "S"), 1, MEDIUM),
                Curated.of("Which part of a plant absorbs water from the soil?", List.of("Leaf", "Stem", "Roots", "Flower"), 2, MEDIUM),
                Curated.of("Through which of these can sound NOT travel?", List.of("Water", "Air", "Vacuum", "Steel"), 2, MEDIUM),
                Curated.of("Boiling changes a liquid into a?", List.of("Solid", "Gas", "Plasma", "Crystal"), 1, MEDIUM),
                Curated.of("What is the normal body temperature of a healthy human?", List.of("30°C", "37°C", "40°C", "25°C"), 1, MEDIUM),
                Curated.of("Which gas do plants release during photosynthesis?", List.of("Carbon dioxide", "Oxygen", "Nitrogen", "Hydrogen"), 1, MEDIUM),
                Curated.of("Which is the basic structural unit of life?", List.of("Tissue", "Cell", "Organ", "Atom"), 1, MEDIUM),
                Curated.of("What is the SI unit of length?", List.of("Gram", "Metre", "Litre", "Second"), 1, MEDIUM),
                Curated.of("Which planet is known as Earth's twin?", List.of("Mars", "Venus", "Mercury", "Jupiter"), 1, MEDIUM),
                Curated.of("Which organ is used for breathing?", List.of("Heart", "Lungs", "Liver", "Kidney"), 1, MEDIUM),
                Curated.of("Plants lose water through a process called?", List.of("Respiration", "Transpiration", "Digestion", "Condensation"), 1, MEDIUM),
                Curated.of("Which metal is liquid at room temperature?", List.of("Iron", "Mercury", "Gold", "Lead"), 1, MEDIUM),
                Curated.of("The centre of an atom is called the?", List.of("Electron", "Nucleus", "Orbit", "Shell"), 1, MEDIUM),
                Curated.of("Which is a renewable source of energy?", List.of("Coal", "Solar", "Petrol", "Diesel"), 1, MEDIUM),
                Curated.of("Earthquakes are measured using the?", List.of("Richter scale", "Beaufort scale", "pH scale", "Celsius scale"), 0, MEDIUM),
                Curated.of("Which vitamin helps blood to clot?", List.of("Vitamin A", "Vitamin C", "Vitamin D", "Vitamin K"), 3, MEDIUM),
                Curated.of("The study of living things is called?", List.of("Chemistry", "Physics", "Biology", "Geology"), 2, MEDIUM),
                Curated.of("Sound travels fastest through?", List.of("Solids", "Liquids", "Gases", "Vacuum"), 0, MEDIUM),
                Curated.of("Which organ controls the body's nervous system?", List.of("Heart", "Brain", "Liver", "Lungs"), 1, MEDIUM),
                // ---------- HARD (30) ----------
                Curated.of("What is the chemical symbol for Potassium?", List.of("P", "Po", "K", "Pt"), 2, HARD),
                Curated.of("What is the chemical symbol for Gold?", List.of("Go", "Gd", "Au", "Ag"), 2, HARD),
                Curated.of("What is the chemical symbol for Iron?", List.of("Ir", "Fe", "In", "I"), 1, HARD),
                Curated.of("What is the SI unit of force?", List.of("Joule", "Newton", "Watt", "Pascal"), 1, HARD),
                Curated.of("What is the SI unit of electric current?", List.of("Volt", "Ohm", "Ampere", "Watt"), 2, HARD),
                Curated.of("What is the SI unit of pressure?", List.of("Pascal", "Newton", "Bar", "Watt"), 0, HARD),
                Curated.of("What is the approximate speed of light?", List.of("3×10⁶ m/s", "3×10⁸ m/s", "3×10⁵ m/s", "3×10¹⁰ m/s"), 1, HARD),
                Curated.of("How many bones are in the adult human body?", List.of("201", "206", "210", "196"), 1, HARD),
                Curated.of("Which gas is commonly known as 'laughing gas'?", List.of("Nitrous oxide", "Carbon dioxide", "Methane", "Oxygen"), 0, HARD),
                Curated.of("What is the pH value of pure water?", List.of("0", "7", "14", "1"), 1, HARD),
                Curated.of("Which molecule is the main energy currency of the cell?", List.of("DNA", "ATP", "RNA", "Glucose"), 1, HARD),
                Curated.of("Who proposed the theory of relativity?", List.of("Newton", "Einstein", "Bohr", "Hawking"), 1, HARD),
                Curated.of("What is the chemical formula of common salt?", List.of("NaCl", "KCl", "HCl", "NaOH"), 0, HARD),
                Curated.of("Which blood group is the universal donor?", List.of("AB positive", "O negative", "A positive", "B negative"), 1, HARD),
                Curated.of("What is the hardest natural substance?", List.of("Gold", "Iron", "Diamond", "Quartz"), 2, HARD),
                Curated.of("What is the SI unit of frequency?", List.of("Hertz", "Decibel", "Watt", "Newton"), 0, HARD),
                Curated.of("Which part of the eye controls the amount of light entering it?", List.of("Cornea", "Retina", "Iris", "Lens"), 2, HARD),
                Curated.of("How many chambers does the human heart have?", List.of("2", "3", "4", "5"), 2, HARD),
                Curated.of("Which planet has the most prominent ring system?", List.of("Jupiter", "Saturn", "Uranus", "Neptune"), 1, HARD),
                Curated.of("Which scientist is credited with the laws of motion?", List.of("Einstein", "Newton", "Galileo", "Darwin"), 1, HARD),
                Curated.of("What is the chemical symbol for Silver?", List.of("Si", "Ag", "Sv", "Au"), 1, HARD),
                Curated.of("What is the chemical symbol for Helium?", List.of("H", "He", "Hl", "Hm"), 1, HARD),
                Curated.of("Which planet is the hottest in our solar system?", List.of("Mercury", "Venus", "Mars", "Jupiter"), 1, HARD),
                Curated.of("What is the SI unit of energy?", List.of("Newton", "Watt", "Joule", "Volt"), 2, HARD),
                Curated.of("What is the SI unit of power?", List.of("Watt", "Joule", "Ampere", "Ohm"), 0, HARD),
                Curated.of("Which subatomic particle carries a negative charge?", List.of("Proton", "Neutron", "Electron", "Nucleus"), 2, HARD),
                Curated.of("Which acid is found in the human stomach?", List.of("Sulphuric acid", "Hydrochloric acid", "Nitric acid", "Acetic acid"), 1, HARD),
                Curated.of("Which is the most abundant metal in the Earth's crust?", List.of("Iron", "Aluminium", "Copper", "Gold"), 1, HARD),
                Curated.of("How many teeth does a typical adult human have?", List.of("28", "30", "32", "34"), 2, HARD),
                Curated.of("Water turning into vapour is called?", List.of("Condensation", "Evaporation", "Freezing", "Melting"), 1, HARD));
    }
}
