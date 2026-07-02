package com.android.volley;

/* JADX INFO: loaded from: classes.dex */
public interface Network {
    NetworkResponse performRequest(Request<?> request) throws AuthFailureException, ServerException, TimeoutException, NoConnectionException;
}
