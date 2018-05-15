package ru.spbau.nikiforovskaya.test_classes;

import ru.spbau.nikiforovskaya.my_junit.annotations.After;
import ru.spbau.nikiforovskaya.my_junit.annotations.Before;
import ru.spbau.nikiforovskaya.my_junit.annotations.Test;

public class ManyAfter {

    @After
    void tearDown1() {
    }

    @Test
    void test() {
    }

    @After
    void tearDown2() {

    }

}
