package ru.spbau.nikiforovskaya.my_junit.exceptions;

/**
 * Exception which is thrown, when something unexpected occurred,
 * during invoking annotated method in class.
 */
public class InvocationMethodException extends Exception {
    public InvocationMethodException(ReflectiveOperationException e) {
        super("Didn't manage to invoke testing methods.", e);
    }
}
