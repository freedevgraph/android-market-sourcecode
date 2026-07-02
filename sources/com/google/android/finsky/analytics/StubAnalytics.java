package com.google.android.finsky.analytics;

import com.google.android.finsky.analytics.Analytics;

/* JADX INFO: loaded from: classes.dex */
public class StubAnalytics implements Analytics {
    @Override // com.google.android.finsky.analytics.Analytics
    public void reportPageView(String url) {
    }

    @Override // com.google.android.finsky.analytics.Analytics
    public void reportVirtualPageView(Analytics.Event event, String cookie) {
    }

    @Override // com.google.android.finsky.analytics.Analytics
    public void reportEvent(Analytics.Event event, String url, String cookie) {
    }
}
