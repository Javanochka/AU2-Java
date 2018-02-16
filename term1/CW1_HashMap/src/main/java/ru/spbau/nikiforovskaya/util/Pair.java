package ru.spbau.nikiforovskaya.util;

import java.util.Map;

/**
 * A comfortable way to store keys and values in HashTable
 */
public class Pair<K, V> implements Map.Entry<K, V>{
    private K key;
    private V value;

    /**
     * Constructor by key and value
     *
     * @param key   A key of type K
     * @param value A value of type V
     */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Get a pair key
     *
     * @return Key, stored in the pair
     */
    public K getKey() {
        return key;
    }

    /**
     * Get a pair value
     *
     * @return Value, stored in the pair
     */
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V temp = this.value;
        this.value = value;
        return temp;
    }
}