package com.google.android.finsky.api;

import com.android.volley.Response;
import com.google.protobuf.micro.MessageMicro;

/* JADX INFO: loaded from: classes.dex */
public class PaginatedDfeRequest<T extends MessageMicro> extends DfeRequest<T> implements Response.Listener<T> {
    private final int mCount;
    private final PaginatedListener<T> mListener;
    private final int mOffset;

    public interface PaginatedListener<T> {
        void onResponse(T t, int i, int i2);
    }

    public PaginatedDfeRequest(String url, int offset, int count, DfeApiContext apiContext, Class<T> responseClass, PaginatedListener<T> listener, Response.ErrorListener errorListener) {
        super(paginateUrl(url, offset, count), apiContext, responseClass, null, errorListener);
        this.mListener = listener;
        setListener(this);
        this.mOffset = offset;
        this.mCount = count;
    }

    private static String paginateUrl(String url, int offset, int count) {
        return url + "&n=" + count + "&o=" + offset;
    }

    @Override // com.android.volley.Response.Listener
    public void onResponse(T response) {
        this.mListener.onResponse(response, this.mOffset, this.mCount);
    }
}
