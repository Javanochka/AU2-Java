package ru.spbau.nikiforovskaya.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldFunctionsTest {

    @Test
    void testGameResultFirstRowXWins() {
        CellState[][] field = new CellState[][] {
                {CellState.IS_X, CellState.IS_X, CellState.IS_X},
                {CellState.IS_O, CellState.IS_O, CellState.EMPTY},
                {CellState.EMPTY, CellState.EMPTY, CellState.EMPTY}
        };
        assertEquals(GameResult.X_WINS, FieldFunctions.checkGameResult(field));
    }

    @Test
    void testGameResultSecondRowXWins() {
        CellState[][] field = new CellState[][] {
                {CellState.IS_O, CellState.IS_O, CellState.EMPTY},
                {CellState.IS_X, CellState.IS_X, CellState.IS_X},
                {CellState.EMPTY, CellState.EMPTY, CellState.EMPTY}
        };
        assertEquals(GameResult.X_WINS, FieldFunctions.checkGameResult(field));
    }

    @Test
    void testGameResultThirdRowXWins() {
        CellState[][] field = new CellState[][] {
                {CellState.IS_O, CellState.IS_O, CellState.EMPTY},
                {CellState.EMPTY, CellState.EMPTY, CellState.EMPTY},
                {CellState.IS_X, CellState.IS_X, CellState.IS_X}
        };
        assertEquals(GameResult.X_WINS, FieldFunctions.checkGameResult(field));
    }

    @Test
    void testGameResultFirstRowOWins() {
        CellState[][] field = new CellState[][] {
                {CellState.IS_O, CellState.IS_O, CellState.IS_O},
                {CellState.IS_X, CellState.IS_X, CellState.EMPTY},
                {CellState.EMPTY, CellState.EMPTY, CellState.IS_X}
        };
        assertEquals(GameResult.O_WINS, FieldFunctions.checkGameResult(field));
    }

    @Test
    void testGameResultSecondRowOWins() {
        CellState[][] field = new CellState[][] {
                {CellState.IS_X, CellState.IS_X, CellState.EMPTY},
                {CellState.IS_O, CellState.IS_O, CellState.IS_O},
                {CellState.EMPTY, CellState.EMPTY, CellState.IS_X}
        };
        assertEquals(GameResult.O_WINS, FieldFunctions.checkGameResult(field));
    }

    @Test
    void testGameResultThirdRowOWins() {
        CellState[][] field = new CellState[][] {
                {CellState.IS_X, CellState.IS_X, CellState.EMPTY},
                {CellState.EMPTY, CellState.EMPTY, CellState.IS_X},
                {CellState.IS_O, CellState.IS_O, CellState.IS_O}
        };
        assertEquals(GameResult.O_WINS, FieldFunctions.checkGameResult(field));
    }

    @Test
    void testGameResultFirstColumnXWins() {
        CellState[][] field = new CellState[][] {
                {CellState.IS_X, CellState.IS_O, CellState.EMPTY},
                {CellState.IS_X, CellState.EMPTY, CellState.EMPTY},
                {CellState.IS_X, CellState.EMPTY, CellState.IS_O}
        };
        assertEquals(GameResult.X_WINS, FieldFunctions.checkGameResult(field));
    }

    @Test
    void testGameResultSecondColumnXWins() {
        CellState[][] field = new CellState[][] {
                {CellState.IS_O, CellState.IS_X, CellState.EMPTY},
                {CellState.EMPTY, CellState.IS_X, CellState.EMPTY},
                {CellState.EMPTY, CellState.IS_X, CellState.IS_O}
        };
        assertEquals(GameResult.X_WINS, FieldFunctions.checkGameResult(field));
    }

    @Test
    void testGameResultThirdColumnXWins() {
        CellState[][] field = new CellState[][] {
                {CellState.EMPTY, CellState.IS_O, CellState.IS_X},
                {CellState.EMPTY, CellState.EMPTY, CellState.IS_X},
                {CellState.IS_O, CellState.EMPTY, CellState.IS_X}
        };
        assertEquals(GameResult.X_WINS, FieldFunctions.checkGameResult(field));
    }

    @Test
    void testGameResultFirstColumnOWins() {
        CellState[][] field = new CellState[][] {
                {CellState.IS_O, CellState.IS_X, CellState.IS_X},
                {CellState.IS_O, CellState.EMPTY, CellState.EMPTY},
                {CellState.IS_O, CellState.EMPTY, CellState.IS_X}
        };
        assertEquals(GameResult.O_WINS, FieldFunctions.checkGameResult(field));
    }

    @Test
    void testGameResultSecondColumnOWins() {
        CellState[][] field = new CellState[][] {
                {CellState.IS_X, CellState.IS_O, CellState.IS_X},
                {CellState.EMPTY, CellState.IS_O, CellState.EMPTY},
                {CellState.EMPTY, CellState.IS_O, CellState.IS_X}
        };
        assertEquals(GameResult.O_WINS, FieldFunctions.checkGameResult(field));
    }

    @Test
    void testGameResultThirdColumnOWins() {
        CellState[][] field = new CellState[][] {
                {CellState.EMPTY, CellState.IS_X, CellState.IS_O},
                {CellState.EMPTY, CellState.EMPTY, CellState.IS_O},
                {CellState.IS_X, CellState.IS_X, CellState.IS_O}
        };
        assertEquals(GameResult.O_WINS, FieldFunctions.checkGameResult(field));
    }

    @Test
    void testGameResultLeftDiagonalXWins() {
        CellState[][] field = new CellState[][] {
                {CellState.IS_X, CellState.IS_O, CellState.EMPTY},
                {CellState.EMPTY, CellState.IS_X, CellState.EMPTY},
                {CellState.IS_O, CellState.EMPTY, CellState.IS_X}
        };
        assertEquals(GameResult.X_WINS, FieldFunctions.checkGameResult(field));
    }

    @Test
    void testGameResultRightDiagonalXWins() {
        CellState[][] field = new CellState[][] {
                {CellState.EMPTY, CellState.IS_O, CellState.IS_X},
                {CellState.EMPTY, CellState.IS_X, CellState.EMPTY},
                {CellState.IS_X, CellState.EMPTY, CellState.IS_O}
        };
        assertEquals(GameResult.X_WINS, FieldFunctions.checkGameResult(field));
    }

    @Test
    void testGameResultLeftDiagonalOWins() {
        CellState[][] field = new CellState[][] {
                {CellState.IS_O, CellState.IS_X, CellState.EMPTY},
                {CellState.EMPTY, CellState.IS_O, CellState.IS_X},
                {CellState.IS_X, CellState.EMPTY, CellState.IS_O}
        };
        assertEquals(GameResult.O_WINS, FieldFunctions.checkGameResult(field));
    }

    @Test
    void testGameResultRightDiagonalOWins() {
        CellState[][] field = new CellState[][] {
                {CellState.EMPTY, CellState.IS_X, CellState.IS_O},
                {CellState.IS_X, CellState.IS_O, CellState.EMPTY},
                {CellState.IS_O, CellState.EMPTY, CellState.IS_X}
        };
        assertEquals(GameResult.O_WINS, FieldFunctions.checkGameResult(field));
    }

    @Test
    void testGameResultEmptyIsProcess() {
        CellState[][] field = new CellState[][] {
                {CellState.EMPTY, CellState.EMPTY, CellState.EMPTY},
                {CellState.EMPTY, CellState.EMPTY, CellState.EMPTY},
                {CellState.EMPTY, CellState.EMPTY, CellState.EMPTY}
        };
        assertEquals(GameResult.PROCESS, FieldFunctions.checkGameResult(field));
    }

    @Test
    void testGameResultNonEmptyIsProcess() {
        CellState[][] field = new CellState[][] {
                {CellState.IS_X, CellState.EMPTY, CellState.EMPTY},
                {CellState.IS_O, CellState.IS_O, CellState.IS_X},
                {CellState.IS_X, CellState.EMPTY, CellState.EMPTY}
        };
        assertEquals(GameResult.PROCESS, FieldFunctions.checkGameResult(field));
    }

    @Test
    void testGameResultDraw() {
        CellState[][] field = new CellState[][] {
                {CellState.IS_X, CellState.IS_O, CellState.IS_X},
                {CellState.IS_O, CellState.IS_O, CellState.IS_X},
                {CellState.IS_X, CellState.IS_X, CellState.IS_O}
        };
        assertEquals(GameResult.DRAW, FieldFunctions.checkGameResult(field));
    }
}