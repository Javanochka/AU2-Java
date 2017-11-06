package ru.spbau.nikiforovskaya.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {

    Trie t;

    @BeforeEach
    void setUp() {
        t = new Trie();
    }

    @Test
    void testAddTheOnly() {
        assertTrue(t.add("key"));
        assertEquals(1, t.size());
    }

    @Test
    void testAddTheSame() {
        t.add("key");
        assertFalse(t.add("key"));
        assertEquals(1, t.size());
    }

    @Test
    void testAddDifferent() {
        t.add("key");
        assertTrue(t.add("another"));
        assertEquals(2, t.size());
    }

    @Test
    void testContainsEmpty() {
        assertFalse(t.contains("key"));
    }

    @Test
    void testContainsTheOnly() {
        t.add("key");
        assertTrue(t.contains("key"));
        assertFalse(t.contains("another"));
    }

    @Test
    void testContainsDifferent() {
        t.add("key");
        t.add("another");
        assertTrue(t.contains("key"));
        assertTrue(t.contains("another"));
    }

    @Test
    void testRemoveEmpty() {
        assertFalse(t.remove("key"));
    }

    @Test
    void testRemoveTheOnly() {
        t.add("key");
        assertTrue(t.remove("key"));
        assertFalse(t.contains("key"));
    }

    @Test
    void testRemoveDifferent() {
        t.add("key");
        t.add("another");
        assertTrue(t.remove("key"));
        assertEquals(1, t.size());
        assertTrue(t.remove("another"));
        assertEquals(0, t.size());
    }

    @Test
    void testSizeEmpty() {
        assertEquals(0, t.size());
    }

    @Test
    void testSizeOnlyAdds() {
        t.add("key");
        assertEquals(1, t.size());
        t.add("wow");
        assertEquals(2, t.size());
        t.add("key2");
        assertEquals(3, t.size());
        t.add("wow");
        assertEquals(3, t.size());
        t.add("");
        assertEquals(4, t.size());
    }

    @Test
    void testSizeWithRemoves() {
        t.add("key");
        assertEquals(1, t.size());
        t.add("wow");
        assertEquals(2, t.size());
        t.add("");
        assertEquals(3, t.size());
        t.remove("key");
        assertEquals(2, t.size());
        t.remove("wow2");
        assertEquals(2, t.size());
    }

    @Test
    void testHowManyStartsWithPrefixEmpty() {
        t.add("key");
        t.add("wow");
        assertEquals(2, t.howManyStartsWithPrefix(""));
    }

    @Test
    void testHowManyStartsWithPrefixNonempty() {
        t.add("abacaba");
        t.add("abac");
        assertEquals(2, t.howManyStartsWithPrefix("aba"));
        assertEquals(1, t.howManyStartsWithPrefix("abaca"));
    }

    @Test
    void testSerialization() throws IOException, ClassNotFoundException {
        t.add("meow");
        t.add("cat");
        t.add("cars");
        t.add("carramba");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        t.serialize(bos);
        Trie t1 = new Trie();
        t1.deserialize(new ByteArrayInputStream(bos.toByteArray()));

    }

}