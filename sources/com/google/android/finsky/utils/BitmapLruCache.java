package com.google.android.finsky.utils;

import android.graphics.Bitmap;

/* JADX INFO: loaded from: classes.dex */
public class BitmapLruCache extends LruCache<String, Bitmap> {
    public BitmapLruCache(int maxSizeInBytes) {
        super(maxSizeInBytes);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.finsky.utils.LruCache
    public int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }
}
