package ru.spbau.nikiforovskaya.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaybeTest {

    private Maybe<Integer> justInt;
    private Maybe<String> justString;
    private Maybe<String> justEmptyString;
    private Maybe<Integer> nothingInt;
    private Maybe<String> nothingString;

    @BeforeEach
    void setUp() {
        justInt = Maybe.just(5);
        justString = Maybe.just("meow");
        justEmptyString = Maybe.just("");
        nothingInt = Maybe.nothing();
        nothingString = Maybe.nothing();
    }

    @Test
    void testGetJustInt() throws MaybeNoValueException {
        assertEquals(Integer.valueOf(5), justInt.get());
    }

    @Test
    void testGetJustString() throws MaybeNoValueException {
        assertEquals("meow", justString.get());
    }

    @Test
    void testGetJustEmptyString() throws MaybeNoValueException {
        assertEquals("", justEmptyString.get());
    }

    @Test
    void testGetNothingInt() throws MaybeNoValueException {
        assertThrows(MaybeNoValueException.class, nothingInt::get);
    }

    @Test
    void testGetNothingString() throws MaybeNoValueException {
        assertThrows(MaybeNoValueException.class, nothingString::get);
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
    void testMapJustInteger() throws MaybeNoValueException {
        Maybe<Integer> res = justInt.map((a) -> Integer.sum(a, 5));
        assertEquals(Integer.valueOf(10), res.get());
    }

    @Test
    void testMapJustString() throws MaybeNoValueException {
        Maybe<String> res = justString.map((a) -> (a + " wow!"));
        assertEquals("meow wow!", res.get());
    }

    @Test
    void testMapJustEmptyString() throws MaybeNoValueException {
        Maybe<String> res = justEmptyString.map((a) -> (a + "wow!"));
        assertEquals("wow!", res.get());
    }

    @Test
    void testMapNothingInteger() throws MaybeNoValueException {
        Maybe<Integer> res = nothingInt.map((a) -> Integer.sum(a, 5));
        assertFalse(res.isPresent());
        assertThrows(MaybeNoValueException.class, res::get);
    }

    @Test
    void testMapNothingString() throws MaybeNoValueException {
        Maybe<String> res = nothingString.map((a) -> (a + " wow!"));
        assertFalse(res.isPresent());
        assertThrows(MaybeNoValueException.class, res::get);
    }
}