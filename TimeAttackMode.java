package Rahma.src;

import java.util.*;
import javax.sound.sampled.*;
import java.io.*;

public class TimeAttackMode {
    // Clip object to handle ticking sound
    private Clip tickingClip;
    // Flag to show welcome message only once
    boolean isFirstTime = true;

    public void play() {
        // Display the welcome message only for the first time
        if (isFirstTime) {
            System.out.println("\n Welcome to Time Attack: Fruit Chaos! 🍉🍒");
            System.out.println("⏳ You have 30 seconds to unscramble fruity words!");
            System.out.println(" Type fast, think faster – **the clock is ticking!** 🕒🔥");
            isFirstTime = false;
        }

        Scanner scanner = new Scanner(System.in);
        // Load words from file
        List<String> words = readFromFile("fruityChaos.txt");
        // Random object for word selection
        Random random = new Random();

        int goal = 0;
        String difficulty = "";
        String nextDifficulty = "";

        // Ask the user to select difficulty level
        System.out.println("How many words do you think you can unscramble in 30 secs?");
        System.out.println("""
        🌟 Select Difficulty:
        1️⃣ Easy (3 words)
        2️⃣ Medium (5 words)
        3️⃣ Hard (7 words)
        4️⃣ Legendary (10 words)
        """);

        int choice = scanner.nextInt();
        scanner.nextLine();

        // Set difficulty level based on user input
        switch (choice) {
            case 1 -> {
                goal = 3;
                difficulty = "Easy";
                nextDifficulty = "Medium";
            }
            case 2 -> {
                goal = 5;
                difficulty = "Medium";
                nextDifficulty = "Hard";
            }
            case 3 -> {
                goal = 7;
                difficulty = "Hard";
                nextDifficulty = "Legendary";
            }
            case 4 -> {
                goal = 10;
                difficulty = "Legendary";
                nextDifficulty = "No Higher Level";
            }
            default -> {
                System.out.println("Invalid choice. Defaulting to Easy mode.");
                goal = 3;
                difficulty = "Easy";
                nextDifficulty = "Medium";
            }
        }

        // Track the player's score
        int score = 0;
        // Start the game timer
        long startTime = System.currentTimeMillis();
        // Track if countdown message has started
        boolean countdownStarted = false;

        // Countdown before starting the game
        System.out.println("\n🎮 Get ready! The game starts in...");
        try {
            for (int i = 3; i > 0; i--) {
                System.out.println("⏳ " + i + "...");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Error with countdown.");
        }
        System.out.println("GO!\n");

        //boolean won = false; // Flag to track if player has won

        // Main game loop
        while (true) {
            // Calculate elapsed time
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

            // Start ticking sound at 20 seconds
            if (elapsedTime >= 20 && elapsedTime < 30 && tickingClip == null) {
                if (!countdownStarted) {
                    System.out.println("💣 Countdown started! Hurry up!");
                    countdownStarted = true;
                }
                System.out.println("⏳ " + (30 - elapsedTime) + " seconds left!");
                // Start ticking sound
                tickingClip = playTickingSound();
            }

            // End game when 30 seconds pass
            if (elapsedTime >= 30) {
                System.out.println("\n💥 Time's up! You reached " + score + "/" + goal + ".");
                // Stop ticking sound
                stopTickingSound();
                // Show after-game options
                AfterOptions(scanner);
                return;
            }


            // Select and scramble a word
            int index = random.nextInt(words.size());
            String originalWord = words.get(index);
            String scrambledWord = Scrambling.scrambleWord(originalWord);

            System.out.println("Unscramble: " + scrambledWord);
            System.out.print("Your guess: ");
            String guess = scanner.nextLine().toLowerCase();

            // Check if the player's guess is correct
            if (guess.equals(originalWord.toLowerCase())) {
                score++;
                // Remove word after being guessed correctly
                words.remove(index);
                System.out.println("✅ Correct! Current score: " + score + "/" + goal);

                // If goal is reached, the player wins
                if (score >= goal) {
                    if (!difficulty.equals("Legendary")) {
                        System.out.println("Wow! You've completed " + difficulty + " Mode in " + elapsedTime + " seconds!");
                        System.out.println("You should give the higher level a try!🤩");
                    }
                    else{
                        System.out.println("Wow! You've completed " + difficulty + " Mode in " + elapsedTime + " seconds!");
                        System.out.println("You are a game expert!!!🤯");
                    }

                    return;
                }
            } else {
                System.out.println("❌ Incorrect! Try again.");
            }
        }
    }

    // Function to play ticking sound
    private Clip playTickingSound() {
        try {
            File soundFile = new File("487724__matrixxx__ticking-timer-10-sec.wav");
            if (!soundFile.exists()) {
                System.out.println("Sound file not found!");
                return null;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            return clip;
        } catch (Exception e) {
            System.out.println("Error playing ticking sound: " + e.getMessage());
            return null;
        }
    }

    // Function to stop ticking sound
    private void stopTickingSound() {
        if (tickingClip != null) {
            tickingClip.stop();
            tickingClip.close();
            tickingClip = null;
        }
    }

    // Function to show after-game options
    private void AfterOptions(Scanner scanner) {
        System.out.println("\n🎮 What would you like to do next?");
        System.out.println("1️⃣ Play again");
        System.out.println("2️⃣ Exit the game");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.println("Restarting the game ...");
                play();
            }
            case 2 -> {
                System.out.println("Exiting the game. Thanks for playing!");
                System.exit(0);
            }
            default -> {
                System.out.println("⚠️ Invalid choice! Try again.");
                AfterOptions(scanner);
            }
        }
    }

    // Function to read words from a file
    private List<String> readFromFile(String filename) {
        List<String> words = new ArrayList<>();
        try {
            Scanner fileScanner = new Scanner(new File(filename));
            while (fileScanner.hasNextLine()) {
                words.add(fileScanner.nextLine().trim());
            }
            fileScanner.close();
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return words;
    }
}
