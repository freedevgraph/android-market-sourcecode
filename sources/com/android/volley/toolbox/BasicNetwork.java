package com.android.volley.toolbox;

import android.content.Context;
import com.android.volley.AuthFailureException;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionException;
import com.android.volley.Request;
import com.android.volley.ServerException;
import com.android.volley.TimeoutException;
import com.android.volley.VolleyLog;
import com.google.android.finsky.utils.Maps;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.impl.cookie.DateUtils;

/* JADX INFO: loaded from: classes.dex */
public class BasicNetwork implements Network {
    protected static final boolean DEBUG = VolleyLog.DEBUG;
    protected final Context mContext;
    protected final HttpStack mHttpStack;

    public BasicNetwork(Context context) {
        this(context, new HurlStack(context));
    }

    public BasicNetwork(Context context, HttpStack httpStack) {
        this.mContext = context;
        this.mHttpStack = httpStack;
    }

    @Override // com.android.volley.Network
    public NetworkResponse performRequest(Request<?> request) throws AuthFailureException, ServerException, TimeoutException, NoConnectionException {
        long start = System.currentTimeMillis();
        String url = request.getUrl();
        HttpResponse httpResponse = null;
        try {
            Map<String, String> headers = Maps.newHashMap();
            headers.putAll(request.getHeaders());
            addCacheHeaders(headers, request.getCacheEntry());
            Map<String, String> postParams = Maps.newHashMap();
            Map<String, String> requestPostParams = request.getPostParams();
            if (requestPostParams != null) {
                postParams.putAll(requestPostParams);
            }
            byte[] rawPostBody = request.getRawPostBody();
            if (rawPostBody != null && postParams.size() > 0) {
                throw new IllegalArgumentException("Cannot handle rawPostBody and postParams at the same time.");
            }
            if (postParams.size() > 0) {
                rawPostBody = encodePostParameters(headers, postParams, request.getPostParamsEncoding());
            }
            HttpResponse httpResponse2 = this.mHttpStack.performRequest(url, headers, rawPostBody);
            StatusLine statusLine = httpResponse2.getStatusLine();
            if (DEBUG) {
                long end = System.currentTimeMillis();
                VolleyLog.v("HTTP response in %d ms, rc=%d for url %s", Long.valueOf(end - start), Integer.valueOf(statusLine.getStatusCode()), url);
            }
            if (statusLine.getStatusCode() == 304) {
                return new NetworkResponse(request.getCacheEntry().data, convertHeaders(httpResponse2.getAllHeaders()), true);
            }
            byte[] responseContents = streamToBytes(httpResponse2.getEntity().getContent());
            if (statusLine.getStatusCode() != 200) {
                throw new IOException();
            }
            return new NetworkResponse(responseContents, convertHeaders(httpResponse2.getAllHeaders()), false);
        } catch (MalformedURLException e) {
            if (DEBUG) {
                logError("BadURL", url, start);
            }
            throw new RuntimeException("Bad URL " + url, e);
        } catch (SocketTimeoutException e2) {
            if (DEBUG) {
                logError("Timeout", url, start);
            }
            throw new TimeoutException();
        } catch (IOException e3) {
            if (0 != 0) {
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode == 401) {
                    if (DEBUG) {
                        logError("AuthFailure", url, start);
                    }
                    throw new AuthFailureException();
                }
                VolleyLog.e("Unexpected response code %d for %s", Integer.valueOf(statusCode), url);
                if (0 != 0) {
                    return new NetworkResponse(null, null, false);
                }
                throw new ServerException();
            }
            if (DEBUG) {
                logError("NoConnection", url, start);
            }
            throw new NoConnectionException();
        }
    }

    private byte[] encodePostParameters(Map<String, String> headers, Map<String, String> postParams, String postParamsEncoding) {
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=" + postParamsEncoding.toLowerCase());
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : postParams.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), postParamsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), postParamsEncoding));
                encodedParams.append('&');
            }
            byte[] rawPostBody = encodedParams.toString().getBytes(postParamsEncoding);
            return rawPostBody;
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + postParamsEncoding, uee);
        }
    }

    private void addCacheHeaders(Map<String, String> headers, Cache.Entry entry) {
        if (entry != null) {
            if (entry.etag != null) {
                headers.put("If-None-Match", entry.etag);
            }
            if (entry.serverDate > 0) {
                Date refTime = new Date(entry.serverDate);
                headers.put("If-Modified-Since", DateUtils.formatDate(refTime));
            }
        }
    }

    protected void logError(String what, String url, long start) {
        long now = System.currentTimeMillis();
        VolleyLog.v("HTTP ERROR(%s) %d ms to fetch %s", what, Long.valueOf(now - start), url);
    }

    static byte[] streamToBytes(InputStream in) throws ServerException, IOException {
        if (in == null) {
            throw new ServerException();
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int count = in.read(buffer);
            if (count != -1) {
                bytes.write(buffer, 0, count);
            } else {
                return bytes.toByteArray();
            }
        }
    }

    private static Map<String, String> convertHeaders(Header[] headers) {
        Map<String, String> result = Maps.newHashMap();
        for (int i = 0; i < headers.length; i++) {
            result.put(headers[i].getName(), headers[i].getValue());
        }
        return result;
    }
}
