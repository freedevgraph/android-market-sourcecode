package com.android.volley;

import android.os.Handler;
import android.os.Looper;
import com.android.volley.Cache;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import java.util.Collections;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public abstract class Request<T> implements Comparable<Request<T>> {
    private Cache.Entry mCacheEntry;
    private boolean mCanceled;
    private boolean mDrainable;
    private final Response.ErrorListener mErrorListener;
    private final VolleyLog.MarkerLog mEventLog;
    private boolean mResponseDelivered;
    private Integer mSequence;
    private boolean mShouldCache;
    private final String mUrl;

    public enum Priority {
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE
    }

    protected abstract void deliverResponse(T t);

    protected abstract Response<T> parseNetworkResponse(NetworkResponse networkResponse);

    public Request(String url, Response.ErrorListener listener) {
        this.mEventLog = VolleyLog.MarkerLog.ENABLED ? new VolleyLog.MarkerLog() : null;
        this.mShouldCache = true;
        this.mCanceled = false;
        this.mDrainable = true;
        this.mResponseDelivered = false;
        this.mCacheEntry = null;
        this.mUrl = url;
        this.mErrorListener = listener;
    }

    void addMarker(String tag) {
        if (VolleyLog.MarkerLog.ENABLED) {
            this.mEventLog.add(tag, Thread.currentThread().getId());
        }
    }

    void finishMarking(final String tag) {
        if (VolleyLog.MarkerLog.ENABLED) {
            final long threadId = Thread.currentThread().getId();
            if (Looper.myLooper() != Looper.getMainLooper()) {
                Handler mainThread = new Handler(Looper.getMainLooper());
                mainThread.post(new Runnable() { // from class: com.android.volley.Request.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Request.this.mEventLog.add(tag, threadId);
                        Request.this.mEventLog.finish(toString());
                    }
                });
            } else {
                this.mEventLog.add(tag, threadId);
                this.mEventLog.finish(toString());
            }
        }
    }

    public final void setSequence(int sequence) {
        this.mSequence = Integer.valueOf(sequence);
    }

    public final int getSequence() {
        if (this.mSequence == null) {
            throw new IllegalStateException("getSequence called before setSequence");
        }
        return this.mSequence.intValue();
    }

    public void setDrainable(boolean drainable) {
        this.mDrainable = drainable;
    }

    public boolean isDrainable() {
        return this.mDrainable;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public String getCacheKey() {
        return getUrl();
    }

    public void setCacheEntry(Cache.Entry entry) {
        this.mCacheEntry = entry;
    }

    public Cache.Entry getCacheEntry() {
        return this.mCacheEntry;
    }

    public void cancel() {
        this.mCanceled = true;
    }

    public boolean isCanceled() {
        return this.mCanceled;
    }

    public Map<String, String> getHeaders() throws AuthFailureException {
        return Collections.emptyMap();
    }

    public Map<String, String> getPostParams() throws AuthFailureException {
        return null;
    }

    public String getPostParamsEncoding() {
        return "UTF-8";
    }

    public byte[] getRawPostBody() throws AuthFailureException {
        return null;
    }

    public final void setShouldCache(boolean shouldCache) {
        this.mShouldCache = shouldCache;
    }

    public final boolean shouldCache() {
        return this.mShouldCache;
    }

    public Priority getPriority() {
        return Priority.NORMAL;
    }

    public void markDelivered() {
        this.mResponseDelivered = true;
    }

    public boolean hasHadResponseDelivered() {
        return this.mResponseDelivered;
    }

    public void deliverError(Response.ErrorCode error, String message) {
        if (this.mErrorListener != null) {
            this.mErrorListener.onErrorResponse(error, message);
        }
    }

    @Override // java.lang.Comparable
    public int compareTo(Request<T> other) {
        Priority left = getPriority();
        Priority right = other.getPriority();
        return left == right ? this.mSequence.intValue() - other.mSequence.intValue() : right.ordinal() - left.ordinal();
    }

    public String toString() {
        return (this.mCanceled ? "[X] " : "[ ] ") + getUrl() + " " + getPriority() + " " + this.mSequence;
    }
}
