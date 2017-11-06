package ru.spbau.nikiforovskaya.util;

/**
 * MaybeException, which is thrown when
 * you try to use non existing object in Maybe.
 */
public class MaybeNoValueException extends Exception {

    public MaybeNoValueException() {}

    // Constructor that accepts a message
    public MaybeNoValueException(String message)
    {
        super(message);
    }
}
