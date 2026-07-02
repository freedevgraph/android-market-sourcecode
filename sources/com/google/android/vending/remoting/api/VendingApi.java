package com.google.android.vending.remoting.api;

import com.android.volley.RequestQueue;

/* JADX INFO: loaded from: classes.dex */
public class VendingApi {
    private final VendingApiContext mApiContext;
    private final RequestQueue mRequestQueue;

    public VendingApi(RequestQueue requestQueue, VendingApiContext apiContext) {
        this.mRequestQueue = requestQueue;
        this.mApiContext = apiContext;
    }
}
