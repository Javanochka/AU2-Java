package ru.spbau.nikiforovskaya.my_junit.exceptions;

/**
 * An exception, which is thrown,
 * when there are several same annotated with Before, After, BeforeClass or AfterClass
 * methods in the class.
 */
public class TooManySameAnnotatedMethodsException extends Exception {
    public TooManySameAnnotatedMethodsException(Class clazz) {
        super("Too many methods annotated " + clazz.getName());
    }
}
