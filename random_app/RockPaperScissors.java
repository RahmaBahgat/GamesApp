package random_app;
import java.util.*;

public class RockPaperScissors {
    private static String[] choices = {"Rock", "Paper", "Scissors"};
    private static Random rand = new Random();
    private static Scanner input = new Scanner(System.in);
    //// the logic of the game
    public void playing() {
        String playAgain;
        do {
            System.out.println("\nWelcome to 🌚Rock,📄Paper, ✂Scissors!");

            System.out.print("How many rounds do you want to play? ");
            int rounds = input.nextInt();
            while (rounds <= 0) {
                System.out.print("Invalid number. Please enter a positive number of rounds: ");
                rounds = input.nextInt();
            }

            int playerScore = 0, computerScore = 0;

            for (int i = 1; i <= rounds; i++) {
                System.out.println("\nRound " + i + " of " + rounds);

                System.out.print("Choose Rock (R), Paper (P), or Scissors (S): ");
                String playerChoice = input.next().trim().toUpperCase();
                while (!playerChoice.equals("R") && !playerChoice.equals("P") && !playerChoice.equals("S")) {
                    System.out.print("Invalid choice. Choose Rock (R), Paper (P), or Scissors (S): ");
                    playerChoice = input.next().trim().toUpperCase();
                }

                int playerIndex = playerChoice.equals("R") ? 0 : (playerChoice.equals("P") ? 1 : 2);
                int computerIndex = rand.nextInt(3);

                System.out.println("You chose: " + choices[playerIndex]);
                System.out.println("Computer chose: " + choices[computerIndex]);

                if (playerIndex == computerIndex) {
                    System.out.println("It's a draw! (¬‿¬)");
                } else if ((playerIndex == 0 && computerIndex == 2) ||
                        (playerIndex == 1 && computerIndex == 0) ||
                        (playerIndex == 2 && computerIndex == 1)) {
                    System.out.println("You win this round! ☜(⌒▽⌒)☞");
                    playerScore++;
                } else {
                    System.out.println("You lost this round! ಥ_ಥ");
                    computerScore++;
                }
            }

            System.out.println("\nFinal Score:");
            System.out.println("You: " + playerScore + " | Computer: " + computerScore);
            if (playerScore > computerScore) {
                System.out.println("Congratulations! You won the game!☜(⌒▽⌒)☞");
            } else if (playerScore < computerScore) {
                System.out.println("Better luck next time! The computer wins. 🤖");
            } else {
                System.out.println("It's a tie! Well played! (¬‿¬)");
            }

            System.out.print("Do you want to play again? (Y/N): ");
            playAgain = input.next().trim().toUpperCase();
        } while (playAgain.equals("Y"));

        System.out.println("Thanks for playing! Goodbye! (✿◕‿◕)");
    }
}