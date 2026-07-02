package com.android.volley;

import android.util.Log;
import com.google.android.finsky.utils.Lists;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class VolleyLog {
    private static String TAG = "Volley";
    public static final boolean DEBUG = Log.isLoggable(TAG, 2);

    public static void v(String format, Object... args) {
        if (DEBUG) {
            Log.v(TAG, buildMessage(format, args));
        }
    }

    public static void d(String format, Object... args) {
        Log.d(TAG, buildMessage(format, args));
    }

    public static void e(String format, Object... args) {
        Log.e(TAG, buildMessage(format, args));
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
            if (stackTrace[i].getClass().equals(VolleyLog.class)) {
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

    static class MarkerLog {
        public static final boolean ENABLED = VolleyLog.DEBUG;
        private final List<Marker> mMarkers = Lists.newArrayList();
        private boolean mFinished = false;

        MarkerLog() {
        }

        private static class Marker {
            public final String name;
            public final long thread;
            public final long time;

            public Marker(String name, long thread, long time) {
                this.name = name;
                this.thread = thread;
                this.time = time;
            }
        }

        public synchronized void add(String name, long threadId) {
            if (this.mFinished) {
                throw new IllegalStateException("Marker added to finished log");
            }
            this.mMarkers.add(new Marker(name, threadId, System.currentTimeMillis()));
        }

        public synchronized void finish(String header) {
            this.mFinished = true;
            long duration = getTotalDuration();
            if (duration > 0) {
                long prevTime = this.mMarkers.get(0).time;
                VolleyLog.d("(%-4d ms) %s", Long.valueOf(duration), header);
                for (Marker marker : this.mMarkers) {
                    long thisTime = marker.time;
                    VolleyLog.d("(+%-4d) [%2d] %s", Long.valueOf(thisTime - prevTime), Long.valueOf(marker.thread), marker.name);
                    prevTime = thisTime;
                }
            }
        }

        protected void finalize() throws Throwable {
            if (!this.mFinished) {
                finish("Request on the loose");
                VolleyLog.e("Marker log finalized without finish() - uncaught exit point for request", new Object[0]);
            }
        }

        private long getTotalDuration() {
            if (this.mMarkers.size() == 0) {
                return 0L;
            }
            long first = this.mMarkers.get(0).time;
            long last = this.mMarkers.get(this.mMarkers.size() - 1).time;
            return last - first;
        }
    }
}
