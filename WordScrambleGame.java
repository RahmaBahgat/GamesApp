import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.sound.sampled.*;



class WordScrambleGame {
    // Reading from files
    public static List<String> readFromFile(String filename) {
        List<String> words = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                words.add(line);
            }
            bufferedReader.close();
            // Exception handling
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return words;
    }

    public static void main(String[] args) {
        // Read words from file
        List<String> words = readFromFile("fruityChaos.txt");

        // take a random word
        String originalWord = words.get(new Random().nextInt(words.size()));

        // Scramble and play
        playGame();
    }

    public static void playTickingSound() {
        try {
            // Plug in the file path
            File soundFile = new File("487724__matrixxx__ticking-timer-10-sec.wav");
            // Validate if the file exists
            if (!soundFile.exists()) {
                System.out.println("Sound file not found!");
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error playing ticking sound: " + e.getMessage());
        }
    }
    // Function to scramble a word by shuffling its letters
    public static String scrambleWord(String word) {
        // Convert the word into a list of characters
        List<Character> letters = new ArrayList<>();
        for (char c : word.toCharArray()) {
            letters.add(c);
        }

        // Shuffle the characters
        Collections.shuffle(letters);

        // Concatenate shuffled characters into a new String
        StringBuilder scrambled = new StringBuilder();
        for (char c : letters) {
            scrambled.append(c);
        }

        return scrambled.toString();
    }


    public static void timeAttackMode() {
        Scanner scanner = new Scanner(System.in);
        List<String> words = readFromFile("fruityChaos.txt");
        Random random = new Random();

        System.out.println("How many words you think you can unscramble in 30 secs?");
        System.out.println("""
        Select Difficulty:
        1 - Easy (3 words)
        2 - Medium (5 words)
        3 - Hard (7 words)
        4 - Legendary (10 words)
        """);

        int choice = scanner.nextInt();
        scanner.nextLine();

        int goal = 0;
        String difficulty = "";

        // Setting the goal
        switch (choice) {
            case 1:
                goal = 3;
                difficulty = "Easy";
                break;
            case 2:
                goal = 5;
                difficulty = "Medium";
                break;
            case 3:
                goal = 7;
                difficulty = "Hard";
                break;
            case 4:
                goal = 10;
                difficulty = "Legendary";
                break;
            default:
                System.out.println("Invalid choice. Defaulting to Easy mode.");
        }



        System.out.println("Welcome to Time Attack: Fruit Chaos! 🍉🍒");
        System.out.println("⏳ You have 30 secs to unscramble " + goal + " fruity words!");
        System.out.println(" Type fast, think faster – the clock is ticking! 🕒🔥");

        int score = 0;
        long startTime = System.currentTimeMillis();
        boolean countdownStarted = false;

        System.out.println("\nGet ready! The game starts in...");
        try {
            for (int i = 3; i > 0; i--) {
                System.out.println("⏳ " + i + "...");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Error with countdown.");
        }
        System.out.println("🎮 GO!\n");

        while (true) {
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

            if (elapsedTime >= 20 && elapsedTime < 30) {
                if (!countdownStarted) {
                    System.out.println("💣 Countdown started! Hurry up!");
                    countdownStarted = true;
                }
                System.out.println("⏳ " + (30 - elapsedTime) + " seconds left!");
                playTickingSound();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Error with countdown.");
                }
            }

            if (System.currentTimeMillis() - startTime >= 30000) {
                System.out.println("💥 BOOM! Time's up! You reached " + score + "/" + goal + ". Try again!");
                return;
            }

            String originalWord = words.get(random.nextInt(words.size()));
            String scrambledWord = scrambleWord(originalWord);

            System.out.println("Unscramble: " + scrambledWord);
            System.out.print("Your guess: ");
            String guess = scanner.nextLine().toLowerCase();

            if (guess.equals(originalWord.toLowerCase())) {
                score++;
                System.out.println("✅ Correct! Current score: " + score + "/" + goal);

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

    public static void streakMode() {
        Scanner scanner = new Scanner(System.in);
        List<String> words = readFromFile("spaceMission.txt");
        Random random = new Random();

        System.out.println(" Welcome to Space Mission Challenge! 👽🛸");
        System.out.println(" Navigate through space by solving 10 cosmic words! ");
        System.out.println(" One wrong move, and you’ll drift into a black hole! 💀🕳");
        System.out.println("""
            Select Difficulty:
            Note: Hint reveals the first letter in the word
            1 - Easy (3 hints)
            2 - Medium (2 hints)
            3 - Hard (1 hint)
            """);

        int choice = scanner.nextInt();
        scanner.nextLine();

        int hints = 0;
        int guesses = 0;
        switch (choice) {
            case 1:
                hints = 3;
                break;
            case 2:
                hints = 2;
                break;
            case 3:
                hints = 1;
                break;
            default:
                System.out.println("Invalid choice. Defaulting to Easy mode.");
        }
        System.out.println("\nGet ready! The game starts in...");
        try {
            for (int i = 3; i > 0; i--) {
                System.out.println("⏳ " + i + "...");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Error with countdown.");
        }
        System.out.println("🎮 GO!\n");

        String originalWord = words.get(random.nextInt(words.size()));
        String scrambledWord = scrambleWord(originalWord);

        while (guesses < 10) {
            System.out.println("Unscramble: " + scrambledWord);
            System.out.print("Your guess (or type 'hint'): ");
            String guess = scanner.nextLine().toLowerCase();

            if (guess.equals("hint")) {
                if (hints > 0) {
                    hints--;
                    System.out.println("🔎 The first letter is: " + originalWord.charAt(0));
                } else {
                    System.out.println("❌ No hints left!");
                }

            }
            else if (guess.equals(originalWord.toLowerCase())) {
                guesses++;
                System.out.println("✅ Correct! Current score: " + guesses + "/10");

                originalWord = words.get(random.nextInt(words.size()));
                scrambledWord = scrambleWord(originalWord);
            }
            else {
                System.out.println("❌ Incorrect! Try again.");
            }
        }

        System.out.println(" Contact NASA! We’ve found the next space genius—YOU! 🏆🚀");
    }

    public static Map<String, String> getMysteryClues() {
        Map<String, String> mysteryClues = new HashMap<>();
        mysteryClues.put("detective", "A person who investigates crimes 🕵️‍♂️");
        mysteryClues.put("evidence", "Something that helps solve a mystery 🔍");
        mysteryClues.put("Sherlock", "A famous detective from 221B Baker Street 🎩");
        mysteryClues.put("Watson", "Sherlock Holmes' loyal friend and assistant 👩‍‍🤝‍👨🏻");
        mysteryClues.put("Moriarty", "Sherlock Holmes’ greatest enemy 😈");
        mysteryClues.put("mystery", "An unexplained event that needs solving 🧩");
        mysteryClues.put("crime", "An illegal act that must be investigated 🔍");
        mysteryClues.put("clue", "A small detail that leads to the truth 🧐");
        mysteryClues.put("Baker", "The street where Sherlock Holmes lived 🏠");
        return mysteryClues;
    }

    public static void mysteryMode(){
        Scanner scanner = new Scanner(System.in);
        Map<String, String> clues = getMysteryClues();
        List<String> words = new ArrayList<>(clues.keySet());
        Random random = new Random();
        System.out.println(" Scramble Case Files (Sherlock Edition)! 🕵️‍♂️🎩");
        System.out.println(" Use the given clue to solve the case. If you struggle, you can examine the evidence.");
        String originalWord = words.get(random.nextInt(words.size()));
        String scrambledWord = scrambleWord(originalWord);
        String clue = clues.get(originalWord);
        System.out.println("\nGet ready! The game starts in...");
        try {
            for (int i = 3; i > 0; i--) {
                System.out.println("⏳ " + i + "...");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Error with countdown.");
        }
        System.out.println("🎮 GO!\n");
        System.out.println("Clue: " + clue);
        System.out.print("Your guess: ");
        String guess = scanner.nextLine().toLowerCase();

        if (guess.equals(originalWord)) {
            System.out.println(" The game is on… and you just played it beautifully! 🎻🔎");
        } else {
            System.out.println("❌ Incorrect! Would you like to **examine the evidence**? (y/n)");
            String choice = scanner.nextLine().toLowerCase();

            if (choice.equals("y")) {
                System.out.println("Scrambled Word: " + scrambledWord);
                System.out.print("Your guess: ");
                guess = scanner.nextLine().toLowerCase();

                if (guess.equals(originalWord)) {
                    System.out.println("✅ Well done, Detective! Case Solved! 🕵️‍♂️");
                } else {
                    System.out.println("💀 Case Closed! The answer was: " + originalWord);
                }
            } else {
                System.out.println("💀 Case Closed! The answer was: " + originalWord);
            }
        }


    }




    public static void playGame () {
            Scanner in = new Scanner(System.in);
            System.out.println("〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰\n");
            System.out.println(" 🧩 Welcome to the Word Scramble Game!\n");
            System.out.println("〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰〰\n");
            System.out.println("""
                    Which mode would you like to play?
                    1. Time Attack: Fruit Chaos
                    2. Streak: Space Mission
                    3. Mystery: Scramble Case Files
                    """);
            int mode = in.nextInt();
            if (mode == 1) {
                timeAttackMode();
            } else if (mode == 2) {
                streakMode();
            } else if (mode == 3) {
                mysteryMode();
            }
    }
    }


