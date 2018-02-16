package ru.spbau.nikiforovskaya.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MyTreeSetTest {

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

    private MyTreeSet<Integer> intSet;
    private MyTreeSet<Pair> pairSet;

    @BeforeEach
    void setUp() {
        intSet = new MyTreeSet<>();
        pairSet = new MyTreeSet<>((o1, o2) -> {
            if (o1.x == o2.x) {
                return Integer.compare(o1.y, o2.y);
            }
            return Integer.compare(o1.x, o2.x);
        });
    }

    @Test
    void testAddPairSetNoConstructor() {
        pairSet = new MyTreeSet<>();
        assertTrue(pairSet.add(new Pair(2, 3)));
        assertThrows(ClassCastException.class, ()->pairSet.add(new Pair(1, 5)));
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
    void testIteratorEmptyIntSet() {
        ArrayList<Integer> inside = new ArrayList<>();
        inside.addAll(intSet);
        assertArrayEquals(new Integer[] {}, inside.toArray());
    }

    @Test
    void testIteratorSeveralIntSet() {
        intSet.addAll(Arrays.asList(4, 8, 1, 9, 10, 2, 3, 6, 5, 7));
        ArrayList<Integer> inside = new ArrayList<>();
        inside.addAll(intSet);
        assertArrayEquals(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                inside.toArray());
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
    void testDescendingIteratorSeveralIntSet() {
        intSet.addAll(Arrays.asList(2, 1, 3));
        Iterator<Integer> it = intSet.descendingIterator();
        assertEquals(Integer.valueOf(3), it.next());
        assertEquals(Integer.valueOf(2), it.next());
        assertEquals(Integer.valueOf(1), it.next());
    }

    @Test
    void testDescendingIteratorManyIntSet() {
        intSet.addAll(Arrays.asList(4, 8, 1, 9, 10, 2, 3, 6, 5, 7));
        Iterator<Integer> it = intSet.descendingIterator();
        ArrayList<Integer> result = new ArrayList<>();
        while(it.hasNext()) {
            int p = it.next();
            result.add(p);
        }
        assertArrayEquals(new Integer[] {10, 9, 8, 7, 6, 5, 4, 3, 2, 1},
                result.toArray());
    }

    @Test
    void testDescendingSetOnlyElementPairSet() {
        pairSet.add(new Pair(2, 3));
        ru.spbau.litvinov.MyTreeSet<Pair> dPairSet = pairSet.descendingSet();
        assertArrayEquals(new Pair[] {new Pair(2, 3)}, dPairSet.toArray());
    }

    @Test
    void testDescendingSetSeveralAddsIntSet() {
        intSet.addAll(Arrays.asList(4, 1, 3, 2));
        ru.spbau.litvinov.MyTreeSet<Integer> dIntSet = intSet.descendingSet();
        assertArrayEquals(new Integer[] {4, 3, 2, 1}, dIntSet.toArray());
    }

    @Test
    void testDescendingSetAddsAfterCreateIntSet() {
        intSet.addAll(Arrays.asList(4, 1, 3, 2));
        ru.spbau.litvinov.MyTreeSet<Integer> dIntSet = intSet.descendingSet();
        assertTrue(dIntSet.add(5));
        assertFalse(dIntSet.add(2));
        assertArrayEquals(new Integer[] {5, 4, 3, 2, 1}, dIntSet.toArray());
    }

    @Test
    void testDescendingSetRemoveAfterCreateIntSet() {
        intSet.addAll(Arrays.asList(4, 1, 3, 2));
        ru.spbau.litvinov.MyTreeSet<Integer> dIntSet = intSet.descendingSet();
        assertTrue(dIntSet.remove(3));
        assertArrayEquals(new Integer[] {4, 2, 1}, dIntSet.toArray());
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
    void testFirstSeveralIntSet() {
        intSet.addAll(Arrays.asList(2, 4, 1, 3));
        assertEquals(Integer.valueOf(1), intSet.first());
    }

    @Test
    void testFirstSeveralAfterRemoveIntSet() {
        intSet.addAll(Arrays.asList(2, 4, 1, 3));
        intSet.remove(1);
        assertEquals(Integer.valueOf(2), intSet.first());
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
    void testLastSeveralIntSet() {
        intSet.addAll(Arrays.asList(2, 4, 1, 3));
        assertEquals(Integer.valueOf(4), intSet.last());
    }

    @Test
    void testLastSeveralAfterRemoveIntSet() {
        intSet.addAll(Arrays.asList(2, 4, 1, 3));
        intSet.remove(4);
        assertEquals(Integer.valueOf(3), intSet.last());
    }

    @Test
    void testLowerEmptyIntSet() {
        assertNull(intSet.lower(5));
    }

    @Test
    void testLowerSeveralIntSet() {
        intSet.addAll(Arrays.asList(3, 1, 2));
        assertEquals(Integer.valueOf(1), intSet.lower(2));
    }

    @Test
    void testLowerNoSuchElementIntSet() {
        intSet.addAll(Arrays.asList(3, 1));
        assertEquals(Integer.valueOf(1), intSet.lower(2));
    }

    @Test
    void testLowerManyIntSet() {
        intSet.addAll(Arrays.asList(4, 8, 1, 9, 10, 2, 3, 6, 5, 7));
        assertEquals(Integer.valueOf(3), intSet.lower(4));
        assertNull(intSet.lower(1));
        assertEquals(Integer.valueOf(6), intSet.lower(7));
    }

    @Test
    void testFloorEmptyIntSet() {
        assertNull(intSet.floor(5));
    }

    @Test
    void testFloorSeveralIntSet() {
        intSet.addAll(Arrays.asList(3, 1, 2));
        assertEquals(Integer.valueOf(2), intSet.floor(2));
    }

    @Test
    void testFloorNoSuchElementIntSet() {
        intSet.addAll(Arrays.asList(3, 1));
        assertEquals(Integer.valueOf(1), intSet.floor(2));
    }

    @Test
    void testFloorManyIntSet() {
        intSet.addAll(Arrays.asList(4, 8, 1, 9, 10, 2, 3, 6, 5, 7));
        assertEquals(Integer.valueOf(4), intSet.floor(4));
        assertNull(intSet.floor(0));
        assertEquals(Integer.valueOf(7), intSet.floor(7));
        assertEquals(Integer.valueOf(10), intSet.floor(11));
    }

    @Test
    void testCeilingEmptyIntSet() {
        assertNull(intSet.ceiling(5));
    }

    @Test
    void testCeilingSeveralIntSet() {
        intSet.addAll(Arrays.asList(3, 1, 2));
        assertEquals(Integer.valueOf(2), intSet.ceiling(2));
    }

    @Test
    void testCeilingNoSuchElementIntSet() {
        intSet.addAll(Arrays.asList(3, 1));
        assertEquals(Integer.valueOf(3), intSet.ceiling(2));
    }

    @Test
    void testCeilingManyIntSet() {
        intSet.addAll(Arrays.asList(4, 8, 1, 9, 10, 2, 3, 6, 5, 7));
        assertEquals(Integer.valueOf(4), intSet.ceiling(4));
        assertNull(intSet.ceiling(11));
        assertEquals(Integer.valueOf(7), intSet.ceiling(7));
        assertEquals(Integer.valueOf(1), intSet.ceiling(0));
    }

    @Test
    void testHigherEmptyPairSet() {
        assertNull(intSet.higher(5));
    }

    @Test
    void testHigherSeveralIntSet() {
        intSet.addAll(Arrays.asList(3, 1, 2));
        assertEquals(Integer.valueOf(3), intSet.higher(2));
    }

    @Test
    void testHigherNoSuchElementIntSet() {
        intSet.addAll(Arrays.asList(3, 1));
        assertEquals(Integer.valueOf(3), intSet.higher(2));
    }

    @Test
    void testHigherManyIntSet() {
        intSet.addAll(Arrays.asList(4, 8, 1, 9, 10, 2, 3, 6, 5, 7));
        assertEquals(Integer.valueOf(5), intSet.higher(4));
        assertNull(intSet.higher(10));
        assertEquals(Integer.valueOf(8), intSet.higher(7));
    }
}