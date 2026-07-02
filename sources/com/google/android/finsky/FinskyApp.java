package com.google.android.finsky;

import android.app.Application;
import android.content.Intent;
import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.ClearCacheRequest;
import com.android.volley.toolbox.DiskBasedCache;
import com.google.android.finsky.analytics.Analytics;
import com.google.android.finsky.analytics.GoogleAnalytics;
import com.google.android.finsky.analytics.StubAnalytics;
import com.google.android.finsky.api.DfeApi;
import com.google.android.finsky.api.DfeApiContext;
import com.google.android.finsky.api.model.DfeToc;
import com.google.android.finsky.config.G;
import com.google.android.finsky.config.GservicesValue;
import com.google.android.finsky.config.PreferenceFile;
import com.google.android.finsky.model.PurchaseStatusTracker;
import com.google.android.finsky.utils.BitmapLoader;
import com.google.android.finsky.utils.FinskyLog;
import com.google.android.finsky.utils.PackageInfoCache;
import com.google.android.vending.remoting.api.VendingApi;
import com.google.android.vending.remoting.api.VendingApiContext;

/* JADX INFO: loaded from: classes.dex */
public class FinskyApp extends Application {
    private static FinskyApp sInstance;
    private Analytics mAnalytics;
    private BitmapLoader mBitmapLoader;
    private Cache mCache;
    private DfeApi mDfeApi;
    private PackageInfoCache mPackageInfoCache;
    private PurchaseStatusTracker mPurchaseStatusTracker;
    private RequestQueue mRequestQueue;
    private DfeToc mToc;
    private VendingApi mVendingApi;

    public void drainAllRequests() {
        get().getRequestQueue().drain();
        get().getBitmapLoader().drain();
    }

    @Override // android.app.Application
    public void onCreate() {
        sInstance = this;
        GservicesValue.init(this);
        PreferenceFile.init(this);
        this.mCache = new DiskBasedCache(getCacheDir());
        this.mRequestQueue = new RequestQueue(this.mCache, new BasicNetwork(this));
        this.mRequestQueue.start();
        this.mBitmapLoader = new BitmapLoader(this.mRequestQueue);
        if (G.analyticsEnabled.get().booleanValue()) {
            this.mAnalytics = new GoogleAnalytics(this);
        } else {
            this.mAnalytics = new StubAnalytics();
        }
        this.mPurchaseStatusTracker = new PurchaseStatusTracker(this);
        this.mPackageInfoCache = new PackageInfoCache(getApplicationContext(), getPackageManager());
    }

    public static FinskyApp get() {
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return this.mRequestQueue;
    }

    public DfeApi getDfeApi() {
        return this.mDfeApi;
    }

    public Analytics getAnalytics() {
        return this.mAnalytics;
    }

    public PackageInfoCache getPackageInfoCache() {
        return this.mPackageInfoCache;
    }

    public PurchaseStatusTracker getPurchaseStatusTracker() {
        return this.mPurchaseStatusTracker;
    }

    public void clearCacheAsync(Runnable callback) {
        this.mRequestQueue.add(new ClearCacheRequest(this.mCache, callback));
    }

    public BitmapLoader getBitmapLoader() {
        return this.mBitmapLoader;
    }

    public Cache getCache() {
        return this.mCache;
    }

    public DfeToc getToc() {
        return this.mToc;
    }

    public void setApiContexts(DfeApiContext dfeContext, VendingApiContext vendingContext) {
        this.mDfeApi = new DfeApi(this.mRequestQueue, dfeContext);
        this.mVendingApi = new VendingApi(this.mRequestQueue, vendingContext);
        sendBroadcast(new Intent("com.google.android.finsky.action.DFE_API_CONTEXT_CHANGED"));
        if (FinskyLog.DEBUG) {
            FinskyLog.d("Created new APIs with contexts (dfe=%s, vending=%s)", dfeContext, vendingContext);
        }
    }

    public void setToc(DfeToc toc) {
        this.mToc = toc;
    }
}
