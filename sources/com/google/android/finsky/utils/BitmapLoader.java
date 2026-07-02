package com.google.android.finsky.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class BitmapLoader {
    private static int MAX_CACHE_SIZE_IN_BYTES = 3145728;
    private final RequestQueue mRequestQueue;
    private Runnable mRunnable;
    private final BitmapLruCache mCachedRemoteImages = new BitmapLruCache(MAX_CACHE_SIZE_IN_BYTES);
    private final HashMap<String, RequestListenerWrapper> mInFlightRequests = Maps.newHashMap();
    private final HashMap<String, RequestListenerWrapper> mBatchedResponses = Maps.newHashMap();
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public interface BitmapLoadedHandler extends Response.Listener<BitmapContainer> {
        void onResponse(BitmapContainer bitmapContainer);
    }

    private interface RemoteRequestCreator {
        Request<?> create();
    }

    public BitmapLoader(RequestQueue queue) {
        this.mRequestQueue = queue;
    }

    public void drain() {
        this.mInFlightRequests.clear();
    }

    private BitmapContainer get(String requestUrl, String cacheKey, Bitmap defaultImage, RemoteRequestCreator remoteRequestCreator, BitmapLoadedHandler bitmapLoadedHandler) {
        if (TextUtils.isEmpty(requestUrl)) {
            return new BitmapContainer(defaultImage, null, null, null);
        }
        Bitmap cachedBitmap = this.mCachedRemoteImages.get(cacheKey);
        if (cachedBitmap != null) {
            return new BitmapContainer(cachedBitmap, null, null, null);
        }
        BitmapContainer bitmapContainer = new BitmapContainer(defaultImage, requestUrl, cacheKey, bitmapLoadedHandler);
        RequestListenerWrapper wrapper = this.mInFlightRequests.get(cacheKey);
        if (wrapper != null) {
            wrapper.addHandler(bitmapContainer);
            return bitmapContainer;
        }
        Request<?> newRequest = remoteRequestCreator.create();
        this.mRequestQueue.add(newRequest);
        this.mInFlightRequests.put(cacheKey, new RequestListenerWrapper(newRequest, bitmapContainer));
        return bitmapContainer;
    }

    public BitmapContainer get(final String requestUrl, Bitmap defaultImage, BitmapLoadedHandler bitmapLoadedHandler, final int maxWidth, final int maxHeight) {
        if (maxWidth != 0 || maxHeight == 0) {
        }
        final String cacheKey = getCacheKey(requestUrl, maxWidth, maxHeight);
        return get(requestUrl, cacheKey, defaultImage, new RemoteRequestCreator() { // from class: com.google.android.finsky.utils.BitmapLoader.1
            @Override // com.google.android.finsky.utils.BitmapLoader.RemoteRequestCreator
            public Request<?> create() {
                return new ImageRequest(requestUrl, new Response.Listener<Bitmap>() { // from class: com.google.android.finsky.utils.BitmapLoader.1.1
                    @Override // com.android.volley.Response.Listener
                    public void onResponse(Bitmap response) {
                        BitmapLoader.this.onGetImageSuccess(cacheKey, response);
                    }
                }, maxWidth, maxHeight, Bitmap.Config.RGB_565, new Response.ErrorListener() { // from class: com.google.android.finsky.utils.BitmapLoader.1.2
                    @Override // com.android.volley.Response.ErrorListener
                    public void onErrorResponse(Response.ErrorCode error, String message) {
                        BitmapLoader.this.onGetImageError(cacheKey);
                    }
                });
            }
        }, bitmapLoadedHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onGetImageSuccess(String cacheKey, Bitmap response) {
        if (response.getHeight() * response.getRowBytes() <= 512000) {
            this.mCachedRemoteImages.put(cacheKey, response);
        }
        RequestListenerWrapper wrapper = this.mInFlightRequests.remove(cacheKey);
        wrapper.responseBitmap = response;
        batchResponse(cacheKey, wrapper);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onGetImageError(String cacheKey) {
        RequestListenerWrapper wrapper = this.mInFlightRequests.remove(cacheKey);
        batchResponse(cacheKey, wrapper);
    }

    public BitmapContainer getOrBindImmediately(String requestUrl, ImageView imageView, BitmapLoadedHandler bitmapLoadedHandler) {
        return getOrBindImmediately(requestUrl, imageView, bitmapLoadedHandler, 0, 0);
    }

    public BitmapContainer getOrBindImmediately(String requestUrl, ImageView imageView, BitmapLoadedHandler bitmapLoadedHandler, int maxWidth, int maxHeight) {
        BitmapContainer container = get(requestUrl, (Bitmap) null, bitmapLoadedHandler, maxWidth, maxHeight);
        if (container.getBitmap() != null) {
            imageView.setImageBitmap(container.getBitmap());
        }
        return container;
    }

    public class BitmapContainer {
        private Bitmap mBitmap;
        private BitmapLoadedHandler mBitmapLoaded;
        private String mCacheKey;
        private final String mRequestUrl;

        public BitmapContainer(Bitmap bitmap, String requestUrl, String cacheKey, BitmapLoadedHandler handler) {
            this.mBitmap = bitmap;
            this.mRequestUrl = requestUrl;
            this.mCacheKey = cacheKey;
            this.mBitmapLoaded = handler;
        }

        public void cancelRequest() {
            if (this.mBitmapLoaded != null) {
                RequestListenerWrapper wrapper = (RequestListenerWrapper) BitmapLoader.this.mInFlightRequests.get(this.mCacheKey);
                if (wrapper == null) {
                    RequestListenerWrapper wrapper2 = (RequestListenerWrapper) BitmapLoader.this.mBatchedResponses.get(this.mCacheKey);
                    if (wrapper2 != null) {
                        wrapper2.removeHandlerAndCancelIfNecessary(this);
                        if (wrapper2.handlers.size() == 0) {
                            BitmapLoader.this.mBatchedResponses.remove(this.mCacheKey);
                            return;
                        }
                        return;
                    }
                    return;
                }
                boolean canceled = wrapper.removeHandlerAndCancelIfNecessary(this);
                if (canceled) {
                    BitmapLoader.this.mInFlightRequests.remove(this.mCacheKey);
                }
            }
        }

        public Bitmap getBitmap() {
            return this.mBitmap;
        }

        public String getRequestUrl() {
            return this.mRequestUrl;
        }
    }

    private class RequestListenerWrapper {
        private List<BitmapContainer> handlers = new ArrayList();
        private Request<?> request;
        private Bitmap responseBitmap;

        public RequestListenerWrapper(Request<?> request, BitmapContainer container) {
            this.request = request;
            this.handlers.add(container);
        }

        public void addHandler(BitmapContainer container) {
            this.handlers.add(container);
        }

        public boolean removeHandlerAndCancelIfNecessary(BitmapContainer container) {
            this.handlers.remove(container);
            if (this.handlers.size() != 0) {
                return false;
            }
            this.request.cancel();
            return true;
        }
    }

    private static String getCacheKey(String url, int maxWidth, int maxHeight) {
        return "#W" + maxWidth + "#H" + maxHeight + url;
    }

    private void batchResponse(String cacheKey, RequestListenerWrapper wrapper) {
        this.mBatchedResponses.put(cacheKey, wrapper);
        if (this.mRunnable == null) {
            this.mRunnable = new Runnable() { // from class: com.google.android.finsky.utils.BitmapLoader.3
                @Override // java.lang.Runnable
                public void run() {
                    for (RequestListenerWrapper wrapper2 : BitmapLoader.this.mBatchedResponses.values()) {
                        for (BitmapContainer container : wrapper2.handlers) {
                            container.mBitmap = wrapper2.responseBitmap;
                            container.mBitmapLoaded.onResponse(container);
                        }
                    }
                    BitmapLoader.this.mBatchedResponses.clear();
                    BitmapLoader.this.mRunnable = null;
                }
            };
            this.mHandler.postDelayed(this.mRunnable, 100L);
        }
    }
}
