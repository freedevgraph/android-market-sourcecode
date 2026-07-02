package com.google.android.finsky.utils;

import java.util.Stack;

/* JADX INFO: loaded from: classes.dex */
public class MainThreadStack<T> extends Stack<T> {
    @Override // java.util.Stack
    public T pop() {
        Utils.ensureOnMainThread();
        return (T) super.pop();
    }

    @Override // java.util.Stack
    public T push(T t) {
        Utils.ensureOnMainThread();
        return (T) super.push(t);
    }

    @Override // java.util.Stack
    public T peek() {
        Utils.ensureOnMainThread();
        return (T) super.peek();
    }

    @Override // java.util.Vector, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean isEmpty() {
        Utils.ensureOnMainThread();
        return super.isEmpty();
    }
}
