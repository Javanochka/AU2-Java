package ru.spbau.nikiforovskaya.test_classes;

import ru.spbau.nikiforovskaya.my_junit.annotations.Test;

public class ShoudThrowButNot {

    @Test
    void test() {

    }

    @Test(expected = AssertionError.class)
    void throwsTest() {

    }
}
