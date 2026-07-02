package com.google.android.finsky.api.model;

import com.android.volley.Request;
import com.google.android.finsky.analytics.Analytics;
import com.google.android.finsky.api.DfeApi;
import com.google.android.finsky.remoting.protos.DeviceDoc;
import com.google.android.finsky.remoting.protos.DocList;
import com.google.android.finsky.remoting.protos.SearchResponse;
import com.google.android.finsky.utils.Lists;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DfeSearch extends BucketedList<SearchResponse> {
    private final Analytics mAnalytics;
    private final DfeApi mDfeApi;
    private final Analytics.Event mEvent;

    public DfeSearch(DfeApi api, String url, Analytics analytics, Analytics.Event event) {
        super(url);
        this.mDfeApi = api;
        this.mAnalytics = analytics;
        this.mEvent = event;
    }

    public DfeSearch(DfeApi api, String url) {
        this(api, url, null, null);
    }

    @Override // com.google.android.finsky.api.model.PaginatedList
    protected Request<?> makeRequest(int offset, int count) {
        return this.mDfeApi.search(this.mUrl, offset, count, this, this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.finsky.api.model.PaginatedList
    public List<Document> getItemsFromResponse(SearchResponse response) {
        if (response.getBucketCount() < 1) {
            return Collections.emptyList();
        }
        String cookie = response.getBucket(0).getAnalyticsCookie();
        if (this.mAnalytics != null) {
            this.mAnalytics.reportVirtualPageView(this.mEvent, cookie);
        }
        List<Document> items = Lists.newArrayList();
        for (DeviceDoc.DeviceDocument item : response.getBucket(0).getDocumentList()) {
            items.add(new Document(item, cookie));
        }
        return items;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean isAggregateResult() {
        return ((SearchResponse) this.mLastResponse).getAggregateQuery();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public String getQuery() {
        return ((SearchResponse) this.mLastResponse).getOriginalQuery();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.finsky.api.model.BucketedList
    public DocList.Bucket getBucket(int index) {
        return ((SearchResponse) this.mLastResponse).getBucket(0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.finsky.api.model.BucketedList
    public int getBucketCount() {
        return ((SearchResponse) this.mLastResponse).getBucketCount();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public List<DocList.Bucket> getBucketList() {
        return ((SearchResponse) this.mLastResponse).getBucketList();
    }
}
