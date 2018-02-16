package ru.spbau.nikiforovskaya.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class StackTest {

    private Stack<Integer> intStack;

    @BeforeEach
    void setUp() {
        intStack = new Stack<>();
    }

    @Test
    void testIsEmptyEmpty() {
        assertTrue(intStack.isEmpty());
    }

    @Test
    void testIsEmptyOnlyElement() {
        intStack.push(3);
        assertFalse(intStack.isEmpty());
    }

    @Test
    void testPushAndPopOnly() {
        intStack.push(5);
        assertEquals(5, (int)intStack.pop());
    }

    @Test
    void testPushAndPopSeveral() {
        for (int i = 0; i < 10; i++) {
            intStack.push(i);
        }
        for (int i = 9; i > -1; i--) {
            assertEquals(i, (int)intStack.pop());
        }
    }

    @Test
    void testPopEmpty() {
        assertThrows(NoSuchElementException.class, intStack::pop);
    }

    @Test
    void testTopEmpty() {
        assertThrows(NoSuchElementException.class, intStack::top);
    }

    @Test
    void testPushAndTopOnly() {
        intStack.push(5);
        assertEquals(5, (int)intStack.top());
    }

    @Test
    void testPushPopTopSeveral() {
        intStack.push(5);
        intStack.push(2);
        assertEquals(2, (int)intStack.pop());
        assertEquals(5, (int)intStack.top());
        intStack.push(5);
        intStack.push(3);
        intStack.push(1);
        assertEquals(1, (int)intStack.pop());
        assertEquals(3, (int)intStack.top());
    }

    @Test
    void testClearEmpty() {
        intStack.clear();
        assertTrue(intStack.isEmpty());
    }

    @Test
    void testClearSeveral() {
        for (int i = 0; i < 10; i++) {
            intStack.push(i);
        }
        intStack.clear();
        assertTrue(intStack.isEmpty());
        assertThrows(NoSuchElementException.class, intStack::pop);
        assertThrows(NoSuchElementException.class, intStack::top);
    }

}