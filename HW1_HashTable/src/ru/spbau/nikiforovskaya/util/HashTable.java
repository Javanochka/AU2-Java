package ru.spbau.nikiforovskaya.util;

/**
 * HashTable realized with separate chaining with linked lists
 *
 * @author Anna Nikiforovskaya
 */

public class HashTable {

    private LinkedList[] data;
    private int size;
    private int capacity;


    private static final int BASE_CAPACITY = 2;
    private static final int HASH_PRIME = 37;

    /**
     * Creates a HashTable, base capacity is 2
     */

    public HashTable() {
        data = new LinkedList[BASE_CAPACITY];
        capacity = BASE_CAPACITY;
    }

    /**
     * Get size
     *
     * @return Number of keys stored in HashTable
     */

    public int size() {
        return size;
    }

    private int getHash(String s) {
        long hash = 0;
        for (int i = 0; i < s.length(); i++) {
            hash = hash * HASH_PRIME + s.charAt(i);
            hash %= capacity;
        }
        return (int) hash;
    }

    /**
     * Checks if there is a such key stored
     *
     * @param key String by which you want to search
     * @return <code>true</code> if there is such element, <code>false</code> otherwise
     */

    public boolean contains(String key) {
        int hash = getHash(key);
        return data[hash] != null && data[hash].contains(key);
    }

    /**
     * Gets an element stored in HashTable by key
     *
     * @param key String by which you want to search
     * @return Returns a found value String, if there is no such key returns <code>null</code>.
     */

    public String get(String key) {
        int hash = getHash(key);
        if (data[hash] == null) {
            return null;
        }

        Pair found = data[hash].get(key);
        return found == null ? null : found.getValue();
    }

    private void rebuild() {
        capacity *= 2;
        LinkedList[] oldData = data;
        data = new LinkedList[capacity];

        for (LinkedList list : oldData) {
            if (list == null) {
                continue;
            }
            Pair[] listArr = list.toArray();
            for (Pair p : listArr) {
                int hash = getHash(p.getKey());
                if (data[hash] == null) {
                    data[hash] = new LinkedList();
                }

                data[hash].add(p.getKey(), p.getValue());
            }
        }

    }

    /**
     * Puts a value by key into HashTable
     *
     * @param key   A key String
     * @param value A value String
     * @return Returns previous value stored by the key,
     * if there was not such key returns <code>null</code>.
     */

    public String put(String key, String value) {
        int hash = getHash(key);
        if (data[hash] == null) {
            data[hash] = new LinkedList();
        }

        LinkedList cur = data[hash];
        Pair removed = cur.remove(key);
        cur.add(key, value);

        if (removed == null) {
            size++;
        }
        if (size * 2 >= capacity) {
            rebuild();
        }

        return removed == null ? null : removed.getValue();
    }

    /**
     * Removes value from HashTable by key
     *
     * @param key A key String by which to remove
     * @return Returns removed value String
     */

    public String remove(String key) {
        int hash = getHash(key);
        if (data[hash] == null) {
            return null;
        }

        Pair removed = data[hash].remove(key);
        if (removed != null) {
            size--;
        }

        return removed == null ? null : removed.getValue();
    }

    /**
     * Clears the HashTable
     */

    public void clear() {
        for (LinkedList list : data) {
            if (list == null) {
                continue;
            }
            list.clear();
        }
        size = 0;
    }
}
