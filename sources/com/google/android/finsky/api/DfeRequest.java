package com.google.android.finsky.api;

import android.net.Uri;
import com.android.volley.AuthFailureException;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.MicroProtoHelper;
import com.google.android.finsky.remoting.protos.Response;
import com.google.android.finsky.utils.FinskyLog;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.protobuf.micro.MessageMicro;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class DfeRequest<T extends MessageMicro> extends Request<Response.ResponseWrapper> {
    private static final boolean DEBUG = FinskyLog.DEBUG;
    private final DfeApiContext mApiContext;
    private Response.Listener<T> mListener;
    private final Class<T> mResponseClass;

    public DfeRequest(String url, DfeApiContext apiContext, Class<T> responseClass, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Uri.withAppendedPath(DfeApi.BASE_URI, url).toString(), errorListener);
        this.mApiContext = apiContext;
        this.mListener = listener;
        this.mResponseClass = responseClass;
    }

    public void setListener(Response.Listener<T> listener) {
        this.mListener = listener;
    }

    @Override // com.android.volley.Request
    public Map<String, String> getHeaders() throws AuthFailureException {
        return this.mApiContext.getHeaders();
    }

    @Override // com.android.volley.Request
    public com.android.volley.Response<Response.ResponseWrapper> parseNetworkResponse(NetworkResponse response) {
        if (DEBUG) {
            FinskyLog.v("Response size: %d", Integer.valueOf(response.data.length));
        }
        try {
            Response.ResponseWrapper wrapper = Response.ResponseWrapper.parseFrom(response.data);
            com.android.volley.Response<Response.ResponseWrapper> error = handleServerCommands(wrapper);
            if (error != null) {
                return error;
            }
            Cache.Entry cacheEntry = parseCacheHeaders(response);
            return com.android.volley.Response.success(wrapper, cacheEntry);
        } catch (InvalidProtocolBufferMicroException e) {
            return com.android.volley.Response.error(Response.ErrorCode.SERVER, null);
        }
    }

    public static Cache.Entry parseCacheHeaders(NetworkResponse response) {
        Cache.Entry entry = HttpHeaderParser.parseCacheHeaders(response);
        long now = System.currentTimeMillis();
        try {
            String softTtlHeader = response.headers.get("X-DFE-Soft-TTL");
            if (softTtlHeader != null) {
                entry.softTtl = Long.parseLong(softTtlHeader) + now;
            }
            String hardTtlHeader = response.headers.get("X-DFE-Hard-TTL");
            if (hardTtlHeader != null) {
                entry.ttl = Long.parseLong(hardTtlHeader) + now;
            }
        } catch (NumberFormatException e) {
            FinskyLog.d("Invalid TTL: %s", response.headers);
            entry.softTtl = 0L;
            entry.ttl = 0L;
        }
        entry.ttl = Math.max(entry.ttl, entry.softTtl);
        return entry;
    }

    private com.android.volley.Response<Response.ResponseWrapper> handleServerCommands(Response.ResponseWrapper wrapper) {
        if (!wrapper.hasCommands()) {
            return null;
        }
        Response.ServerCommands commands = wrapper.getCommands();
        if (commands.hasLogErrorStacktrace()) {
            FinskyLog.d(commands.getLogErrorStacktrace(), new Object[0]);
        }
        if (commands.getClearCache()) {
            this.mApiContext.clearCache();
        }
        if (commands.hasDisplayErrorMessage()) {
            return com.android.volley.Response.error(Response.ErrorCode.SERVER, commands.getDisplayErrorMessage());
        }
        return null;
    }

    @Override // com.android.volley.Request
    public void deliverResponse(Response.ResponseWrapper responseWrapper) {
        MessageMicro parsedResponseFromWrapper = MicroProtoHelper.getParsedResponseFromWrapper(responseWrapper.getPayload(), Response.Payload.class, this.mResponseClass);
        if (parsedResponseFromWrapper != null) {
            this.mListener.onResponse((T) parsedResponseFromWrapper);
        } else {
            FinskyLog.e("Null parsed response for %s, request %s", this.mResponseClass.getSimpleName(), getClass().getSimpleName());
            deliverError(Response.ErrorCode.SERVER, null);
        }
    }

    @Override // com.android.volley.Request
    public void deliverError(Response.ErrorCode error, String message) {
        if (error == Response.ErrorCode.AUTH) {
            this.mApiContext.invalidateAuthToken();
        }
        super.deliverError(error, message);
    }
}
