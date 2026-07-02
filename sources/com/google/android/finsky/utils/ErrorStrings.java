package com.google.android.finsky.utils;

import android.content.Context;
import com.android.volley.Response;
import com.google.android.finsky.R;

/* JADX INFO: loaded from: classes.dex */
public class ErrorStrings {
    public static String get(Context context, Response.ErrorCode error, String message) {
        switch (error) {
            case AUTH:
                return context.getString(R.string.auth_required_error);
            case SERVER:
                return message != null ? message : context.getString(R.string.server_error);
            case NETWORK:
                return context.getString(R.string.network_error);
            case TIMEOUT:
                return context.getString(R.string.timeout_error);
            default:
                throw new IllegalArgumentException("Invalid error state " + error);
        }
    }
}
