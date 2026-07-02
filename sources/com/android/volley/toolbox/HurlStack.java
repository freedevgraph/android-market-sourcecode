package com.android.volley.toolbox;

import android.content.Context;
import com.android.volley.VolleyLog;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

/* JADX INFO: loaded from: classes.dex */
public class HurlStack implements HttpStack {
    private static final boolean DEBUG = VolleyLog.DEBUG;
    private final Context mContext;

    public HurlStack(Context context) {
        this.mContext = context;
    }

    @Override // com.android.volley.toolbox.HttpStack
    public HttpResponse performRequest(String url, Map<String, String> headers, byte[] rawPostBody) throws IOException {
        String rewritten = UrlTools.rewrite(this.mContext, url);
        if (DEBUG) {
            VolleyLog.v("HTTP get %s -> %s", url, rewritten);
        }
        if (rewritten == null) {
            throw new IOException("URL blocked by UrlRules: " + url);
        }
        URL parsedUrl = new URL(rewritten);
        HttpURLConnection connection = openConnection(parsedUrl);
        for (String headerName : headers.keySet()) {
            connection.addRequestProperty(headerName, headers.get(headerName));
        }
        handlePost(connection, rawPostBody);
        ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
        int responseCode = connection.getResponseCode();
        if (responseCode == -1) {
            throw new IOException("Could not retrieve response code from HttpUrlConnection.");
        }
        StatusLine responseStatus = new BasicStatusLine(protocolVersion, connection.getResponseCode(), connection.getResponseMessage());
        BasicHttpResponse response = new BasicHttpResponse(responseStatus);
        response.setEntity(entityFromConnection(connection));
        for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
            if (header.getKey() != null) {
                Header h = new BasicHeader(header.getKey(), header.getValue().get(0));
                response.addHeader(h);
            }
        }
        return response;
    }

    private static HttpEntity entityFromConnection(HttpURLConnection connection) {
        InputStream inputStream;
        BasicHttpEntity entity = new BasicHttpEntity();
        try {
            inputStream = connection.getInputStream();
        } catch (IOException e) {
            inputStream = connection.getErrorStream();
        }
        entity.setContent(inputStream);
        entity.setContentLength(connection.getContentLength());
        entity.setContentEncoding(connection.getContentEncoding());
        entity.setContentType(connection.getContentType());
        return entity;
    }

    private HttpURLConnection openConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(20000);
        connection.setReadTimeout(20000);
        connection.setUseCaches(false);
        connection.setDoInput(true);
        return connection;
    }

    private void handlePost(HttpURLConnection connection, byte[] rawPostBody) throws IOException {
        if (rawPostBody != null) {
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(rawPostBody);
            out.close();
        }
    }
}
