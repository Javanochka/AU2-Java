package ru.spbau.nikiforovskaya.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaybeTest {

    Maybe<Integer> justInt;
    Maybe<String> justString;
    Maybe<String> justEmptyString;
    Maybe<Integer> nothingInt;
    Maybe<String> nothingString;

    @BeforeEach
    void setUp() {
        justInt = Maybe.just(5);
        justString = Maybe.just("meow");
        justEmptyString = Maybe.just("");
        nothingInt = Maybe.nothing();
        nothingString = Maybe.nothing();
    }

    @Test
    void testGetJustInt() throws MaybeException {
        assertEquals(Integer.valueOf(5), justInt.get());
    }

    @Test
    void testGetJustString() throws MaybeException {
        assertEquals("meow", justString.get());
    }

    @Test
    void testGetJustEmptyString() throws MaybeException {
        assertEquals("", justEmptyString.get());
    }

    @Test
    void testGetNothingInt() throws MaybeException {
        assertThrows(MaybeException.class, nothingInt::get);
    }

    @Test
    void testGetNothingString() throws MaybeException {
        assertThrows(MaybeException.class, nothingString::get);
    }

    @Test
    void isPresentJustInt() {
        assertTrue(justInt.isPresent());
    }

    @Test
    void isPresentJustString() {
        assertTrue(justString.isPresent());
    }

    @Test
    void isPresentJustEmptyString() {
        assertTrue(justEmptyString.isPresent());
    }

    @Test
    void isPresentNothingInt() {
        assertFalse(nothingInt.isPresent());
    }

    @Test
    void isPresentNothingString() {
        assertFalse(nothingString.isPresent());
    }

    @Test
    void testMapJustInteger() throws MaybeException {
        Maybe<Integer> res = justInt.map((a) -> Integer.sum(a, 5));
        assertEquals(Integer.valueOf(10), res.get());
    }

    @Test
    void testMapJustString() throws MaybeException {
        Maybe<String> res = justString.map((a) -> (a + " wow!"));
        assertEquals("meow wow!", res.get());
    }

    @Test
    void testMapJustEmptyString() throws MaybeException {
        Maybe<String> res = justEmptyString.map((a) -> (a + "wow!"));
        assertEquals("wow!", res.get());
    }

    @Test
    void testMapNothingInteger() throws MaybeException {
        Maybe<Integer> res = nothingInt.map((a) -> Integer.sum(a, 5));
        assertFalse(res.isPresent());
        assertThrows(MaybeException.class, res::get);
    }

    @Test
    void testMapNothingString() throws MaybeException {
        Maybe<String> res = nothingString.map((a) -> (a + " wow!"));
        assertFalse(res.isPresent());
        assertThrows(MaybeException.class, res::get);
    }
}