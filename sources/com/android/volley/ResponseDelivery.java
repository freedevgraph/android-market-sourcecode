package com.android.volley;

import com.android.volley.Response;

/* JADX INFO: loaded from: classes.dex */
public interface ResponseDelivery {
    void discardBefore(int i);

    void postError(Request<?> request, Response.ErrorCode errorCode);

    void postResponse(Request<?> request, Response<?> response);
}
