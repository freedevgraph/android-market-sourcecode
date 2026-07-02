package com.android.volley;

import android.os.Handler;
import android.os.Looper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class RequestQueue {
    private static AtomicInteger sSequenceGenerator = new AtomicInteger();
    private final Cache mCache;
    private CacheDispatcher mCacheDispatcher;
    private final PriorityBlockingQueue<Request> mCacheQueue;
    private final ResponseDelivery mDelivery;
    private NetworkDispatcher[] mDispatchers;
    private final Network mNetwork;
    private final PriorityBlockingQueue<Request> mNetworkQueue;

    public RequestQueue(Cache cache, Network network, int threadPoolSize, ResponseDelivery delivery) {
        this.mCacheQueue = new PriorityBlockingQueue<>();
        this.mNetworkQueue = new PriorityBlockingQueue<>();
        this.mCache = cache;
        this.mNetwork = network;
        this.mDispatchers = new NetworkDispatcher[threadPoolSize];
        this.mDelivery = delivery;
    }

    public RequestQueue(Cache cache, Network network) {
        this(cache, network, 4, new ExecutorDelivery(new Handler(Looper.getMainLooper())));
    }

    public void start() {
        stop();
        this.mCacheDispatcher = new CacheDispatcher(this.mCacheQueue, this.mNetworkQueue, this.mCache, this.mDelivery);
        this.mCacheDispatcher.start();
        for (int i = 0; i < this.mDispatchers.length; i++) {
            NetworkDispatcher networkDispatcher = new NetworkDispatcher(this.mNetworkQueue, this.mNetwork, this.mCache, this.mDelivery);
            this.mDispatchers[i] = networkDispatcher;
            networkDispatcher.start();
        }
    }

    public void stop() {
        if (this.mCacheDispatcher != null) {
            this.mCacheDispatcher.quit();
        }
        for (int i = 0; i < this.mDispatchers.length; i++) {
            if (this.mDispatchers[i] != null) {
                this.mDispatchers[i].quit();
            }
        }
    }

    public void drain() {
        clearUndrainable(this.mCacheQueue);
        clearUndrainable(this.mNetworkQueue);
        int discardMarker = sSequenceGenerator.incrementAndGet();
        this.mDelivery.discardBefore(discardMarker);
    }

    private void clearUndrainable(PriorityBlockingQueue<Request> queue) {
        List<Request> pending = new ArrayList<>();
        queue.drainTo(pending);
        for (Request request : pending) {
            if (!request.isDrainable()) {
                queue.add(request);
            }
        }
    }

    public Request add(Request request) {
        request.setSequence(sSequenceGenerator.incrementAndGet());
        request.addMarker("add-to-queue");
        if (request.shouldCache()) {
            this.mCacheQueue.add(request);
        } else {
            this.mNetworkQueue.add(request);
        }
        return request;
    }
}
