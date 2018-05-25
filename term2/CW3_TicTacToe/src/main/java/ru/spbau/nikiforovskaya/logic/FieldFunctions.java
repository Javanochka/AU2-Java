package ru.spbau.nikiforovskaya.logic;

/** Class with useful functions to work with game field. */
public class FieldFunctions {

    /**
     * Checks if there is an empty cell in the field.
     * @param field current game field
     * @return {@code true}, if there's an empty cell, {@code false} otherwise.
     */
    private static boolean hasEmptyCell(CellState[][] field) {
        for (CellState[] row : field) {
            for (CellState a : row) {
                if (a == CellState.EMPTY) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int[][] d = new int[][]{{0, 1}, {1, 0}, {1, 1}, {-1, 1}};

    /**
     * Returns the result of the game.
     * (Who has won, or draw, or process)
     * @param field current game field
     * @return the result of the game.
     */
    public static GameResult checkGameResult(CellState[][] field) {
        for (int i = 0; i < field.length; i++) {
            if (field[i][0] != CellState.EMPTY) {
                for (int[] dir : d) {
                    if (i + 2 * dir[0] >= 3 || i + 2 * dir[0] < 0) {
                        continue;
                    }
                    if (field[i][0] != field[i + dir[0]][dir[1]]) {
                        continue;
                    }
                    if (field[i][0].equals(field[i + 2 * dir[0]][2 * dir[1]])) {
                        return field[i][0]== CellState.IS_X ? GameResult.X_WINS :
                                GameResult.O_WINS;
                    }
                }
            }
        }
        for (int i = 0; i < field.length; i++) {
            if (field[0][i] != CellState.EMPTY) {
                for (int[] dir : d) {
                    if (i + 2 * dir[1] >= 3 || i + 2 * dir[1] < 0 || dir[0] < 0) {
                        continue;
                    }
                    if (field[0][i] != field[dir[0]][i + dir[1]]) {
                        continue;
                    }
                    if (field[0][i] == field[2 * dir[0]][i + 2 * dir[1]]) {
                        return field[0][i] == CellState.IS_X ? GameResult.X_WINS :
                                GameResult.O_WINS;
                    }
                }
            }
        }
        if (hasEmptyCell(field)) {
            return GameResult.PROCESS;
        }
        return GameResult.DRAW;
    }
}
