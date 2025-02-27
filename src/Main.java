package Rahma.src;

import java.util.*;

class WordScrambleGame {
    public static void main(String[] args) {
        playGame(); // Start the game
    }

    public static void playGame() {
        Scanner in = new Scanner(System.in);

        // Display a decorative welcome message
        System.out.println("▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪");
        System.out.println("   🧩 Welcome to the Word Scramble Game! ");
        System.out.println("▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪\n");

        // Game mode selection menu
        System.out.println("Select a Game Mode:\n");
        System.out.println("1️⃣ Time Attack: Fruit Chaos 🍒🍓");
        System.out.println("   Solve as many scrambled fruit names as possible within 30 seconds! ⏳");
        System.out.println("2️⃣ Streak Mode: Cosmic Countdown 👽");
        System.out.println("   Keep a streak of correct answers—one wrong move and you're lost in space! 🚀");
        System.out.println("3️⃣ Mystery Mode: Sher[LOCK]ED Files 🕵️");
        System.out.println("   Solve scrambled crime-related words using mysterious clues. 🔍");
        System.out.println("4️⃣ Heist Mode: The Ultimate Word Heist 🤑");
        System.out.println("   Form as many words as possible before the cops arrive! 🚨");

        System.out.print("\nEnter the number of the mode you want to play: ");
        int mode = in.nextInt(); // Read user choice

        // Start the selected game mode
        if (mode == 1) {
            new TimeAttackMode().play();
        } else if (mode == 2) {
            new StreakMode().play();
        } else if (mode == 3) {
            new MysteryMode().play();
        } else if (mode == 4) {
            new HeistLettersMode().play();
        } else {
            System.out.println("\n Invalid choice! Please restart the game and select a valid mode.");
        }
    }
}
