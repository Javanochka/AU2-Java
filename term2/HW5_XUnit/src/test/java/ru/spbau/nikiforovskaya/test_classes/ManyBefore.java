package ru.spbau.nikiforovskaya.test_classes;

import ru.spbau.nikiforovskaya.my_junit.annotations.Before;
import ru.spbau.nikiforovskaya.my_junit.annotations.BeforeClass;
import ru.spbau.nikiforovskaya.my_junit.annotations.Test;

public class ManyBefore {

    @Before
    void setUp1() {
    }

    @Test
    void test() {
    }

    @Before
    void setUp2() {

    }

}
