package ru.spbau.nikiforovskaya.functional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Function2Test {

    private Function1<Integer, Integer> mul3;
    private Function2<Integer, Integer, Integer> sum;
    private Function2<String, String, String> concat;
    private Function1<String, Integer> length;

    @BeforeEach
    void setUp() {
        mul3 = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer arg) {
                return arg * 3;
            }
        };
        sum = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 + arg2;
            }
        };
        concat = new Function2<String, String, String>() {
            @Override
            public String apply(String arg1, String arg2) {
                return arg1 + arg2;
            }
        };
        length = new Function1<String, Integer>() {
            @Override
            public Integer apply(String arg) {
                return arg.length();
            }
        };
    }

    @Test
    void testComposeSameParameterSumMul3() {
        Function2<Integer, Integer, Integer> result = sum.compose(mul3);
        assertEquals(Integer.valueOf(15), result.apply(2, 3));
        assertEquals(Integer.valueOf(3), result.apply(1, 0));
        assertEquals(Integer.valueOf(24), result.apply(3, 5));
    }

    @Test
    void testComposeDifferentParameterConcatLength() {
        Function2<String, String, Integer> result = concat.compose(length);
        assertEquals(Integer.valueOf(7), result.apply("aba", "caba"));
        assertEquals(Integer.valueOf(0), result.apply("", ""));
        assertEquals(Integer.valueOf(6), result.apply("abuu", "bu"));
    }

    @Test
    void testBind1Sum() {
        Function1<Integer, Integer> result = sum.bind1(5);
        assertEquals(Integer.valueOf(6), result.apply(1));
        assertEquals(Integer.valueOf(5), result.apply(0));
        assertEquals(Integer.valueOf(4), result.apply(-1));
    }

    @Test
    void testBind1Concat() {
        Function1<String, String> result = concat.bind1("a");
        assertEquals("aha", result.apply("ha"));
        assertEquals("a", result.apply(""));
        assertEquals("auto", result.apply("uto"));
    }

    @Test
    void testBind2Sum() {
        Function1<Integer, Integer> result = sum.bind2(5);
        assertEquals(Integer.valueOf(6), result.apply(1));
        assertEquals(Integer.valueOf(5), result.apply(0));
        assertEquals(Integer.valueOf(4), result.apply(-1));
    }

    @Test
    void testBind2Concat() {
        Function1<String, String> result = concat.bind2("a");
        assertEquals("aha", result.apply("ah"));
        assertEquals("a", result.apply(""));
        assertEquals("murta", result.apply("murt"));
    }

    @Test
    void testCurrySum() {
        Function1<Integer, Integer> result = sum.curry(5);
        assertEquals(Integer.valueOf(6), result.apply(1));
        assertEquals(Integer.valueOf(5), result.apply(0));
        assertEquals(Integer.valueOf(4), result.apply(-1));
    }

    @Test
    void testCurryConcat() {
        Function1<String, String> result = concat.curry("a");
        assertEquals("aha", result.apply("ah"));
        assertEquals("a", result.apply(""));
        assertEquals("murta", result.apply("murt"));
    }
}