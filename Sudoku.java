import java.util.Random;
import java.util.Scanner;

public class Sudoku {
    private static final int SIZE = 9;
    private static final int EMPTY = 0;
    private static final Random random = new Random();
    private int[][] solutionGrid = new int[SIZE][SIZE];
    private int[][] puzzleGrid = new int[SIZE][SIZE];

    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku();
        sudoku.generateSolution();
        sudoku.createPuzzle();
        sudoku.showMenu();
    }

    // Generate a complete, valid Sudoku solution grid
    public void generateSolution() {
        fillGrid(solutionGrid, 0, 0);
    }

    // Recursive helper to fill the grid
    private boolean fillGrid(int[][] grid, int row, int col) {
        if (row == SIZE) return true; // Completed all rows

        if (grid[row][col] != EMPTY) // Skip already filled cells
            return fillGrid(grid, nextRow(row, col), nextCol(col));

        for (int num = 1; num <= SIZE; num++) {
            if (isSafe(grid, row, col, num)) {
                grid[row][col] = num;
                if (fillGrid(grid, nextRow(row, col), nextCol(col))) return true;
                grid[row][col] = EMPTY; // Backtrack
            }
        }
        return false; // No valid number found, backtrack
    }

    // Create a solvable puzzle by removing numbers
    public void createPuzzle() {
        // Copy solution to puzzle grid
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(solutionGrid[i], 0, puzzleGrid[i], 0, SIZE);
        }

        int cellsToRemove = SIZE * SIZE / 2; // Remove half the cells
        while (cellsToRemove > 0) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            if (puzzleGrid[row][col] != EMPTY) {
                puzzleGrid[row][col] = EMPTY;
                cellsToRemove--;
            }
        }
    }

    // Display any 9x9 grid (solution or puzzle)
    public void printGrid(int[][] grid) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                System.out.print(grid[row][col] == EMPTY ? " " : grid[row][col]);
                System.out.print(" ");
                if ((col + 1) % 3 == 0 && col + 1 != SIZE) System.out.print("| ");
            }
            System.out.println();
            if ((row + 1) % 3 == 0 && row + 1 != SIZE) {
                System.out.println("---------------------");
            }
        }
    }

    // Determine the next row and column
    private int nextRow(int row, int col) {
        return (col == SIZE - 1) ? row + 1 : row;
    }

    private int nextCol(int col) {
        return (col + 1) % SIZE;
    }

    // Check if placing num in grid[row][col] is valid
    private boolean isSafe(int[][] grid, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (grid[row][i] == num || grid[i][col] == num ||
                    grid[row - row % 3 + i / 3][col - col % 3 + i % 3] == num) {
                return false;
            }
        }
        return true;
    }

    // Show menu and handle user choices
    private void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n1. Show Solution");
            System.out.println("2. Generate New Puzzle");
            System.out.println("3. Quit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.println("Sudoku Solution:");
                    printGrid(solutionGrid);
                }
                case 2 -> {
                    generateSolution();
                    createPuzzle();
                    System.out.println("New Sudoku Puzzle:");
                    printGrid(puzzleGrid);
                }
                case 3 -> {
                    System.out.println("Exiting program.");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please choose again.");
            }
        }
        scanner.close();
    }
}
