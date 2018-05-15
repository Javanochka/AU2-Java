package ru.spbau.nikiforovskaya.test_classes;

import ru.spbau.nikiforovskaya.my_junit.annotations.Test;

public class IgnoredTest {

    @Test
    void test() {

    }

    @Test(ignore = "bug in the test")
    void testIgnored() {
        throw new AssertionError();
    }
}
