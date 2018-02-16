package ru.spbau.nikiforovskaya.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.spbau.nikiforovskaya.functional.Function1;
import ru.spbau.nikiforovskaya.functional.Function2;
import ru.spbau.nikiforovskaya.functional.Predicate;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CollectionsTest {

    private Function1<Integer, Integer> add3;
    private Function1<String, Integer> stringToInt;
    private Predicate<Integer> isEven;
    private Function2<Integer, Integer, Integer> sum;
    private Function2<Integer, Integer, Integer> div;
    private Function2<String, String, String> concat;

    @BeforeEach
    void setUp() {
        add3 = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer arg) {
                return arg + 3;
            }
        };
        stringToInt = new Function1<String, Integer>() {
            @Override
            public Integer apply(String arg) {
                return Integer.parseInt(arg);
            }
        };
        isEven = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg % 2 == 0;
            }
        };
        sum = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 + arg2;
            }
        };
        div = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 / arg2;
            }
        };
        concat = new Function2<String, String, String>() {
            @Override
            public String apply(String arg1, String arg2) {
                return arg1 + arg2;
            }
        };
    }

    @Test
    void testMapSameArgumentsAdd3() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(0);
        list.add(4);
        ArrayList<Integer> result = Collections.map(add3, list);
        assertArrayEquals(new Integer[] {5, 3, 7}, result.toArray());
    }

    @Test
    void testMapDifferentArgumentsStringToInt() {
        ArrayList<String> list = new ArrayList<>();
        list.add("24");
        list.add("566");
        list.add("-10");
        ArrayList<Integer> result = Collections.map(stringToInt, list);
        assertArrayEquals(new Integer[] {24, 566, -10}, result.toArray());
    }

    @Test
    void testMapEmptyListAdd3() {
        ArrayList<Integer> list = new ArrayList<>();
        ArrayList<Integer> result = Collections.map(add3, list);
        assertArrayEquals(new Integer[] {}, result.toArray());
    }

    @Test
    void testFilterNonConstantIsEven() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(4);
        list.add(1);
        list.add(2);
        list.add(3);
        ArrayList<Integer> result = Collections.filter(isEven, list);
        assertArrayEquals(new Integer[] {4, 2}, result.toArray());
    }

    @Test
    void testFilterConstantTrue() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(4);
        list.add(1);
        list.add(2);
        list.add(3);
        ArrayList<Integer> result = Collections.filter(Predicate.ALWAYS_TRUE(), list);
        assertArrayEquals(new Integer[] {4, 1, 2, 3}, result.toArray());
    }

    @Test
    void testFilterEmptyIsEven() {
        ArrayList<Integer> list = new ArrayList<>();
        ArrayList<Integer> result = Collections.filter(isEven, list);
        assertArrayEquals(new Integer[] {}, result.toArray());
    }

    @Test
    void testTakeWhileNonConstantIsEven() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(4);
        list.add(1);
        list.add(2);
        list.add(3);
        ArrayList<Integer> result = Collections.takeWhile(isEven, list);
        assertArrayEquals(new Integer[] {4}, result.toArray());
    }

    @Test
    void testTakeWhileConstantTrue() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(4);
        list.add(1);
        list.add(2);
        list.add(3);
        ArrayList<Integer> result = Collections.takeWhile(Predicate.ALWAYS_TRUE(), list);
        assertArrayEquals(new Integer[] {4, 1, 2, 3}, result.toArray());
    }

    @Test
    void testTakeWhileConstantFalse() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(4);
        list.add(1);
        list.add(2);
        list.add(3);
        ArrayList<Integer> result = Collections.takeWhile(Predicate.ALWAYS_FALSE(), list);
        assertArrayEquals(new Integer[] {}, result.toArray());
    }

    @Test
    void testTakeUnlessNonConstantIsEven() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(1);
        list.add(2);
        list.add(3);
        ArrayList<Integer> result = Collections.takeUnless(isEven, list);
        assertArrayEquals(new Integer[] {1, 1}, result.toArray());
    }

    @Test
    void testTakeUnlessConstantTrue() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(4);
        list.add(1);
        list.add(2);
        list.add(3);
        ArrayList<Integer> result = Collections.takeUnless(Predicate.ALWAYS_TRUE(), list);
        assertArrayEquals(new Integer[] {}, result.toArray());
    }

    @Test
    void testTakeUnlessConstantFalse() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(4);
        list.add(1);
        list.add(2);
        list.add(3);
        ArrayList<Integer> result = Collections.takeUnless(Predicate.ALWAYS_FALSE(), list);
        assertArrayEquals(new Integer[] {4, 1, 2, 3}, result.toArray());
    }

    @Test
    void testFoldlSum() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(4);
        list.add(1);
        list.add(2);
        list.add(3);
        int result = Collections.foldl(sum, 0, list);
        assertEquals(10, result);
    }

    @Test
    void testFoldlConcat() {
        ArrayList<String> list = new ArrayList<>();
        list.add("4");
        list.add("1");
        list.add("2");
        list.add("3");
        String result = Collections.foldl(concat, "", list);
        assertEquals("4123", result);
    }

    @Test
    void testFoldlDiv() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        int result = Collections.foldl(div, 12, list);
        assertEquals(2, result);
    }

    @Test
    void testFoldrSum() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(4);
        list.add(1);
        list.add(2);
        list.add(3);
        int result = Collections.foldr(sum, 0, list);
        assertEquals(10, result);
    }

    @Test
    void testFoldrConcat() {
        ArrayList<String> list = new ArrayList<>();
        list.add("4");
        list.add("1");
        list.add("2");
        list.add("3");
        String result = Collections.foldr(concat, "", list);
        assertEquals("4123", result);
    }

    @Test
    void testFoldrDiv() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(12);
        list.add(6);
        list.add(2);
        int result = Collections.foldr(div, 1, list);
        assertEquals(4, result);
    }
}