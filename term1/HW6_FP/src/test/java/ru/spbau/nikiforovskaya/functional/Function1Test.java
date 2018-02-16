package ru.spbau.nikiforovskaya.functional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Function1Test {

    private Function1<Integer, Integer> add3;
    private Function1<Integer, Integer> mul5;
    private Function1<Integer, String> intToString;

    @BeforeEach
    void setUp() {
        add3 = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer arg) {
                return arg + 3;
            }
        };
        mul5 = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer arg) {
                return arg * 5;
            }
        };
        intToString = new Function1<Integer, String>() {
            @Override
            public String apply(Integer arg) {
                return Integer.toString(arg);
            }
        };
    }

    @Test
    void testComposeSameParameterAdd3Mul5() {
        Function1<Integer, Integer> result = add3.compose(mul5);
        assertEquals(Integer.valueOf(15), result.apply(0));
        assertEquals(Integer.valueOf(20), result.apply(1));
        assertEquals(Integer.valueOf(10), result.apply(-1));
    }

    @Test
    void testComposeSameParameterMul5Add3() {
        Function1<Integer, Integer> result = mul5.compose(add3);
        assertEquals(Integer.valueOf(3), result.apply(0));
        assertEquals(Integer.valueOf(8), result.apply(1));
        assertEquals(Integer.valueOf(-2), result.apply(-1));
    }

    @Test
    void testComposeDifferentParametersMul5IntToString() {
        Function1<Integer, String> result = mul5.compose(intToString);
        assertEquals("0", result.apply(0));
        assertEquals("5", result.apply(1));
        assertEquals("-5", result.apply(-1));
        assertEquals("15", result.apply(3));
    }

}