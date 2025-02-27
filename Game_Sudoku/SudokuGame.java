package Game_Sudoku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.Random;
import java.util.Stack;

public class SudokuGame extends JFrame {
    private static final int GRID_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private JTextField[][] cells = new JTextField[GRID_SIZE][GRID_SIZE];
    private int[][] solution = new int[GRID_SIZE][GRID_SIZE];
    private int[][] dif_levels = new int[GRID_SIZE][GRID_SIZE];
    private String currentDifficulty = "Easy";
    private JTextField selectedCell = null;
    private static final int CELL_SIZE = 10;

    private JButton easyButton;
    private JButton mediumButton;
    private JButton hardButton;
    private JButton solveButton;
    private Stack<Point> moveHistory = new Stack<>();
    private int mistakeCount = 0;
    private final int MAX_MISTAKES = 3;
    private int hintCount = 3;

    SudokuGame() {
        this.setTitle("Sudoku Solver");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(800, 750);
        this.setLayout(null);
        ImageIcon logoIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Game_Sudoku/sudoku.png")));
        this.setIconImage(logoIcon.getImage());
        
        ImageIcon undoIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Game_Sudoku/undo.png")));
        Image scaledImage = undoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        undoIcon = new ImageIcon(scaledImage);

        // Undo Button
        JButton undoButton = new JButton(undoIcon);
        undoButton.setFont(new Font("Arial", Font.BOLD, 20));
        undoButton.setPreferredSize(new Dimension(50, 50));
        undoButton.setBackground(Color.LIGHT_GRAY);
        undoButton.setFocusPainted(false);
        undoButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        undoButton.setBounds(530, 330, 50, 50); // Positioning

        undoButton.addActionListener(e -> {
            if (!moveHistory.isEmpty()) {
                Point lastMove = moveHistory.pop();
                cells[lastMove.x][lastMove.y].setText(""); // Clear last move
            }
        });

        this.add(undoButton);
        
        //hint button
        JButton hintButton = createHintButton();
        this.add(hintButton);

        //Difficulty level panel
        JPanel difficultyPanel = new JPanel();
        difficultyPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        difficultyPanel.setBounds(10, 10, 300, 40);
        JLabel difficultyLabel = new JLabel("Difficulty:");
        difficultyPanel.add(difficultyLabel);

        easyButton = new JButton("Easy");
        mediumButton = new JButton("Medium");
        hardButton = new JButton("Hard");

        easyButton.addActionListener(e -> changeDifficulty("Easy"));
        mediumButton.addActionListener(e -> changeDifficulty("Medium"));
        hardButton.addActionListener(e -> changeDifficulty("Hard"));

        difficultyPanel.add(easyButton);
        difficultyPanel.add(mediumButton);
        difficultyPanel.add(hardButton);
        this.add(difficultyPanel);

        // Solve button
        solveButton = new JButton("Solve Sudoku");
        solveButton.setBounds(650, 20, 130, 40); // Adjusted X position to the right
        solveButton.addActionListener(e -> solveSudokuAndDisplay());
        this.add(solveButton);

        // Create New Game button
        JButton newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Arial", Font.BOLD, 20)); // Set font size
        newGameButton.setBounds(530, 660, 250, 50);
        newGameButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(this,
                    "Start New Game?\nCurrent game progress will be lost.",
                    "Confirm New Game",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (response == JOptionPane.OK_OPTION) {
                // Reset the board and start a new game
                clearGrid();
                generateSudoku(currentDifficulty);
            }
        });
        this.add(newGameButton);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        boardPanel.setBounds(10, 100, 500, 500);
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col] = new JTextField();
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                cells[row][col].setFont(new Font("Arial", Font.BOLD, 20));

                int top = (row % SUBGRID_SIZE == 0) ? 3 : 1;
                int left = (col % SUBGRID_SIZE == 0) ? 3 : 1;
                int bottom = (row == GRID_SIZE - 1) ? 3 : 1;
                int right = (col == GRID_SIZE - 1) ? 3 : 1;
                cells[row][col].setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));

                final int r = row, c = col;
                // Disable direct input by adding a KeyListener
                cells[row][col].addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        e.consume(); // Ignore any key typed events
                    }
                });

                // Add mouse listener to select the cell
                cells[row][col].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        selectCell(r, c);
                    }
                });

                boardPanel.add(cells[row][col]);
            }
        }

        this.add(boardPanel);
        updateButtonColors();
        generateSudoku(currentDifficulty);

        JPanel numberPanel = new JPanel(new GridLayout(3, 3, 5, 5)); // 3x3 layout
        numberPanel.setBounds(530, 400, 250, 250);

        for (int i = 1; i <= 9; i++) {
            JButton numberButton = new JButton(String.valueOf(i)); // Set the text
            numberButton.setFont(new Font("Arial", Font.BOLD, 50)); // Make text bigger

            final int number = i;
            numberButton.addActionListener(e -> {
                if (selectedCell != null && selectedCell.isEditable()) {
                    int row = getCellRow(selectedCell);
                    int col = getCellCol(selectedCell);
                    // Save move to history before changing the text
                    moveHistory.push(new Point(row, col));
                    selectedCell.setText(String.valueOf(number));
                    validateCell(getCellRow(selectedCell), getCellCol(selectedCell));
                    resetHighlight();
                    checkCompletion(row, col); // Check only the relevant row and column
                    selectedCell = null; // Deselect the cell after input
                }
            });

            numberPanel.add(numberButton);
        }

        this.add(numberPanel);

        this.setVisible(true);
    }

    private JButton createHintButton() {
        // Load hint icon
        ImageIcon hintIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Game_Sudoku/hint.png")));
        Image scaledHintImage = hintIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        hintIcon = new ImageIcon(scaledHintImage);

        // Button with icon
        JButton hintButton = new JButton(hintIcon);
        hintButton.setFont(new Font("Arial", Font.BOLD, 15));
        hintButton.setPreferredSize(new Dimension(80, 50));
        hintButton.setBackground(Color.LIGHT_GRAY);
        hintButton.setFocusPainted(false);
        hintButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        hintButton.setBounds(590, 330, 60, 50); // Adjusted position inside the panel

        // Hint count label (badge)
        JLabel hintLabel = new JLabel(String.valueOf(hintCount), SwingConstants.CENTER);
        hintLabel.setFont(new Font("Arial", Font.BOLD, 15));
        hintLabel.setForeground(Color.WHITE);
        hintLabel.setOpaque(true);
        hintLabel.setBackground(Color.RED);
        hintLabel.setBounds(66, 0, 25, 25); // Position inside the panel
        hintLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        hintLabel.setVerticalAlignment(SwingConstants.CENTER);

        // Panel to hold button and label
        JPanel hintPanel = new JPanel(null);
        hintPanel.setBounds(590, 330, 100, 100);
        hintPanel.setOpaque(false); // Ensure panel transparency
        hintPanel.add(hintButton);
        hintPanel.add(hintLabel);

        // Ensure the label is always on top
        hintPanel.setComponentZOrder(hintLabel, 0);
        hintPanel.setComponentZOrder(hintButton, 1);

        // Button ActionListener
        hintButton.addActionListener(e -> {
            if (hintCount > 0) {
                giveHint(hintLabel); // Pass hintLabel to update count
            } else {
                JOptionPane.showMessageDialog(null, "No hints left!");
            }
        });

        this.add(hintPanel);
        return hintButton;
    }

    private void giveHint(JLabel hintLabel) {
        if (hintCount <= 0) {
            JOptionPane.showMessageDialog(this, "No more hints available!", "Hint Limit Reached", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedCell == null || !selectedCell.isEditable() || !selectedCell.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select an empty cell first!", "Hint Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedRow = getCellRow(selectedCell);
        int selectedCol = getCellCol(selectedCell);

        selectedCell.setText(String.valueOf(solution[selectedRow][selectedCol]));
        selectedCell.setForeground(Color.BLUE);
        selectedCell.setEditable(false);

        hintCount--;  // Decrease hint counter
        hintLabel.setText(String.valueOf(hintCount)); // Update displayed hint count

        hintLabel.repaint(); // Force repaint to ensure visibility

        selectedCell = null;
    }

    private void checkCompletion(int row, int col) {
        // Check if the specific row is completed and valid
        if (isRowCompleted(row) && isRowValid(row)) {
            animateRow(row);
        }

        // Check if the specific column is completed and valid
        if (isColumnCompleted(col) && isColumnValid(col)) {
            animateColumn(col);
        }

        // Check if the specific box is completed and valid
        int boxIndex = (row / SUBGRID_SIZE) * SUBGRID_SIZE + (col / SUBGRID_SIZE);
        if (isBoxCompleted(boxIndex) && isBoxValid(boxIndex)) {
            animateBox(boxIndex);
        }
    }

    private boolean isRowCompleted(int row) {
        for (int col = 0; col < GRID_SIZE; col++) {
            if (cells[row][col].getText().isEmpty()) {
                return false; // If any cell is empty, the row is not completed
            }
        }
        return true; // All cells are filled
    }

    private boolean isColumnCompleted(int col) {
        for (int row = 0; row < GRID_SIZE; row++) {
            if (cells[row][col].getText().isEmpty()) {
                return false; // If any cell is empty, the column is not completed
            }
        }
        return true; // All cells are filled
    }

    private boolean isBoxCompleted(int boxIndex) {
        int startRow = (boxIndex / SUBGRID_SIZE) * SUBGRID_SIZE;
        int startCol = (boxIndex % SUBGRID_SIZE) * SUBGRID_SIZE;

        for (int r = 0; r < SUBGRID_SIZE; r++) {
            for (int c = 0; c < SUBGRID_SIZE; c++) {
                if (cells[startRow + r][startCol + c].getText().isEmpty()) {
                    return false; // If any cell is empty, the box is not completed
                }
            }
        }
        return true; // All cells are filled
    }

    private boolean isRowValid(int row) {
        boolean[] seen = new boolean[GRID_SIZE + 1]; // To track numbers 1-9
        for (int col = 0; col < GRID_SIZE; col++) {
            String text = cells[row][col].getText().trim();
            if (text.isEmpty()) return false;

            int num;
            try {
                num = Integer.parseInt(text);
            } catch (NumberFormatException e) {
                return false;
            }

            if (num < 1 || num > 9 || seen[num]) {
                return false; // Number out of range OR duplicate
            }
            seen[num] = true;
        }
        return true;
    }

    private boolean isColumnValid(int col) {
        boolean[] seen = new boolean[GRID_SIZE + 1];
        for (int row = 0; row < GRID_SIZE; row++) {
            String text = cells[row][col].getText().trim();
            if (text.isEmpty()) return false;

            int num;
            try {
                num = Integer.parseInt(text);
            } catch (NumberFormatException e) {
                return false;
            }

            if (num < 1 || num > 9 || seen[num]) {
                return false; // Number out of range OR duplicate
            }
            seen[num] = true;
        }
        return true;
    }

    private boolean isBoxValid(int boxIndex) {
        boolean[] seen = new boolean[GRID_SIZE + 1];
        int startRow = (boxIndex / SUBGRID_SIZE) * SUBGRID_SIZE;
        int startCol = (boxIndex % SUBGRID_SIZE) * SUBGRID_SIZE;

        for (int r = 0; r < SUBGRID_SIZE; r++) {
            for (int c = 0; c < SUBGRID_SIZE; c++) {
                String text = cells[startRow + r][startCol + c].getText().trim();
                if (text.isEmpty()) return false;

                int num;
                try {
                    num = Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    return false;
                }

                if (num < 1 || num > 9 || seen[num]) {
                    return false; // Number out of range OR duplicate
                }
                seen[num] = true;
            }
        }
        return true;
    }

    private void animateRow(int row) {
        Timer timer = new Timer(50, new ActionListener() {
            int step = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (step < CELL_SIZE) {
                    for (int col = 0; col < GRID_SIZE; col++) {
                        cells[row][col].setBackground(new Color(200, 200, 255, 255 - step * 255 / CELL_SIZE));
                    }
                    step++;
                } else {
                    ((Timer) e.getSource()).stop();
                    for (int col = 0; col < GRID_SIZE; col++) {
                        cells[row][col].setBackground(Color.LIGHT_GRAY); // Set final color to light gray
                    }
                }
            }
        });
        timer.start();
    }

    private void animateColumn(int col) {
        Timer timer = new Timer(100, new ActionListener() {
            int step = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (step < CELL_SIZE) {
                    for (int row = 0; row < GRID_SIZE; row++) {
                        cells[row][col].setBackground(new Color(200, 200, 255, 255 - step * 255 / CELL_SIZE));
                    }
                    step++;
                } else {
                    ((Timer) e.getSource()).stop();
                    for (int row = 0; row < GRID_SIZE; row++) {
                        cells[row][col].setBackground(Color.LIGHT_GRAY); // Set final color to light gray
                    }
                }
            }
        });
        timer.start();
    }

    private void animateBox(int boxIndex) {
        Timer timer = new Timer(100, new ActionListener() {
            int step = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                int startRow = (boxIndex / SUBGRID_SIZE) * SUBGRID_SIZE;
                int startCol = (boxIndex % SUBGRID_SIZE) * SUBGRID_SIZE;

                if (step < CELL_SIZE) {
                    for (int r = 0; r < SUBGRID_SIZE; r++) {
                        for (int c = 0; c < SUBGRID_SIZE; c++) {
                            cells[startRow + r][startCol + c].setBackground(new Color(200, 200, 255, 255 - step * 255 / CELL_SIZE));
                        }
                    }
                    step++;
                } else {
                    ((Timer) e.getSource()).stop();
                    for (int r = 0; r < SUBGRID_SIZE; r++) {
                        for (int c = 0; c < SUBGRID_SIZE; c++) {
                            cells[startRow + r][startCol + c].setBackground(Color.LIGHT_GRAY); // Set final color to light gray
                        }
                    }
                }
            }
        });
        timer.start();
    }

    private int getCellRow(JTextField cell) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (cells[row][col] == cell) {
                    return row;
                }
            }
        }
        return -1; // Not found
    }

    private int getCellCol(JTextField cell) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (cells[row][col] == cell) {
                    return col;
                }
            }
        }
        return -1; // Not found
    }

    private void selectCell(int row, int col) {
        if (dif_levels[row][col] != 0) return; // Ignore pre-filled cells

        if (selectedCell != null) {
            resetHighlight();
        }

        selectedCell = cells[row][col];
        highlightRegion(row, col);
    }

    private void highlightRegion(int row, int col) {
        cells[row][col].setBackground(Color.LIGHT_GRAY);
        for (int i = 0; i < GRID_SIZE; i++) {
            cells[row][i].setBackground(Color.LIGHT_GRAY);
            cells[i][col].setBackground(Color.LIGHT_GRAY);
        }
        int boxRowStart = (row / SUBGRID_SIZE) * SUBGRID_SIZE;
        int boxColStart = (col / SUBGRID_SIZE) * SUBGRID_SIZE;
        for (int r = 0; r < SUBGRID_SIZE; r++) {
            for (int c = 0; c < SUBGRID_SIZE; c++) {
                cells[boxRowStart + r][boxColStart + c].setBackground(Color.LIGHT_GRAY);
            }
        }
    }

    private void resetHighlight() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (dif_levels[row][col] == 0) {
                    cells[row][col].setBackground(Color.WHITE);
                }
            }
        }
    }

    private void changeDifficulty(String difficulty) {
        int response = JOptionPane.showConfirmDialog(this,
                "Start New Game?\nCurrent game progress will be lost.",
                "Confirm New Game",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (response == JOptionPane.OK_OPTION) {
            // User chose to start a new game
            currentDifficulty = difficulty;
            updateButtonColors();
            generateSudoku(currentDifficulty);
        } else {
            // User chose to cancel, do nothing
            System.out.println("User  canceled the difficulty change.");
        }
    }

    private void updateButtonColors() {
        easyButton.setBackground(currentDifficulty.equals("Easy") ? Color.GREEN : null);
        mediumButton.setBackground(currentDifficulty.equals("Medium") ? Color.YELLOW : null);
        hardButton.setBackground(currentDifficulty.equals("Hard") ? Color.RED : null);
    }

    private void generateSudoku(String difficulty) {
        fillSolution();
        createPuzzle(difficulty);
        updateGrid();
    }

    private void fillSolution() {
        clearGrid();
        solveSudoku(solution);
    }

    private void createPuzzle(String difficulty) {
        int removeCells = switch (difficulty) {
            case "Easy" -> 35;
            case "Medium" -> 45;
            case "Hard" -> 55;
            default -> 40;
        };

        for (int i = 0; i < GRID_SIZE; i++) {
            System.arraycopy(solution[i], 0, dif_levels[i], 0, GRID_SIZE);
        }

        Random random = new Random();
        while (removeCells > 0) {
            int row = random.nextInt(GRID_SIZE);
            int col = random.nextInt(GRID_SIZE);
            if (dif_levels[row][col] != 0) {
                dif_levels[row][col] = 0;
                removeCells--;
            }
        }
    }

    private void updateGrid() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (dif_levels[row][col] != 0) {
                    cells[row][col].setText(String.valueOf(dif_levels[row][col]));
                    cells[row][col].setEditable(false); // Pre-filled cells are not editable
                    cells[row][col].setBackground(new Color(220, 220, 220));
                } else {
                    cells[row][col].setText("");
                    cells[row][col].setEditable(true); // Editable cells
                    cells[row][col].setBackground(Color.WHITE);
                }
                cells[row][col].setForeground(Color.BLACK);
            }
        }
    }

    public void clearGrid() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j].setText("");  // Assuming you have a JTextField grid
                cells[i][j].setBackground(Color.WHITE); // Reset background color
            }
        }
    }

    private boolean solveSudoku(int[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= GRID_SIZE; num++) {
                        if (isValidMove(board, row, col, num)) {
                            board[row][col] = num;
                            if (solveSudoku(board)) {
                                return true;
                            }
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValidMove(int[][] board, int row, int col, int num) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }

        int startRow = (row / SUBGRID_SIZE) * SUBGRID_SIZE;
        int startCol = (col / SUBGRID_SIZE) * SUBGRID_SIZE;
        for (int r = 0; r < SUBGRID_SIZE; r++) {
            for (int c = 0; c < SUBGRID_SIZE; c++) {
                if (board[startRow + r][startCol + c] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private void validateCell(int row, int col) {
        if (!cells[row][col].isEditable()) {
            return;
        }

        String input = cells[row][col].getText().trim();
        if (!input.matches("[1-9]")) {
            cells[row][col].setForeground(Color.BLACK);
            return;
        }

        int num = Integer.parseInt(input);
        if (num == solution[row][col]) {
            cells[row][col].setForeground(Color.GREEN);
        } else {
            cells[row][col].setForeground(Color.RED);
            mistakeCount++; // Increase mistake count

            if (mistakeCount >= MAX_MISTAKES) {
                showMistakeDialog(); // Show warning dialog
            }
        }
    }

    private void solveSudokuAndDisplay() {
        int[][] tempBoard = new int[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            System.arraycopy(dif_levels[i], 0, tempBoard[i], 0, GRID_SIZE);
        }

        if (solveSudoku(tempBoard)) {
            for (int row = 0; row < GRID_SIZE; row++) {
                for (int col = 0; col < GRID_SIZE; col++) {
                    if (dif_levels[row][col] == 0) {
                        cells[row][col].setText(String.valueOf(tempBoard[row][col]));
                        cells[row][col].setForeground(Color.BLUE);
                    }
                }
            }
        }
    }

    private boolean isCorrectMove(int row, int col, int number) {
        String text = cells[row][col].getText().trim(); // Get the text from the cell
        if (text.isEmpty()) return false; // If the cell is empty, it's not correct

        try {
            int cellNumber = Integer.parseInt(text); // Convert text to an integer
            return cellNumber == number; // Compare with the correct number
        } catch (NumberFormatException e) {
            return false; // If conversion fails, it's not a valid move
        }
    }

    private void showMistakeDialog() {
        int choice = JOptionPane.showOptionDialog(this,
                "You made 3 mistakes! Do you want to try again or start a new game?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                new String[]{"Try Again", "New Game"},
                "Try Again");

        if (choice == JOptionPane.YES_OPTION) {
            mistakeCount = 0; // Reset mistakes
            resetWrongCells(); // Allow corrections
        } else {
            clearGrid(); // Start new game
            generateSudoku(currentDifficulty);
            mistakeCount = 0;
        }
    }

    private void resetWrongCells() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (cells[row][col].getForeground().equals(Color.RED)) {
                    cells[row][col].setText(""); // Clear only mistakes
                }
            }
        }
    }
}