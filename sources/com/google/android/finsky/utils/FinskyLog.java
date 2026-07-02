package com.google.android.finsky.utils;

import android.util.Log;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class FinskyLog {
    private static String TAG = "Finsky";
    public static final boolean DEBUG = Log.isLoggable(TAG, 2);

    public static void v(String format, Object... args) {
        Log.v(TAG, buildMessage(format, args));
    }

    public static void d(String format, Object... args) {
        Log.d(TAG, buildMessage(format, args));
    }

    public static void w(String format, Object... args) {
        Log.w(TAG, buildMessage(format, args));
    }

    public static void e(String format, Object... args) {
        Log.e(TAG, buildMessage(format, args));
    }

    public static void wtf(String format, Object... args) {
        Log.wtf(TAG, buildMessage(format, args));
    }

    private static String buildMessage(String str, Object... objArr) {
        String str2;
        String str3 = objArr == null ? str : String.format(Locale.US, str, objArr);
        StackTraceElement[] stackTrace = new Throwable().fillInStackTrace().getStackTrace();
        int i = 2;
        while (true) {
            if (i >= stackTrace.length) {
                str2 = "<unknown>";
                break;
            }
            if (stackTrace[i].getClass().equals(FinskyLog.class)) {
                i++;
            } else {
                String className = stackTrace[i].getClassName();
                String strSubstring = className.substring(className.lastIndexOf(46) + 1);
                str2 = strSubstring.substring(strSubstring.lastIndexOf(36) + 1) + "." + stackTrace[i].getMethodName();
                break;
            }
        }
        return String.format(Locale.US, "[%d] %s: %s", Long.valueOf(Thread.currentThread().getId()), str2, str3);
    }
}
