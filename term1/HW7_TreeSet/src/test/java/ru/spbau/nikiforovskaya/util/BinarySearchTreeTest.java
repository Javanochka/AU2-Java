package ru.spbau.nikiforovskaya.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTreeTest {

    private class Pair {
        int x;
        int y;

        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (!o.getClass().isInstance(this)) {
                return false;
            }
            Pair po = (Pair)o;
            return po.x == x && po.y == y;
        }

        @Override
        public String toString() {
            return "(" + x + ", "+ y + ")";
        }
    }

    private BinarySearchTree<Integer> intSet;
    private BinarySearchTree<Pair> pairSet;

    @BeforeEach
    void setUp() {
        intSet = new BinarySearchTree<>(Integer::compareTo);
        pairSet = new BinarySearchTree<>((o1, o2) -> {
            if (o1.x == o2.x) {
                return Integer.compare(o1.y, o2.y);
            }
            return Integer.compare(o1.x, o2.x);
        });
    }

    @Test
    void testAddOnlyIntSet() {
        assertTrue(intSet.add(3));
        assertArrayEquals(new Integer[]{3}, intSet.toArray());
    }

    @Test
    void testAddOnlyPairSet() {
        assertTrue(pairSet.add(new Pair(1, 2)));
        assertArrayEquals(new Pair[]{new Pair(1, 2)}, pairSet.toArray());
    }

    @Test
    void testAddTwiceTheSameIntSet() {
        assertTrue(intSet.add(3));
        assertFalse(intSet.add(3));
        assertArrayEquals(new Integer[]{3}, intSet.toArray());
    }

    @Test
    void testAddTwiceTheSamePairSet() {
        assertTrue(pairSet.add(new Pair(1, 2)));
        assertFalse(pairSet.add(new Pair(1, 2)));
        assertArrayEquals(new Pair[]{new Pair(1, 2)}, pairSet.toArray());
    }

    @Test
    void testAddSeveralIntSet() {
        assertTrue(intSet.add(3));
        assertTrue(intSet.add(-3));
        assertTrue(intSet.add(1));
        assertArrayEquals(new Integer[]{-3, 1, 3}, intSet.toArray());
    }

    @Test
    void testAddSeveralPairSet() {
        assertTrue(pairSet.add(new Pair(1, 2)));
        assertTrue(pairSet.add(new Pair(-1, -1)));
        assertTrue(pairSet.add(new Pair(-1, 0)));
        assertArrayEquals(new Pair[]{
                new Pair(-1, -1),
                new Pair(-1, 0),
                new Pair(1, 2)
        }, pairSet.toArray());
    }

    @Test
    void testContainsInEmptyIntSet() {
        assertFalse(intSet.contains(2));
        assertFalse(intSet.contains(0));
        assertFalse(intSet.contains(-3));
    }

    @Test
    void testContainsInEmptyPairSet() {
        assertFalse(pairSet.contains(new Pair(2, 3)));
        assertFalse(pairSet.contains(new Pair(3, -1)));
        assertFalse(pairSet.contains(new Pair(-4, 0)));
    }

    @Test
    void testContainsOnlyIntSet() {
        intSet.add(0);
        assertFalse(intSet.contains(2));
        assertTrue(intSet.contains(0));
        assertFalse(intSet.contains(-3));
    }

    @Test
    void testContainsOnlyPairSet() {
        pairSet.add(new Pair(2, 3));
        assertTrue(pairSet.contains(new Pair(2, 3)));
        assertFalse(pairSet.contains(new Pair(3, -1)));
        assertFalse(pairSet.contains(new Pair(-4, 0)));
    }

    @Test
    void testContainsSeveralIntSet() {
        intSet.add(-3);
        intSet.add(-1);
        intSet.add(5);
        intSet.add(0);
        assertFalse(intSet.contains(2));
        assertTrue(intSet.contains(0));
        assertTrue(intSet.contains(-3));
        assertFalse(intSet.contains(4));
        assertTrue(intSet.contains(-1));
        assertTrue(intSet.contains(5));
    }

    @Test
    void testContainsSeveralPairSet() {
        pairSet.add(new Pair(2, 3));
        pairSet.add(new Pair(-4, 1));
        pairSet.add(new Pair(0, 2));
        pairSet.add(new Pair(1, 1));
        assertTrue(pairSet.contains(new Pair(2, 3)));
        assertTrue(pairSet.contains(new Pair(-4, 1)));
        assertTrue(pairSet.contains(new Pair(0, 2)));
        assertTrue(pairSet.contains(new Pair(1, 1)));
        assertFalse(pairSet.contains(new Pair(3, -1)));
        assertFalse(pairSet.contains(new Pair(-4, 0)));
    }

    @Test
    void testRemoveEmptyIntSet() {
        assertFalse(intSet.remove(5));
    }

    @Test
    void testRemoveEmptyPairSet() {
        assertFalse(pairSet.remove(new Pair(2, 3)));
    }

    @Test
    void testRemoveTheOnlyIntSet() {
        intSet.add(5);
        assertTrue(intSet.remove(5));
        assertArrayEquals(new Integer[] {}, intSet.toArray());
    }

    @Test
    void testRemoveTheOnlyPairSet() {
        pairSet.add(new Pair(2, 3));
        assertTrue(pairSet.remove(new Pair(2, 3)));
        assertArrayEquals(new Pair[] {}, pairSet.toArray());
    }


    @Test
    void testRemoveFromTwoElementIntSet() {
        intSet.add(5);
        intSet.add(-3);
        assertTrue(intSet.remove(-3));
        assertArrayEquals(new Integer[] {5}, intSet.toArray());
    }

    @Test
    void testRemoveFromSeveralIntSet() {
        intSet.add(5);
        intSet.add(-3);
        intSet.add(2);
        assertTrue(intSet.remove(2));
        assertArrayEquals(new Integer[] {-3, 5}, intSet.toArray());
    }

    @Test
    void testRemoveFromSeveralPairSet() {
        pairSet.add(new Pair(-2, 0));
        pairSet.add(new Pair(2, 3));
        pairSet.add(new Pair(0, 1));
        assertTrue(pairSet.remove(new Pair(2, 3)));
        assertArrayEquals(new Pair[] {
                new Pair(-2, 0),
                new Pair(0, 1)
        }, pairSet.toArray());
    }

    @Test
    void testRemoveFromManyIntSet() {
        int[] order = new int[] {4, 8, 1, 9, 10, 2, 3, 6, 5, 7};
        for (int t : order) {
            intSet.add(t);
        }

        assertTrue(intSet.remove(2));
        assertTrue(intSet.remove(5));
        assertTrue(intSet.remove(1));
        assertTrue(intSet.remove(8));
        assertArrayEquals(new Integer[] {3, 4, 6, 7, 9, 10}, intSet.toArray());
    }

    @Test
    void testSizeEmptyPairSet() {
        assertEquals(0, pairSet.size());
    }

    @Test
    void testSizeSeveralAddsIntSet() {
        for (int i = 0; i < 10; i++) {
            intSet.add(i);
            assertEquals(i + 1, intSet.size());
        }
    }

    @Test
    void testSizeSeveralAddsAndRemovesIntSet() {
        intSet.add(2);
        intSet.add(1);
        assertEquals(2, intSet.size());
        intSet.remove(2);
        assertEquals(1, intSet.size());
        intSet.add(5);
        intSet.add(7);
        intSet.add(-1);
        assertEquals(4, intSet.size());
        intSet.remove(5);
        assertEquals(3, intSet.size());
    }

    @Test
    void testDescendingIteratorEmptyPairSet() {
        Iterator<Pair> it = pairSet.descendingIterator();
        ArrayList<Pair> result = new ArrayList<>();
        while(it.hasNext()) {
            Pair p = it.next();
            result.add(p);
        }
        assertArrayEquals(new Pair[] {}, result.toArray());
    }

    @Test
    void testDescendingIteratorOnlyIntSet() {
        intSet.add(2);
        Iterator<Integer> it = intSet.descendingIterator();
        ArrayList<Integer> result = new ArrayList<>();
        while(it.hasNext()) {
            int p = it.next();
            result.add(p);
        }
        assertArrayEquals(new Integer[] {2}, result.toArray());
    }

    @Test
    void testFirstEmptyPairSet() {
        assertNull(pairSet.first());
    }

    @Test
    void testFirstTheOnlyPairSet() {
        pairSet.add(new Pair(2, 3));
        assertEquals(new Pair(2, 3), pairSet.first());
    }

    @Test
    void testLastEmptyPairSet() {
        assertNull(pairSet.last());
    }

    @Test
    void testLastTheOnlyPairSet() {
        pairSet.add(new Pair(2, 3));
        assertEquals(new Pair(2, 3), pairSet.last());
    }

    @Test
    void testLowerEmptyIntSet() {
        assertNull(intSet.lower(5));
    }

    @Test
    void testLowerSeveralIntSet() {
        intSet.add(6);
        intSet.add(2);
        intSet.add(3);
        assertEquals(Integer.valueOf(3), intSet.lower(5));
    }

    @Test
    void testFloorEmptyIntSet() {
        assertNull(intSet.floor(5));
    }

    @Test
    void testFloorSeveralIntSet() {
        intSet.add(6);
        intSet.add(2);
        intSet.add(3);
        assertEquals(Integer.valueOf(3), intSet.floor(3));
    }

    @Test
    void testCeilingEmptyIntSet() {
        assertNull(intSet.ceiling(5));
    }

    @Test
    void testCeilingSeveralIntSet() {
        intSet.add(6);
        intSet.add(2);
        intSet.add(3);
        assertEquals(Integer.valueOf(6), intSet.ceiling(5));
    }

    @Test
    void testHigherEmptyIntSet() {
        assertNull(intSet.higher(5));
    }

    @Test
    void testHigherSeveralIntSet() {
        intSet.add(6);
        intSet.add(2);
        intSet.add(3);
        assertEquals(Integer.valueOf(6), intSet.higher(3));
    }
}