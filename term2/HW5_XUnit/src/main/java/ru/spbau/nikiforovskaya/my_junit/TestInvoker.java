package ru.spbau.nikiforovskaya.my_junit;

import ru.spbau.nikiforovskaya.my_junit.annotations.*;
import ru.spbau.nikiforovskaya.my_junit.exceptions.InvocationMethodException;
import ru.spbau.nikiforovskaya.my_junit.exceptions.TooManyAnnotationsException;
import ru.spbau.nikiforovskaya.my_junit.exceptions.TooManySameAnnotatedMethodsException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * TestInvoker can invoke all the tests in the given class.
 * Note, that before all tests there will be invoked a method with annotation BeforeClass, if exists.
 * Before each test there will be invoked a method with annotation Before, if exists.
 * After each test there will be invoked a method with annotation After, if exists.
 * After all tests there will be invoked a method with annotation AfterClass, if exists.
 */
public class TestInvoker {

    /**
     * Runs the invoker, pass the name of the class to arguments.
     * @param args args[0] -- name of the class, where the tests are.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Pass name of the class with tests as argument.");
            return;
        }

        Class testClass;

        try {
            testClass = Class.forName(args[0]);
        } catch (ClassNotFoundException e) {
            System.out.println("Didn't found the class. Try again.");
            return;
        }

        try {
            runTestsInClass(testClass);
        } catch (TooManySameAnnotatedMethodsException | TooManyAnnotationsException e) {
            System.out.println(e.getMessage());
        } catch (InvocationMethodException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause().getMessage());
        }
    }

    private static Method before;
    private static Method beforeClass;
    private static Method after;
    private static Method afterClass;

    private static ArrayList<TestMethod> tests;

    private static void runTestsInClass(Class testClass)
            throws TooManySameAnnotatedMethodsException, TooManyAnnotationsException,
            InvocationMethodException {
        tests = new ArrayList<>();
        before = null;
        beforeClass = null;
        after = null;
        afterClass = null;
        classifyMethods(testClass);
        try {
            invokeTests(testClass);
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new InvocationMethodException(e);
        }
    }

    private static void classifyMethods(Class testClass)
            throws TooManySameAnnotatedMethodsException, TooManyAnnotationsException {
        for (Method method : testClass.getDeclaredMethods()) {
            method.setAccessible(true);
            int numberOfAnnotations = 0;
            if (method.getAnnotation(Test.class) != null) {
                Test info = method.getAnnotation(Test.class);
                tests.add(new TestMethod(method, info.expected(), info.ignore()));
                numberOfAnnotations++;
            }
            if (method.getAnnotation(BeforeClass.class) != null) {
                if (beforeClass != null) {
                    throw new TooManySameAnnotatedMethodsException(BeforeClass.class);
                }
                beforeClass = method;
                numberOfAnnotations++;
            }
            if (method.getAnnotation(Before.class) != null) {
                if (before != null) {
                    throw new TooManySameAnnotatedMethodsException(Before.class);
                }
                before = method;
                numberOfAnnotations++;
            }
            if (method.getAnnotation(AfterClass.class) != null) {
                if (afterClass != null) {
                    throw new TooManySameAnnotatedMethodsException(AfterClass.class);
                }
                afterClass = method;
                numberOfAnnotations++;
            }
            if (method.getAnnotation(After.class) != null) {
                if (after != null) {
                    throw new TooManySameAnnotatedMethodsException(After.class);
                }
                after = method;
                numberOfAnnotations++;
            }
            if (numberOfAnnotations > 1) {
                throw new TooManyAnnotationsException();
            }
        }
    }

    private static void invokeTests(Class testClass)
            throws InvocationTargetException, IllegalAccessException, InstantiationException {
        System.out.println("Testing started.\n");

        if (beforeClass != null) {
            beforeClass.invoke(testClass.newInstance());
        }

        int numberAllTests = tests.size();
        int ignoredTests = 0;
        int failedTests = 0;
        long sumTime = 0;

        for (TestMethod test : tests) {
            if (before != null) {
                System.err.println(before);
                System.err.println(testClass.getName());
                before.invoke(testClass.newInstance());
            }

            if (!test.ignoreReason.equals(Test.EMPTY)) {
                System.out.printf("Test %s ignored. Reason: %s\n", test.method.getName(),
                        test.ignoreReason);
                ignoredTests++;
            } else {
                System.out.printf("Test %s started.\n", test.method.getName());
                long testTime = System.currentTimeMillis();
                boolean wasException = false;
                try {
                    test.method.invoke(testClass.newInstance());
                } catch (InvocationTargetException error) {
                    wasException = true;
                    if (!error.getCause().getClass().equals(test.throwable)) {
                        System.out.printf("Test %s failed. The error type is %s, message is %s.\n",
                                test.method.getName(), error.getCause().getClass().getName(),
                                error.getCause().getMessage());
                        failedTests++;
                    } else {
                        System.out.printf("Test %s passed.\n", test.method.getName());
                    }
                }
                if (!wasException) {
                    if (test.throwable.equals(Test.DefaultException.class)) {
                        System.out.printf("Test %s passed.\n", test.method.getName());
                    } else {
                        failedTests++;
                        System.out.printf("Test %s failed. Expected exception: %s\n",
                                test.method.getName(), test.throwable.getName());
                    }
                }
                testTime = System.currentTimeMillis() - testTime;
                System.out.printf("Test %s finished in %d milliseconds.\n\n",
                        test.method.getName(), testTime);
                sumTime += testTime;
            }

            if (after != null) {
                after.invoke(testClass.newInstance());
            }
        }

        if (afterClass != null) {
            afterClass.invoke(testClass.newInstance());
        }

        System.out.println();
        System.out.printf("Found %d tests, ran %d tests, %d ignored, %d failed. Finished in %d milliseconds.\n",
                numberAllTests, numberAllTests - ignoredTests, ignoredTests, failedTests, sumTime);
    }


    private static class TestMethod {
        private Method method;
        private Class<? extends Throwable> throwable;
        private String ignoreReason;

        private TestMethod(Method method, Class<? extends Throwable> throwable,
                           String ignoreReason) {
            this.method = method;
            this.throwable = throwable;
            this.ignoreReason = ignoreReason;
        }
    }
}
