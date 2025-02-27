package words_app;

import java.util.*;
import javax.sound.sampled.*;
import java.io.*;

public class HeistLettersMode {
    // Flag to check if siren has started
    private boolean sirenStarted = false;
    private Clip sirenClip;
    // Map of letter sets to possible words
    private final Map<String, Set<String>> letterWordMap = createLetterWordMap();

    public void play() {
        Scanner scanner = new Scanner(System.in);

        // Select a random letter set
        List<String> keys = new ArrayList<>(letterWordMap.keySet());
        String randomLetters = keys.get(new Random().nextInt(keys.size()));
        Set<String> possibleWords = new HashSet<>(letterWordMap.get(randomLetters));

        // Display the game introduction
        System.out.println("\n Welcome to Heist Mode! 🤑");
        System.out.println(" Form as many words as possible before the police arrive! 🚔");
        System.out.println(" You have 45 seconds! Start typing words when the countdown ends! ⏳");

        int score = 0; // Initialize player score
        long startTime = System.currentTimeMillis(); // Start the game timer
        boolean countdownStarted = false; // Track if countdown message has started

        // Countdown before the game starts
        System.out.println("\n🎮 Get ready! The heist starts in...");
        try {
            for (int i = 3; i > 0; i--) {
                System.out.println("⏳ " + i + "...");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Error with countdown.");
        }

        // Display the random letters for word formation
        System.out.println("\nGO!\n");
        System.out.println(" Use these letters: " + randomLetters.toUpperCase());

        // Main game loop
        while (true) {
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000; // Calculate elapsed time

            // Display warning message at 30 seconds
            if (elapsedTime >= 30 && elapsedTime < 35 && !countdownStarted) {
                System.out.println("The police are coming! Hurry up! ☠");
                countdownStarted = true;
            }

            // Play siren sound from 35 to 45 seconds
            if (elapsedTime >= 35 && elapsedTime < 45) {
                if (!sirenStarted) {
                    System.out.println("The cops are coming! Grab more words! 🚨");
                    playSirenSound();
                    sirenStarted = true;
                }
            }

            // End the game at 45 seconds
            if (elapsedTime >= 45) {
                stopSiren(); // Stop the siren
                System.out.println("You've been caught! 💀");
                for (int i = 0; i < 5; i++) {
                    System.out.println("\033[31m💀 WASTED 💀\033[0m");// Red text effect

                }
                afterOptions(scanner); // Show after-game options
                return;
            }

            // Take user input for words
            System.out.print("💬 Your word: ");
            String userWord = scanner.nextLine().toLowerCase();

            // Check if word is valid
            if (possibleWords.contains(userWord)) {
                score++;
                possibleWords.remove(userWord); // Remove word after being guessed
                System.out.println("✅ Word accepted! Score: " + score);
            } else {
                System.out.println("❌ Invalid word! Try again.");
            }
        }
    }

    // Function to play police siren sound
    private void playSirenSound() {
        try {
            File soundFile = new File("police_siren.wav"); // Load the siren sound file

            // Check if the file exists
            if (!soundFile.exists()) {
                System.out.println("Sound file not found!");
                return;
            }

            // Initialize the audio stream
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            sirenClip = AudioSystem.getClip();
            sirenClip.open(audioStream);
            sirenClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.out.println("Error playing siren sound: " + e.getMessage());
        }
    }

    // Function to stop police siren sound
    private void stopSiren() {
        if (sirenClip != null && sirenClip.isRunning()) {
            sirenClip.stop(); // Stop the siren
            sirenClip.close(); // Close the clip
        }
    }

    // Function to show after-game options
    private void afterOptions(Scanner scanner) {
        System.out.println("\n🎮 **What would you like to do next?**");
        System.out.println("1️⃣ Play Again");
        System.out.println("2️⃣ Exit the game");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.println(" Restarting Heist Mode...");
                play();
            }
            case 2 -> {
                System.out.println(" Exiting the game. Thanks for playing! 🙌");
                System.exit(0);
            }
            default -> {
                System.out.println("⚠️ Invalid choice! Try again.");
                afterOptions(scanner);
            }
        }
    }

    // Function to manually create the letter-to-words map
    private static Map<String, Set<String>> createLetterWordMap() {
        Map<String, Set<String>> map = new HashMap<>();

        // Manually defined letter sets and their possible words
        map.put("raept", new HashSet<>(Arrays.asList("part", "rate", "tear", "peat", "pear", "trap", "pet")));
        map.put("gimnpu", new HashSet<>(Arrays.asList("pig", "gum", "jump", "mug", "pug", "ping")));
        map.put("tacrs", new HashSet<>(Arrays.asList("car", "star", "scar", "cast", "cart", "arts", "cat")));
        map.put("dlohw", new HashSet<>(Arrays.asList("hold", "howl", "low", "who", "owl")));
        map.put("snipg", new HashSet<>(Arrays.asList("sign", "pins", "spin", "sip", "sin")));
        map.put("chear", new HashSet<>(Arrays.asList("reach", "race", "care", "ache", "char")));
        map.put("odlke", new HashSet<>(Arrays.asList("lode", "old", "dole", "led", "ode")));
        map.put("lstei", new HashSet<>(Arrays.asList("tiles", "lite", "site", "set", "tie")));
        map.put("bnlate", new HashSet<>(Arrays.asList("table", "bleat", "belt", "beat", "net", "let")));
        map.put("voiln", new HashSet<>(Arrays.asList("violin", "lion", "loin", "oil", "nil")));

        return map;
    }
}
