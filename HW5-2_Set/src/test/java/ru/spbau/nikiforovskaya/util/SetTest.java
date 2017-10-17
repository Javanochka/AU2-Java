package ru.spbau.nikiforovskaya.util;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class SetTest {


    Set<Integer> intSet;
    Set<String> stringSet;
    Random r;

    @Before
    public void setUp() {
        intSet = new Set<>();
        stringSet = new Set<>();
        r = new Random(5);
    }

    @Test
    public void testAddIntOnly() {
        intSet.add(1);
        assertArrayEquals(new Integer[]{1}, intSet.toArrayList().toArray());
    }

    @Test
    public void testAddStringOnly() {
        stringSet.add("aba");
        assertArrayEquals(new String[]{"aba"}, stringSet.toArrayList().toArray());
    }

    @Test
    public void testAddIntDuplicates() {
        intSet.add(1);
        intSet.add(1);
        assertArrayEquals(new Integer[]{1}, intSet.toArrayList().toArray());
    }

    @Test
    public void testAddStringDuplicates() {
        stringSet.add("aba");
        stringSet.add("aba");
        assertArrayEquals(new String[]{"aba"}, stringSet.toArrayList().toArray());
    }

    @Test
    public void testAddIntDifferent() {
        intSet.add(1);
        intSet.add(2);
        assertArrayEquals(new Integer[]{1, 2}, intSet.toArrayList().toArray());
    }

    @Test
    public void testAddStringDifferent() {
        stringSet.add("aba");
        stringSet.add("acd");
        assertArrayEquals(new String[]{"aba", "acd"}, stringSet.toArrayList().toArray());
    }

    @Test
    public void testContainsIntOnly() {
        intSet.add(1);
        assertTrue(intSet.contains(1));
        assertFalse(intSet.contains(3));
    }

    @Test
    public void testContainsStringOnly() {
        stringSet.add("aba");
        assertTrue(stringSet.contains("aba"));
        assertFalse(stringSet.contains("other"));
    }

    @Test
    public void testContainsIntDifferent() {
        intSet.add(1);
        intSet.add(2);
        assertTrue(intSet.contains(1));
        assertTrue(intSet.contains(2));
        assertFalse(intSet.contains(3));
    }

    @Test
    public void testContainsStringDifferent() {
        stringSet.add("aba");
        stringSet.add("acd");
        assertTrue(stringSet.contains("aba"));
        assertTrue(stringSet.contains("acd"));
        assertFalse(stringSet.contains("abother"));
    }

    @Test
    public void testSizeIntEmpty() {
        assertEquals(0, intSet.size());
    }

    @Test
    public void testSizeStringEmpty() {
        assertEquals(0, stringSet.size());
    }

    @Test
    public void testSizeIntOnly() {
        intSet.add(1);
        assertEquals(1, intSet.size());
    }

    @Test
    public void testSizeStringOnly() {
        stringSet.add("aba");
        assertEquals(1, stringSet.size());
    }

    @Test
    public void testSizeIntDuplicates() {
        intSet.add(1);
        intSet.add(1);
        assertEquals(1, intSet.size());
    }

    @Test
    public void testSizeStringDuplicates() {
        stringSet.add("aba");
        stringSet.add("aba");
        assertEquals(1, stringSet.size());
    }

    @Test
    public void testSizeIntDifferent() {
        intSet.add(1);
        intSet.add(2);
        assertEquals(2, intSet.size());
    }

    @Test
    public void testSizeStringDifferent() {
        stringSet.add("aba");
        stringSet.add("acd");
        assertEquals(2, stringSet.size());
    }

    @Test
    public void testToArrayListInt() {
        TreeSet<Integer> res = new TreeSet<>();
        for (int i = 0; i < 10; i++) {
            int t = r.nextInt(10);
            res.add(t);
            intSet.add(t);
        }
        assertArrayEquals(res.toArray(), intSet.toArrayList().toArray());
    }

    @Test
    public void testToArrayListString() {
        TreeSet<String> res = new TreeSet<>();
        for (int i = 0; i < 10; i++) {
            int t = r.nextInt(10);
            res.add(Integer.toString(t));
            stringSet.add(Integer.toString(t));
        }
        assertArrayEquals(res.toArray(), stringSet.toArrayList().toArray());
    }

}