package games_app;
import java.util.*;
//// reusing of the needed function and scanner from the memory game
import static games_app.MemoryGame.input;
import static games_app.MemoryGame.playAgain;
import static games_app.MemoryGame.playSound;

public class BoardGame {
    public   Random random = new Random();
    public   Map<String, String> judgeCards = new HashMap<>();
    public   Set<Integer> bonusCards , minusCards , judgeCardPositions ;
    public int player1Position = 0, player2Position = 0;
    public int player1Score = 0, player2Score = 0;

    ////set of some questions for the judge cards
    public void initializeJudgeCards() {
        judgeCards.put("watermelon", "What is the red-colored fruit that consists of 10 letters?");
        judgeCards.put("0", "What is 1 ÷ infinite ?(answer wit ha number)");
        judgeCards.put("water", "What do cows drink?");
        judgeCards.put("bamboo", "What do pandas eat?");
        judgeCards.put("diamond", "What is the hardest natural substance on Earth?");
        judgeCards.put("xvi", "What is the Roman numeral for 16?");
        judgeCards.put("hydrogen", "Which element has atomic number 1?");
        judgeCards.put("venus", "Which planet is known as the morning star?");
        judgeCards.put("8", "How many legs does a spider have?(answer wit ha number)");
        judgeCards.put("pacific", "What is the largest ocean on Earth?");
        judgeCards.put("cheetah", "What is the fastest land animal?");
        judgeCards.put("mitochondria", "What is the powerhouse of the cell?");
        judgeCards.put("gallium", "Which metal melts at human body temperature?");
        judgeCards.put("fibonacci", "Which mathematical sequence starts with 0 and 1, with each term being the sum of the previous two?");
    }
    ////separate the special cards randomly in the board  every game in specific numbers
    public void initializeSpecialCards() {
        Set<Integer> allPositions = new HashSet<>();
        bonusCards = new HashSet<>();
        minusCards = new HashSet<>();
        judgeCardPositions = new HashSet<>();

        while (bonusCards.size() < 5) {
            //// skip cell 1 and 56
            int pos = random.nextInt(56 - 2) + 2;
            if (!allPositions.contains(pos)) {
                bonusCards.add(pos);
                allPositions.add(pos);
            }
        }

        while (minusCards.size() < 5) {
            int pos = random.nextInt(56 - 2) + 2;
            if (!allPositions.contains(pos)) {
                minusCards.add(pos);
                allPositions.add(pos);
            }
        }

        while (judgeCardPositions.size() < 7) {
            int pos = random.nextInt(56 - 2) + 2;
            if (!allPositions.contains(pos)) {
                judgeCardPositions.add(pos);
                allPositions.add(pos);
            }
        }
    }

    public void printBoard() {
        System.out.println("\n📌 Board Positions:");
        for (int i = 1; i <= 56; i++) {
            String cell;
            ////both in the same cell
            if (i == player2Position && i == player1Position)
                cell = "🟣";
            else if (i == player1Position)
                cell = "🔴";
            else if (i == player2Position)
                cell = "🔵";
            else if (judgeCardPositions.contains(i))
                cell = "🃏";
            else if (bonusCards.contains(i))
                cell = "🍀";
            else if (minusCards.contains(i))
                cell = "🛑";
            else
                cell = String.format(Locale.US, "%2d ", i);

            System.out.print(cell);
            if (i % 8 == 0) System.out.println();
        }
        System.out.println();
    }
    ////get random dice number
    public int rollDice() {
        return random.nextInt(6) + 1;
    }

    public void askJudgeQuestion(int playerNum) {
        //// Get a random key (answer) from the judgeCards map and it's question
        Object[] keys = judgeCards.keySet().toArray();
        String correctAnswer = (String) keys[random.nextInt(keys.length)];
        String question = judgeCards.get(correctAnswer);

        System.out.print("🃏 Judge Card! " + question + " :");
        String playerAnswer = input.next().trim().toLowerCase();

        if (playerAnswer.equals(correctAnswer)) {
            System.out.println("✅ Correct! +2 points.");
            updateScore(playerNum, 2);
        }
        else {
            System.out.println("❌ Wrong! -1 point.");
            updateScore(playerNum, -1);
        }
    }

