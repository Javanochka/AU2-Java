package ru.spbau.nikiforovskaya.test_classes;

import ru.spbau.nikiforovskaya.my_junit.annotations.BeforeClass;
import ru.spbau.nikiforovskaya.my_junit.annotations.Test;

public class ManyBeforeClass {

    @BeforeClass
    void setUp1() {

    }

    @Test
    void test() {

    }

    @BeforeClass
    void setUp2() {

    }
}
