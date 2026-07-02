package com.google.android.finsky.api.model;

import com.google.android.finsky.remoting.protos.DocList;

/* JADX INFO: loaded from: classes.dex */
public abstract class BucketedList<T> extends PaginatedList<T> {
    public abstract DocList.Bucket getBucket(int i);

    public abstract int getBucketCount();

    protected BucketedList(String url) {
        super(url);
    }

    protected BucketedList(String url, boolean autoLoadNextPage) {
        super(url, autoLoadNextPage);
    }
}
