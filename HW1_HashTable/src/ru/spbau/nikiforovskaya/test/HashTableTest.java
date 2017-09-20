package ru.spbau.nikiforovskaya.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.spbau.nikiforovskaya.util.HashTable;

import static org.junit.jupiter.api.Assertions.*;


class HashTableTest {

    private HashTable ht;

    @BeforeEach
    void setUp() {
        ht = new HashTable();
    }

    private void fillHashTableWithManyElements() {
        for (int i = 0; i < 10; i++) {
            ht.put(Integer.toString(i), Character.toString((char)(i + 'a')));
        }
    }

    @Test
    void testSizeEmpty() {
        assertEquals(0, ht.size());
    }

    @Test
    void testSizeOneElement() {
        ht.put("key", "value");
        assertEquals(1, ht.size());
    }

    @Test
    void testSizeDifferentKeys() {
        fillHashTableWithManyElements();
        assertEquals(10, ht.size());
    }

    @Test
    void testSizeSameKeys() {
        ht.put("key", "value");
        ht.put("key", "value2");
        assertEquals(1, ht.size());
    }

    @Test
    void testSizeAfterRemove() {
        ht.put("1", "1");
        ht.put("2", "2");
        ht.remove("1");
        assertEquals(1, ht.size());
    }

    @Test
    void testContainsEmpty() {
        assertFalse(ht.contains("key"));
    }

    @Test
    void testContainsTheOnly() {
        ht.put("key", "value");
        assertTrue(ht.contains("key"));
    }

    @Test
    void testContainsManyElements() {
        fillHashTableWithManyElements();
        assertTrue(ht.contains("5"));
        assertFalse(ht.contains("a"));
    }

    @Test
    void testGetEmpty() {
        assertNull(ht.get("key"));
    }

    @Test
    void testGetTheOnly() {
        ht.put("key", "value");
        assertEquals("value", ht.get("key"));
    }

    @Test
    void testGetManyDifferentKeys() {
        fillHashTableWithManyElements();
        for (int i = 0; i < 10; i++) {
            assertEquals(Character.toString((char)(i + 'a')), ht.get(Integer.toString(i)));
        }
        assertEquals(10, ht.size());
    }

    @Test
    void testGetManySameKeys() {
        for (int i = 0; i < 10; i++) {
            ht.put("A", Integer.toString(i));
        }
        assertEquals("9", ht.get("A"));
    }

    @Test
    void testPutIntoEmpty() {
        assertEquals(null, ht.put("key", "value"));
    }

    @Test
    void testPutAfterDifferent() {
        ht.put("key", "value");
        assertEquals(null, ht.put("hey", "way"));
    }

    @Test
    void testPutAfterSame() {
        ht.put("key", "value");
        assertEquals("value", ht.put("key", "another"));
    }

    @Test
    void testRemoveEmpty() {
        assertNull(ht.remove("key"));
        assertEquals(0, ht.size());
    }

    @Test
    void testRemoveTheOnly() {
        ht.put("key", "value");
        assertEquals("value", ht.remove("key"));
        assertEquals(0, ht.size());
        assertFalse(ht.contains("key"));
    }

    @Test
    void testRemoveMany() {
        fillHashTableWithManyElements();
        assertEquals("f", ht.remove("5"));
        assertEquals(9, ht.size());
    }

    @Test
    void testClearEmpty() {
        ht.clear();
    }

    @Test
    void testClearTheOnly() {
        ht.put("key", "value");
        ht.clear();
        assertFalse(ht.contains("key"));
    }

    @Test
    void testClearMany() {
        fillHashTableWithManyElements();
        ht.clear();
        assertFalse(ht.contains("5"));
        assertEquals(0, ht.size());
    }
}