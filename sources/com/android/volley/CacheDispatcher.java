package com.android.volley;

import com.android.volley.Cache;
import java.util.concurrent.BlockingQueue;

/* JADX INFO: loaded from: classes.dex */
public class CacheDispatcher extends Thread {
    private static final boolean DEBUG = VolleyLog.DEBUG;
    private final Cache mCache;
    private final BlockingQueue<Request> mCacheQueue;
    private final ResponseDelivery mDelivery;
    private final BlockingQueue<Request> mNetworkQueue;
    private volatile boolean mQuit = false;

    public CacheDispatcher(BlockingQueue<Request> cacheQueue, BlockingQueue<Request> networkQueue, Cache cache, ResponseDelivery delivery) {
        this.mCacheQueue = cacheQueue;
        this.mNetworkQueue = networkQueue;
        this.mCache = cache;
        this.mDelivery = delivery;
    }

    public void quit() {
        this.mQuit = true;
        interrupt();
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        if (DEBUG) {
            VolleyLog.v("start new dispatcher", new Object[0]);
        }
        this.mCache.initialize();
        while (true) {
            try {
                Request<?> requestTake = this.mCacheQueue.take();
                requestTake.addMarker("cache-queue-take");
                if (requestTake.isCanceled()) {
                    requestTake.finishMarking("cache-discard-canceled");
                } else {
                    Cache.Entry entry = this.mCache.get(requestTake.getCacheKey());
                    if (entry == null) {
                        requestTake.addMarker("cache-miss");
                        this.mNetworkQueue.put(requestTake);
                    } else {
                        if (!entry.isExpired()) {
                            requestTake.addMarker("cache-hit");
                            Response<?> response = requestTake.parseNetworkResponse(new NetworkResponse(entry.data));
                            requestTake.addMarker("cache-hit-parsed");
                            response.intermediate = entry.refreshNeeded();
                            requestTake.markDelivered();
                            this.mDelivery.postResponse(requestTake, response);
                        }
                        if (entry.refreshNeeded()) {
                            requestTake.addMarker("cache-hit-refresh-needed");
                            requestTake.setCacheEntry(entry);
                            this.mNetworkQueue.put(requestTake);
                        }
                    }
                }
            } catch (InterruptedException e) {
                if (this.mQuit) {
                    return;
                }
            }
        }
    }
}
