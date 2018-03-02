package ru.spbau.nikiforovskaya.util;

import java.util.function.Function;

/**
 * An interface for the task wrapper
 * @param <T> a type of data which will be processed.
 */
public interface LightFuture<T> {

    /**
     * Checks if task is ready.
     * @return {@code true} if task is done, {@code false} otherwise
     */
    boolean isReady();

    /**
     * Returns the result of task calculation.
     * @return the result of calculation.
     * @throws LightExecutionException if there occurred
     */
    T get() throws LightExecutionException;

    /**
     * Applies the function to the result of previous calculation.
     * @param function a function to apply to the result.
     * @return a new task wrapper
     */
    LightFuture<T> thenApply(Function<T, T> function);

    /** Exception which is thrown when there occurred some problem during calculations. */
    class LightExecutionException extends Exception {
        LightExecutionException(Exception e) {
            super(e);
        }
    }
}
