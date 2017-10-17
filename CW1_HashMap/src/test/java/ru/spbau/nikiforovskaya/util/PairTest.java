package ru.spbau.nikiforovskaya.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {
    Pair<String, String> p;

    @BeforeEach
    void setUp() {
        p = new Pair<String, String>("aba", "dudo");
    }

    @Test
    void testGetKey() {
        assertEquals("aba", p.getKey());
    }

    @Test
    void testGetValue() {
        assertEquals("dudo", p.getValue());
    }

    @Test
    void testSetValue() {
        p.setValue("meow");
        assertEquals(p.getValue(), "meow");
    }
}