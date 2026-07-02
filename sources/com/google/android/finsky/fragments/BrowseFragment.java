package com.google.android.finsky.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.finsky.FinskyApp;
import com.google.android.finsky.R;
import com.google.android.finsky.api.model.DfeBrowse;
import com.google.android.finsky.api.model.DfeList;
import com.google.android.finsky.layout.FinskyActionBar;

/* JADX INFO: loaded from: classes.dex */
public class BrowseFragment extends PageFragment<DfeBrowse> {
    private DfeBrowse mBrowseData;
    private DfeList mContentListData;
    private FinskyActionBar mFinskyActionBar;
    private DfeList mPromoListData;
    private final PromoViewBinder mPromoBinder = new PromoViewBinder();
    private final CategoriesViewBinder mCategoriesBinder = new CategoriesViewBinder();
    private final BrowseContentViewBinder mBucketListViewBinder = new BrowseContentViewBinder();
    private final SingleCorpusSearchViewBinder mSingleBucketViewBinder = new SingleCorpusSearchViewBinder();

    public static BrowseFragment newInstance(String url) {
        BrowseFragment fragment = new BrowseFragment();
        Bundle args = new Bundle();
        args.putString("finsky.PageFragment.url", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override // com.google.android.finsky.fragments.PageFragment
    protected int getLayoutRes() {
        return R.layout.home_frame;
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
        this.mFinskyActionBar = this.mActivity.getFinskyActionBar();
        if (this.mBrowseData == null) {
            this.mBrowseData = new DfeBrowse(this.mDfeApi, this.mUrl, FinskyApp.get().getAnalytics());
            this.mBrowseData.addDataChangedListener(this);
            this.mCategoriesBinder.setData(this.mBrowseData, this.mFinskyActionBar.getChannelToLoad());
        }
        if (!isDataReady()) {
            switchToLoading();
            requestData();
        }
    }

    @Override // com.google.android.finsky.fragments.PageFragment
    protected void onInitViewBinders() {
        this.mPromoBinder.init(this.mActivity, this.mNavigationManager, this.mBitmapLoader);
        this.mCategoriesBinder.init(this.mActivity, this.mNavigationManager, this.mBitmapLoader);
        this.mBucketListViewBinder.init(this.mActivity, this.mNavigationManager, this.mBitmapLoader);
        this.mSingleBucketViewBinder.init(this.mActivity, this.mNavigationManager, this.mBitmapLoader);
    }

    private boolean isMultiBucket() {
        DfeList listData = getContentListData();
        if (listData != null && listData.getBucketCount() > 1) {
            return true;
        }
        return false;
    }

    @Override // com.google.android.finsky.fragments.PageFragment
    public void refresh() {
        if (isDataReady()) {
            rebindViews();
            return;
        }
        DfeList list = getContentListData();
        if (list != null) {
            list.clearTransientState();
        }
        DfeList list2 = getPromoListData();
        if (list2 != null) {
            list2.clearTransientState();
        }
        super.refresh();
    }

    @Override // com.google.android.finsky.fragments.PageFragment
    protected void requestData() {
        DfeList list = getContentListData();
        if (list != null) {
            list.startLoadItems();
        }
        DfeList list2 = getPromoListData();
        if (list2 != null) {
            list2.startLoadItems();
        }
    }

    @Override // com.google.android.finsky.fragments.PageFragment
    protected void rebindViews() {
        this.mFinskyActionBar.setupBreadcrumbs(this.mBrowseData.getBreadcrumbList());
        switchToData();
        this.mPromoBinder.bind((ViewGroup) this.mDataView.findViewById(R.id.home_promo_panel));
        this.mCategoriesBinder.bind((ViewGroup) this.mDataView.findViewById(R.id.home_list_panel));
        int rows = getResources().getInteger(R.integer.browse_num_rows);
        int columnsPerBucket = this.mBrowseData.hasCategories() ? getResources().getInteger(R.integer.browse_num_cols_with_categories) : getResources().getInteger(R.integer.browse_num_cols_no_categories);
        if (isMultiBucket()) {
            this.mBucketListViewBinder.bind(this.mDataView, columnsPerBucket, rows);
        } else {
            this.mSingleBucketViewBinder.bind(this.mDataView, columnsPerBucket);
        }
    }

    @Override // android.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        this.mPromoBinder.onDestroyView();
        this.mSingleBucketViewBinder.onDestroyView();
    }

    @Override // com.google.android.finsky.fragments.PageFragment, com.google.android.finsky.api.model.OnDataChangedListener
    public void onDataChanged() {
        if (isDataReady()) {
            DfeList listData = getContentListData();
            if (isMultiBucket()) {
                this.mBucketListViewBinder.setData(listData);
            } else {
                this.mSingleBucketViewBinder.setData(listData);
            }
            DfeList promoData = getPromoListData();
            this.mPromoBinder.setData(promoData);
            super.onDataChanged();
            return;
        }
        requestData();
    }

    private DfeList getContentListData() {
        if (this.mContentListData == null) {
            if (this.mBrowseData == null || !this.mBrowseData.isReady()) {
                return null;
            }
            this.mContentListData = this.mBrowseData.buildContentList();
            this.mContentListData.addDataChangedListener(this);
        }
        return this.mContentListData;
    }

    private DfeList getPromoListData() {
        if (this.mPromoListData == null) {
            if (this.mBrowseData == null || !this.mBrowseData.isReady() || !this.mBrowseData.hasPromotionalItems()) {
                return null;
            }
            this.mPromoListData = this.mBrowseData.buildPromoList();
            this.mPromoListData.addDataChangedListener(this);
        }
        return this.mPromoListData;
    }

    private boolean isDataReady() {
        return this.mBrowseData != null && this.mBrowseData.isReady() && getContentListData().isReady() && (!this.mBrowseData.hasPromotionalItems() || getPromoListData().isReady());
    }
}
