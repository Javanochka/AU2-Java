package ru.spbau.nikiforovskaya.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Generic Map implementation. Can return entries in tha order they were put into.
 * @param <K> a key type by which to store at the hash map
 * @param <V> a value type which to store at the hash map
 */
public class HashMap<K, V> implements Map<K,V> {

    private LinkedList<K,V>[] data;
    private LinkedList<K,V> order;

    private int size;
    private int capacity;

    private static final int BASE_CAPACITY = 4;

    /** Creates a HashTable, base capacity is 4 */
    public HashMap() {
        capacity = BASE_CAPACITY;
        data = (LinkedList<K, V>[])new LinkedList[capacity];
        order = new LinkedList<>();
    }

    protected int getHash(Object s) {
        return (s.hashCode() % capacity + capacity) % capacity;
    }

    private class HashMapIterator implements Iterator<Entry<K, V>> {

        LinkedList<K,V>.Node current = order.getHead();

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Entry<K, V> next() {
            Pair<K, V> p = current.getData();
            current = current.getNext();
            if (!get(p.getKey()).equals(p.getValue())) {
                return next();
            }
            return p;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int hash = getHash(key);
        return data[hash] != null && data[hash].contains((K) key);
    }

    @Override
    public boolean containsValue(Object value) {
        for (LinkedList<K, V> list : data) {
            if (list == null) {
                continue;
            }
            for (LinkedList<K,V>.Node i = list.getHead(); i != null; i = i.getNext()) {
                if (i.getData().getValue().equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        int hash = getHash(key);
        if (data[hash] == null) {
            return null;
        }

        Pair<K,V> found = data[hash].get((K) key);
        return found == null ? null : found.getValue();
    }

    private void rebuild() {
        capacity *= 2;
        LinkedList<K, V>[] oldData = data;
        data = (LinkedList<K, V>[])new LinkedList[capacity];

        for (LinkedList<K, V> list : oldData) {
            if (list == null) {
                continue;
            }
            for (LinkedList<K,V>.Node i = list.getHead(); i != null; i = i.getNext()) {
                Pair<K, V> p = i.getData();
                int hash = getHash(p.getKey());
                if (data[hash] == null) {
                    data[hash] = new LinkedList<K, V>();
                }

                data[hash].add(p.getKey(), p.getValue());
            }
        }

    }

    @Override
    public V put(K key, V value) {
        int hash = getHash(key);
        if (data[hash] == null) {
            data[hash] = new LinkedList<K, V>();
        }

        LinkedList<K, V> current = data[hash];
        Pair<K, V> removed = current.remove(key);
        current.add(key, value);

        if (removed == null) {
            size++;
        }
        if (size * BASE_CAPACITY >= capacity) {
            rebuild();
        }

        order.add(key, value);

        return removed == null ? null : removed.getValue();
    }

    @Override
    public V remove(Object key) {
        int hash = getHash(key);
        if (data[hash] == null) {
            return null;
        }

        Pair<K, V> removed = data[hash].remove((K)key);
        if (removed != null) {
            size--;
        }

        return removed == null ? null : removed.getValue();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        for (LinkedList<K, V> list : data) {
            if (list == null) {
                continue;
            }
            list.clear();
        }
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new Set<Entry<K, V>>() {
            @Override
            public int size() {
                return size;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<Entry<K, V>> iterator() {
                return new HashMapIterator();
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(Entry<K, V> kvEntry) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends Entry<K, V>> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }
        };
    }
}
