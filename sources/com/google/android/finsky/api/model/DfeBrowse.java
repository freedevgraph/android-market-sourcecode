package com.google.android.finsky.api.model;

import android.text.TextUtils;
import com.android.volley.Response;
import com.google.android.finsky.analytics.Analytics;
import com.google.android.finsky.api.DfeApi;
import com.google.android.finsky.remoting.protos.Browse;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DfeBrowse extends DfeModel implements Response.Listener<Browse.BrowseResponse> {
    private final Analytics mAnalytics;
    private Browse.BrowseResponse mBrowseResponse;
    private final DfeApi mDfeApi;

    public DfeBrowse(DfeApi dfeApi, String landingUrl, Analytics analytics) {
        this.mDfeApi = dfeApi;
        this.mAnalytics = analytics;
        dfeApi.getBrowseLayout(landingUrl, this, this);
    }

    public List<Browse.BrowseLink> getBreadcrumbList() {
        if (this.mBrowseResponse != null) {
            return this.mBrowseResponse.getBreadcrumbList();
        }
        return null;
    }

    public List<Browse.BrowseLink> getCategoryList() {
        if (this.mBrowseResponse != null) {
            return this.mBrowseResponse.getCategoryList();
        }
        return null;
    }

    public boolean hasCategories() {
        return (this.mBrowseResponse == null || this.mBrowseResponse.getCategoryList() == null || this.mBrowseResponse.getCategoryList().isEmpty()) ? false : true;
    }

    public boolean hasPromotionalItems() {
        return (this.mBrowseResponse == null || TextUtils.isEmpty(this.mBrowseResponse.getPromoUrl())) ? false : true;
    }

    public DfeList buildContentList() {
        if (isReady()) {
            return new DfeList(this.mDfeApi, this.mBrowseResponse.getContentsUrl(), true, this.mAnalytics, Analytics.Event.BROWSE);
        }
        return null;
    }

    public DfeList buildPromoList() {
        if (hasPromotionalItems()) {
            return new DfeList(this.mDfeApi, this.mBrowseResponse.getPromoUrl(), false, this.mAnalytics, Analytics.Event.PROMOTED);
        }
        return null;
    }

    public boolean isReady() {
        return this.mBrowseResponse != null;
    }

    @Override // com.android.volley.Response.Listener
    public void onResponse(Browse.BrowseResponse response) {
        clearErrors();
        this.mBrowseResponse = response;
        notifyDataSetChanged();
    }
}
