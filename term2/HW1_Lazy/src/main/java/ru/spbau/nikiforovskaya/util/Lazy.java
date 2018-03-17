package ru.spbau.nikiforovskaya.util;

/**
 * An interface for lazy calculations.
 * @param <T> is the result to be stored
 */
public interface Lazy<T> {

    /**
     * Returns the result of calculation, which is done only once.
     * @return the result of calculation
     */
    T get();
}
