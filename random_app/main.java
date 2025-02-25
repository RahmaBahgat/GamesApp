package random_app;
import static random_app.MemoryGame.input;

public class main {
    public static void main() {
        String in = "Y";
        while (!in.equalsIgnoreCase("N")) {
            do {
                System.out.print("\nWelcome to playful app!(. ❛ ᴗ ❛.)\nWould you like to play a game (Y/N)? ");
                in = input.next().trim().toUpperCase();
            } while (!in.equals("Y") && !in.equals("N"));

            if (in.equalsIgnoreCase("Y")) {
                System.out.print("1-Connect 4\n2-Memory game\n3-board game\nEnter your choice (1/2/3): ");
                in = input.next().trim().toUpperCase();

                if (in.equalsIgnoreCase("1")) {
                    Connect4 game = new Connect4();
                    game.playing();
                }
                else if (in.equalsIgnoreCase("2")) {
                    MemoryGame game = new MemoryGame();
                    game.playing();
                }
                else if (in.equalsIgnoreCase("3")) {
                    BoardGame game = new BoardGame();
                    game.playing();
                }
                else {
                    System.out.println("Invalid choice. Try again :)");
                }
            }
            else if (!in.equalsIgnoreCase("Y") && !in.equalsIgnoreCase("N")) {
                System.out.println("Invalid choice. Try again :)");
            }
        }
        System.out.println("bye bye (´。＿。｀)");
    }
}
