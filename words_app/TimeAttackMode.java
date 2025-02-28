package words_app;

import java.util.*;
import javax.sound.sampled.*;
import java.io.*;

public class TimeAttackMode {
    private Clip tickingClip;
    private boolean isFirstTime = true;

    public void play() {
        if (isFirstTime) {
            System.out.println("\n Welcome to Time Attack: Fruit Chaos! 🍉🍒");
            System.out.println("⏳ You have 30 seconds to unscramble fruity words!");
            System.out.println(" Type fast, think faster – the clock is ticking! 🕒🔥");
            isFirstTime = false;
        }

        Scanner scanner = new Scanner(System.in);
        List<String> words = readTextFile("fruityChaos.txt");
        if (words.isEmpty()) {
            System.out.println("Error: No words found in the file!");
            return;
        }

        Random random = new Random();
        int goal = 0;
        String difficulty = "";

        System.out.println("How many words do you think you can unscramble in 30 secs?");
        System.out.println("""
        🌟 Select Difficulty:
        1⃣ Easy (3 words)
        2⃣ Medium (5 words)
        3⃣ Hard (7 words)
        4⃣ Legendary (10 words)
        """);

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                goal = 3;
                difficulty = "Easy";
            }
            case 2 -> {
                goal = 5;
                difficulty = "Medium";
            }
            case 3 -> {
                goal = 7;
                difficulty = "Hard";
            }
            case 4 -> { goal = 10;
                difficulty = "Legendary";
            }
            default -> {
                System.out.println("Invalid choice. Defaulting to Easy mode.");
                goal = 3;
                difficulty = "Easy";
            }
        }

        int score = 0;
        long startTime = System.currentTimeMillis();
        boolean countdownStarted = false;

        System.out.println("\n🎮 Get ready! The game starts in...");
        try {
            for (int i = 3; i > 0; i--) {
                System.out.println("⏳ " + i + "...");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Error with countdown.");
        }
        System.out.println("🔥 GO! 🔥\n");

        while (true) {
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

            if (elapsedTime >= 20 && elapsedTime < 30 && tickingClip == null) {
                if (!countdownStarted) {
                    System.out.println("💣 Countdown started! Hurry up!");
                    countdownStarted = true;
                }
                System.out.println("⏳ " + (30 - elapsedTime) + " seconds left!");
                playSound("487724__matrixxx__ticking-timer-10-sec.wav");
            }

            if (elapsedTime >= 30) {
                System.out.println("\n💥 Time's up! You reached " + score + "/" + goal + ".");
                stopTickingSound();
                AfterOptions(scanner);
                return;
            }

            int index = random.nextInt(words.size());
            String originalWord = words.get(index);
            String scrambledWord = Scrambling.scrambleWord(originalWord);

            System.out.println("Unscramble: " + scrambledWord);
            System.out.print("Your guess: ");
            String guess = scanner.nextLine().toLowerCase();

            if (guess.equals(originalWord.toLowerCase())) {
                score++;
                words.remove(index);
                System.out.println("✅ Correct! Current score: " + score + "/" + goal);

                if (score >= goal) {
                    stopTickingSound();
                    System.out.println("🎉 Wow! You've completed " + difficulty + " Mode in " + elapsedTime + " seconds!");
                    AfterOptions(scanner);
                    return;
                }
            } else {
                System.out.println("❌ Incorrect! Try again.");
            }
        }
    }

    public static List<String> readTextFile(String fileName) {
        List<String> words = new ArrayList<>();
        try {
            InputStream inputStream = TimeAttackMode.class.getResourceAsStream(fileName);
            if (inputStream == null) {
                System.err.println("Error: File not found - " + fileName);
                return words;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim());
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return words;
    }

    public static void playSound(String soundFile) {
        try {
            InputStream audioSrc = TimeAttackMode.class.getResourceAsStream(soundFile);
            if (audioSrc == null) {
                System.err.println("Error: Sound file not found - " + soundFile);
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioSrc);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopTickingSound() {
        if (tickingClip != null && tickingClip.isRunning()) {
            tickingClip.stop();
            tickingClip.close();
            tickingClip = null;
        }
    }

    private void AfterOptions(Scanner scanner) {
        System.out.println("\n🎮 What would you like to do next?");
        System.out.println("1⃣ Play again");
        System.out.println("2⃣ Exit the game");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.println("Restarting the game...");
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
}