package com.android.volley;

import java.util.Collections;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class NetworkResponse {
    public final byte[] data;
    public final Map<String, String> headers;
    public final boolean notModified;

    public NetworkResponse(byte[] data, Map<String, String> headers, boolean notModified) {
        this.data = data;
        this.headers = headers;
        this.notModified = notModified;
    }

    public NetworkResponse(byte[] data) {
        this(data, Collections.emptyMap(), false);
    }
}
