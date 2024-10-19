package Project;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class NQueenProblem {

    /* Backtracking algorithm: n queens */
    public void backtrack(int n, List<List<String>> state, List<List<List<String>>> res,
                          boolean[] rows, boolean[] cols, boolean[] diags1, boolean[] diags2, Set<String> uniqueSolutions) {
        // When all rows are filled with queens, record the solution
        if (allRowsOccupied(rows, n)) {
            // Convert the solution to a string for easy comparison
            String solutionString = convertToString(state);

            // Add the solution only if it's not a duplicate
            if (!uniqueSolutions.contains(solutionString)) {
                uniqueSolutions.add(solutionString);

                // Add the board configuration to the result
                List<List<String>> copyState = new ArrayList<>();
                for (List<String> sRow : state) {
                    copyState.add(new ArrayList<>(sRow));
                }
                res.add(copyState);
            }
            return;
        }

        // Traverse all rows and columns to attempt placing a queen
        for (int row = 0; row < n; row++) {
            // Skip the row if a queen is already placed there
            if (rows[row]) continue;

            for (int col = 0; col < n; col++) {
                int diag1 = row - col + n - 1;
                int diag2 = row + col;
//graceis handsome
                // Check if it's safe to place a queen
                if (!cols[col] && !diags1[diag1] && !diags2[diag2]) {
                    // Place the queen
                    state.get(row).set(col, "Q");
                    rows[row] = cols[col] = diags1[diag1] = diags2[diag2] = true;

                    // Continue to attempt placements with this new configuration
                    backtrack(n, state, res, rows, cols, diags1, diags2, uniqueSolutions);

                    // Backtrack: Remove the queen and reset the position
                    state.get(row).set(col, ".");
                    rows[row] = cols[col] = diags1[diag1] = diags2[diag2] = false;
                }
            }
        }
    }

    // Helper function to check if all rows have been occupied by queens
    private boolean allRowsOccupied(boolean[] rows, int n) {
        for (int i = 0; i < n; i++) {
            if (!rows[i]) return false;
        }
        return true;
    }

    // Convert a board state into a canonical string for comparison
    private String convertToString(List<List<String>> state) {
        StringBuilder sb = new StringBuilder();
        for (List<String> row : state) {
            for (String cell : row) {
                sb.append(cell);
            }
            sb.append("\n");  // Use newline to separate rows
        }
        return sb.toString();
    }

    /* Solve n queens with an optional initial queen's position set */
    public List<List<List<String>>> nQueens(int n, Integer firstRow, Integer firstCol) {
        // Initialize an n*n size chessboard, where 'Q' represents the queen and '.' represents an empty spot
        List<List<String>> state = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<String> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(".");
            }
            state.add(row);
        }

        boolean[] rows = new boolean[n]; // Track which rows have queens
        boolean[] cols = new boolean[n]; // Track which columns have queens
        boolean[] diags1 = new boolean[2 * n - 1]; // Track main diagonals with queens
        boolean[] diags2 = new boolean[2 * n - 1]; // Track minor diagonals with queens

        // If the user chose to place the first queen, mark the position
        if (firstRow != null && firstCol != null) {
            state.get(firstRow).set(firstCol, "Q");
            rows[firstRow] = cols[firstCol] = true;
            diags1[firstRow - firstCol + n - 1] = true;
            diags2[firstRow + firstCol] = true;
        }

        List<List<List<String>>> res = new ArrayList<>();
        Set<String> uniqueSolutions = new HashSet<>();  // To store unique solutions as strings

        // Start backtracking from any row (since we don't have a strict row order now)
        backtrack(n, state, res, rows, cols, diags1, diags2, uniqueSolutions);

        return res;
    }

    // Method to ask the user a yes/no question (y/n)
    private static String getYesNoInput(Scanner scanner, String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.next().trim().toLowerCase();
            if (input.equals("y") || input.equals("n")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'y' or 'n'.");
            }
        }
        return input;
    }

    // Main method to test the solution
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        NQueenProblem problem = new NQueenProblem();
        boolean continuePlaying = true;

        while (continuePlaying) {
            // Get user input for the board size
            System.out.print("Enter the size of the board (n): ");
            int n = scanner.nextInt();

            // Ask if the user wants to place the first queen
            String placeFirstQueen = getYesNoInput(scanner, "Do you want to place the first queen? (y/n): ");

            Integer firstRow = null;
            Integer firstCol = null;

            if (placeFirstQueen.equals("y")) {
                // Get user input for the initial queen's position
                System.out.print("Enter the row (0-based index) of the first queen: ");
                firstRow = scanner.nextInt();
                System.out.print("Enter the column (0-based index) of the first queen: ");
                firstCol = scanner.nextInt();
            }

            // Solve the N-Queens problem with or without the user's first queen placement
            List<List<List<String>>> solutions = problem.nQueens(n, firstRow, firstCol);

            // Print the solutions
            if (solutions.isEmpty()) {
                System.out.println("No solutions found.");
            } else {
                // Add this line here
                System.out.println("Number of solutions found: " + solutions.size());
                System.out.println("=============================");

                // Print the solutions
                for (List<List<String>> solution : solutions) {
                    for (List<String> row : solution) {
                        System.out.println(String.join(" ", row));
                    }
                    System.out.println();
                }
            }

            // Ask the user if they want to continue or quit
            String continueGame = getYesNoInput(scanner, "Do you want to solve another board? (y/n): ");
            if (continueGame.equals("n")) {
                continuePlaying = false;
                System.out.println("Goodbye!");
            }
        }
    }
}
