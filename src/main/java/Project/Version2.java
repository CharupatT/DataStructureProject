package Project;

/*
 ref: https://www.javatpoint.com/n-queens-problems
 with oop + exception handling
 may adjust user interface and oop nakaa
*/

import java.util.*;

class Board {
    private int size;
    private List<Integer> queenPositions;

    public Board(int size) {
        this.size = size;
        this.queenPositions = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            queenPositions.add(-1); // Initialize queen positions to -1 (not placed)
        }
    }

    public void setQueen(int row, int column) {
        if (row >= 0 && row < size) {
            queenPositions.set(row, column);
        }
    }

    public void display() {
        for (int i = 0; i < size; i++) {
            System.out.print("row" + (i + 1) + " ");  // Print row label (row1, row2, etc.)
            for (int j = 0; j < size; j++) {
                if (queenPositions.get(i) == j) {
                    System.out.print(" Q ");  // Print queen
                } else {
                    System.out.print(" . ");  // Print empty space
                }
            }
            System.out.println();
        }
        System.out.println(); // Blank line for separation
    }

    public List<Integer> getQueenPositions() {
        return queenPositions;
    }

    public int getSize() {
        return size;
    }
}

class QueenPlacement {
    private Board board;
    private int solutionCount = 0;
    private boolean foundMatch = false;

    public QueenPlacement(int size) {
        this.board = new Board(size);
    }

    public Board getBoard() {
        return board;  // Expose the board instance
    }

    public boolean place(int k, int i) {
        List<Integer> positions = board.getQueenPositions();
        for (int j = 0; j < k; j++) {
            if (positions.get(j) == i || Math.abs(positions.get(j) - i) == Math.abs(j - k)) {
                return false;
            }
        }
        return true;
    }

    public void solveNQueens(int firstQueenRow, int firstQueenCol, boolean isManual) {
        int n = board.getSize();
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[] {0, -1}); // Start at row 0, column -1 (not placed)

        while (!stack.isEmpty()) {
            int[] position = stack.pop();
            int row = position[0];
            int col = position[1] + 1;

            while (col < n && !place(row, col)) {
                col++;
            }

            if (col < n) {
                board.setQueen(row, col);
                stack.push(new int[] {row, col}); // Save this placement

                if (row == n - 1) { // Last row
                    if (!isManual || board.getQueenPositions().get(firstQueenRow) == firstQueenCol) {
                        foundMatch = true;
                        solutionCount++;
                        System.out.println("==============================");
                        System.out.println("Solution " + solutionCount + ":");
                        board.display();  // Display the board with row labels
                    }
                } else {
                    stack.push(new int[] {row + 1, -1}); // Move to the next row
                }
            } else {
                board.setQueen(row, -1); // Backtrack
            }
        }
    }

    public boolean isFoundMatch() {
        return foundMatch;
    }
}

class NQueensSolver {
    private Scanner scanner;
    private QueenPlacement queenPlacement;

    public NQueensSolver() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            try {
                int n = getBoardSize();
                boolean isManual = askForManualPlacement();
                int firstQueenRow = -1, firstQueenCol = -1;

                if (isManual) {
                    firstQueenRow = getQueenPosition("row", n);
                    firstQueenCol = getQueenPosition("column", n);
                }

                // Initialize and display the board with the user's queen
                queenPlacement = new QueenPlacement(n);
                if (isManual) {
                    queenPlacement.getBoard().setQueen(firstQueenRow, firstQueenCol);
                    System.out.println("==============================");
                    System.out.println("Board with User's First Queen Placed:");
                    queenPlacement.getBoard().display();
                }

                // Find solutions
                queenPlacement.solveNQueens(firstQueenRow, firstQueenCol, isManual);

                // If no matching solution was found, inform the user
                if (isManual && !queenPlacement.isFoundMatch()) {
                    System.out.println("No solution.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();  // Clear the scanner buffer
            }

            if (!askToContinue()) {
                System.out.println("Exiting the program. Goodbye!");
                break;
            }
        }
        scanner.close();  // Close the scanner
    }

    private int getBoardSize() {
        int n = 0;
        while (n < 4) {
            System.out.print("Enter the size of the board (n >= 4)\n=> ");
            n = scanner.nextInt();
            if (n < 4) {
                System.out.println("Board size must be at least 4. Please try again.");
            }
        }
        return n;
    }

    private boolean askForManualPlacement() {
        String manualPlacement;
        do {
            System.out.print("Do you want to manually place the first queen? (y for yes, n for no)\n=> ");
            manualPlacement = scanner.next().trim().toLowerCase();
            if (!manualPlacement.equals("y") && !manualPlacement.equals("n")) {
                System.out.println("Invalid input, please select 'y' or 'n'.");
            }
        } while (!manualPlacement.equals("y") && !manualPlacement.equals("n"));
        return manualPlacement.equals("y");
    }

    private int getQueenPosition(String type, int n) {
        int position;
        do {
            System.out.print("Enter the " + type + " number for the first queen (1 to " + n + ")\n=> ");
            position = scanner.nextInt();
            if (position < 1 || position > n) {
                System.out.println("Invalid " + type + " number. Please try again.");
            }
        } while (position < 1 || position > n);
        return position - 1; // Convert to 0-based index for internal storage
    }

    private boolean askToContinue() {
        System.out.print("Do you want to try again? (y for yes, any other key to exit)\n=> ");
        String tryAgain = scanner.next().trim().toLowerCase();
        return tryAgain.equals("y");
    }
}

public class Version2 {
    public static void main(String[] args) {
        NQueensSolver solver = new NQueensSolver();
        solver.start();
    }
}


