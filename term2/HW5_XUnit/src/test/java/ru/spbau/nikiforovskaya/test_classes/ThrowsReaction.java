package ru.spbau.nikiforovskaya.test_classes;

import ru.spbau.nikiforovskaya.my_junit.annotations.Test;

public class ThrowsReaction {

    @Test
    void test() {

    }

    @Test(expected = MyVerySpecialException.class)
    void throwsTest() throws MyVerySpecialException {
        throw new MyVerySpecialException();
    }

    class MyVerySpecialException extends Exception {

    }
}
