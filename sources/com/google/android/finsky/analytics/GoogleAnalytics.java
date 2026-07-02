package com.google.android.finsky.analytics;

import android.content.Context;
import android.net.Uri;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.google.android.finsky.analytics.Analytics;
import com.google.android.finsky.config.G;
import com.google.android.finsky.utils.FinskyLog;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes.dex */
public class GoogleAnalytics implements Analytics {
    private static final Uri URL_BASE = Uri.parse("http://android.clients.google.com/fdfe");
    private Executor mRequestQueue = Executors.newSingleThreadExecutor();

    public GoogleAnalytics(Context context) {
        GoogleAnalyticsTracker.getInstance().start(G.analyticsAccountId.get(), 10, context);
    }

    @Override // com.google.android.finsky.analytics.Analytics
    public void reportPageView(final String url) {
        if (FinskyLog.DEBUG) {
            FinskyLog.v("URL %s", url);
        }
        this.mRequestQueue.execute(new Runnable() { // from class: com.google.android.finsky.analytics.GoogleAnalytics.1
            @Override // java.lang.Runnable
            public void run() {
                GoogleAnalyticsTracker.getInstance().trackPageView(url);
            }
        });
    }

    @Override // com.google.android.finsky.analytics.Analytics
    public void reportVirtualPageView(Analytics.Event event, String cookie) {
        Uri.Builder uriBuilder = URL_BASE.buildUpon().appendQueryParameter("e", event.toString());
        if (cookie != null) {
            uriBuilder.appendQueryParameter("c", cookie);
        }
        reportPageView(uriBuilder.build().toString());
    }

    @Override // com.google.android.finsky.analytics.Analytics
    public void reportEvent(final Analytics.Event event, final String url, final String cookie) {
        if (FinskyLog.DEBUG) {
            FinskyLog.v("%s, URL %s, cookie %s", event, url, cookie);
        }
        if (url != null) {
            this.mRequestQueue.execute(new Runnable() { // from class: com.google.android.finsky.analytics.GoogleAnalytics.2
                @Override // java.lang.Runnable
                public void run() {
                    GoogleAnalyticsTracker.getInstance().trackEvent(url, event.toString(), cookie, -1);
                }
            });
        }
    }
}
