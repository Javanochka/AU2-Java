package ru.spbau.nikiforovskaya.util;

/**
 * MaybeException, which is thrown when
 * you try to use non existing object in Maybe.
 */
public class MaybeException extends Exception {

    public MaybeException() {}

    // Constructor that accepts a message
    public MaybeException(String message)
    {
        super(message);
    }
}
