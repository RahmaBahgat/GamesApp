package words_app;

import java.util.*;
import java.io.File;

public class StreakMode {
    public void play() {
        Scanner scanner = new Scanner(System.in);
        List<String> words = readFromFile("words_app/src/spaceMission.txt");
        Random random = new Random();

        // Display the game introduction
        System.out.println("🚀 Welcome to Space Mission Challenge! 👽🛸");
        System.out.println(" Navigate through space by solving cosmic words! ");
        System.out.println(" One wrong move, and you’ll drift into a black hole! 💀🕳");

        // Display difficulty selection
        System.out.println("""
            🌟 Select Difficulty:
            (Hint reveals the first letter)
            1️⃣ Easy (3 hints)
            2️⃣ Medium (2 hints)
            3️⃣ Hard (1 hint)
            """);

        // Read the difficulty choice
        int choice = scanner.nextInt();
        scanner.nextLine();

        int hints = 0;
        switch (choice) {
            case 1 -> hints = 3; // Easy mode: 3 hints
            case 2 -> hints = 2; // Medium mode: 2 hints
            case 3 -> hints = 1; // Hard mode: 1 hint
            default -> {
                System.out.println("Invalid choice. Defaulting to Easy mode.");
                hints = 3;
            }
        }

        // Countdown before the game starts
        System.out.println("\n🚀 Get ready! The game starts in...");
        try {
            for (int i = 3; i > 0; i--) {
                System.out.println("⏳ " + i + "...");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Error with countdown.");
        }
        System.out.println("🔥 GO! 🔥\n");

        // Select and scramble the first word
        int index = random.nextInt(words.size());
        String originalWord = words.get(index);
        String scrambledWord = Scrambling.scrambleWord(originalWord);

        // Display the scrambled word
        System.out.println("Unscramble: " + scrambledWord);
        System.out.print("Your guess (or type 'hint'): ");
        String guess = scanner.nextLine().toLowerCase();

        // Handle hints
        if (guess.equals("hint")) {
            if (hints > 0) {
                hints--;
                System.out.println("🔎 The first letter is: " + originalWord.charAt(0));
                System.out.print("Your guess: ");
                guess = scanner.nextLine().toLowerCase();
            } else {
                System.out.println("❌ No hints left!");
            }
        }

        // Check if the guess is correct
        if (guess.equals(originalWord.toLowerCase())) {
            System.out.println("✅ Correct!");
        } else {
            System.out.println("💀 Wrong move! You've drifted into a black hole... Game Over!");
            afterGameOptions(scanner); // Show after-game options
            return;
        }

        // If player wins, show after-game options
        System.out.println(" Contact NASA! We’ve found the next space genius—YOU! 🏆🚀");
        afterGameOptions(scanner);
    }

    // Function to display after-game options
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
                System.out.println("Exiting the game. Thanks for playing! 🚀");
                System.exit(0);
            }
            default -> {
                System.out.println("⚠️ Invalid choice! Try again.");
                afterGameOptions(scanner);
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
