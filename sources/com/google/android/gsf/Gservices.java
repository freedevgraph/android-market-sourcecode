package com.google.android.gsf;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public class Gservices {
    private static HashMap<String, String> sCache;
    private static ContentResolver sResolver;
    private static Object sVersionToken;
    public static final Uri CONTENT_URI = Uri.parse("content://com.google.android.gsf.gservices");
    public static final Uri CONTENT_PREFIX_URI = Uri.parse("content://com.google.android.gsf.gservices/prefix");
    public static final Pattern TRUE_PATTERN = Pattern.compile("^(1|true|t|on|yes|y)$", 2);
    public static final Pattern FALSE_PATTERN = Pattern.compile("^(0|false|f|off|no|n)$", 2);

    /* JADX WARN: Type inference failed for: r0v3, types: [com.google.android.gsf.Gservices$1] */
    private static void ensureCacheInitializedLocked(final ContentResolver cr) {
        if (sCache == null) {
            sCache = new HashMap<>();
            sVersionToken = new Object();
            sResolver = cr;
            new Thread() { // from class: com.google.android.gsf.Gservices.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    Looper.prepare();
                    cr.registerContentObserver(Gservices.CONTENT_URI, true, new ContentObserver(new Handler(Looper.myLooper())) { // from class: com.google.android.gsf.Gservices.1.1
                        @Override // android.database.ContentObserver
                        public void onChange(boolean z) {
                            synchronized (Gservices.class) {
                                Gservices.sCache.clear();
                                Object unused = Gservices.sVersionToken = new Object();
                            }
                        }
                    });
                    Looper.loop();
                }
            }.start();
        }
    }

    public static String getString(ContentResolver contentResolver, String str, String str2) {
        synchronized (Gservices.class) {
            ensureCacheInitializedLocked(contentResolver);
            Object obj = sVersionToken;
            if (sCache.containsKey(str)) {
                String str3 = sCache.get(str);
                return str3 != null ? str3 : str2;
            }
            Cursor cursorQuery = sResolver.query(CONTENT_URI, null, null, new String[]{str}, null);
            if (cursorQuery == null) {
                return str2;
            }
            try {
                cursorQuery.moveToFirst();
                String string = cursorQuery.getString(1);
                synchronized (Gservices.class) {
                    if (obj == sVersionToken) {
                        sCache.put(str, string);
                    }
                }
                if (string == null) {
                    string = str2;
                }
                cursorQuery.close();
                return string;
            } catch (Throwable th) {
                cursorQuery.close();
                throw th;
            }
        }
    }

    public static String getString(ContentResolver cr, String key) {
        return getString(cr, key, null);
    }

    public static int getInt(ContentResolver cr, String key, int defValue) {
        String valString = getString(cr, key);
        if (valString == null) {
            return defValue;
        }
        try {
            int value = Integer.parseInt(valString);
            return value;
        } catch (NumberFormatException e) {
            return defValue;
        }
    }

    public static long getLong(ContentResolver cr, String key, long defValue) {
        String valString = getString(cr, key);
        if (valString == null) {
            return defValue;
        }
        try {
            long value = Long.parseLong(valString);
            return value;
        } catch (NumberFormatException e) {
            return defValue;
        }
    }

    public static boolean getBoolean(ContentResolver cr, String key, boolean defValue) {
        String valString = getString(cr, key);
        if (valString == null || valString.equals("")) {
            return defValue;
        }
        if (TRUE_PATTERN.matcher(valString).matches()) {
            return true;
        }
        if (FALSE_PATTERN.matcher(valString).matches()) {
            return false;
        }
        Log.w("Gservices", "attempt to read gservices key " + key + " (value \"" + valString + "\") as boolean");
        return defValue;
    }

    public static Map<String, String> getStringsByPrefix(ContentResolver cr, String... prefixes) {
        Cursor c = cr.query(CONTENT_PREFIX_URI, null, null, prefixes, null);
        TreeMap<String, String> out = new TreeMap<>();
        if (c != null) {
            while (c.moveToNext()) {
                try {
                    out.put(c.getString(0), c.getString(1));
                } finally {
                    c.close();
                }
            }
        }
        return out;
    }

    public static Object getVersionToken(ContentResolver contentResolver) {
        Object obj;
        synchronized (Gservices.class) {
            ensureCacheInitializedLocked(contentResolver);
            obj = sVersionToken;
        }
        return obj;
    }
}
