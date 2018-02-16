package ru.spbau.nikiforovskaya.util;

/** Injector exception which is thrown when couldn't find needed implementation. */
public class ImplementationNotFoundException extends Exception {

    public ImplementationNotFoundException() {
        super("Couldn't find needed class for object construction.");
    }
}
