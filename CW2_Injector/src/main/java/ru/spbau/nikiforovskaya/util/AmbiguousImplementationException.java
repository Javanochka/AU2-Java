package ru.spbau.nikiforovskaya.util;

/** Injector exception which is thrown when found too many implementations. */
public class AmbiguousImplementationException extends Exception {

    public AmbiguousImplementationException() {
        super("Found too many implementations of needed interface.");
    }
}
