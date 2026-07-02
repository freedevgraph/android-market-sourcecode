package com.google.android.finsky.api.model;

import com.android.volley.Response;
import com.google.android.finsky.utils.Sets;
import java.util.HashSet;

/* JADX INFO: loaded from: classes.dex */
public abstract class DfeModel implements Response.ErrorListener {
    private Response.ErrorCode mErrorCode;
    private String mErrorMessage;
    private HashSet<OnDataChangedListener> mListeners = Sets.newHashSet();
    private HashSet<Response.ErrorListener> mErrorListeners = Sets.newHashSet();

    public void addDataChangedListener(OnDataChangedListener handler) {
        this.mListeners.add(handler);
    }

    public void addErrorListener(Response.ErrorListener errorListener) {
        this.mErrorListeners.add(errorListener);
    }

    public Response.ErrorCode getErrorCode() {
        return this.mErrorCode;
    }

    public String getErrorMessage() {
        return this.mErrorMessage;
    }

    public boolean inErrorState() {
        return (this.mErrorCode == null && this.mErrorMessage == null) ? false : true;
    }

    @Override // com.android.volley.Response.ErrorListener
    public void onErrorResponse(Response.ErrorCode errorCode, String message) {
        this.mErrorCode = errorCode;
        this.mErrorMessage = message;
        notifyErrorOccured(errorCode, message);
    }

    public void removeDataChangedListener(OnDataChangedListener handler) {
        this.mListeners.remove(handler);
    }

    protected void clearErrors() {
        this.mErrorCode = null;
        this.mErrorMessage = null;
    }

    protected void unregisterAll() {
        this.mListeners.clear();
        this.mListeners.clear();
    }

    protected void notifyDataSetChanged() {
        OnDataChangedListener[] listeners = (OnDataChangedListener[]) this.mListeners.toArray(new OnDataChangedListener[0]);
        for (OnDataChangedListener onDataChangedListener : listeners) {
            onDataChangedListener.onDataChanged();
        }
    }

    protected void notifyErrorOccured(Response.ErrorCode error, String message) {
        Response.ErrorListener[] listeners = (Response.ErrorListener[]) this.mErrorListeners.toArray(new Response.ErrorListener[0]);
        for (Response.ErrorListener errorListener : listeners) {
            errorListener.onErrorResponse(error, message);
        }
    }
}
