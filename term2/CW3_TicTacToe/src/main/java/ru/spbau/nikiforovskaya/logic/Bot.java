package ru.spbau.nikiforovskaya.logic;

import javafx.util.Pair;

/** An interface for bots playing in the game. */
public interface Bot {

    /**
     * Returns a position, where the bot makes a move.
     * @param field current game field
     * @param turnX if bot has to put x or o.
     * @return pair of two numbers - position in the field where to move.
     */
    Pair<Integer, Integer> makeMove(CellState[][] field, boolean turnX);
}
