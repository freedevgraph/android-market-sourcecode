package com.google.android.finsky.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class LruCache<K, V> {
    private int createCount;
    private int evictionCount;
    private int hitCount;
    private final LinkedHashMap<K, V> map;
    private int maxSize;
    private int missCount;
    private int putCount;
    private int size;

    public LruCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize;
        this.map = new LinkedHashMap<>(0, 0.75f, true);
    }

    public final synchronized V get(K key) {
        V result;
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        V result2 = this.map.get(key);
        if (result2 != null) {
            this.hitCount++;
            result = result2;
        } else {
            this.missCount++;
            V result3 = create(key);
            if (result3 != null) {
                this.createCount++;
                this.size += safeSizeOf(key, result3);
                this.map.put(key, result3);
                trimToSize(this.maxSize);
            }
            result = result3;
        }
        return result;
    }

    public final synchronized V put(K key, V value) {
        V previous;
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }
        this.putCount++;
        this.size += safeSizeOf(key, value);
        previous = this.map.put(key, value);
        if (previous != null) {
            this.size -= safeSizeOf(key, previous);
        }
        trimToSize(this.maxSize);
        return previous;
    }

    private void trimToSize(int maxSize) {
        Map.Entry<K, V> toEvict;
        while (this.size > maxSize && !this.map.isEmpty() && (toEvict = this.map.entrySet().iterator().next()) != null) {
            K key = toEvict.getKey();
            V value = toEvict.getValue();
            this.map.remove(key);
            this.size -= safeSizeOf(key, value);
            this.evictionCount++;
            entryEvicted(key, value);
        }
        if (this.size < 0 || (this.map.isEmpty() && this.size != 0)) {
            throw new IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
        }
    }

    protected void entryEvicted(K key, V value) {
    }

    protected V create(K key) {
        return null;
    }

    private int safeSizeOf(K key, V value) {
        int result = sizeOf(key, value);
        if (result < 0) {
            throw new IllegalStateException("Negative size: " + key + "=" + value);
        }
        return result;
    }

    protected int sizeOf(K key, V value) {
        return 1;
    }

    public final synchronized String toString() {
        int hitPercent;
        int accesses = this.hitCount + this.missCount;
        hitPercent = accesses != 0 ? (this.hitCount * 100) / accesses : 0;
        return String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", Integer.valueOf(this.maxSize), Integer.valueOf(this.hitCount), Integer.valueOf(this.missCount), Integer.valueOf(hitPercent));
    }
}
