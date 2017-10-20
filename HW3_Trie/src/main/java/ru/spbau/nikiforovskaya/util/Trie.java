package ru.spbau.nikiforovskaya.util;

import java.io.*;
import java.util.HashMap;

/**
 * A trie realization, which stores strings.
 * Can add new strings, remove stored, check if contains.
 * Can count how many stored strings starts with specified prefix.
 * A trie can be serialized.
 */

public class Trie implements Serializable {

    private Node root = new Node();

    private class Node implements Serializable {
        private HashMap<Character, Node> go = new HashMap<>();
        private boolean isTerminal;
        private int suffixNumber;

        private Node move(char symbol) {
            if (!go.containsKey(symbol)) {
                go.put(symbol, new Node());
            }
            return go.get(symbol);
        }

        private boolean canMove(char symbol) {
            return go.containsKey(symbol);
        }
    }

    /**
     * Adds a new string element into the trie.
     * Does nothing, if there such string exists.
     * Requires linear time in the element's length.
     * @param element a string to add into the trie
     * @return {@code true}, if added a new element, {@code false} otherwise.
     */
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }

        Node current = root;
        for (int i = 0; i < element.length(); i++) {
            current.suffixNumber++;
            current = current.move(element.charAt(i));
        }
        current.suffixNumber++;
        current.isTerminal = true;
        return true;
    }

    /**
     * Checks if there exists such string in the trie.
     * Requires linear time in the element's length.
     * @param element a string to check
     * @return {@code true} if there exists such string, {@code false} otherwise.
     */
    public boolean contains(String element) {
        Node current = root;
        for (int i = 0; i < element.length(); i++) {
            if (!current.canMove(element.charAt(i))) {
                return false;
            }
            current = current.move(element.charAt(i));
        }
        return current.isTerminal;
    }

    /**
     * Removes a string from the trie.
     * Requires linear time in the element's length.
     * @param element a string to remove
     * @return {@code true} if there was such element, {@code false} otherwise.
     */
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }
        Node current = root;
        for (int i = 0; i < element.length(); i++) {
            current.suffixNumber--;
            current = current.move(element.charAt(i));
        }
        current.suffixNumber--;
        current.isTerminal = false;
        return true;
    }

    /**
     * Get number of strings in the trie.
     * @return a number of strings in the trie.
     */
    public int size() {
        return root.suffixNumber;
    }

    /**
     * Returns a number of strings which start with the specified prefix.
     * Requires linear time in the prefix's length.
     * @param prefix a string of which to count number of suffixes
     * @return a number of strings which start with the specified prefix.
     */
    public int howManyStartsWithPrefix(String prefix) {
        Node current = root;
        for (int i = 0; i < prefix.length(); i++) {
            current = current.move(prefix.charAt(i));
        }
        return current.suffixNumber;
    }

    /**
     * Writes a trie into the output stream.
     * @param out an output stream to write into
     * @throws IOException if couldn't write properly to the given output stream.
     */
    public void serialize(OutputStream out) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(this);
        oos.close();
    }

    /**
     * Reads a trie from the input stream.
     * @param in an input stream to read from
     * @throws IOException if couldn't read properly to the given output stream.
     * @throws ClassNotFoundException if tries to read something which is not a Trie.
     */
    public void deserialize(InputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(in);
        Trie t = (Trie) ois.readObject();
        root = t.root;
        ois.close();
    }
}
