package com.android.volley;

import com.android.volley.Cache;

/* JADX INFO: loaded from: classes.dex */
public class Response<T> {
    public final Cache.Entry cacheEntry;
    public final ErrorCode error;
    public boolean intermediate;
    public final String message;
    public final T result;

    public enum ErrorCode {
        OK,
        SERVER,
        TIMEOUT,
        NETWORK,
        AUTH
    }

    public interface ErrorListener {
        void onErrorResponse(ErrorCode errorCode, String str);
    }

    public interface Listener<T> {
        void onResponse(T t);
    }

    public static <T> Response<T> success(T result, Cache.Entry cacheEntry) {
        return new Response<>(result, cacheEntry);
    }

    public static <T> Response<T> error(ErrorCode error, String displayMessage) {
        return new Response<>(error, displayMessage);
    }

    private Response(T result, Cache.Entry cacheEntry) {
        this.intermediate = false;
        this.result = result;
        this.cacheEntry = cacheEntry;
        this.error = ErrorCode.OK;
        this.message = null;
    }

    private Response(ErrorCode error, String message) {
        this.intermediate = false;
        this.result = null;
        this.cacheEntry = null;
        this.error = error;
        this.message = message;
    }
}
