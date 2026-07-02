package com.android.volley.toolbox;

import com.android.volley.AuthFailureException;
import java.io.IOException;
import java.util.Map;
import org.apache.http.HttpResponse;

/* JADX INFO: loaded from: classes.dex */
public interface HttpStack {
    HttpResponse performRequest(String str, Map<String, String> map, byte[] bArr) throws AuthFailureException, IOException;
}