    public void updateScore(int playerNum, int points) {
        if (playerNum == 1) player1Score += points;
        else player2Score += points;
    }

    public void checkSpecialCards(int playerNum) {
        int position = (playerNum == 1) ? player1Position : player2Position;

        if (bonusCards.contains(position)) {
            System.out.println("🍀 Bonus Card! +3 points.");
            updateScore(playerNum, 3);
        }

        if (minusCards.contains(position)) {
            System.out.println("🛑 Minus Card! -1 point & move back 3 spaces.");
            updateScore(playerNum, -1);
            if (playerNum == 1) player1Position = Math.max(1, player1Position - 3);
            else player2Position = Math.max(1, player2Position - 3);
        }

        if (judgeCardPositions.contains(position)) {
            askJudgeQuestion(playerNum);
        }
    }

    public void movePlayer(int playerNum, int roll) {
        if (playerNum == 1) {
            //// End turn, move to next player if the Roll exceeds board limit
            if (player1Position + roll > 56) {
                System.out.println("🤷🏽‍♀️ Can't move! Roll exceeds board limit. Turn skipped.");
                return;
            }
            player1Position += roll;
            checkSpecialCards(1);
            //// If landed on a Minus Card, move back and check again if the player is not out of the board
            if (minusCards.contains(player1Position)) {
                System.out.println("🛑 Minus Card! -1 point & move back 3 spaces.");
                updateScore(1, -1);
                player1Position = Math.max(1, player1Position - 3);
                checkSpecialCards(1);
            }
        }
        else {
            if (player2Position + roll > 56) {
                System.out.println("🤷🏽 Can't move! Roll exceeds board limit. Turn skipped.");
                return;
            }
            player2Position += roll;
            checkSpecialCards(2);

            if (minusCards.contains(player2Position)) {
                System.out.println("🛑 Minus Card! -1 point & move back 3 spaces.");
                updateScore(2, -1);
                player2Position = Math.max(1, player2Position - 3);
                checkSpecialCards(2);
            }
        }
    }

    public void determineWinner() {
        if (player1Score > player2Score) {
            new Thread(() -> playSound("goodresult-82807.wav")).start();
            System.out.println("🏆 " + "(🔴)" + " wins!");
        }
        else if (player2Score > player1Score) {
            new Thread(() -> playSound("goodresult-82807.wav")).start();
            System.out.println("🏆 " + "(🔵)" + " wins!");
        }
        else {
            new Thread(() -> playSound("game-bonus-144751.wav")).start();
            System.out.println("🤝 It's a draw!");
        }
        System.out.println("\n🎉 Game Over! Final Scores:");
        System.out.println(" (🔴) Score: " + player1Score);
        System.out.println(" (🔵) Score: " + player2Score);
    }

    public void playing() {
        String response = "Y";
        while (!response.equals("N")) {
            initializeJudgeCards();
            initializeSpecialCards();
            player1Position = 0;
            player2Position = 0;
            player1Score = 0;
            player2Score = 0;

            System.out.println("\nWelcome to the board Game ... best of luck! (✿◠‿◠)");
            System.out.println("this game is played on a 56-cell board with 5 bonus cards and 5 minus cards. and 7 judge cards.\n");
            System.out.println("1- minus cards gives you -1 point and move back 3 spaces\n2-bonus cards gives you 3 points");
            System.out.println("3- judge cards gives you +2 points for correct answers or -1 point for wrong");
            printBoard();

            while (player1Position < 56 && player2Position < 56) {
                System.out.print("\n🎲  (🔴) turn . ENTER anything to roll: ");
                input.next();
                int roll = rollDice();
                System.out.println("(🔴) rolled a " + roll + "!");
                movePlayer(1, roll);
                printBoard();

                //// Check if game ends
                if (player1Position >= 56) break;

                System.out.print("\n🎲 (🔵) turn . ENTER anything to roll: ");
                input.next();
                roll = rollDice();
                System.out.println("(🔵)  rolled a " + roll + "!");
                movePlayer(2, roll);
                printBoard();
            }
            determineWinner();
        //// Ask if the user wants to play again
        response = playAgain();
        }
        System.out.println("\nThanks for playing! Goodbye! (✿◕‿◕)");
    }
}