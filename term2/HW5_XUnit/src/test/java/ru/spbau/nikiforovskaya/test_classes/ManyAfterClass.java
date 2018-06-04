package ru.spbau.nikiforovskaya.test_classes;

import ru.spbau.nikiforovskaya.my_junit.annotations.AfterClass;
import ru.spbau.nikiforovskaya.my_junit.annotations.Test;

public class ManyAfterClass {

    @AfterClass
    void tearDown1() {

    }

    @Test
    void test() {

    }

    @AfterClass
    void tearDown2() {

    }
}
