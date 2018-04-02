package ru.spbau.nikiforovskaya.logic;

import javafx.util.Pair;

import java.util.Random;

/** Class, producing bots for game. */
public class BotFactory {

    /**
     * Returns easy bot, which moves randomly.
     * @return easy bot, which moves randomly.
     */
    public static Bot getEasyBot() {
        return new Bot() {

            Random r = new Random();

            @Override
            public Pair<Integer, Integer> makeMove(CellState[][] field, boolean turnX) {
                int numberOfFree = 0;
                for (CellState[] row : field) {
                    for (CellState cell : row) {
                        if (cell == CellState.EMPTY) {
                            numberOfFree++;
                        }
                    }
                }
                if (numberOfFree == 0) {
                    return null;
                }
                int position = r.nextInt(numberOfFree);
                int i = 0;
                for (int r = 0; r < field.length; r++) {
                    for (int c = 0; c < field[r].length; c++) {
                        if (field[r][c] == CellState.EMPTY) {
                            if (i == position) {
                                return new Pair<>(r, c);
                            }
                            i++;
                        }
                    }
                }
                return null;
            }
        };
    }

    /**
     * Returns hard bot, which moves the best way.
     * Never loses.
     * @return hard bot, which moves the best way.
     */
    public static Bot getHardBot() {
        return (field, turnX) -> {
            int[] result = dfsDecide(field, turnX ? CellState.IS_X : CellState.IS_O);
            return new Pair<>(result[1], result[2]);
        };
    }

    private static int[] dfsDecide(CellState[][] current, CellState toPut) {
        int maxResult = -1;
        int mi = -1;
        int mj = -1;
        for (int i = 0; i < current.length; i++) {
            for (int j = 0; j < current[i].length; j++) {
                if (current[i][j] == CellState.EMPTY) {
                    current[i][j] = toPut;
                    GameResult result = FieldFunctions.checkGameResult(current);
                    int[] res = null;
                    if (result == GameResult.PROCESS) {
                        res = dfsDecide(current, toPut.invert());
                    }
                    current[i][j] = CellState.EMPTY;
                    int resValue = result == GameResult.PROCESS ? res[0] :
                            result == GameResult.DRAW ? 1 : 2;
                    if (resValue > maxResult) {
                        maxResult = resValue;
                        mi = i;
                        mj = j;
                    }
                }
            }
        }
        return new int[] {2 - maxResult, mi, mj};
    }
}
