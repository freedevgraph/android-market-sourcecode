package com.google.android.finsky.model;

import android.app.ActionBar;

/* JADX INFO: loaded from: classes.dex */
public class ChannelTab extends Tab {
    private ActionBar.Tab mActionBarTab;

    public ChannelTab(String name, int id, String dataUrl, String iconUrl, ActionBar.Tab actionBarTab) {
        super(name, id, dataUrl, iconUrl);
        this.mActionBarTab = actionBarTab;
    }

    public void setActionBarTab(ActionBar.Tab actionBarTab) {
        this.mActionBarTab = actionBarTab;
    }

    public ActionBar.Tab getActionBarTab() {
        return this.mActionBarTab;
    }
}
