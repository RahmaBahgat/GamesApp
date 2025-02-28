package words_app;

import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static words_app.TimeAttackMode.readTextFile;
public class StreakMode {
    public void play() {
        Scanner scanner = new Scanner(System.in);
        List<String> words = readTextFile("spaceMission.txt");
        Random random = new Random();
        int correctGuesses = 0;

        System.out.println("\ud83d\ude80 Welcome to Space Mission Challenge! \ud83d\udc7d\ud83d\udef8");
        System.out.println(" Navigate through space by solving cosmic words! ");
        System.out.println(" One wrong move, and you’ll drift into a black hole! \ud83d\udc80\ud83d\udd73");

        System.out.println("""
            \ud83c\udf1f Select Difficulty:
            (Hint reveals the first letter)
            1️⃣ Easy (3 hints)
            2️⃣ Medium (2 hints)
            3️⃣ Hard (1 hint)
            """);

        int choice = scanner.nextInt();
        scanner.nextLine();
        int hints = switch (choice) {
            case 1 -> 3;
            case 2 -> 2;
            case 3 -> 1;
            default -> {
                System.out.println("Invalid choice. Defaulting to Easy mode.");
                yield 3;
            }
        };

        System.out.println("\n\ud83d\ude80 Get ready! The game starts in...");
        try {
            for (int i = 3; i > 0; i--) {
                System.out.println("\u23f3 " + i + "...");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Error with countdown.");
        }
        System.out.println("\ud83d\udd25 GO! \ud83d\udd25\n");

        while (correctGuesses < 10 && !words.isEmpty()) {
            int index = random.nextInt(words.size());
            String originalWord = words.remove(index);
            String scrambledWord = Scrambling.scrambleWord(originalWord);

            System.out.println("Unscramble: " + scrambledWord);
            System.out.print("Your guess (or type 'hint'): ");
            String guess = scanner.nextLine().toLowerCase();

            if (guess.equals("hint")) {
                if (hints > 0) {
                    hints--;
                    System.out.println("\ud83d\udd0e The first letter is: " + originalWord.charAt(0));
                    System.out.print("Your guess: ");
                    guess = scanner.nextLine().toLowerCase();
                } else {
                    System.out.println("❌ No hints left!");
                }
            }

            if (guess.equals(originalWord.toLowerCase())) {
                System.out.println("✅ Correct!");
                correctGuesses++;
            } else {
                System.out.println("\ud83d\udc80 Wrong move! You've drifted into a black hole... Game Over!");
                afterGameOptions(scanner, words);
                return;
            }
        }

        if (correctGuesses == 10) {
            System.out.println(" Contact NASA! We’ve found the next space genius—YOU! \ud83c\udfc6\ud83d\ude80");
        }

        writeToFile("spaceMission.txt", words);
        afterGameOptions(scanner, words);
    }

    private void afterGameOptions(Scanner scanner, List<String> words) {
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
                System.out.println("Exiting the game. Thanks for playing! \ud83d\ude80");
                System.exit(0);
            }
            default -> {
                System.out.println("⚠️ Invalid choice! Try again.");
                afterGameOptions(scanner, words);
            }
        }
    }

    private void writeToFile(String filename, List<String> words) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (String word : words) {
                writer.write(word + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }


}