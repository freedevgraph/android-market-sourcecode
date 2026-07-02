package com.google.android.finsky.api.model;

import com.android.volley.Request;
import com.google.android.finsky.analytics.Analytics;
import com.google.android.finsky.api.DfeApi;
import com.google.android.finsky.remoting.protos.DeviceDoc;
import com.google.android.finsky.remoting.protos.DocList;
import com.google.android.finsky.utils.Lists;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DfeList extends BucketedList<DocList.ListResponse> {
    private final Analytics mAnalytics;
    private final DfeApi mDfeApi;
    private final Analytics.Event mEvent;

    public DfeList(DfeApi api, String url, boolean autoLoadNextPage, Analytics analytics, Analytics.Event event) {
        super(url, autoLoadNextPage);
        this.mDfeApi = api;
        this.mEvent = event;
        this.mAnalytics = analytics;
    }

    @Override // com.google.android.finsky.api.model.PaginatedList
    protected Request<?> makeRequest(int offset, int count) {
        return this.mDfeApi.getList(this.mUrl, offset, count, this, this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.finsky.api.model.PaginatedList
    public List<Document> getItemsFromResponse(DocList.ListResponse response) {
        List<Document> items = Lists.newArrayList();
        if (response.getBucketCount() > 0) {
            String cookie = response.getBucket(0).getAnalyticsCookie();
            this.mAnalytics.reportVirtualPageView(this.mEvent, cookie);
            for (DeviceDoc.DeviceDocument item : response.getBucket(0).getDocumentList()) {
                items.add(new Document(item, cookie));
            }
        }
        return items;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.finsky.api.model.BucketedList
    public DocList.Bucket getBucket(int index) {
        return ((DocList.ListResponse) this.mLastResponse).getBucket(0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.finsky.api.model.BucketedList
    public int getBucketCount() {
        return ((DocList.ListResponse) this.mLastResponse).getBucketCount();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public List<DocList.Bucket> getBucketList() {
        return ((DocList.ListResponse) this.mLastResponse).getBucketList();
    }
}
