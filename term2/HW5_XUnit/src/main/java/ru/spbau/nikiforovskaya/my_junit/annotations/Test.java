package ru.spbau.nikiforovskaya.my_junit.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Test annotation.
 * Has arguments expected and ignore.
 * If ignore is EMPTY, does not run the test.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {

    /** A default empty ignore reason. */
    String EMPTY = "";

    /**
     * Returns a throwable which is expected to be thrown from test.
     * @return a throwable which is expected to be thrown from test.
     */
    Class<? extends Throwable> expected() default DefaultException.class;

    /**
     * Returns an ignore reason.
     * @return an ignore reason.
     */
    String ignore() default EMPTY;

    /** A default exception in expected. */
    class DefaultException extends Exception {

    }
}
