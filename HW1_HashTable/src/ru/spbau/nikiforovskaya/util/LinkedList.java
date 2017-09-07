package ru.spbau.nikiforovskaya.util;

/**
 * A singly linked list to be used into HashTable,
 * which stores a pair of key and value.
 *
 * @author Anna Nikiforovskaya
 */

public class LinkedList {

    private Node head;
    private Node tail;

    private int size;

    private class Node {
        public Node next;
        public Pair data;

        public Node(Pair data) {
            this.data = data;
        }
    }

    /**
     * Returns a head of the list
     *
     * @return a head of the list
     */

    public Node getHead() {
        return head;
    }

    /**
     * Adds a new pair of key and value into the end of list
     *
     * @param key   A String which you want to store value at
     * @param value A String which is a value, stored by the key
     */

    public void add(String key, String value) {
        if (head == null) {
            head = new Node(new Pair(key, value));
            tail = head;
            size++;
            return;
        }
        tail.next = new Node(new Pair(key, value));
        tail = tail.next;
        size++;
    }

    /**
     * Gets a pair from list by key
     *
     * @param key A String which pair you want to return
     * @return <code>Pair</code> if there is a such element, <code>null</code> otherwise
     */


    public Pair get(String key) {
        for (Node cur = head; cur != null; cur = cur.next) {
            if (cur.data.getKey().equals(key)) {
                return cur.data;
            }
        }
        return null;
    }

    /**
     * Remove a pair from list by key
     *
     * @param key A String which you want to remove value at
     * @return <code>Pair</code> which has been removed or
     * <code>null</code> if there was no such pair
     */

    public Pair remove(String key) {
        if (head == null) {
            return null;
        }

        if (head.data.getKey().equals(key)) {
            Pair removed = head.data;
            head = head.next;
            size--;
            return removed;
        }
        for (Node cur = head; cur.next != null; cur = cur.next) {
            if (cur.next.data.getKey().equals(key)) {
                Pair removed = cur.next.data;
                cur.next = cur.next.next;
                size--;
                return removed;
            }
        }
        return null;
    }

    /**
     * Checks if there is a pair with such key.
     *
     * @param key A key string you want to check.
     * @return <code>true</code> if the list contains such key, <code>false</code> otherwise.
     */

    public boolean contains(String key) {
        for (Node cur = head; cur != null; cur = cur.next) {
            if (cur.data.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Converts the list to an array of pairs of key and value
     *
     * @return Array of all Pairs which are in the list
     */


    public Pair[] toArray() {
        Pair[] temp = new Pair[size];
        int ind = 0;

        for (Node cur = head; cur != null; cur = cur.next) {
            temp[ind++] = cur.data;
        }
        return temp;
    }

    /**
     * Clears the list
     */

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }
}
