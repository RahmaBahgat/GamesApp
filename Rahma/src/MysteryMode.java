package Rahma.src;

import javax.sound.sampled.*;
import java.io.*;
import java.util.*;

public class MysteryMode {
    private Clip clip;

    public void play() {
        Scanner scanner = new Scanner(System.in);
        // Get the mystery clues
        Map<String, String> clues = getMysteryClues();
        List<String> words = new ArrayList<>(clues.keySet());
        // Random object to select words
        Random random = new Random();

        // Display game introduction
        System.out.println("\n🕵️‍♂️ Scramble Case Files (Sherlock Edition)! 🎩");
        System.out.println("Use the given clue to solve the case. If you struggle, you can examine the evidence.🧐");

        // Play the Sherlock theme music
        playSherlockTheme();

        while (true) {
            // Select a random word and scramble it
            String originalWord = words.get(random.nextInt(words.size()));
            String scrambledWord = Scrambling.scrambleWord(originalWord);
            String clue = clues.get(originalWord);

            // Display the clue
            System.out.println("\n Clue: " + clue);
            System.out.print(" Your guess: ");
            String guess = scanner.nextLine().toLowerCase();

            // Check if the guess is correct
            if (guess.equals(originalWord.toLowerCase())) {
                System.out.println("Right!!! The game is on… and you just played it beautifully!🎻🔎");
                // Stop the music
                stopSherlockTheme();
                // Show after-game options
                afterGameOptions(scanner);
                return;
            } else {
                // If incorrect, offer the player a chance to examine the evidence
                System.out.println("❌ Incorrect! Would you like to examine the evidence? (y/n)🤫");
                String choice = scanner.nextLine().toLowerCase();

                if (choice.equals("y")) {
                    // Show scrambled word as a hint
                    System.out.println("🔍 Scrambled Word: " + scrambledWord);
                    System.out.print("📝 Your guess: ");
                    guess = scanner.nextLine().toLowerCase();

                    // Check again if the guess is correct
                    if (guess.equals(originalWord.toLowerCase())) {
                        System.out.println("✅ Well done, Detective! Case Solved! 🕵️‍♂️");
                        // Stop the music
                        stopSherlockTheme();
                        // Show after-game options
                        afterGameOptions(scanner);
                        return;
                    } else {
                        System.out.println("💀 Case Closed! The answer was: " + originalWord);
                        // Stop the music
                        stopSherlockTheme();
                        // Show after-game options
                        afterGameOptions(scanner);
                        return;
                    }
                } else {
                    System.out.println("💀 Case Closed! The answer was: " + originalWord);
                    stopSherlockTheme();
                    afterGameOptions(scanner);
                    return;
                }
            }
        }
    }

    // Function to play Sherlock theme music
    private void playSherlockTheme() {
        try {
            File soundFile = new File("Sherlock_theme.wav"); // Load the audio file

            // Initialize the audio stream
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip(); // Create a clip object
            clip.open(audioStream); // Open the audio file
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the theme music
            clip.start(); // Start playing

        } catch (Exception e) {
            System.out.println("Error playing Sherlock theme: " + e.getMessage());
        }
    }

    // Function to stop Sherlock theme music
    private void stopSherlockTheme() {
        if (clip != null && clip.isRunning()) {
            clip.stop(); // Stop the music
            clip.close(); // Close the clip
        }
    }

    // Function to show after-game options
    private void afterGameOptions(Scanner scanner) {
        System.out.println("\n🎮 **What would you like to do next?**");
        System.out.println("1️⃣ Play again");
        System.out.println("2️⃣ Exit the game");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.println("Restarting the game...");
                play();
            }
            case 2 -> {
                System.out.println("Exiting the game. Thanks for playing! 🎩");
                System.exit(0);
            }
            default -> {
                System.out.println("⚠️ Invalid choice! Try again.");
                afterGameOptions(scanner);
            }
        }
    }

    // Function to store mystery clues and corresponding words
    private Map<String, String> getMysteryClues() {
        Map<String, String> mysteryClues = new HashMap<>();
        mysteryClues.put("detective", "A person who investigates crimes 🕵️‍♂️");
        mysteryClues.put("evidence", "Something that helps solve a mystery 🔍");
        mysteryClues.put("Sherlock", "A famous detective from 221B Baker Street 🎩");
        mysteryClues.put("Watson", "Sherlock Holmes' loyal friend and assistant 👩‍🤝‍👨");
        mysteryClues.put("Moriarty", "Sherlock Holmes’ greatest enemy 😈");
        mysteryClues.put("mystery", "An unexplained event that needs solving 🧩");
        mysteryClues.put("crime", "An illegal act that must be investigated 🔍");
        mysteryClues.put("clue", "A small detail that leads to the truth 🧐");
        mysteryClues.put("Baker", "The street where Sherlock Holmes lived 🏠");
        return mysteryClues;
    }
}
