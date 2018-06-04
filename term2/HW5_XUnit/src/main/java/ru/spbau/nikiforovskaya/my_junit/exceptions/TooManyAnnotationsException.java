package ru.spbau.nikiforovskaya.my_junit.exceptions;

/**
 * An exception, which thrown,
 * if there are too many my_junit annotations on the same method.
 */
public class TooManyAnnotationsException extends Exception {
    public TooManyAnnotationsException() {
        super("Too many my_junit annotations on the same method.");
    }
}
