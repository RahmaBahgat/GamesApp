package words_app;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.Scanner;

class WordScrambleGame {
    WordScrambleGame() {
    }

    public static void main(String[] args) {
        playGame();
    }

    public static void playGame() {
        Scanner in = new Scanner(System.in);
        System.out.println("▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪");
        System.out.println("   \ud83e\udde9 Welcome to the Word Scramble Game! ");
        System.out.println("▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪\n");
        System.out.println("Select a Game Mode:\n");
        System.out.println("1️⃣ Time Attack: Fruit Chaos \ud83c\udf52\ud83c\udf53");
        System.out.println("   Solve as many scrambled fruit names as possible within 30 seconds! ⏳");
        System.out.println("2️⃣ Streak Mode: Cosmic Countdown \ud83d\udc7d");
        System.out.println("   Keep a streak of correct answers—one wrong move and you're lost in space! \ud83d\ude80");
        System.out.println("3️⃣ Mystery Mode: Sher[LOCK]ED Files \ud83d\udd75️");
        System.out.println("   Solve scrambled crime-related words using mysterious clues. \ud83d\udd0d");
        System.out.println("4️⃣ Heist Mode: The Ultimate Word Heist \ud83e\udd11");
        System.out.println("   Form as many words as possible before the cops arrive! \ud83d\udea8");
        System.out.print("\nEnter the number of the mode you want to play: ");
        int mode = in.nextInt();
        if (mode == 1) {
            (new TimeAttackMode()).play();
        } else if (mode == 2) {
            (new StreakMode()).play();
        } else if (mode == 3) {
            (new MysteryMode()).play();
        } else if (mode == 4) {
            (new HeistLettersMode()).play();
        } else {
            System.out.println("\n Invalid choice! Please restart the game and select a valid mode.");
        }

    }
}
