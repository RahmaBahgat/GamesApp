package random_app;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String play = "Y";

        while (!play.equalsIgnoreCase("N")) {
            do {
                System.out.print("\nWelcome to playful app!(. ❛ ᴗ ❛.)\nWould you like to play a game (Y/N)? ");
                play = scanner.nextLine().trim().toUpperCase();
            } while (!play.equals("Y") && !play.equals("N"));

            if (play.equalsIgnoreCase("Y")) {
                System.out.print("1-Connect 4\n2-Memory game\n3-rock ,paper ,scissors\n4-board game\nEnter your choice (1/2/3/4): ");
                play = scanner.nextLine();

                if (play.equalsIgnoreCase("1")) {
                    Connect4 game = new Connect4();
                    game.playing();
                } else if (play.equalsIgnoreCase("2")) {
                    MemoryGame game = new MemoryGame();
                    game.playing();
                }
                else if (play.equalsIgnoreCase("3")) {
                    RockPaperScissors game = new RockPaperScissors();
                    game.playing();
                }
                else if (play.equalsIgnoreCase("4")) {
                    BoardGame game = new BoardGame();
                    game.playing();
                }
                else {
                    System.out.println("Invalid choice. Try again :)");
                }
            } else if (!play.equalsIgnoreCase("Y") && !play.equalsIgnoreCase("N")) {
                System.out.println("Invalid choice. Try again :)");
            }
        }
        System.out.println("bye bye (´。＿。｀)");
        scanner.close();
    }
}
