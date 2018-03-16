package ru.spbau.nikiforovskaya.logic;

/**
 * Possible cell state in the game field.
 * Can be empty, x or o.
 */
public enum CellState {
    EMPTY, IS_X, IS_O;

    /**
     * A function to invert cell state.
     * @return empty, if cell was empty, x if there was o, and o if there was x.
     */
    public CellState invert() {
        switch (this) {
            case EMPTY: {
                return EMPTY;
            }
            case IS_X: {
                return IS_O;
            }
            default: {
                return IS_X;
            }
        }
    }
}
