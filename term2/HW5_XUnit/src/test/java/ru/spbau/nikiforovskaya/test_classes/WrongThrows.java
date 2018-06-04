package ru.spbau.nikiforovskaya.test_classes;

import ru.spbau.nikiforovskaya.my_junit.annotations.Test;

public class WrongThrows {

    @Test
    void test() {

    }

    @Test(expected = MyVerySpecialException.class)
    void throwsTest() {
        throw new AssertionError("ha-ha");
    }

    class MyVerySpecialException extends Exception {

    }
}
