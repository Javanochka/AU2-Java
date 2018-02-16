package ru.spbau.nikiforovskaya.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.spbau.nikiforovskaya.util.LinkedList;
import ru.spbau.nikiforovskaya.util.Pair;

import static org.junit.jupiter.api.Assertions.*;


class LinkedListTest {

    private LinkedList l;

    @BeforeEach
    void setUp() {
        l = new LinkedList();
    }

    private void fillListWithManyElements() {
        for (int i = 0; i < 10; i++) {
            l.add(Integer.toString(i), Character.toString((char)(i + 'a')));
        }
    }

    @Test
    void testGetHeadEmpty() {
        assertNull(l.getHead());
    }

    @Test
    void testAddToEmpty() {
        l.add("cat", "Asya");
        assertEquals("cat", l.getHead().getData().getKey());
        assertEquals("Asya", l.getHead().getData().getValue());
    }

    @Test
    void testAddManyIterating() {
        fillListWithManyElements();
        int j = 0;
        for (LinkedList.Node i = l.getHead(); i != null; i = i.getNext()) {
            assertEquals(Integer.toString(j), i.getData().getKey());
            assertEquals(Character.toString((char)(j + 'a')), i.getData().getValue());
            j++;
        }
    }

    @Test
    void testGetFromEmpty() {
        assertNull(l.get("anything"));
    }

    @Test
    void testGetTheOnly() {
        l.add("key", "value");
        assertEquals("key", l.get("key").getKey());
        assertEquals("value", l.get("key").getValue());
    }

    @Test
    void testGetFromManyElementsList() {
        fillListWithManyElements();
        assertEquals("5", l.get("5").getKey());
        assertEquals("f", l.get("5").getValue());
    }

    @Test
    void testRemoveFromEmpty() {
        assertNull(l.remove("key"));
    }


    @Test
    void testRemoveTheOnly() {
        l.add("key", "value");
        Pair res = l.remove("key");
        assertEquals("key", res.getKey());
        assertEquals("value", res.getValue());
        assertNull(l.get("key"));
    }

    @Test
    void testRemoveFromManyElementsList() {
        fillListWithManyElements();
        Pair res = l.remove("5");
        assertEquals("5", res.getKey());
        assertEquals("f", res.getValue());
        assertNull(l.get("5"));
    }

    @Test
    void testContainsEmpty() {
        assertFalse(l.contains("key"));
    }

    @Test
    void testContainsTheOnly() {
        l.add("key", "value");
        assertTrue(l.contains("key"));
    }

    @Test
    void testContainsInManyElementsList() {
        fillListWithManyElements();
        assertTrue(l.contains("5"));
        assertFalse(l.contains("10438"));
    }

    @Test
    void testClearTheOnly() {
        l.add("key", "value");
        l.clear();
        assertFalse(l.contains("key"));
    }

    @Test
    void testClearManyElementsList() {
        fillListWithManyElements();
        l.clear();
        for (int i = 0; i < 10; i++) {
            assertFalse(l.contains(Integer.toString(i)));
        }
    }
}