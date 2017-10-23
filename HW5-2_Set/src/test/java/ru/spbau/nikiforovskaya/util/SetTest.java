package ru.spbau.nikiforovskaya.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class SetTest {


    Set<Integer> intSet;
    Set<String> stringSet;
    Random r;

    @BeforeEach
    void setUp() {
        intSet = new Set<>();
        stringSet = new Set<>();
        r = new Random(5);
    }

    @Test
    void testAddIntOnly() {
        intSet.add(1);
        assertArrayEquals(new Integer[]{1}, intSet.toArrayList().toArray());
    }

    @Test
    void testAddStringOnly() {
        stringSet.add("aba");
        assertArrayEquals(new String[]{"aba"}, stringSet.toArrayList().toArray());
    }

    @Test
    void testAddIntDuplicates() {
        intSet.add(1);
        intSet.add(1);
        assertArrayEquals(new Integer[]{1}, intSet.toArrayList().toArray());
    }

    @Test
    void testAddStringDuplicates() {
        stringSet.add("aba");
        stringSet.add("aba");
        assertArrayEquals(new String[]{"aba"}, stringSet.toArrayList().toArray());
    }

    @Test
    void testAddIntDifferent() {
        intSet.add(1);
        intSet.add(2);
        assertArrayEquals(new Integer[]{1, 2}, intSet.toArrayList().toArray());
    }

    @Test
    void testAddStringDifferent() {
        stringSet.add("aba");
        stringSet.add("acd");
        assertArrayEquals(new String[]{"aba", "acd"}, stringSet.toArrayList().toArray());
    }

    @Test
    void testContainsIntOnly() {
        intSet.add(1);
        assertTrue(intSet.contains(1));
        assertFalse(intSet.contains(3));
    }

    @Test
    void testContainsStringOnly() {
        stringSet.add("aba");
        assertTrue(stringSet.contains("aba"));
        assertFalse(stringSet.contains("other"));
    }

    @Test
    void testContainsIntDifferent() {
        intSet.add(1);
        intSet.add(2);
        assertTrue(intSet.contains(1));
        assertTrue(intSet.contains(2));
        assertFalse(intSet.contains(3));
    }

    @Test
    void testContainsStringDifferent() {
        stringSet.add("aba");
        stringSet.add("acd");
        assertTrue(stringSet.contains("aba"));
        assertTrue(stringSet.contains("acd"));
        assertFalse(stringSet.contains("abother"));
    }

    @Test
    void testSizeIntEmpty() {
        assertEquals(0, intSet.size());
    }

    @Test
    void testSizeStringEmpty() {
        assertEquals(0, stringSet.size());
    }

    @Test
    void testSizeIntOnly() {
        intSet.add(1);
        assertEquals(1, intSet.size());
    }

    @Test
    void testSizeStringOnly() {
        stringSet.add("aba");
        assertEquals(1, stringSet.size());
    }

    @Test
    void testSizeIntDuplicates() {
        intSet.add(1);
        intSet.add(1);
        assertEquals(1, intSet.size());
    }

    @Test
    void testSizeStringDuplicates() {
        stringSet.add("aba");
        stringSet.add("aba");
        assertEquals(1, stringSet.size());
    }

    @Test
    void testSizeIntDifferent() {
        intSet.add(1);
        intSet.add(2);
        assertEquals(2, intSet.size());
    }

    @Test
    void testSizeStringDifferent() {
        stringSet.add("aba");
        stringSet.add("acd");
        assertEquals(2, stringSet.size());
    }

    @Test
    void testToArrayListInt() {
        TreeSet<Integer> res = new TreeSet<>();
        for (int i = 0; i < 10; i++) {
            int t = r.nextInt(10);
            res.add(t);
            intSet.add(t);
        }
        assertArrayEquals(res.toArray(), intSet.toArrayList().toArray());
    }

    @Test
    void testToArrayListString() {
        TreeSet<String> res = new TreeSet<>();
        for (int i = 0; i < 10; i++) {
            int t = r.nextInt(10);
            res.add(Integer.toString(t));
            stringSet.add(Integer.toString(t));
        }
        assertArrayEquals(res.toArray(), stringSet.toArrayList().toArray());
    }

}