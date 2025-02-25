package random_app;
import java.util.*;
import static random_app.MemoryGame.input;

class Connect4 {
    public String[][] board = new String[6][7];

    //// Set the board with all empty cells
    private void initializeBoard() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                board[i][j] = " -   ";
            }
        }
    }
    //// Display the board
    public void displayBoard() {
        for (String[] row : board) {
            for (String cell : row) {
                System.out.print(cell);
            }
            System.out.println();
        }
        System.out.println(" 1    2   3   4    5   6    7");
    }
    //// update the board after making a move
    public boolean updateBoard(int column, String symbol) {
        if (column < 0 || column >= 7) return false;
        for (int i = 5; i >= 0; i--) {
            if (board[i][column].equals(" -   ")) {
                board[i][column] = symbol;
                return true;
            }
        }
        return false;
    }
    //// Check all the winning cases
    public boolean isWin(String symbol) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7 - 3; j++) {
                if (board[i][j].equals(symbol) && board[i][j + 1].equals(symbol) &&
                        board[i][j + 2].equals(symbol) && board[i][j + 3].equals(symbol)) {
                    return true;
                }
            }
        }

        for (int i = 0; i < 6 - 3; i++) {
            for (int j = 0; j < 7; j++) {
                if (board[i][j].equals(symbol) && board[i + 1][j].equals(symbol) &&
                        board[i + 2][j].equals(symbol) && board[i + 3][j].equals(symbol)) {
                    return true;
                }
            }
        }

        for (int i = 0; i < 6 - 3; i++) {
            for (int j = 0; j < 7 - 3; j++) {
                if (board[i][j].equals(symbol) && board[i + 1][j + 1].equals(symbol) &&
                        board[i + 2][j + 2].equals(symbol) && board[i + 3][j + 3].equals(symbol)) {
                    return true;
                }
            }
        }

        for (int i = 3; i < 6; i++) {
            for (int j = 0; j < 7 - 3; j++) {
                if (board[i][j].equals(symbol) && board[i - 1][j + 1].equals(symbol) &&
                        board[i - 2][j + 2].equals(symbol) && board[i - 3][j + 3].equals(symbol)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isDraw() {
        for (String cell : board[0]) {
            if (cell.equals(" -   ")) return false;
        }
        return true;
    }

    //// Player vs Player mode
    private void playVsPlayer() {
        String currentPlayer = "🔴";
        while (true) {
            displayBoard();
            System.out.print( " (" + currentPlayer + ") enter a column (1-7): ");
            int column = input.nextInt() - 1;

            if (!updateBoard(column, currentPlayer)) {
                System.out.println("Invalid choice. Try again :)");
                continue;
            }

            if (isWin(currentPlayer)) {
                displayBoard();
                System.out.println(currentPlayer + " wins! ☜(⌒▽⌒)☞");
                break;
            }

            if (isDraw()) {
                displayBoard();
                System.out.println("It's a draw! (¬‿¬)");
                break;
            }
            currentPlayer = (currentPlayer.equals("🔴")) ? "🔵" : "🔴";
        }
    }
    //// Player vs Random Computer mode
    private void playVsRandomComputer() {
        Random random = new Random();
        String currentPlayer = "🔴";

        while (true) {
            displayBoard();
            if (currentPlayer.equals("🔴")) {
                System.out.print(" (🔴) enter a column (1-7): ");
                int column = input.nextInt() - 1;

                if (!updateBoard(column, "🔴")) {
                    System.out.println("Invalid choice. Try again :)");
                    continue;
                }

                if (isWin("🔴")) {
                    displayBoard();
                    System.out.println( "🔴 wins! ☜(⌒▽⌒)☞");
                    break;
                }
            }
            else {
                System.out.println("Computer (🔵) is making a move...");
                int column;

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
                System.out.println("It's a draw! (¬‿¬)");
                break;
            }
            currentPlayer = (currentPlayer.equals("🔴")) ? "🔵" : "🔴";
        }
    }
    //// Playing the game
    public void playing() {
        String response = "Y";
        while (!response.equals("N")) {
            initializeBoard();

            System.out.println("Welcome to Connect 4! (✿◠‿◠)");
            System.out.println("1. Player vs Player");
            System.out.println("2. Player vs Random Computer");
            System.out.print("Choose an option (1/2): ");

            String choice = input.next().trim();
            if (choice.equals("1")) {
                playVsPlayer();
            } else if (choice.equals("2")) {
                playVsRandomComputer();
            } else {
                System.out.println("Invalid choice. Try again :)");
            }

            do {
                System.out.print("\nDo you want to play again? (Y/N): ");
                response = input.next().trim().toUpperCase();
            } while (!response.equals("Y") && !response.equals("N"));
        }
        System.out.println("\nThanks for playing! Goodbye! (✿◕‿◕)");
    }
}
