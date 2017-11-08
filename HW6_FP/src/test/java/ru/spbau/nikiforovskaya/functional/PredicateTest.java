package ru.spbau.nikiforovskaya.functional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PredicateTest {

    private Predicate<Integer> isEven;
    private Predicate<Integer> isDivByThree;

    @BeforeEach
    void setUp() {
        isEven = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg % 2 == 0;
            }
        };
        isDivByThree = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg % 3 == 0;
            }
        };
    }

    @Test
    void testAlwaysTrue() {
        for (int i = 0; i < 10; i++) {
            assertTrue(Predicate.<Integer>ALWAYS_TRUE().apply(i));
        }
        for (int i = 0; i < 10; i++) {
            assertTrue(Predicate.<String>ALWAYS_TRUE().apply(Integer.toString(i)));
        }
    }

    @Test
    void testAlwaysFalse() {
        for (int i = 0; i < 10; i++) {
            assertFalse(Predicate.<Integer>ALWAYS_FALSE().apply(i));
        }
        for (int i = 0; i < 10; i++) {
            assertFalse(Predicate.<String>ALWAYS_FALSE().apply(Integer.toString(i)));
        }
    }

    @Test
    void testOrBothNonConstant() {
        Predicate<Integer> result = isEven.or(isDivByThree);
        assertEquals(false, result.apply(1));
        assertEquals(true, result.apply(2));
        assertEquals(true, result.apply(3));
        assertEquals(true, result.apply(4));
        assertEquals(false, result.apply(5));
    }

    @Test
    void testOrNonConstantAndTrue() {
        Predicate<Integer> result = isDivByThree.or(Predicate.ALWAYS_TRUE());
        assertEquals(true, result.apply(1));
        assertEquals(true, result.apply(2));
        assertEquals(true, result.apply(3));
        assertEquals(true, result.apply(4));
        assertEquals(true, result.apply(5));
    }

    @Test
    void testOrNonConstantAndFalse() {
        Predicate<Integer> result = isDivByThree.or(Predicate.ALWAYS_FALSE());
        assertEquals(false, result.apply(1));
        assertEquals(false, result.apply(2));
        assertEquals(true, result.apply(3));
        assertEquals(false, result.apply(4));
        assertEquals(false, result.apply(5));
    }

    @Test
    void testAndBothNonConstant() {
        Predicate<Integer> result = isEven.and(isDivByThree);
        assertEquals(false, result.apply(1));
        assertEquals(false, result.apply(2));
        assertEquals(false, result.apply(3));
        assertEquals(false, result.apply(4));
        assertEquals(false, result.apply(5));
        assertEquals(true, result.apply(6));
    }

    @Test
    void testAndNonConstantAndTrue() {
        Predicate<Integer> result = isDivByThree.and(Predicate.ALWAYS_TRUE());
        assertEquals(false, result.apply(1));
        assertEquals(false, result.apply(2));
        assertEquals(true, result.apply(3));
        assertEquals(false, result.apply(4));
        assertEquals(false, result.apply(5));
    }

    @Test
    void testAndNonConstantAndFalse() {
        Predicate<Integer> result = isDivByThree.and(Predicate.ALWAYS_FALSE());
        assertEquals(false, result.apply(1));
        assertEquals(false, result.apply(2));
        assertEquals(false, result.apply(3));
        assertEquals(false, result.apply(4));
        assertEquals(false, result.apply(5));
    }

    @Test
    void testNotIsEven() {
        Predicate<Integer> result = isEven.not();
        assertEquals(true, result.apply(1));
        assertEquals(false, result.apply(2));
        assertEquals(true, result.apply(3));
        assertEquals(false, result.apply(4));
        assertEquals(true, result.apply(5));
    }

    @Test
    void testNotIsDivByThree() {
        Predicate<Integer> result = isDivByThree.not();
        assertEquals(true, result.apply(1));
        assertEquals(true, result.apply(2));
        assertEquals(false, result.apply(3));
        assertEquals(true, result.apply(4));
        assertEquals(true, result.apply(5));
    }
}