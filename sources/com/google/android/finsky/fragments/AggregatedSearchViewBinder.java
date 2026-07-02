package com.google.android.finsky.fragments;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.google.android.finsky.FinskyApp;
import com.google.android.finsky.R;
import com.google.android.finsky.adapters.BucketAdapter;
import com.google.android.finsky.analytics.Analytics;
import com.google.android.finsky.api.model.DfeSearch;
import com.google.android.finsky.navigationmanager.NavigationManager;
import com.google.android.finsky.remoting.protos.DocList;
import com.google.android.finsky.utils.BitmapLoader;

/* JADX INFO: loaded from: classes.dex */
public class AggregatedSearchViewBinder {
    private BitmapLoader mBitmapLoader;
    private Context mContext;
    private DfeSearch mData;
    private NavigationManager mNavManager;

    public void init(Context context, NavigationManager nm, BitmapLoader bitmapLoader) {
        if (this.mContext != context) {
            this.mContext = context;
            this.mNavManager = nm;
            this.mBitmapLoader = bitmapLoader;
            this.mData = null;
        }
    }

    public void setData(DfeSearch data) {
        this.mData = data;
    }

    public void bind(ViewGroup root, int columns, int rows) {
        Analytics analytics = FinskyApp.get().getAnalytics();
        for (DocList.Bucket bucket : this.mData.getBucketList()) {
            analytics.reportVirtualPageView(Analytics.Event.SEARCH_RESULTS, bucket.getAnalyticsCookie());
        }
        ListView list = (ListView) root.findViewById(R.id.bucket_list_view);
        list.setEmptyView(root.findViewById(R.id.no_results_view));
        BucketAdapter adapter = new BucketAdapter(this.mContext, this.mNavManager, this.mBitmapLoader, this.mData.getBucketList(), columns, rows, this.mData.getQuery(), true, true);
        list.setAdapter((ListAdapter) adapter);
    }
}
