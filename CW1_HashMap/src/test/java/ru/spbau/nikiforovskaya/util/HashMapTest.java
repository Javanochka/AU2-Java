package ru.spbau.nikiforovskaya.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.spbau.nikiforovskaya.util.HashMap;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class HashMapTest {

    private HashMap<String, String> ht;

    private class HashTableStupidHash extends HashMap<String, String> {
        protected int getHash(String s) {
            return 0;
        }
    }

    @BeforeEach
    void setUp() {
        ht = new HashMap<String, String>();
    }

    private static void fillHashTableWithManyElements(HashMap<String, String> ht) {
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
        fillHashTableWithManyElements(ht);
        assertEquals(10, ht.size());
    }

    @Test
    void testSizeManyKeysBadHasher() {
        ht = new HashTableStupidHash();
        fillHashTableWithManyElements(ht);
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
        assertFalse(ht.containsKey("key"));
    }

    @Test
    void testContainsTheOnly() {
        ht.put("key", "value");
        assertTrue(ht.containsKey("key"));
    }

    @Test
    void testContainsManyElements() {
        fillHashTableWithManyElements(ht);
        assertTrue(ht.containsKey("5"));
        assertFalse(ht.containsKey("a"));
    }

    @Test
    void testContainsManyElementsBadHasher() {
        ht = new HashTableStupidHash();
        fillHashTableWithManyElements(ht);
        assertTrue(ht.containsKey("5"));
        assertFalse(ht.containsKey("a"));
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
        fillHashTableWithManyElements(ht);
        for (int i = 0; i < 10; i++) {
            assertEquals(Character.toString((char)(i + 'a')), ht.get(Integer.toString(i)));
        }
        assertEquals(10, ht.size());
    }

    @Test
    void testGetManyKeysBadHasher() {
        ht = new HashTableStupidHash();
        fillHashTableWithManyElements(ht);
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
    void testPutAfterDifferentBadHasher() {
        ht = new HashTableStupidHash();
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
        assertFalse(ht.containsKey("key"));
    }

    @Test
    void testRemoveMany() {
        fillHashTableWithManyElements(ht);
        assertEquals("f", ht.remove("5"));
        assertEquals(9, ht.size());
    }

    @Test
    void testRemoveManyBadHasher() {
        ht = new HashTableStupidHash();
        fillHashTableWithManyElements(ht);
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
        assertFalse(ht.containsKey("key"));
    }

    @Test
    void testClearMany() {
        fillHashTableWithManyElements(ht);
        ht.clear();
        assertFalse(ht.containsKey("5"));
        assertEquals(0, ht.size());
    }

    @Test
    void testClearManyBadHasher() {
        ht = new HashTableStupidHash();
        fillHashTableWithManyElements(ht);
        ht.clear();
        assertFalse(ht.containsKey("5"));
        assertEquals(0, ht.size());
    }

    @Test
    void testEntrySet() {
        fillHashTableWithManyElements(ht);
        String[] resultKey = new String[10];
        String[] resultValue = new String[10];
        int i = 0;
        for (Map.Entry<String, String> entry : ht.entrySet()) {
            resultKey[i] = entry.getKey();
            resultValue[i] = entry.getValue();
            i++;
        }
        for (int j = 0; j < 10; j++) {
            assertEquals(Integer.toString(j), resultKey[j]);
            assertEquals(Character.toString((char)(j + 'a')), resultValue[j]);
        }
    }
}