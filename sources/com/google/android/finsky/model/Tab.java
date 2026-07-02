package com.google.android.finsky.model;

/* JADX INFO: loaded from: classes.dex */
public class Tab {
    private String mDataUrl;
    private final String mIconUrl;
    private final int mId;
    private final String mName;

    public Tab(String name, int id, String dataUrl, String iconUrl) {
        this.mName = name;
        this.mId = id;
        this.mDataUrl = dataUrl;
        this.mIconUrl = iconUrl;
    }

    public String getName() {
        return this.mName;
    }

    public int getId() {
        return this.mId;
    }

    public String getDataUrl() {
        return this.mDataUrl;
    }

    public String toString() {
        return this.mName;
    }
}
