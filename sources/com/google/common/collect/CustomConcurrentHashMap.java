package com.google.common.collect;

import com.google.common.base.Function;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: loaded from: classes.dex */
final class CustomConcurrentHashMap {

    public interface ComputingStrategy<K, V, E> extends Strategy<K, V, E> {
        V compute(K k, E e, Function<? super K, ? extends V> function);

        V waitForValue(E e) throws InterruptedException;
    }

    public interface Internals<K, V, E> {
        boolean removeEntry(E e);

        boolean removeEntry(E e, V v);
    }

    public interface Strategy<K, V, E> {
        E copyEntry(K k, E e, E e2);

        boolean equalKeys(K k, Object obj);

        boolean equalValues(V v, Object obj);

        int getHash(E e);

        K getKey(E e);

        E getNext(E e);

        V getValue(E e);

        int hashKey(Object obj);

        E newEntry(K k, int i, E e);

        void setInternals(Internals<K, V, E> internals);

        void setValue(E e, V v);
    }

    private CustomConcurrentHashMap() {
    }

    static final class Builder {
        int initialCapacity = -1;
        int concurrencyLevel = -1;

        Builder() {
        }

        public <K, V, E> ConcurrentMap<K, V> buildComputingMap(ComputingStrategy<K, V, E> strategy, Function<? super K, ? extends V> computer) {
            if (strategy == null) {
                throw new NullPointerException("strategy");
            }
            if (computer == null) {
                throw new NullPointerException("computer");
            }
            return new ComputingImpl(strategy, this, computer);
        }

        int getInitialCapacity() {
            if (this.initialCapacity == -1) {
                return 16;
            }
            return this.initialCapacity;
        }

