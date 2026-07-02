package com.google.android.finsky.fragments;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.android.volley.Response;
import com.google.android.finsky.R;
import com.google.android.finsky.adapters.SingleCorpusSearchAdapter;
import com.google.android.finsky.api.model.BucketedList;
import com.google.android.finsky.api.model.DfeSearch;
import com.google.android.finsky.api.model.OnDataChangedListener;
import com.google.android.finsky.model.Bucket;
import com.google.android.finsky.navigationmanager.NavigationManager;
import com.google.android.finsky.remoting.protos.DocList;
import com.google.android.finsky.utils.BitmapLoader;
import com.google.android.finsky.utils.ErrorStrings;

/* JADX INFO: loaded from: classes.dex */
public class SingleCorpusSearchViewBinder implements Response.ErrorListener, OnDataChangedListener {
    private SingleCorpusSearchAdapter mAdapter;
    private BitmapLoader mBitmapLoader;
    private ViewGroup mContentLayout;
    private Context mContext;
    private boolean mHasLoadedAtLeastOnce;
    private ListView mListView;
    private NavigationManager mNavigationManager;
    private BucketedList<?> mRequest;

    public void init(Context context, NavigationManager nm, BitmapLoader bitmapLoader) {
        if (this.mContext != context) {
            this.mContext = context;
            this.mNavigationManager = nm;
            this.mBitmapLoader = bitmapLoader;
            this.mRequest = null;
        }
    }

    public void setData(BucketedList<?> data) {
        this.mHasLoadedAtLeastOnce = false;
        if (this.mRequest != null) {
            this.mRequest.detachAll();
        }
        this.mRequest = data;
        this.mRequest.clearTransientState();
        this.mRequest.addDataChangedListener(this);
        this.mRequest.addErrorListener(this);
        this.mRequest.startLoadItems();
    }

    public void bind(ViewGroup root, int columnCount) {
        this.mContentLayout = root;
        this.mListView = (ListView) root.findViewById(R.id.bucket_list_view);
        DocList.Bucket bucket = this.mRequest.getBucketCount() == 0 ? null : this.mRequest.getBucket(0);
        if (bucket == null) {
            this.mListView.setEmptyView(this.mContentLayout.findViewById(R.id.no_results_view));
            return;
        }
        boolean isSearchRequest = this.mRequest instanceof DfeSearch;
        this.mAdapter = new SingleCorpusSearchAdapter(this.mContext, this.mBitmapLoader, this.mNavigationManager, this.mRequest, columnCount, new Bucket(bucket), isSearchRequest);
        if (this.mHasLoadedAtLeastOnce) {
            this.mListView.setEmptyView(null);
        } else {
            this.mListView.setEmptyView(this.mContentLayout.findViewById(R.id.no_results_view));
        }
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
    }

    @Override // com.android.volley.Response.ErrorListener
    public void onErrorResponse(Response.ErrorCode error, String message) {
        if (this.mListView != null) {
            ErrorStrings.get(this.mContext, error, message);
            this.mAdapter.triggerFooterErrorMode();
        }
    }

    @Override // com.google.android.finsky.api.model.OnDataChangedListener
    public void onDataChanged() {
        if (!this.mHasLoadedAtLeastOnce && this.mListView != null) {
            this.mListView.setEmptyView(this.mContentLayout.findViewById(R.id.no_results_view));
            this.mHasLoadedAtLeastOnce = true;
        }
    }

    public void onDestroyView() {
        this.mAdapter = null;
        this.mListView = null;
    }
}
