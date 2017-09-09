package ru.spbau.nikiforovskaya.util;

/**
 * A singly linked list to be used into HashTable,
 * which stores a pair of key and value.
 */
public class LinkedList {

    private Node head;
    private Node tail;

    /**
     * A list consists of nodes.
     * Node can be used to iterate over List
     */
    public class Node {
        private Node next;
        private Pair data;

        private Node(Pair data) {
            this.data = data;
        }

        /**
         * Get next node in the list.
         *
         * @return Next node.
         */
        public Node getNext() {
            return next;
        }

        /**
         * Get data stored in the node.
         *
         * @return Pair of key and value.
         */
        public Pair getData() {
            return data;
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
            return;
        }
        tail.next = new Node(new Pair(key, value));
        tail = tail.next;
    }

    /**
     * Gets a pair from list by key
     *
     * @param key A String which pair you want to return
     * @return <code>Pair</code> if there is a such element, <code>null</code> otherwise
     */
    public Pair get(String key) {
        for (Node current = head; current != null; current = current.next) {
            if (current.data.getKey().equals(key)) {
                return current.data;
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
            return removed;
        }
        for (Node current = head; current.next != null; current = current.next) {
            if (current.next.data.getKey().equals(key)) {
                Pair removed = current.next.data;
                current.next = current.next.next;
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
        for (Node current = head; current != null; current = current.next) {
            if (current.data.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Clears the list
     */
    public void clear() {
        head = null;
        tail = null;
    }
}
