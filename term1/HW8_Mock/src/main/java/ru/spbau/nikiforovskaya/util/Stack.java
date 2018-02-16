package ru.spbau.nikiforovskaya.util;

import java.util.NoSuchElementException;

/**
 * Stack (first in last out) implementation using list
 * @param <T> type of elements to store in Stack
 */
public class Stack<T> {

    private Node root = null;

    /**
     * Checks if stack is empty
     * @return {@code true} if empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Puts element in back of the stack.
     * @param element element to put into stack
     */
    public void push(T element) {
        root = new Node(element, root);
    }

    /**
     * Returns the last element in the stack and deletes it.
     * @return the last element in the stack
     */
    public T pop() {
        if (root == null) {
            throw new NoSuchElementException("pop from empty stack");
        }
        T result = root.value;
        root = root.next;
        return result;
    }

    /**
     * Returns the last element in the stack but doesn't delete it.
     * @return the last element in the stack
     */
    public T top() {
        if (root == null) {
            throw new NoSuchElementException("pop from empty stack");
        }
        return root.value;
    }

    /** Deletes all the elements, stored in the stack.*/
    public void clear() {
        root = null;
    }

    private class Node {
        private T value;
        private Node next;

        private Node(T value, Node next) {
            this.value = value;
            this.next = next;
        }
    }
}
