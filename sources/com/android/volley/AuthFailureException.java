package com.android.volley;

/* JADX INFO: loaded from: classes.dex */
public class AuthFailureException extends Exception {
    public AuthFailureException() {
    }

    public AuthFailureException(String message) {
        super(message);
    }

    public AuthFailureException(String message, Exception reason) {
        super(message, reason);
    }
}
