package com.google.android.finsky.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.finsky.R;
import com.google.android.finsky.api.model.DfeSearch;
import com.google.android.finsky.api.model.OnDataChangedListener;

/* JADX INFO: loaded from: classes.dex */
public class SearchFragment extends PageFragment<DfeSearch> implements OnDataChangedListener {
    private DfeSearch mSearchData;
    private final SingleCorpusSearchViewBinder mSearchResultsBinder = new SingleCorpusSearchViewBinder();
    private final AggregatedSearchViewBinder mAggregatedSearchResultsBinder = new AggregatedSearchViewBinder();

    public static SearchFragment newInstance(String url) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("finsky.PageFragment.url", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override // com.google.android.finsky.fragments.PageFragment
    protected int getLayoutRes() {
        return R.layout.search_frame;
    }

    @Override // com.google.android.finsky.fragments.PageFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = super.onCreateView(inflater, container, savedInstanceState);
        if (isDataReady()) {
            rebindViews();
        }
        return result;
    }

    @Override // com.google.android.finsky.fragments.PageFragment, android.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (this.mSearchData == null) {
            this.mSearchData = new DfeSearch(this.mDfeApi, this.mUrl);
            this.mSearchData.addDataChangedListener(this);
            this.mAggregatedSearchResultsBinder.setData(this.mSearchData);
            this.mSearchResultsBinder.setData(this.mSearchData);
        }
        if (!isDataReady()) {
            switchToLoading();
            requestData();
        }
    }

    @Override // com.google.android.finsky.fragments.PageFragment
    protected void onInitViewBinders() {
        this.mAggregatedSearchResultsBinder.init(this.mActivity, this.mNavigationManager, this.mBitmapLoader);
        this.mSearchResultsBinder.init(this.mActivity, this.mNavigationManager, this.mBitmapLoader);
    }

    @Override // com.google.android.finsky.fragments.PageFragment
    public void refresh() {
        if (isDataReady()) {
            rebindViews();
        } else {
            this.mSearchData.clearTransientState();
            super.refresh();
        }
    }

    @Override // com.google.android.finsky.fragments.PageFragment
    protected void requestData() {
        this.mSearchData.startLoadItems();
    }

    @Override // com.google.android.finsky.fragments.PageFragment
    protected void rebindViews() {
        int rows = getResources().getInteger(R.integer.search_num_rows);
        int columns = getResources().getInteger(R.integer.search_num_cols);
        if (this.mSearchData.isAggregateResult()) {
            this.mAggregatedSearchResultsBinder.bind(this.mDataView, columns, rows);
        } else {
            this.mSearchResultsBinder.bind(this.mDataView, columns);
        }
        this.mSearchData.removeDataChangedListener(this);
    }

    @Override // android.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        this.mSearchResultsBinder.onDestroyView();
    }

    private boolean isDataReady() {
        return this.mSearchData != null && this.mSearchData.isReady();
    }
}
