package ru.spbau.nikiforovskaya.util;

import java.util.ArrayList;

/**
 * A set realization, using unbalanced binary search tree inside.
 * Can add elements, check if they are in the set and print all the set in the sorted order.
 * @param <T> a type of elements to store in set. Should be comparable.
 */
public class Set<T extends Comparable<? super T>> {

    private Node root;
    private int size = 0;

    private class Node {
        public Node left;
        public Node right;
        public T value;

        public Node(T value) {
            this.value = value;
        }
    }

    /**
     * Adds element to the set.
     * If there was such value doesn't do anything.
     * @param value an element you want to add.
     *              Assumes that it is not {@code null}.
     */
    public void add(T value) {
        if (contains(value)) {
            return;
        }
        Node i = root;
        Node parent = null;
        for (; i != null;) {
            parent = i;
            i = i.value.compareTo(value) > 0 ? i.left : i.right;
        }
        if (parent == null) {
            root = new Node(value);
            size++;
            return;
        }

        if (parent.value.compareTo(value) > 0) {
            parent.left = new Node(value);
        } else {
            parent.right = new Node(value);
        }
        size++;
    }

    /**
     * Checks if a given element exists in the set.
     * @param value an element to check
     * @return {@code true} if there is such value in the set,
     * {@code false} otherwise.
     */
    public boolean contains(T value) {
        Node i = root;
        for (; i != null;) {
            int compareResult = i.value.compareTo(value);
            if (compareResult == 0) {
                return true;
            }
            i = compareResult > 0 ? i.left : i.right;
        }
        return false;
    }

    private void printTree(Node i, ArrayList<T> result) {
        if (i == null) {
            return;
        }
        printTree(i.left, result);
        result.add(i.value);
        printTree(i.right, result);
    }

    /**
     * Returns an ArrayList of elements stored in the set in the sorted order.
     * @return ArrayList of elements stored in the set in the sorted order.
     */
    public ArrayList<T> toArrayList() {
        ArrayList<T> result = new ArrayList<T>();
        printTree(root, result);
        return result;
    }

    /**
     * Returns the number of elements stored in the set.
     * @return the number of elements stored in the set.
     */
    public int size() {
        return size;
    }
}
