package com.google.common.collect;

import com.google.common.base.FinalizableReferenceQueue;
import com.google.common.base.FinalizableSoftReference;
import com.google.common.base.FinalizableWeakReference;
import com.google.common.base.Function;
import com.google.common.collect.CustomConcurrentHashMap;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public final class MapMaker {
    private static final ValueReference<Object, Object> COMPUTING = new ValueReference<Object, Object>() { // from class: com.google.common.collect.MapMaker.1
        @Override // com.google.common.collect.MapMaker.ValueReference
        public Object get() {
            return null;
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public ValueReference<Object, Object> copyFor(ReferenceEntry<Object, Object> entry) {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public Object waitForValue() {
            throw new AssertionError();
        }
    };
    private boolean useCustomMap;
    private Strength keyStrength = Strength.STRONG;
    private Strength valueStrength = Strength.STRONG;
    private long expirationNanos = 0;
    private final CustomConcurrentHashMap.Builder builder = new CustomConcurrentHashMap.Builder();

    private interface ReferenceEntry<K, V> {
        int getHash();

        K getKey();

        ReferenceEntry<K, V> getNext();

        ValueReference<K, V> getValueReference();

        void setValueReference(ValueReference<K, V> valueReference);

        void valueReclaimed();
    }

    private enum Strength {
        WEAK { // from class: com.google.common.collect.MapMaker.Strength.1
            @Override // com.google.common.collect.MapMaker.Strength
            boolean equal(Object a, Object b) {
                return a == b;
            }

            @Override // com.google.common.collect.MapMaker.Strength
            int hash(Object o) {
                return System.identityHashCode(o);
            }

            @Override // com.google.common.collect.MapMaker.Strength
            <K, V> ValueReference<K, V> referenceValue(ReferenceEntry<K, V> entry, V value) {
                return new WeakValueReference(value, entry);
            }

            @Override // com.google.common.collect.MapMaker.Strength
            <K, V> ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, K key, int hash, ReferenceEntry<K, V> next) {
                return next == null ? new WeakEntry(internals, key, hash) : new LinkedWeakEntry(internals, key, hash, next);
            }
        },
        SOFT { // from class: com.google.common.collect.MapMaker.Strength.2
            @Override // com.google.common.collect.MapMaker.Strength
            boolean equal(Object a, Object b) {
                return a == b;
            }

            @Override // com.google.common.collect.MapMaker.Strength
            int hash(Object o) {
                return System.identityHashCode(o);
            }

            @Override // com.google.common.collect.MapMaker.Strength
            <K, V> ValueReference<K, V> referenceValue(ReferenceEntry<K, V> entry, V value) {
                return new SoftValueReference(value, entry);
            }

            @Override // com.google.common.collect.MapMaker.Strength
            <K, V> ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, K key, int hash, ReferenceEntry<K, V> next) {
                return next == null ? new SoftEntry(internals, key, hash) : new LinkedSoftEntry(internals, key, hash, next);
            }
        },
        STRONG { // from class: com.google.common.collect.MapMaker.Strength.3
            @Override // com.google.common.collect.MapMaker.Strength
            boolean equal(Object a, Object b) {
                return a.equals(b);
            }

            @Override // com.google.common.collect.MapMaker.Strength
            int hash(Object o) {
                return o.hashCode();
            }

            @Override // com.google.common.collect.MapMaker.Strength
            <K, V> ValueReference<K, V> referenceValue(ReferenceEntry<K, V> entry, V value) {
                return new StrongValueReference(value);
            }

            @Override // com.google.common.collect.MapMaker.Strength
            <K, V> ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, K key, int hash, ReferenceEntry<K, V> next) {
                return next == null ? new StrongEntry(internals, key, hash) : new LinkedStrongEntry(internals, key, hash, next);
            }
        };

        abstract boolean equal(Object obj, Object obj2);

        abstract int hash(Object obj);

        abstract <K, V> ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, K k, int i, ReferenceEntry<K, V> referenceEntry);

        abstract <K, V> ValueReference<K, V> referenceValue(ReferenceEntry<K, V> referenceEntry, V v);
    }

    private interface ValueReference<K, V> {
        ValueReference<K, V> copyFor(ReferenceEntry<K, V> referenceEntry);

        V get();

        V waitForValue() throws InterruptedException;
    }

    public MapMaker weakKeys() {
        return setKeyStrength(Strength.WEAK);
    }

    private MapMaker setKeyStrength(Strength strength) {
        if (this.keyStrength != Strength.STRONG) {
            throw new IllegalStateException("Key strength was already set to " + this.keyStrength + ".");
        }
        this.keyStrength = strength;
        this.useCustomMap = true;
        return this;
    }

    public <K, V> ConcurrentMap<K, V> makeComputingMap(Function<? super K, ? extends V> computingFunction) {
        return new StrategyImpl(this, computingFunction).map;
    }

    private static class StrategyImpl<K, V> implements Serializable, CustomConcurrentHashMap.ComputingStrategy<K, V, ReferenceEntry<K, V>> {
        private static final long serialVersionUID = 0;
        final long expirationNanos;
        CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals;
        final Strength keyStrength;
        final ConcurrentMap<K, V> map;
        final Strength valueStrength;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.CustomConcurrentHashMap.ComputingStrategy
        public /* bridge */ /* synthetic */ Object compute(Object obj, Object x1, Function x2) {
            return compute(obj, (ReferenceEntry<Object, V>) x1, (Function<? super Object, ? extends V>) x2);
        }

        StrategyImpl(MapMaker maker, Function<? super K, ? extends V> computer) {
            this.keyStrength = maker.keyStrength;
            this.valueStrength = maker.valueStrength;
            this.expirationNanos = maker.expirationNanos;
            this.map = maker.builder.buildComputingMap(this, computer);
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public void setValue(ReferenceEntry<K, V> entry, V value) {
            setValueReference(entry, this.valueStrength.referenceValue(entry, value));
            if (this.expirationNanos > 0) {
                scheduleRemoval(entry.getKey(), value);
            }
        }

        void scheduleRemoval(K key, V value) {
            final WeakReference<K> keyReference = new WeakReference<>(key);
            final WeakReference<V> valueReference = new WeakReference<>(value);
            ExpirationTimer.instance.schedule(new TimerTask() { // from class: com.google.common.collect.MapMaker.StrategyImpl.1
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    Object obj = keyReference.get();
                    if (obj != null) {
                        StrategyImpl.this.map.remove(obj, valueReference.get());
                    }
                }
            }, TimeUnit.NANOSECONDS.toMillis(this.expirationNanos));
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public boolean equalKeys(K a, Object b) {
            return this.keyStrength.equal(a, b);
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public boolean equalValues(V a, Object b) {
            return this.valueStrength.equal(a, b);
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public int hashKey(Object key) {
            return this.keyStrength.hash(key);
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public K getKey(ReferenceEntry<K, V> entry) {
            return entry.getKey();
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public int getHash(ReferenceEntry<K, V> entry) {
            return entry.getHash();
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public ReferenceEntry<K, V> newEntry(K key, int hash, ReferenceEntry<K, V> next) {
            return this.keyStrength.newEntry(this.internals, key, hash, next);
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public ReferenceEntry<K, V> copyEntry(K key, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
            ValueReference<K, V> valueReference = original.getValueReference();
            if (valueReference == MapMaker.COMPUTING) {
                ReferenceEntry<K, V> newEntry = newEntry((Object) key, original.getHash(), (ReferenceEntry) newNext);
                newEntry.setValueReference(new FutureValueReference(original, newEntry));
                return newEntry;
            }
            ReferenceEntry<K, V> newEntry2 = newEntry((Object) key, original.getHash(), (ReferenceEntry) newNext);
            newEntry2.setValueReference(valueReference.copyFor(newEntry2));
            return newEntry2;
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.ComputingStrategy
        public V waitForValue(ReferenceEntry<K, V> entry) throws InterruptedException {
            ValueReference<K, V> valueReference = entry.getValueReference();
            if (valueReference == MapMaker.COMPUTING) {
                synchronized (entry) {
                    while (true) {
                        valueReference = entry.getValueReference();
                        if (valueReference != MapMaker.COMPUTING) {
                            break;
                        }
                        entry.wait();
                    }
                }
            }
            return valueReference.waitForValue();
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public V getValue(ReferenceEntry<K, V> entry) {
            ValueReference<K, V> valueReference = entry.getValueReference();
            return valueReference.get();
        }

        public V compute(K key, ReferenceEntry<K, V> entry, Function<? super K, ? extends V> computer) {
            try {
                V value = computer.apply(key);
                if (value == null) {
                    String message = computer + " returned null for key " + key + ".";
                    setValueReference(entry, new NullOutputExceptionReference(message));
                    throw new NullOutputException(message);
                }
                setValue((ReferenceEntry) entry, (Object) value);
                return value;
            } catch (ComputationException e) {
                setValueReference(entry, new ComputationExceptionReference(e.getCause()));
                throw e;
            } catch (Throwable t) {
                setValueReference(entry, new ComputationExceptionReference(t));
                throw new ComputationException(t);
            }
        }

        void setValueReference(ReferenceEntry<K, V> entry, ValueReference<K, V> valueReference) {
            boolean notifyOthers = entry.getValueReference() == MapMaker.COMPUTING;
            entry.setValueReference(valueReference);
            if (notifyOthers) {
                synchronized (entry) {
                    entry.notifyAll();
                }
            }
        }

        private class FutureValueReference implements ValueReference<K, V> {
            final ReferenceEntry<K, V> newEntry;
            final ReferenceEntry<K, V> original;

            FutureValueReference(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newEntry) {
                this.original = original;
                this.newEntry = newEntry;
            }

            @Override // com.google.common.collect.MapMaker.ValueReference
            public V get() {
                boolean success = false;
                try {
                    V value = this.original.getValueReference().get();
                    success = true;
                    return value;
                } finally {
                    if (!success) {
                        removeEntry();
                    }
                }
            }

            @Override // com.google.common.collect.MapMaker.ValueReference
            public ValueReference<K, V> copyFor(ReferenceEntry<K, V> entry) {
                return new FutureValueReference(this.original, entry);
            }

            @Override // com.google.common.collect.MapMaker.ValueReference
            public V waitForValue() throws InterruptedException {
                boolean z = false;
                try {
                    boolean z2 = true;
                    return (V) StrategyImpl.this.waitForValue((ReferenceEntry) this.original);
                } finally {
                    if (!z) {
                        removeEntry();
                    }
                }
            }

            void removeEntry() {
                StrategyImpl.this.internals.removeEntry(this.newEntry);
            }
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public ReferenceEntry<K, V> getNext(ReferenceEntry<K, V> entry) {
            return entry.getNext();
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public void setInternals(CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals) {
            this.internals = internals;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeObject(this.keyStrength);
            out.writeObject(this.valueStrength);
            out.writeLong(this.expirationNanos);
            out.writeObject(this.internals);
            out.writeObject(this.map);
        }

        private static class Fields {
            static final Field keyStrength = findField("keyStrength");
            static final Field valueStrength = findField("valueStrength");
            static final Field expirationNanos = findField("expirationNanos");
            static final Field internals = findField("internals");
            static final Field map = findField("map");

            private Fields() {
            }

            static Field findField(String str) {
                try {
                    Field declaredField = StrategyImpl.class.getDeclaredField(str);
                    declaredField.setAccessible(true);
                    return declaredField;
                } catch (NoSuchFieldException e) {
                    throw new AssertionError(e);
                }
            }
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            try {
                Fields.keyStrength.set(this, in.readObject());
                Fields.valueStrength.set(this, in.readObject());
                Fields.expirationNanos.set(this, Long.valueOf(in.readLong()));
                Fields.internals.set(this, in.readObject());
                Fields.map.set(this, in.readObject());
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K, V> ValueReference<K, V> computing() {
        return (ValueReference<K, V>) COMPUTING;
    }

    private static class NullOutputExceptionReference<K, V> implements ValueReference<K, V> {
        final String message;

        NullOutputExceptionReference(String message) {
            this.message = message;
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public V get() {
            return null;
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public ValueReference<K, V> copyFor(ReferenceEntry<K, V> entry) {
            return this;
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public V waitForValue() {
            throw new NullOutputException(this.message);
        }
    }

    private static class ComputationExceptionReference<K, V> implements ValueReference<K, V> {
        final Throwable t;

        ComputationExceptionReference(Throwable t) {
            this.t = t;
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public V get() {
            return null;
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public ValueReference<K, V> copyFor(ReferenceEntry<K, V> entry) {
            return this;
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public V waitForValue() {
            throw new AsynchronousComputationException(this.t);
        }
    }

    private static class QueueHolder {
        static final FinalizableReferenceQueue queue = new FinalizableReferenceQueue();

        private QueueHolder() {
        }
    }

    private static class StrongEntry<K, V> implements ReferenceEntry<K, V> {
        final int hash;
        final CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals;
        final K key;
        volatile ValueReference<K, V> valueReference = MapMaker.computing();

        StrongEntry(CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, K key, int hash) {
            this.internals = internals;
            this.key = key;
            this.hash = hash;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public K getKey() {
            return this.key;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public void setValueReference(ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public void valueReclaimed() {
            this.internals.removeEntry(this, null);
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public ReferenceEntry<K, V> getNext() {
            return null;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public int getHash() {
            return this.hash;
        }
    }

    private static class LinkedStrongEntry<K, V> extends StrongEntry<K, V> {
        final ReferenceEntry<K, V> next;

        LinkedStrongEntry(CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, K key, int hash, ReferenceEntry<K, V> next) {
            super(internals, key, hash);
            this.next = next;
        }

        @Override // com.google.common.collect.MapMaker.StrongEntry, com.google.common.collect.MapMaker.ReferenceEntry
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }

    private static class SoftEntry<K, V> extends FinalizableSoftReference<K> implements ReferenceEntry<K, V> {
        final int hash;
        final CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals;
        volatile ValueReference<K, V> valueReference;

        SoftEntry(CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, K key, int hash) {
            super(key, QueueHolder.queue);
            this.valueReference = MapMaker.computing();
            this.internals = internals;
            this.hash = hash;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public K getKey() {
            return (K) get();
        }

        @Override // com.google.common.base.FinalizableReference
        public void finalizeReferent() {
            this.internals.removeEntry(this);
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public void setValueReference(ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public void valueReclaimed() {
            this.internals.removeEntry(this, null);
        }

        public ReferenceEntry<K, V> getNext() {
            return null;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public int getHash() {
            return this.hash;
        }
    }

    private static class LinkedSoftEntry<K, V> extends SoftEntry<K, V> {
        final ReferenceEntry<K, V> next;

        LinkedSoftEntry(CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, K key, int hash, ReferenceEntry<K, V> next) {
            super(internals, key, hash);
            this.next = next;
        }

        @Override // com.google.common.collect.MapMaker.SoftEntry, com.google.common.collect.MapMaker.ReferenceEntry
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }

    private static class WeakEntry<K, V> extends FinalizableWeakReference<K> implements ReferenceEntry<K, V> {
        final int hash;
        final CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals;
        volatile ValueReference<K, V> valueReference;

        WeakEntry(CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, K key, int hash) {
            super(key, QueueHolder.queue);
            this.valueReference = MapMaker.computing();
            this.internals = internals;
            this.hash = hash;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public K getKey() {
            return (K) get();
        }

        @Override // com.google.common.base.FinalizableReference
        public void finalizeReferent() {
            this.internals.removeEntry(this);
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public void setValueReference(ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public void valueReclaimed() {
            this.internals.removeEntry(this, null);
        }

        public ReferenceEntry<K, V> getNext() {
            return null;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public int getHash() {
            return this.hash;
        }
    }

    private static class LinkedWeakEntry<K, V> extends WeakEntry<K, V> {
        final ReferenceEntry<K, V> next;

        LinkedWeakEntry(CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, K key, int hash, ReferenceEntry<K, V> next) {
            super(internals, key, hash);
            this.next = next;
        }

        @Override // com.google.common.collect.MapMaker.WeakEntry, com.google.common.collect.MapMaker.ReferenceEntry
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }

    private static class WeakValueReference<K, V> extends FinalizableWeakReference<V> implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;

        WeakValueReference(V referent, ReferenceEntry<K, V> entry) {
            super(referent, QueueHolder.queue);
            this.entry = entry;
        }

        @Override // com.google.common.base.FinalizableReference
        public void finalizeReferent() {
            this.entry.valueReclaimed();
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public ValueReference<K, V> copyFor(ReferenceEntry<K, V> entry) {
            return new WeakValueReference(get(), entry);
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public V waitForValue() {
            return get();
        }
    }

    private static class SoftValueReference<K, V> extends FinalizableSoftReference<V> implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;

        SoftValueReference(V referent, ReferenceEntry<K, V> entry) {
            super(referent, QueueHolder.queue);
            this.entry = entry;
        }

        @Override // com.google.common.base.FinalizableReference
        public void finalizeReferent() {
            this.entry.valueReclaimed();
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public ValueReference<K, V> copyFor(ReferenceEntry<K, V> entry) {
            return new SoftValueReference(get(), entry);
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public V waitForValue() {
            return get();
        }
    }

    private static class StrongValueReference<K, V> implements ValueReference<K, V> {
        final V referent;

        StrongValueReference(V referent) {
            this.referent = referent;
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public V get() {
            return this.referent;
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public ValueReference<K, V> copyFor(ReferenceEntry<K, V> entry) {
            return this;
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public V waitForValue() {
            return get();
        }
    }
}
