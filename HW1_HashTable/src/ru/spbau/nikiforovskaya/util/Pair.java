package ru.spbau.nikiforovskaya.util;

/**
 * A comfortable way to store keys and values in HashTable
 *
 * @author Anna Nikiforovskaya
 */

public class Pair {
    private String key;
    private String value;

    /**
     * Constructor by key and value
     *
     * @param key   A key-String
     * @param value A value-String
     */

    public Pair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Get a pair key
     *
     * @return Key, stored in the pair
     */

    public String getKey() {
        return key;
    }

    /**
     * Get a pair value
     *
     * @return Value, stored in the pair
     */

    public String getValue() {
        return value;
    }
}