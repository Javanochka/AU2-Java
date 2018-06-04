package ru.spbau.nikiforovskaya.my_junit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.spbau.nikiforovskaya.my_junit.exceptions.TooManyAnnotationsException;
import ru.spbau.nikiforovskaya.my_junit.exceptions.TooManySameAnnotatedMethodsException;
import ru.spbau.nikiforovskaya.test_classes.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class TestInvokerTest {

    private Method runner;
    private ByteArrayOutputStream output;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void initialize() throws NoSuchMethodException {
        Class tester = TestInvoker.class;
        runner = tester.getDeclaredMethod("runTestsInClass", Class.class);
        runner.setAccessible(true);

        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @Test
    void testMultipleAnnotations() {
        try {
            runner.invoke(null, MultipleAnnotations.class);
            assertTrue(false);
        } catch (InvocationTargetException e) {
            assertTrue(e.getCause().getClass().equals(TooManyAnnotationsException.class));
        } catch (Exception e) {
            assertTrue(false);
        }

    }

    @Test
    void testManyBeforeClass() {
        try {
            runner.invoke(null, ManyBeforeClass.class);
            assertTrue(false);
        } catch (InvocationTargetException e) {
            assertTrue(e.getCause().getClass().equals(TooManySameAnnotatedMethodsException.class));
            assertEquals("Too many methods annotated ru.spbau.nikiforovskaya.my_junit.annotations.BeforeClass", e.getCause().getMessage());
        } catch (Exception e) {
            assertTrue(false);
        }

    }

    @Test
    void testManyBefore() {
        try {
            runner.invoke(null, ManyBefore.class);
            assertTrue(false);
        } catch (InvocationTargetException e) {
            assertTrue(e.getCause().getClass().equals(TooManySameAnnotatedMethodsException.class));
            assertEquals("Too many methods annotated ru.spbau.nikiforovskaya.my_junit.annotations.Before", e.getCause().getMessage());
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void testManyAfterClass() {
        try {
            runner.invoke(null, ManyAfterClass.class);
            assertTrue(false);
        } catch (InvocationTargetException e) {
            assertTrue(e.getCause().getClass().equals(TooManySameAnnotatedMethodsException.class));
            assertEquals("Too many methods annotated ru.spbau.nikiforovskaya.my_junit.annotations.AfterClass", e.getCause().getMessage());
        } catch (Exception e) {
            assertTrue(false);
        }

    }

    @Test
    void testManyAfter() {
        try {
            runner.invoke(null, ManyAfter.class);
            assertTrue(false);
        } catch (InvocationTargetException e) {
            assertTrue(e.getCause().getClass().equals(TooManySameAnnotatedMethodsException.class));
            assertEquals("Too many methods annotated ru.spbau.nikiforovskaya.my_junit.annotations.After", e.getCause().getMessage());
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void testEmptyClass() throws InvocationTargetException, IllegalAccessException {
        runner.invoke(null, Empty.class);
        String result = output.toString();
        assertTrue(result.contains("Found 0 tests, ran 0 tests, 0 ignored, 0 failed."));
    }

    @Test
    void testIgnored() throws InvocationTargetException, IllegalAccessException {
        runner.invoke(null, IgnoredTest.class);
        String result = output.toString();
        assertTrue(result.contains("test passed"));
        assertTrue(result.contains("testIgnored ignored. Reason: bug in the test"));
        assertTrue(result.contains("Found 2 tests, ran 1 tests, 1 ignored, 0 failed."));
    }

    @Test
    void testThrowsReaction() throws InvocationTargetException, IllegalAccessException {
        runner.invoke(null, ThrowsReaction.class);
        String result = output.toString();
        assertTrue(result.contains("throwsTest passed"));
        assertTrue(result.contains("test passed"));
        assertTrue(result.contains("Found 2 tests, ran 2 tests, 0 ignored, 0 failed"));
    }

    @Test
    void testWrongThrows() throws InvocationTargetException, IllegalAccessException {
        runner.invoke(null, WrongThrows.class);
        String result = output.toString();
        assertTrue(result.contains("throwsTest failed. " +
                "The error type is java.lang.AssertionError, message is ha-ha."));
        assertTrue(result.contains("test passed"));
        assertTrue(result.contains("Found 2 tests, ran 2 tests, 0 ignored, 1 failed"));
    }

    @Test
    void testNoThrowWhenShouldBe() throws InvocationTargetException, IllegalAccessException {
        runner.invoke(null, ShoudThrowButNot.class);
        String result = output.toString();
        assertTrue(result.contains("throwsTest failed. " +
                "Expected exception: java.lang.AssertionError"));
        assertTrue(result.contains("test passed"));
        assertTrue(result.contains("Found 2 tests, ran 2 tests, 0 ignored, 1 failed"));
    }
}