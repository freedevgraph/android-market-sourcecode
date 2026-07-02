package com.google.android.finsky.fragments;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.google.android.finsky.FinskyApp;
import com.google.android.finsky.R;
import com.google.android.finsky.adapters.BucketAdapter;
import com.google.android.finsky.analytics.Analytics;
import com.google.android.finsky.api.model.DfeList;
import com.google.android.finsky.navigationmanager.NavigationManager;
import com.google.android.finsky.utils.BitmapLoader;

/* JADX INFO: loaded from: classes.dex */
public class BrowseContentViewBinder {
    private BitmapLoader mBitmapLoader;
    private Context mContext;
    private DfeList mData;
    private NavigationManager mNavManager;

    public void init(Context context, NavigationManager nm, BitmapLoader bitmapLoader) {
        if (this.mContext != context) {
            this.mContext = context;
            this.mNavManager = nm;
            this.mBitmapLoader = bitmapLoader;
            this.mData = null;
        }
    }

    public void setData(DfeList data) {
        this.mData = data;
    }

    public void bind(ViewGroup view, int bucketContentColumns, int bucketContentRows) {
        if (this.mData.getBucketCount() > 0) {
            FinskyApp.get().getAnalytics().reportVirtualPageView(Analytics.Event.BROWSE, this.mData.getBucket(0).getAnalyticsCookie());
        }
        ListView list = (ListView) view.findViewById(R.id.bucket_list_view);
        list.setEmptyView(view.findViewById(R.id.no_results_view));
        BucketAdapter adapter = new BucketAdapter(this.mContext, this.mNavManager, this.mBitmapLoader, this.mData.getBucketList(), bucketContentColumns, bucketContentRows, null, false, false);
        list.setAdapter((ListAdapter) adapter);
    }
}
