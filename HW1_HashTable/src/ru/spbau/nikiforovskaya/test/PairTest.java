package ru.spbau.nikiforovskaya.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.spbau.nikiforovskaya.util.Pair;
import static org.junit.jupiter.api.Assertions.*;

class PairTest {

    Pair p;

    @BeforeEach
    void setUp() {
        p = new Pair("aba", "dudo");
    }

    @Test
    void testGetKey() {
        assertEquals("aba", p.getKey());
    }

    @Test
    void testGetValue() {
        assertEquals("dudo", p.getValue());
    }
}