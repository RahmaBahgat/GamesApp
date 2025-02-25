package games_app;
//// import all the needed
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MemoryGame {
    public List<String> cards;
    public String response = "Y";
    ////matchedSet is a set of integers that stores the indices of matched cards.
    //// It prevents the players or computer from selecting already matched cards.
    public Set<Integer> matchedSet;
    public int size, playermoves,friendmoves, matches,friendScore,playerScore ,randomScore,wrong,pairs;
    public String bonusEmoji = "🍀";
    public Random rand = new Random();
    public static Scanner input = new Scanner(System.in);
    public boolean isPlayerTurn, playWithFriend,playAgainstRandom;
    public String[] availableEmojis = {"👽", "🐸", "🦚", "🐬", "💎", "🍏", "🌍", "💙", "🥑", "🧙", "🚙"};

    public void initializeGame() {
        //// Reset variables for a new game
        playerScore = 0;
        friendScore = 0;
        randomScore = 0;
        playermoves = 0;
        friendmoves = 0;
        matches = 0;
        wrong = 0;
        size = 0;
        //// Choose board size
        System.out.print("Choose board size: (10, 15, or 25): ");
        size = input.nextInt();
        while (size != 10 &&  size != 25 && size != 15) {
            System.out.println("Invalid choice. Try again :)");
            System.out.print("Choose board size: (10, 15, or 25): ");
            size = input.nextInt();
        }
        cards = new ArrayList<>();
        matchedSet = new HashSet<>();
        ////setting the cards
        pairs = (size == 10) ? 5 : (size == 15 ? 7 : 11);

        for (int i = 0; i < pairs; i++) {
            cards.add(availableEmojis[i]);
            cards.add(availableEmojis[i]);
        }
        //// Add bonus cards if size is 15 or 25
        if (size > 10) {
            System.out.println("There are bonus cards that give you 3 points in this game ... try to catch them (✿◠‿◠)");
            for (int i = 0; i < (size == 15 ? 1 : 3); i++) {
                cards.add(bonusEmoji);
            }
        }

        //// Randomly shuffle the cards
        Collections.shuffle(cards);

        ////set the play mode
        String userInput;
        do {
            System.out.print("1- against a friend \n2- against computer\n3- solo \n");
            System.out.print("Choose an option (1/2/3): ");
            userInput = input.next();
        } while (!userInput.equals("1") && !userInput.equals("2") && !userInput.equals("3"));

        playAgainstRandom = userInput.equals("2");
        playWithFriend = userInput.equals("1");
        isPlayerTurn = true;
    }
    ////customizing the look of the game board
    public void printBoard() {
        for (int i = 0; i < cards.size(); i++) {
            if (matchedSet.contains(i + 1)) {
                System.out.printf(Locale.US,"| %-3s", cards.get(i));
            }
            else {
                System.out.printf(Locale.US,"| %-3d", i + 1);
            }
            if ((i + 1) % 5 == 0 || i == cards.size() - 1) System.out.println();
        }
    }
    ////check validation and doing a turn
    public void playTurn(int card1, int card2) {
        if (card1 == card2 || card1 < 1 || card1 > size || card2 < 1 || card2 > size || matchedSet.contains(card1) || matchedSet.contains(card2)) {
            System.out.println("Invalid choice. Try again :)");
            return;
        }

        System.out.println("Card 1: " + cards.get(card1 - 1));
        System.out.println("Card 2: " + cards.get(card2 - 1));

        if (cards.get(card1 - 1).equals(bonusEmoji) || cards.get(card2 - 1).equals(bonusEmoji)) {
            System.out.println("Bonus card! +3 points");
            if (playWithFriend) {
                if (isPlayerTurn) playerScore += 3;
                else friendScore += 3;
            }
            else if (playAgainstRandom){
                if (isPlayerTurn) playerScore += 3;
                else randomScore += 3;
            }
            else playerScore += 3;

            if(cards.get(card1 - 1).equals(bonusEmoji))  matchedSet.add(card1);
            else  matchedSet.add(card2);
        }
        else if (cards.get(card1 - 1).equals(cards.get(card2 - 1))) {
            System.out.println("It's a match! +1 point");
            matchedSet.add(card1);
            matchedSet.add(card2);
            matches++;
            if (playWithFriend) {
                if (isPlayerTurn) playerScore += 1;
                else friendScore += 1;
            } else if (playAgainstRandom) {
                if (isPlayerTurn) playerScore += 1;
                else randomScore += 1;
            } else playerScore += 1;
        }
        else {
            System.out.println("Not a match. -1 point");
            if (playWithFriend) {
                if (isPlayerTurn) playerScore -=1;
                else friendScore -=1;
            }
            else if (playAgainstRandom){
                if (isPlayerTurn) playerScore -=1;
                else randomScore -=1;
            }
            else playerScore -=1; wrong++;
        }

        if (playWithFriend) {
            if (isPlayerTurn) playermoves++;
            else friendmoves++;
        } else {
            playermoves++;
        }

        isPlayerTurn = !isPlayerTurn;  // Switch turns after each round
    }
    ////playing alone mode
    public void playSolo() {
        while (matches < pairs) {
            printBoard();
            System.out.print("Enter first card (1-" + size + "): ");
            int card1 = input.nextInt();
            System.out.print("Enter second card (1-" + size + "): ");
            int card2 = input.nextInt();
            playTurn(card1, card2);
        }
        printGameOver();
    }
    ////playing with friend mode
    public void playWithFriend() {
        while (matches < pairs) {
            printBoard();
            System.out.print((isPlayerTurn ? "Player 1" : "Player 2") + ", enter first card (1-" + size + "): ");
            int card1 = input.nextInt();
            System.out.print((isPlayerTurn ? "Player 1" : "Player 2") + ", enter second card (1-" + size + "): ");
            int card2 = input.nextInt();

            playTurn(card1, card2);
        }
        printGameOver();
    }
    ////playing with computer mode
    public void playAgainstComputer() {
        while (matches < pairs) {
            printBoard();
            int card1, card2;
            if (isPlayerTurn) {
                System.out.print("Enter first card (1-" + size + "): ");
                card1 = input.nextInt();
                System.out.print("Enter second card (1-" + size + "): ");
                card2 = input.nextInt();
            }
            else {
                ////making random move
                card1 = rand.nextInt(size) + 1;
                card2 = rand.nextInt(size) + 1;
                while (card1 == card2 || matchedSet.contains(card1) || matchedSet.contains(card2)) {
                    card1 = rand.nextInt(size) + 1;
                    card2 = rand.nextInt(size) + 1;
                }
                System.out.println("Computer is making a move...\n");
                System.out.println("Random player chose: " + card1 + " & " + card2);
            }
            playTurn(card1, card2);
        }
        printGameOver();
    }
    public static void playSound(String soundFile) {
        try {
            File file = new File(soundFile);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        }
        ////make sure that the sound is there and usable
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    ////displaying the end result of the game
    public void printGameOver() {
        printBoard();
        System.out.println("\nGame over!");

        if (playAgainstRandom) {  // Case: Playing against a random player
            System.out.println("Player score: " + playerScore);
            System.out.println("Random player score: " + randomScore);

            if (playerScore < randomScore) {
                new Thread(() -> playSound("beated-by-a-computer-by-tromosm-281034.wav")).start();
                System.out.println("You lost ಥ_ಥ");
            }
            else if (playerScore > randomScore) {
                new Thread(() -> playSound("goodresult-82807.wav")).start();
                System.out.println("You win ☜(⌒▽⌒)☞");
            }
            else {
                new Thread(() -> playSound("game-bonus-144751.wav")).start();
                System.out.println("It's a draw! (¬‿¬)");
            }
        }
        else if (playWithFriend) {
            System.out.println("\nFriend Mode Results:");
            System.out.println("Player 1 score: " + playerScore + " in " + playermoves + " moves.");
            System.out.println("Player 2 score: " + friendScore + " in " + friendmoves + " moves.");

            if (playerScore > friendScore) {
                new Thread(() -> playSound("goodresult-82807.wav")).start();
                System.out.println("Player 1 wins! (ﾉ◕ヮ◕)ﾉ");
            } else if (playerScore < friendScore) {
                new Thread(() -> playSound("goodresult-82807.wav")).start();
                System.out.println("Player 2 wins! (ﾉ◕ヮ◕)ﾉ");
            } else {
                new Thread(() -> playSound("game-bonus-144751.wav")).start();
                System.out.println("It's a tie! What a close game! (¬‿¬)");
            }
        }
        else {
            System.out.println("You scored: " + playerScore + " in " + playermoves + " moves with " + wrong + " wrong moves.");
            System.out.println("Try to improve next time! (★‿★)");
        }
    }
    //// Ask if the user wants to play again
    public static String playAgain(){
        String UserInput;
        do {
            System.out.print("\nDo you want to play again? (Y/N): ");
            UserInput = input.next().trim().toUpperCase();
        } while (!UserInput.equals("Y") && !UserInput.equals("N"));
        return UserInput;
    }
    ////staring the play
    public void playing() {
        while (!Objects.equals(response, "N")) {
            System.out.println("\nWelcome to the Memory Game! (✿◠‿◠)");
            System.out.println("Try to find matching pairs and score points!\n");

            initializeGame();

            if (playAgainstRandom) {
                playAgainstComputer();
            } else if (playWithFriend) {
                playWithFriend();
            } else {
                playSolo();
            }
            response = playAgain();
        }
        System.out.println("\nThanks for playing! Goodbye! (✿◕‿◕)");
    }
}