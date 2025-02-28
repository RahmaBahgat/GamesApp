package words_app;

import java.util.*;
import javax.sound.sampled.*;
import static words_app.TimeAttackMode.playSound;

public class HeistLettersMode {
    private boolean sirenStarted = false;
    private Clip sirenClip;
    private final Map<String, Set<String>> letterWordMap = createLetterWordMap();

    public void play() {
        Scanner scanner = new Scanner(System.in);
        List<String> keys = new ArrayList<>(letterWordMap.keySet());
        String randomLetters = keys.get(new Random().nextInt(keys.size()));
        Set<String> possibleWords = new HashSet<>(letterWordMap.get(randomLetters));

        System.out.println("\n Welcome to Heist Mode! 🤑");
        System.out.println(" Form 4 words before the police arrive! 🚔");
        System.out.println(" You have 45 seconds! Start typing words when the countdown ends! ⏳");

        int score = 0;
        long startTime = System.currentTimeMillis();
        boolean countdownStarted = false;

        System.out.println("\n🎮 Get ready! The heist starts in...");
        try {
            for (int i = 3; i > 0; i--) {
                System.out.println("⏳ " + i + "...");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Error with countdown.");
        }

        System.out.println("\nGO!\n");
        System.out.println(" Use these letters: " + randomLetters.toUpperCase());

        while (true) {
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

            if (elapsedTime >= 30 && elapsedTime < 35 && !countdownStarted) {
                System.out.println("The police are coming! Hurry up! ☠");
                countdownStarted = true;
            }

            if (elapsedTime >= 35 && elapsedTime < 45) {
                if (!sirenStarted) {
                    System.out.println("The cops are coming! Grab more words! 🚨");
                    playSound("police_siren.wav");
                    sirenStarted = true;
                }
            }

            if (elapsedTime >= 45) {
                stopSiren();
                if (score > 4) {
                    System.out.println("🎉 You escaped successfully with a score of " + score + "! 🏆");
                } else {
                    System.out.println("You've been caught! 💀");
                    for (int i = 0; i < 5; i++) {
                        System.out.println("\033[31m💀 WASTED 💀\033[0m");
                    }
                }
                afterOptions(scanner);
                return;
            }

            System.out.print("💬 Your word: ");
            String userWord = scanner.nextLine().toLowerCase();

            if (possibleWords.contains(userWord)) {
                score++;
                possibleWords.remove(userWord);
                System.out.println("✅ Word accepted! Score: " + score);
            } else {
                System.out.println("❌ Invalid word! Try again.");
            }
        }
    }


    private void stopSiren() {
        if (sirenClip != null && sirenClip.isRunning()) {
            sirenClip.stop();
            sirenClip.close();
        }
    }

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

    private static Map<String, Set<String>> createLetterWordMap() {
        Map<String, Set<String>> map = new HashMap<>();
        map.put("raept", new HashSet<>(Arrays.asList("part", "rate", "tear", "peat", "pear", "trap", "pet", "rapt")));
        map.put("gimnpu", new HashSet<>(Arrays.asList("pig", "gum", "jump", "mug", "pug", "ping", "imp")));
        map.put("tacrs", new HashSet<>(Arrays.asList("car", "star", "scar", "cast", "cart", "arts", "cat", "rats")));map.put("dlohw", new HashSet<>(Arrays.asList("hold", "howl", "low", "who", "owl", "old")));
        map.put("snipg", new HashSet<>(Arrays.asList("sign", "pins", "spin", "sip", "sin", "ping")));
        map.put("chear", new HashSet<>(Arrays.asList("reach", "race", "care", "ache", "char", "arch")));
        map.put("odlke", new HashSet<>(Arrays.asList("lode", "old", "dole", "led", "ode", "doe")));
        map.put("lstei", new HashSet<>(Arrays.asList("tiles", "lite", "site", "set", "tie", "isle")));
        map.put("bnlate", new HashSet<>(Arrays.asList("table", "bleat", "belt", "beat", "net", "let", "bane")));
        map.put("voiln", new HashSet<>(Arrays.asList("violin", "lion", "loin", "oil", "nil", "vino")));
        return map;
    }
}
