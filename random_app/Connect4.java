package random_app;
import java.util.*;

class Connect4 {
    private String[][] board;

    public Connect4() {
        board = new String[6][7];
        initializeBoard();
    }
    ////Set the board with all null cells
    private void initializeBoard() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                board[i][j] = " -   ";
            }
        }
    }
    ////display the board
    public void displayBoard() {
        for (String[] chars : board) {
            for (String aChar : chars) {
                System.out.print(aChar);
            }
            System.out.println();
        }
        System.out.println(" 1    2   3   4    5   6    7");
    }

    public boolean updateBoard(int column, String symbol) {
        if (column < 0 || column >= 7) return false;
        for (int i = 6 - 1; i >= 0; i--) {
            if (board[i][column] == " -   ") {
                board[i][column] = symbol;
                return true;
            }
        }
        return false;
    }
    ////check all the winning cases
    public boolean isWin(String symbol) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7 - 3; j++) {
                if (board[i][j] == symbol && board[i][j + 1] == symbol &&
                        board[i][j + 2] == symbol && board[i][j + 3] == symbol) {
                    return true;
                }
            }
        }

        for (int i = 0; i < 6 - 3; i++) {
            for (int j = 0; j < 7; j++) {
                if (board[i][j] == symbol && board[i + 1][j] == symbol &&
                        board[i + 2][j] == symbol && board[i + 3][j] == symbol) {
                    return true;
                }
            }
        }

        for (int i = 0; i < 6 - 3; i++) {
            for (int j = 0; j < 7 - 3; j++) {
                if (board[i][j] == symbol && board[i + 1][j + 1] == symbol &&
                        board[i + 2][j + 2] == symbol && board[i + 3][j + 3] == symbol) {
                    return true;
                }
            }
        }

        for (int i = 3; i < 6; i++) {
            for (int j = 0; j < 7 - 3; j++) {
                if (board[i][j] == symbol && board[i - 1][j + 1] == symbol &&
                        board[i - 2][j + 2] == symbol && board[i - 3][j + 3] == symbol) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isDraw() {
        for (String c : board[0]) {
            if (c == " -   ") return false;
        }
        return true;
    }

    ////playing with friend mode
    private void playVsPlayer(Scanner scanner) {
        System.out.print("Enter Player 🔴's name: ");
        String player1 = scanner.next();
        System.out.print("Enter Player 🔵's name: ");
        String player2 = scanner.next();

        String currentPlayer = "🔴";
        String currentPlayerName = player1;

        while (true) {
            displayBoard();
            System.out.print(currentPlayerName + " (" + currentPlayer + ") enter a column (1-7): ");
            int column = scanner.nextInt() - 1;

            if (!updateBoard(column, currentPlayer)) {
                System.out.println("Invalid choice. Try again :)");
                continue;
            }

            if (isWin(currentPlayer)) {
                displayBoard();
                System.out.println(currentPlayerName + " wins!☜(⌒▽⌒)☞");
                break;
            }
            if (isDraw()) {
                displayBoard();
                System.out.println("It's a draw!(¬‿¬)");
                break;
            }
            currentPlayer = (currentPlayer.equals("🔴")) ? "🔵" : "🔴";
            currentPlayerName = (currentPlayerName.equals(player1)) ? player2 : player1;
        }
    }
    ////play with computer mode
    private void playVsRandomComputer(Scanner scanner) {
        System.out.print("Enter Player 🔴's name: ");
        String player = scanner.next();
        Random random = new Random();
        String currentPlayer = "🔴";

        while (true) {
            displayBoard();
            if (currentPlayer == "🔴") {
                System.out.print(player + " (🔴) enter a column (1-7): ");
                int column = scanner.nextInt() - 1;
                if (!updateBoard(column, "🔴")) {
                    System.out.println("Invalid choice. Try again :)");
                    continue;
                }
                if (isWin("🔴")) {
                    displayBoard();
                    System.out.println(currentPlayer + " wins!☜(⌒▽⌒)☞");
                    break;
                }
            } else {
                System.out.println("Computer (🔵) is making a move...");
                int column;
               ////getting a random move from the available  list
                do {
                    column = random.nextInt(7);
                } while (!updateBoard(column, "🔵"));
                System.out.println("Computer chose column: " + (column + 1));
                if (isWin("🔵")) {
                    displayBoard();
                    System.out.println("Computer wins! (¬_¬ )");
                    break;
                }
            }
            if (isDraw()) {
                displayBoard();
                System.out.println("It's a draw!(¬‿¬)");
                break;
            }
            currentPlayer = (currentPlayer == "🔴") ? "🔵" : "🔴";
        }
    }
    ////playing the game
    public void playing() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Connect 4! (✿◠‿◠)");
        System.out.println("1. Player vs Player");
        System.out.println("2. Player vs Random Computer");
        System.out.print("Choose an option (1/2): ");

        String choice = scanner.next().toUpperCase(); // Read input as string

        if (choice.equals("1")) {
            playVsPlayer(scanner);
        } else if (choice.equals("2")) {
            playVsRandomComputer(scanner);
        } else {
            System.out.println("Invalid choice. Try again :)");
        }

        System.out.print("Would you like to play again? (Y/N): ");
        choice = scanner.next().toUpperCase();
        if (choice.equals("Y")) {
            playing();
        }
    }
}