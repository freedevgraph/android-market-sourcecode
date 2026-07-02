package com.android.volley;

import com.android.volley.Response;
import java.util.concurrent.BlockingQueue;

/* JADX INFO: loaded from: classes.dex */
public class NetworkDispatcher extends Thread {
    private final Cache mCache;
    private final ResponseDelivery mDelivery;
    private final Network mNetwork;
    private final BlockingQueue<Request> mQueue;
    private volatile boolean mQuit = false;

    public NetworkDispatcher(BlockingQueue<Request> queue, Network network, Cache cache, ResponseDelivery delivery) {
        this.mQueue = queue;
        this.mNetwork = network;
        this.mCache = cache;
        this.mDelivery = delivery;
    }

    public void quit() {
        this.mQuit = true;
        interrupt();
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            try {
                Request<?> requestTake = this.mQueue.take();
                try {
                    requestTake.addMarker("network-queue-take");
                    if (requestTake.isCanceled()) {
                        requestTake.finishMarking("network-discard-cancelled");
                    } else {
                        NetworkResponse networkResponse = this.mNetwork.performRequest(requestTake);
                        requestTake.addMarker("network-http-complete");
                        if (networkResponse.notModified && requestTake.hasHadResponseDelivered()) {
                            requestTake.finishMarking("not-modified");
                        } else {
                            Response<?> response = requestTake.parseNetworkResponse(networkResponse);
                            requestTake.addMarker("network-parse-complete");
                            if (requestTake.shouldCache() && response.cacheEntry != null) {
                                this.mCache.put(requestTake.getCacheKey(), response.cacheEntry);
                                requestTake.addMarker("network-cache-written");
                            }
                            requestTake.markDelivered();
                            this.mDelivery.postResponse(requestTake, response);
                        }
                    }
                } catch (AuthFailureException e) {
                    this.mDelivery.postError(requestTake, Response.ErrorCode.AUTH);
                } catch (NoConnectionException e2) {
                    this.mDelivery.postError(requestTake, Response.ErrorCode.NETWORK);
                } catch (ServerException e3) {
                    this.mDelivery.postError(requestTake, Response.ErrorCode.SERVER);
                } catch (TimeoutException e4) {
                    this.mDelivery.postError(requestTake, Response.ErrorCode.TIMEOUT);
                } catch (Exception e5) {
                    VolleyLog.e("Unhandled exception %s", e5.toString());
                    this.mDelivery.postError(requestTake, Response.ErrorCode.SERVER);
                }
            } catch (InterruptedException e6) {
                if (this.mQuit) {
                    return;
                }
            }
        }
    }
}
