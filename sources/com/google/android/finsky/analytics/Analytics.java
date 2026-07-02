package com.google.android.finsky.analytics;

/* JADX INFO: loaded from: classes.dex */
public interface Analytics {

    public enum Event {
        VIEW_ITEM,
        BUY_ITEM,
        SAMPLE_ITEM,
        MANAGE_ITEM,
        BROWSE,
        DETAILS,
        WIDGET,
        SEARCH,
        CONTENT_FILTER,
        SEARCH_RESULTS,
        RELATED,
        MORE_BY,
        PROMOTED,
        SEARCH_SUGGESTION,
        PURCHASE_STARTED,
        PURCHASE_COMPLETE,
        PURCHASE_WEBVIEW
    }

    void reportEvent(Event event, String str, String str2);

    void reportPageView(String str);

    void reportVirtualPageView(Event event, String str);
}