        int getConcurrencyLevel() {
            if (this.concurrencyLevel == -1) {
                return 16;
            }
            return this.concurrencyLevel;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int rehash(int h) {
        int h2 = h + ((h << 15) ^ (-12931));
        int h3 = h2 ^ (h2 >>> 10);
        int h4 = h3 + (h3 << 3);
        int h5 = h4 ^ (h4 >>> 6);
        int h6 = h5 + (h5 << 2) + (h5 << 14);
        return (h6 >>> 16) ^ h6;
    }

    static class Impl<K, V, E> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable {
        private static final long serialVersionUID = 1;
        Set<Map.Entry<K, V>> entrySet;
        Set<K> keySet;
        final int segmentMask;
        final int segmentShift;
        final Impl<K, V, E>.Segment[] segments;
        final Strategy<K, V, E> strategy;
        Collection<V> values;

        Impl(Strategy<K, V, E> strategy, Builder builder) {
            int concurrencyLevel = builder.getConcurrencyLevel();
            int initialCapacity = builder.getInitialCapacity();
            int segmentShift = 0;
            int segmentCount = 1;
            while (segmentCount < (concurrencyLevel > 65536 ? 65536 : concurrencyLevel)) {
                segmentShift++;
                segmentCount <<= 1;
            }
            this.segmentShift = 32 - segmentShift;
            this.segmentMask = segmentCount - 1;
            this.segments = newSegmentArray(segmentCount);
            initialCapacity = initialCapacity > 1073741824 ? 1073741824 : initialCapacity;
            int segmentCapacity = initialCapacity / segmentCount;
            int segmentSize = 1;
            while (segmentSize < (segmentCapacity * segmentCount < initialCapacity ? segmentCapacity + 1 : segmentCapacity)) {
                segmentSize <<= 1;
            }
            for (int i = 0; i < this.segments.length; i++) {
                this.segments[i] = new Segment(segmentSize);
            }
            this.strategy = strategy;
            strategy.setInternals(new InternalsImpl());
        }

        int hash(Object key) {
            int h = this.strategy.hashKey(key);
            return CustomConcurrentHashMap.rehash(h);
        }

        class InternalsImpl implements Internals<K, V, E>, Serializable {
            static final long serialVersionUID = 0;

            InternalsImpl() {
            }

            @Override // com.google.common.collect.CustomConcurrentHashMap.Internals
            public boolean removeEntry(E entry, V value) {
                if (entry == null) {
                    throw new NullPointerException("entry");
                }
                int hash = Impl.this.strategy.getHash(entry);
                return Impl.this.segmentFor(hash).removeEntry(entry, hash, value);
            }

            @Override // com.google.common.collect.CustomConcurrentHashMap.Internals
            public boolean removeEntry(E entry) {
                if (entry == null) {
                    throw new NullPointerException("entry");
                }
                int hash = Impl.this.strategy.getHash(entry);
                return Impl.this.segmentFor(hash).removeEntry(entry, hash);
            }
        }

        Impl<K, V, E>.Segment[] newSegmentArray(int i) {
            return (Segment[]) Array.newInstance((Class<?>) Segment.class, i);
        }

        Impl<K, V, E>.Segment segmentFor(int hash) {
            return this.segments[(hash >>> this.segmentShift) & this.segmentMask];
        }

        final class Segment extends ReentrantLock {
            volatile int count;
            int modCount;
            volatile AtomicReferenceArray<E> table;
            int threshold;

            Segment(int initialCapacity) {
                setTable(newEntryArray(initialCapacity));
            }

            AtomicReferenceArray<E> newEntryArray(int size) {
                return new AtomicReferenceArray<>(size);
            }

            void setTable(AtomicReferenceArray<E> newTable) {
                this.threshold = (newTable.length() * 3) / 4;
                this.table = newTable;
            }

            E getFirst(int hash) {
                AtomicReferenceArray<E> table = this.table;
                return table.get((table.length() - 1) & hash);
            }

            public E getEntry(Object obj, int i) {
                K key;
                Strategy<K, V, E> strategy = Impl.this.strategy;
                if (this.count != 0) {
                    for (Object first = getFirst(i); first != null; first = strategy.getNext((E) first)) {
                        if (strategy.getHash((E) first) == i && (key = strategy.getKey((E) first)) != null && strategy.equalKeys(key, obj)) {
                            return (E) first;
                        }
                    }
                }
                return null;
            }

            V get(Object obj, int i) {
                Object entry = getEntry(obj, i);
                if (entry == null) {
                    return null;
                }
                return Impl.this.strategy.getValue((E) entry);
            }

            boolean containsKey(Object obj, int i) {
                K key;
                Strategy<K, V, E> strategy = Impl.this.strategy;
                if (this.count != 0) {
                    for (Object first = getFirst(i); first != null; first = strategy.getNext((E) first)) {
                        if (strategy.getHash((E) first) == i && (key = strategy.getKey((E) first)) != null && strategy.equalKeys(key, obj)) {
                            return strategy.getValue((E) first) != null;
                        }
                    }
                }
                return false;
            }

            /* JADX WARN: Type inference fix 'apply assigned field type' failed
            java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
            	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
            	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
            	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
             */
            boolean containsValue(Object obj) {
                Strategy<K, V, E> strategy = Impl.this.strategy;
                if (this.count != 0) {
                    AtomicReferenceArray<E> atomicReferenceArray = this.table;
                    int length = atomicReferenceArray.length();
                    for (int i = 0; i < length; i++) {
                        for (E e = atomicReferenceArray.get(i); e; e = (E) strategy.getNext(e)) {
                            V value = strategy.getValue(e);
                            if (value != null && strategy.equalValues(value, obj)) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }

            boolean replace(K k, int i, V v, V v2) {
                Strategy<K, V, E> strategy = Impl.this.strategy;
                lock();
                try {
                    for (Object first = getFirst(i); first != null; first = strategy.getNext((E) first)) {
                        K key = strategy.getKey((E) first);
                        if (strategy.getHash((E) first) == i && key != null && strategy.equalKeys(k, key)) {
                            V value = strategy.getValue((E) first);
                            if (value == null) {
                                return false;
                            }
                            if (strategy.equalValues(value, v)) {
                                strategy.setValue((E) first, v2);
                                return true;
                            }
                        }
                    }
                    return false;
                } finally {
                    unlock();
                }
            }

            V replace(K k, int i, V v) {
                Strategy<K, V, E> strategy = Impl.this.strategy;
                lock();
                try {
                    for (Object first = getFirst(i); first != null; first = strategy.getNext((E) first)) {
                        K key = strategy.getKey((E) first);
                        if (strategy.getHash((E) first) == i && key != null && strategy.equalKeys(k, key)) {
                            V value = strategy.getValue((E) first);
                            if (value == null) {
                                return null;
                            }
                            strategy.setValue((E) first, v);
                            return value;
                        }
                    }
                    return null;
                } finally {
                    unlock();
                }
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference fix 'apply assigned field type' failed
            java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
            	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
            	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
            	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
             */
            V put(K k, int i, V v, boolean z) {
                Strategy<K, V, E> strategy = Impl.this.strategy;
                lock();
                try {
                    int i2 = this.count;
                    int i3 = i2 + 1;
                    if (i2 > this.threshold) {
                        expand();
                    }
                    AtomicReferenceArray<E> atomicReferenceArray = this.table;
                    int length = i & (atomicReferenceArray.length() - 1);
                    Object obj = atomicReferenceArray.get(length);
                    for (Object next = obj; next != null; next = strategy.getNext(next)) {
                        Object key = strategy.getKey(next);
                        if (strategy.getHash(next) == i && key != null && strategy.equalKeys(k, key)) {
                            V v2 = (V) strategy.getValue(next);
                            if (z && v2 != null) {
                                return v2;
                            }
                            strategy.setValue(next, v);
                            return v2;
                        }
                    }
                    this.modCount++;
                    Object objNewEntry = strategy.newEntry(k, i, obj);
                    strategy.setValue(objNewEntry, v);
                    atomicReferenceArray.set(length, objNewEntry);
                    this.count = i3;
                    return null;
                } finally {
                    unlock();
                }
            }

            void expand() {
                AtomicReferenceArray<E> atomicReferenceArray = this.table;
                int length = atomicReferenceArray.length();
                if (length < 1073741824) {
                    Strategy<K, V, E> strategy = Impl.this.strategy;
                    AtomicReferenceArray<E> atomicReferenceArrayNewEntryArray = newEntryArray(length << 1);
                    this.threshold = (atomicReferenceArrayNewEntryArray.length() * 3) / 4;
                    int length2 = atomicReferenceArrayNewEntryArray.length() - 1;
                    for (int i = 0; i < length; i++) {
                        E e = atomicReferenceArray.get(i);
                        if (e != null) {
                            E next = strategy.getNext(e);
                            int hash = strategy.getHash(e) & length2;
                            if (next == null) {
                                atomicReferenceArrayNewEntryArray.set(hash, e);
                            } else {
                                E e2 = e;
                                int i2 = hash;
                                for (E next2 = next; next2 != null; next2 = strategy.getNext(next2)) {
                                    int hash2 = strategy.getHash(next2) & length2;
                                    if (hash2 != i2) {
                                        i2 = hash2;
                                        e2 = next2;
                                    }
                                }
                                atomicReferenceArrayNewEntryArray.set(i2, e2);
                                for (E next3 = e; next3 != e2; next3 = strategy.getNext(next3)) {
                                    K key = strategy.getKey(next3);
                                    if (key != null) {
                                        int hash3 = strategy.getHash(next3) & length2;
                                        atomicReferenceArrayNewEntryArray.set(hash3, strategy.copyEntry(key, next3, atomicReferenceArrayNewEntryArray.get(hash3)));
                                    }
                                }
                            }
                        }
                    }
                    this.table = atomicReferenceArrayNewEntryArray;
                }
            }

            V remove(Object obj, int i) {
                Strategy<K, V, E> strategy = Impl.this.strategy;
                lock();
                try {
                    int i2 = this.count - 1;
                    AtomicReferenceArray<E> atomicReferenceArray = this.table;
                    int length = i & (atomicReferenceArray.length() - 1);
                    E e = atomicReferenceArray.get(length);
                    for (E next = e; next != null; next = strategy.getNext(next)) {
                        K key = strategy.getKey(next);
                        if (strategy.getHash(next) == i && key != null && strategy.equalKeys(key, obj)) {
                            V value = Impl.this.strategy.getValue(next);
                            this.modCount++;
                            E next2 = strategy.getNext(next);
                            for (E next3 = e; next3 != next; next3 = strategy.getNext(next3)) {
                                K key2 = strategy.getKey(next3);
                                if (key2 != null) {
                                    next2 = strategy.copyEntry(key2, next3, next2);
                                }
                            }
                            atomicReferenceArray.set(length, next2);
                            this.count = i2;
                            return value;
                        }
                    }
                    return null;
                } finally {
                    unlock();
                }
            }

            boolean remove(Object obj, int i, Object obj2) {
                Strategy<K, V, E> strategy = Impl.this.strategy;
                lock();
                try {
                    int i2 = this.count - 1;
                    AtomicReferenceArray<E> atomicReferenceArray = this.table;
                    int length = i & (atomicReferenceArray.length() - 1);
                    E e = atomicReferenceArray.get(length);
                    for (E next = e; next != null; next = strategy.getNext(next)) {
                        K key = strategy.getKey(next);
                        if (strategy.getHash(next) == i && key != null && strategy.equalKeys(key, obj)) {
                            V value = Impl.this.strategy.getValue(next);
                            if (obj2 == value || (obj2 != null && value != null && strategy.equalValues(value, obj2))) {
                                this.modCount++;
                                E next2 = strategy.getNext(next);
                                for (E next3 = e; next3 != next; next3 = strategy.getNext(next3)) {
                                    K key2 = strategy.getKey(next3);
                                    if (key2 != null) {
                                        next2 = strategy.copyEntry(key2, next3, next2);
                                    }
                                }
                                atomicReferenceArray.set(length, next2);
                                this.count = i2;
                                return true;
                            }
                            return false;
                        }
                    }
                    return false;
                } finally {
                    unlock();
                }
            }

            public boolean removeEntry(E e, int i, V v) {
                Strategy<K, V, E> strategy = Impl.this.strategy;
                lock();
                try {
                    int i2 = this.count - 1;
                    AtomicReferenceArray<E> atomicReferenceArray = this.table;
                    int length = i & (atomicReferenceArray.length() - 1);
                    E e2 = atomicReferenceArray.get(length);
                    for (E next = e2; next != null; next = strategy.getNext(next)) {
                        if (strategy.getHash(next) == i && e.equals(next)) {
                            V value = strategy.getValue(next);
                            if (value == v || (v != null && strategy.equalValues(value, v))) {
                                this.modCount++;
                                E next2 = strategy.getNext(next);
                                for (E next3 = e2; next3 != next; next3 = strategy.getNext(next3)) {
                                    K key = strategy.getKey(next3);
                                    if (key != null) {
                                        next2 = strategy.copyEntry(key, next3, next2);
                                    }
                                }
                                atomicReferenceArray.set(length, next2);
                                this.count = i2;
                                return true;
                            }
                            return false;
                        }
                    }
                    return false;
                } finally {
                    unlock();
                }
            }

            public boolean removeEntry(E e, int i) {
                Strategy<K, V, E> strategy = Impl.this.strategy;
                lock();
                try {
                    int i2 = this.count - 1;
                    AtomicReferenceArray<E> atomicReferenceArray = this.table;
                    int length = i & (atomicReferenceArray.length() - 1);
                    E e2 = atomicReferenceArray.get(length);
                    for (E next = e2; next != null; next = strategy.getNext(next)) {
                        if (strategy.getHash(next) == i && e.equals(next)) {
                            this.modCount++;
                            E next2 = strategy.getNext(next);
                            for (E next3 = e2; next3 != next; next3 = strategy.getNext(next3)) {
                                K key = strategy.getKey(next3);
                                if (key != null) {
                                    next2 = strategy.copyEntry(key, next3, next2);
                                }
                            }
                            atomicReferenceArray.set(length, next2);
                            this.count = i2;
                            return true;
                        }
                    }
                    return false;
                } finally {
                    unlock();
                }
            }

            void clear() {
                if (this.count != 0) {
                    lock();
                    try {
                        AtomicReferenceArray<E> table = this.table;
                        for (int i = 0; i < table.length(); i++) {
                            table.set(i, null);
                        }
                        this.modCount++;
                        this.count = 0;
                    } finally {
                        unlock();
                    }
                }
            }
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean isEmpty() {
            Impl<K, V, E>.Segment[] segments = this.segments;
            int[] mc = new int[segments.length];
            int mcsum = 0;
            for (int i = 0; i < segments.length; i++) {
                if (segments[i].count != 0) {
                    return false;
                }
                int i2 = segments[i].modCount;
                mc[i] = i2;
                mcsum += i2;
            }
            if (mcsum != 0) {
                for (int i3 = 0; i3 < segments.length; i3++) {
                    if (segments[i3].count != 0 || mc[i3] != segments[i3].modCount) {
                        return false;
                    }
                }
            }
            return true;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            Impl<K, V, E>.Segment[] segments = this.segments;
            long sum = 0;
            long check = 0;
            int[] mc = new int[segments.length];
            for (int k = 0; k < 2; k++) {
                check = 0;
                sum = 0;
                int mcsum = 0;
                for (int i = 0; i < segments.length; i++) {
                    sum += (long) segments[i].count;
                    int i2 = segments[i].modCount;
                    mc[i] = i2;
                    mcsum += i2;
                }
                if (mcsum != 0) {
                    int i3 = 0;
                    while (true) {
                        if (i3 >= segments.length) {
                            break;
                        }
                        check += (long) segments[i3].count;
                        if (mc[i3] == segments[i3].modCount) {
                            i3++;
                        } else {
                            check = -1;
                            break;
                        }
                    }
                }
                if (check == sum) {
                    break;
                }
            }
            if (check != sum) {
                sum = 0;
                for (Impl<K, V, E>.Segment segment : segments) {
                    segment.lock();
                }
                for (Impl<K, V, E>.Segment segment2 : segments) {
                    sum += (long) segment2.count;
                }
                for (Impl<K, V, E>.Segment segment3 : segments) {
                    segment3.unlock();
                }
            }
            if (sum > 2147483647L) {
                return Integer.MAX_VALUE;
            }
            return (int) sum;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V get(Object key) {
            if (key == null) {
                throw new NullPointerException("key");
            }
            int hash = hash(key);
            return segmentFor(hash).get(key, hash);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(Object key) {
            if (key == null) {
                throw new NullPointerException("key");
            }
            int hash = hash(key);
            return segmentFor(hash).containsKey(key, hash);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsValue(Object value) {
            if (value == null) {
                throw new NullPointerException("value");
            }
            Impl<K, V, E>.Segment[] segments = this.segments;
            int[] mc = new int[segments.length];
            for (int k = 0; k < 2; k++) {
                int mcsum = 0;
                for (int i = 0; i < segments.length; i++) {
                    int i2 = segments[i].count;
                    int i3 = segments[i].modCount;
                    mc[i] = i3;
                    mcsum += i3;
                    if (segments[i].containsValue(value)) {
                        return true;
                    }
                }
                boolean cleanSweep = true;
                if (mcsum != 0) {
                    int i4 = 0;
                    while (true) {
                        if (i4 >= segments.length) {
                            break;
                        }
                        int i5 = segments[i4].count;
                        if (mc[i4] != segments[i4].modCount) {
                            cleanSweep = false;
                            break;
                        }
                        i4++;
                    }
                }
                if (cleanSweep) {
                    return false;
                }
            }
            for (Impl<K, V, E>.Segment segment : segments) {
                segment.lock();
            }
            boolean found = false;
            try {
                int len$ = segments.length;
                int i$ = 0;
                while (true) {
                    if (i$ >= len$) {
                        break;
                    }
                    Impl<K, V, E>.Segment segment2 = segments[i$];
                    if (segment2.containsValue(value)) {
                        found = true;
                        break;
                    }
                    i$++;
                }
                return found;
            } finally {
                for (Impl<K, V, E>.Segment segment3 : segments) {
                    segment3.unlock();
                }
            }
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V put(K key, V value) {
            if (key == null) {
                throw new NullPointerException("key");
            }
            if (value == null) {
                throw new NullPointerException("value");
            }
            int hash = hash(key);
            return segmentFor(hash).put(key, hash, value, false);
        }

        @Override // java.util.Map, java.util.concurrent.ConcurrentMap
        public V putIfAbsent(K key, V value) {
            if (key == null) {
                throw new NullPointerException("key");
            }
            if (value == null) {
                throw new NullPointerException("value");
            }
            int hash = hash(key);
            return segmentFor(hash).put(key, hash, value, true);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void putAll(Map<? extends K, ? extends V> m) {
            for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
                put(e.getKey(), e.getValue());
            }
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V remove(Object key) {
            if (key == null) {
                throw new NullPointerException("key");
            }
            int hash = hash(key);
            return segmentFor(hash).remove(key, hash);
        }

        @Override // java.util.Map, java.util.concurrent.ConcurrentMap
        public boolean remove(Object key, Object value) {
            if (key == null) {
                throw new NullPointerException("key");
            }
            int hash = hash(key);
            return segmentFor(hash).remove(key, hash, value);
        }

        @Override // java.util.Map, java.util.concurrent.ConcurrentMap
        public boolean replace(K key, V oldValue, V newValue) {
            if (key == null) {
                throw new NullPointerException("key");
            }
            if (oldValue == null) {
                throw new NullPointerException("oldValue");
            }
            if (newValue == null) {
                throw new NullPointerException("newValue");
            }
            int hash = hash(key);
            return segmentFor(hash).replace(key, hash, oldValue, newValue);
        }

        @Override // java.util.Map, java.util.concurrent.ConcurrentMap
        public V replace(K key, V value) {
            if (key == null) {
                throw new NullPointerException("key");
            }
            if (value == null) {
                throw new NullPointerException("value");
            }
            int hash = hash(key);
            return segmentFor(hash).replace(key, hash, value);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            for (Impl<K, V, E>.Segment segment : this.segments) {
                segment.clear();
            }
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<K> keySet() {
            Set<K> ks = this.keySet;
            if (ks != null) {
                return ks;
            }
            KeySet keySet = new KeySet();
            this.keySet = keySet;
            return keySet;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Collection<V> values() {
            Collection<V> vs = this.values;
            if (vs != null) {
                return vs;
            }
            Values values = new Values();
            this.values = values;
            return values;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> es = this.entrySet;
            if (es != null) {
                return es;
            }
            EntrySet entrySet = new EntrySet();
            this.entrySet = entrySet;
            return entrySet;
        }

        abstract class HashIterator {
            AtomicReferenceArray<E> currentTable;
            Impl<K, V, E>.WriteThroughEntry lastReturned;
            E nextEntry;
            Impl<K, V, E>.WriteThroughEntry nextExternal;
            int nextSegmentIndex;
            int nextTableIndex = -1;

            HashIterator() {
                this.nextSegmentIndex = Impl.this.segments.length - 1;
                advance();
            }

            final void advance() {
                this.nextExternal = null;
                if (!nextInChain() && !nextInTable()) {
                    while (this.nextSegmentIndex >= 0) {
                        Impl<K, V, E>.Segment[] segmentArr = Impl.this.segments;
                        int i = this.nextSegmentIndex;
                        this.nextSegmentIndex = i - 1;
                        Impl<K, V, E>.Segment seg = segmentArr[i];
                        if (seg.count != 0) {
                            this.currentTable = seg.table;
                            this.nextTableIndex = this.currentTable.length() - 1;
                            if (nextInTable()) {
                                return;
                            }
                        }
                    }
                }
            }

            boolean nextInChain() {
                Strategy<K, V, E> s = Impl.this.strategy;
                if (this.nextEntry != null) {
                    this.nextEntry = s.getNext(this.nextEntry);
                    while (this.nextEntry != null) {
                        if (!advanceTo(this.nextEntry)) {
                            this.nextEntry = s.getNext(this.nextEntry);
                        } else {
                            return true;
                        }
                    }
                }
                return false;
            }

            boolean nextInTable() {
                while (this.nextTableIndex >= 0) {
                    AtomicReferenceArray<E> atomicReferenceArray = this.currentTable;
                    int i = this.nextTableIndex;
                    this.nextTableIndex = i - 1;
                    E e = atomicReferenceArray.get(i);
                    this.nextEntry = e;
                    if (e != null && (advanceTo(this.nextEntry) || nextInChain())) {
                        return true;
                    }
                }
                return false;
            }

            boolean advanceTo(E entry) {
                Strategy<K, V, E> s = Impl.this.strategy;
                K key = s.getKey(entry);
                V value = s.getValue(entry);
                if (key == null || value == null) {
                    return false;
                }
                this.nextExternal = new WriteThroughEntry(key, value);
                return true;
            }

            public boolean hasNext() {
                return this.nextExternal != null;
            }

            Impl<K, V, E>.WriteThroughEntry nextEntry() {
                if (this.nextExternal == null) {
                    throw new NoSuchElementException();
                }
                this.lastReturned = this.nextExternal;
                advance();
                return this.lastReturned;
            }

            public void remove() {
                if (this.lastReturned == null) {
                    throw new IllegalStateException();
                }
                Impl.this.remove(this.lastReturned.getKey());
                this.lastReturned = null;
            }
        }

        final class KeyIterator extends Impl<K, V, E>.HashIterator implements Iterator<K> {
            KeyIterator() {
                super();
            }

            @Override // java.util.Iterator
            public K next() {
                return super.nextEntry().getKey();
            }
        }

        final class ValueIterator extends Impl<K, V, E>.HashIterator implements Iterator<V> {
            ValueIterator() {
                super();
            }

            @Override // java.util.Iterator
            public V next() {
                return super.nextEntry().getValue();
            }
        }

        final class WriteThroughEntry extends AbstractMapEntry<K, V> {
            final K key;
            V value;

            WriteThroughEntry(K key, V value) {
                this.key = key;
                this.value = value;
            }

            @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
            public K getKey() {
                return this.key;
            }

            @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
            public V getValue() {
                return this.value;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
            public V setValue(V v) {
                if (v == null) {
                    throw new NullPointerException();
                }
                V v2 = (V) Impl.this.put(getKey(), v);
                this.value = v;
                return v2;
            }
        }

        final class EntryIterator extends Impl<K, V, E>.HashIterator implements Iterator<Map.Entry<K, V>> {
            EntryIterator() {
                super();
            }

            @Override // java.util.Iterator
            public Map.Entry<K, V> next() {
                return nextEntry();
            }
        }

        final class KeySet extends AbstractSet<K> {
            KeySet() {
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<K> iterator() {
                return new KeyIterator();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return Impl.this.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean isEmpty() {
                return Impl.this.isEmpty();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(Object o) {
                return Impl.this.containsKey(o);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(Object o) {
                return Impl.this.remove(o) != null;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public void clear() {
                Impl.this.clear();
            }
        }

        final class Values extends AbstractCollection<V> {
            Values() {
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
            public Iterator<V> iterator() {
                return new ValueIterator();
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public int size() {
                return Impl.this.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public boolean isEmpty() {
                return Impl.this.isEmpty();
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public boolean contains(Object o) {
                return Impl.this.containsValue(o);
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public void clear() {
                Impl.this.clear();
            }
        }

        final class EntrySet extends AbstractSet<Map.Entry<K, V>> {
            EntrySet() {
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Map.Entry<K, V>> iterator() {
                return new EntryIterator();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(Object obj) {
                Map.Entry entry;
                Object key;
                if ((obj instanceof Map.Entry) && (key = (entry = (Map.Entry) obj).getKey()) != null) {
                    Object obj2 = Impl.this.get(key);
                    if (obj2 != null) {
                        if (Impl.this.strategy.equalValues((V) obj2, entry.getValue())) {
                            return true;
                        }
                    }
                    return false;
                }
                return false;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(Object o) {
                if (!(o instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry<?, ?> e = (Map.Entry) o;
                Object key = e.getKey();
                return key != null && Impl.this.remove(key, e.getValue());
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return Impl.this.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean isEmpty() {
                return Impl.this.isEmpty();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public void clear() {
                Impl.this.clear();
            }
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeInt(size());
            out.writeInt(this.segments.length);
            out.writeObject(this.strategy);
            for (Map.Entry<K, V> entry : entrySet()) {
                out.writeObject(entry.getKey());
                out.writeObject(entry.getValue());
            }
            out.writeObject(null);
        }

        static class Fields {
            static final Field segmentShift = findField("segmentShift");
            static final Field segmentMask = findField("segmentMask");
            static final Field segments = findField("segments");
            static final Field strategy = findField("strategy");

            Fields() {
            }

            static Field findField(String str) {
                try {
                    Field declaredField = Impl.class.getDeclaredField(str);
                    declaredField.setAccessible(true);
                    return declaredField;
                } catch (NoSuchFieldException e) {
                    throw new AssertionError(e);
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            try {
                int initialCapacity = in.readInt();
                int concurrencyLevel = in.readInt();
                Strategy<K, V, E> strategy = (Strategy) in.readObject();
                if (concurrencyLevel > 65536) {
                    concurrencyLevel = 65536;
                }
                int segmentShift = 0;
                int segmentCount = 1;
                while (segmentCount < concurrencyLevel) {
                    segmentShift++;
                    segmentCount <<= 1;
                }
                Fields.segmentShift.set(this, Integer.valueOf(32 - segmentShift));
                Fields.segmentMask.set(this, Integer.valueOf(segmentCount - 1));
                Fields.segments.set(this, newSegmentArray(segmentCount));
                if (initialCapacity > 1073741824) {
                    initialCapacity = 1073741824;
                }
                int segmentCapacity = initialCapacity / segmentCount;
                if (segmentCapacity * segmentCount < initialCapacity) {
                    segmentCapacity++;
                }
                int segmentSize = 1;
                while (segmentSize < segmentCapacity) {
                    segmentSize <<= 1;
                }
                for (int i = 0; i < this.segments.length; i++) {
                    this.segments[i] = new Segment(segmentSize);
                }
                Fields.strategy.set(this, strategy);
                while (true) {
                    Object object = in.readObject();
                    if (object != null) {
                        put(object, in.readObject());
                    } else {
                        return;
                    }
                }
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            }
        }
    }

    static class ComputingImpl<K, V, E> extends Impl<K, V, E> {
        static final long serialVersionUID = 0;
        final Function<? super K, ? extends V> computer;
        final ComputingStrategy<K, V, E> computingStrategy;

        ComputingImpl(ComputingStrategy<K, V, E> strategy, Builder builder, Function<? super K, ? extends V> computer) {
            super(strategy, builder);
            this.computingStrategy = strategy;
            this.computer = computer;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:34:0x00af A[PHI: r8
          0x00af: PHI (r8v1 java.lang.Object) = (r8v0 java.lang.Object), (r8v3 java.lang.Object) binds: [B:8:0x001e, B:17:0x0079] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        @Override // com.google.common.collect.CustomConcurrentHashMap.Impl, java.util.AbstractMap, java.util.Map
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public V get(java.lang.Object r21) {
            /*
                Method dump skipped, instruction units count: 231
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.CustomConcurrentHashMap.ComputingImpl.get(java.lang.Object):java.lang.Object");
        }
    }
}
