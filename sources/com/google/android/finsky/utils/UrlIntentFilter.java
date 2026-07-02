package com.google.android.finsky.utils;

import android.net.Uri;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public class UrlIntentFilter {
    private static final Map<Pattern, Integer> sUrlPatterns = Maps.newLinkedHashMap();

    public static class Result {
        public final int corpus;
        public final String extra;
        public final int type;

        public Result(int type, int corpus, String extra) {
            this.type = type;
            this.corpus = corpus;
            this.extra = extra;
        }
    }

    static {
        addUriPattern("https?://market\\.android\\.com/details\\?.*id=app[:-]([^=&]+).*", 2, 3);
        addUriPattern("https?://market\\.android\\.com/details\\?.*id=book[:-]([^=&]+).*", 2, 1);
        addUriPattern("https?://market\\.android\\.com/details\\?.*id=movie[:-]([^=&]+).*", 2, 4);
        addUriPattern("https?://market\\.android\\.com/details\\?.*id=([^=&]+).*", 2, 3);
        addUriPattern("https?://market\\.android\\.com/search\\?.*q=([^=&]+).*", 3, 0);
        addUriPattern("market://details\\?id=(.*)", 2, 3);
        addUriPattern("market://search\\?q=(.*)", 3, 3);
        addUriPattern("https?://market\\.android\\.com/books/search\\?.*q=([^=&]+).*", 3, 1);
        addUriPattern("https?://market\\.android\\.com/books/(.+)/buy", 4, 1);
        addUriPattern("https?://market\\.android\\.com/books/([^=&]+).*", 2, 1);
        addUriPattern("https?://market\\.android\\.com/?", 1, 3);
        addUriPattern("https?://market\\.android\\.com/apps/?", 1, 3);
        addUriPattern("https?://market\\.android\\.com/books/?", 1, 1);
        addUriPattern("https?://market\\.android\\.com/movies/?", 1, 4);
    }

    private static void addUriPattern(String regex, int type, int backend) {
        Pattern pattern = Pattern.compile(regex);
        sUrlPatterns.put(pattern, Integer.valueOf((type << 16) | backend));
    }

    public static Result matchUri(String url) {
        for (Pattern pattern : sUrlPatterns.keySet()) {
            Matcher matcher = pattern.matcher(url);
            if (matcher.matches()) {
                int typeAndBackend = sUrlPatterns.get(pattern).intValue();
                int type = typeAndBackend >> 16;
                int backend = typeAndBackend & 65535;
                String extra = matcher.groupCount() > 0 ? matcher.group(1) : null;
                return new Result(type, backend, Uri.decode(extra));
            }
        }
        return null;
    }
}
