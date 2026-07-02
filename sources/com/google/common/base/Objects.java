package com.google.common.base;

/* JADX INFO: loaded from: classes.dex */
public final class Objects {
    private Objects() {
    }

    public static boolean equal(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }
}
