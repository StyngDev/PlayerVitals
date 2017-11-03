package com.styng.playervitals.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> {

    private final Map<K, CacheEntry<K, V>> cache = new HashMap<>();
    private CacheEntry<K, V> first;
    private CacheEntry<K, V> last;
    private final int maxCache;

    public LRUCache() {
        this(-1);
    }

    public LRUCache(int maxCache) {
        this.maxCache = maxCache;
    }

    public V remove(K key) {
        CacheEntry<K, V> entry = cache.remove(key);
        if (entry != null) {
            accessed(entry);
            if (entry.previous != null) {
                first = entry.previous;
                entry.previous.next = null;
            }
            return entry.value;
        }
        return null;
    }

    public V getValue(K key) {
        CacheEntry<K, V> entry = get(key);
        if (entry != null)
            return entry.value;
        return null;
    }

    public CacheEntry<K, V> get(K key) {
        CacheEntry<K, V> entry = cache.get(key);
        if (entry != null) {
            accessed(entry);
            return entry;
        }
        return null;
    }

    public CacheEntry<K, V> cache(K key, V value) {
        CacheEntry<K, V> entry = cache.get(key);
        if (entry != null) {
            entry.value = value;
            accessed(entry);
        } else {
            entry = new CacheEntry<>(key, value);
            cache.put(key, entry);
            entry.previous = first;
            if (first != null)
                first.next = entry;
            if (last == null)
                last = first;
            entry.next = null;
            first = entry;

            if (size() > maxCache && last != null)
                remove(last.key);
        }
        return entry;
    }

    private void accessed(CacheEntry<K, V> entry) {
        if (entry.previous != null)
            entry.previous.next = entry.next;
        else
            last = entry.next;
        if (entry.next != null)
            entry.next.previous = entry.previous;
        if (first != null)
            first.next = entry;
        entry.previous = first;
        first = entry;
    }

    public CacheEntry<K, V> getFirst() {
        return first;
    }

    public CacheEntry<K, V> getLast() {
        return last == null ? first : last;
    }

    public int size() {
        return cache.size();
    }

    public Iterable<CacheEntry<K, V>> accessSorted() {
        Collection<CacheEntry<K, V>> entries = new ArrayList<>(cache.size());
        CacheEntry<K, V> current = first;
        while (current != null) {
            entries.add(current);
            current = current.previous;
        }
        return entries;
    }

    public static class CacheEntry<T, U> {

        private final T key;
        private U value;
        private CacheEntry<T, U> previous;
        private CacheEntry<T, U> next;

        private CacheEntry(T key, U value) {
            this.key = key;
            this.value = value;
        }

        public T getKey() {
            return key;
        }

        public U getValue() {
            return value;
        }
    }
}
