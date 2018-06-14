package ru.spbau.nikiforovskaya.logic;

import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotFactoryTest {
    private Bot easyBot;
    private Bot hardBot;

    @BeforeEach
    void setUp() {
        easyBot = BotFactory.getEasyBot();
        hardBot = BotFactory.getHardBot();
    }

    @Test
    void testEasyBotMovesOnlyOnEmptyCells() {
        CellState[][] field = new CellState[][] {
                {CellState.EMPTY, CellState.IS_X, CellState.IS_O},
                {CellState.IS_X, CellState.IS_O, CellState.EMPTY},
                {CellState.EMPTY, CellState.EMPTY, CellState.EMPTY}
        };

        for (int i = 0; i < 10; i++) {
            Pair<Integer, Integer> move = easyBot.makeMove(field, true);
            assertEquals(CellState.EMPTY, field[move.getKey()][move.getValue()]);
            move = easyBot.makeMove(field, false);
            assertEquals(CellState.EMPTY, field[move.getKey()][move.getValue()]);
        }
    }

    private GameResult gameResult;
    private boolean turnX;
    @Test
    void testHardAndHardGivesDraw() {
        CellState[][] field = new CellState[][] {
                {CellState.EMPTY, CellState.EMPTY, CellState.EMPTY},
                {CellState.EMPTY, CellState.EMPTY, CellState.EMPTY},
                {CellState.EMPTY, CellState.EMPTY, CellState.EMPTY}
        };
        gameResult = GameResult.PROCESS;
        turnX = true;

        Thread bot1 = new Thread(() -> {
            while (!Thread.interrupted() && gameResult == GameResult.PROCESS) {
                synchronized (field) {
                    if (!turnX || gameResult != GameResult.PROCESS) {
                        Thread.yield();
                        continue;
                    }
                    Pair<Integer, Integer> turn = hardBot.makeMove(field, true);
                    int i = turn.getKey();
                    int j = turn.getValue();
                    field[i][j] = CellState.IS_X;
                    turnX ^= true;
                    gameResult = FieldFunctions.checkGameResult(field);
                }
            }
        });
        bot1.start();
        Thread bot2 = new Thread(() -> {
            while (!Thread.interrupted() && gameResult == GameResult.PROCESS) {
                synchronized (field) {
                    if (turnX || gameResult != GameResult.PROCESS) {
                        Thread.yield();
                        continue;
                    }
                    Pair<Integer, Integer> turn = hardBot.makeMove(field, false);
                    int i = turn.getKey();
                    int j = turn.getValue();
                    field[i][j] = CellState.IS_O;
                    turnX ^= true;
                    gameResult = FieldFunctions.checkGameResult(field);
                }
            }
        });
        bot2.start();
        while (gameResult == GameResult.PROCESS) {
            Thread.yield();
        }
        bot1.interrupt();
        bot2.interrupt();
        assertEquals(GameResult.DRAW, gameResult);
    }

    @Test
    void testHardNeverLosesToEasyHardStarts() {
        CellState[][] field = new CellState[][] {
                {CellState.EMPTY, CellState.EMPTY, CellState.EMPTY},
                {CellState.EMPTY, CellState.EMPTY, CellState.EMPTY},
                {CellState.EMPTY, CellState.EMPTY, CellState.EMPTY}
        };
        gameResult = GameResult.PROCESS;
        turnX = true;

        Thread bot1 = new Thread(() -> {
            while (!Thread.interrupted() && gameResult == GameResult.PROCESS) {
                synchronized (field) {
                    if (!turnX || gameResult != GameResult.PROCESS) {
                        Thread.yield();
                        continue;
                    }
                    Pair<Integer, Integer> turn = hardBot.makeMove(field, true);
                    int i = turn.getKey();
                    int j = turn.getValue();
                    field[i][j] = CellState.IS_X;
                    turnX ^= true;
                    gameResult = FieldFunctions.checkGameResult(field);
                }
            }
        });
        bot1.start();
        Thread bot2 = new Thread(() -> {
            while (!Thread.interrupted() && gameResult == GameResult.PROCESS) {
                synchronized (field) {
                    if (turnX || gameResult != GameResult.PROCESS) {
                        Thread.yield();
                        continue;
                    }
                    Pair<Integer, Integer> turn = easyBot.makeMove(field, false);
                    int i = turn.getKey();
                    int j = turn.getValue();
                    field[i][j] = CellState.IS_O;
                    turnX ^= true;
                    gameResult = FieldFunctions.checkGameResult(field);
                }
            }
        });
        bot2.start();
        while (gameResult == GameResult.PROCESS) {
            Thread.yield();
        }
        bot1.interrupt();
        bot2.interrupt();
        assertNotEquals(GameResult.O_WINS, gameResult);
    }

    @Test
    void testHardNeverLosesToEasyEasyStarts() {
        CellState[][] field = new CellState[][] {
                {CellState.EMPTY, CellState.EMPTY, CellState.EMPTY},
                {CellState.EMPTY, CellState.EMPTY, CellState.EMPTY},
                {CellState.EMPTY, CellState.EMPTY, CellState.EMPTY}
        };
        gameResult = GameResult.PROCESS;
        turnX = true;

        Thread bot1 = new Thread(() -> {
            while (!Thread.interrupted() && gameResult == GameResult.PROCESS) {
                synchronized (field) {
                    if (!turnX || gameResult != GameResult.PROCESS) {
                        Thread.yield();
                        continue;
                    }
                    Pair<Integer, Integer> turn = easyBot.makeMove(field, true);
                    int i = turn.getKey();
                    int j = turn.getValue();
                    field[i][j] = CellState.IS_X;
                    turnX ^= true;
                    gameResult = FieldFunctions.checkGameResult(field);
                }
            }
        });
        bot1.start();
        Thread bot2 = new Thread(() -> {
            while (!Thread.interrupted() && gameResult == GameResult.PROCESS) {
                synchronized (field) {
                    if (turnX || gameResult != GameResult.PROCESS) {
                        Thread.yield();
                        continue;
                    }
                    Pair<Integer, Integer> turn = hardBot.makeMove(field, false);
                    int i = turn.getKey();
                    int j = turn.getValue();
                    field[i][j] = CellState.IS_O;
                    turnX ^= true;
                    gameResult = FieldFunctions.checkGameResult(field);
                }
            }
        });
        bot2.start();
        while (gameResult == GameResult.PROCESS) {
            Thread.yield();
        }
        bot1.interrupt();
        bot2.interrupt();
        assertNotEquals(GameResult.X_WINS, gameResult);
    }
}