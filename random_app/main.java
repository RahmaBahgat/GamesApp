package random_app;
import static random_app.MemoryGame.input;
import static random_app.MemoryGame.response;

public class main {
    public static void main() {
        while (!response.equalsIgnoreCase("N")) {
            do {
                System.out.print("\nWelcome to playful app!(. ❛ ᴗ ❛.)\nWould you like to play a game (Y/N)? ");
                response = input.next().trim().toUpperCase();
            } while (!response.equals("Y") && !response.equals("N"));

            if (response.equalsIgnoreCase("Y")) {
                System.out.print("1-Connect 4\n2-Memory game\n3-board game\nEnter your choice (1/2/3): ");
                response = input.next().trim().toUpperCase();

                if (response.equalsIgnoreCase("1")) {
                    Connect4 game = new Connect4();
                    game.playing();
                }
                else if (response.equalsIgnoreCase("2")) {
                    MemoryGame game = new MemoryGame();
                    game.playing();
                }
                else if (response.equalsIgnoreCase("3")) {
                    BoardGame game = new BoardGame();
                    game.playing();
                }
                else {
                    System.out.println("Invalid choice. Try again :)");
                }
            }
            else if (!response.equalsIgnoreCase("Y") && !response.equalsIgnoreCase("N")) {
                System.out.println("Invalid choice. Try again :)");
            }
        }
        System.out.println("bye bye (´。＿。｀)");
    }
}