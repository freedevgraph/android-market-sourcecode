package com.google.android.finsky.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.Response;
import com.google.android.finsky.R;
import com.google.android.finsky.activities.MainActivity;
import com.google.android.finsky.api.DfeApi;
import com.google.android.finsky.api.model.DfeModel;
import com.google.android.finsky.api.model.OnDataChangedListener;
import com.google.android.finsky.layout.LayoutSwitcher;
import com.google.android.finsky.navigationmanager.NavigationManager;
import com.google.android.finsky.utils.BitmapLoader;
import com.google.android.finsky.utils.ErrorStrings;

/* JADX INFO: loaded from: classes.dex */
public abstract class PageFragment<T extends DfeModel> extends Fragment implements Response.ErrorListener, LayoutSwitcher.RetryButtonListener, OnDataChangedListener {
    protected MainActivity mActivity;
    protected BitmapLoader mBitmapLoader;
    protected Context mContext;
    protected ViewGroup mDataView;
    protected DfeApi mDfeApi;
    private LayoutSwitcher mLayoutSwitcher;
    protected NavigationManager mNavigationManager;
    protected String mUrl;

    protected abstract int getLayoutRes();

    protected abstract void onInitViewBinders();

    protected abstract void rebindViews();

    protected abstract void requestData();

    @Override // android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mUrl = getArguments().getString("finsky.PageFragment.url");
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        this.mDataView = (ViewGroup) view.findViewById(R.id.page_content);
        this.mLayoutSwitcher = new LayoutSwitcher(view, R.id.page_content, R.id.page_error_indicator, R.id.page_loading_indicator, this, 2);
        return view;
    }

    @Override // android.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        if (activity != this.mActivity) {
            this.mActivity = (MainActivity) getActivity();
            this.mContext = this.mActivity;
            this.mNavigationManager = this.mActivity.getNavigationManager();
            this.mDfeApi = this.mActivity.getDfeApi();
            this.mBitmapLoader = this.mActivity.getBitmapLoader();
            onInitViewBinders();
        }
    }

    public void refresh() {
        requestData();
    }

    @Override // com.google.android.finsky.layout.LayoutSwitcher.RetryButtonListener
    public void onRetry() {
        refresh();
    }

    public void onDataChanged() {
        if (isAdded()) {
            switchToData();
            rebindViews();
        }
    }

    protected void switchToBlank() {
        this.mLayoutSwitcher.switchToBlankMode();
    }

    protected void switchToLoading() {
        this.mLayoutSwitcher.switchToLoadingDelayed(350);
    }

    protected void switchToError(String msg) {
        this.mLayoutSwitcher.switchToErrorMode(msg);
    }

    protected void switchToData() {
        this.mLayoutSwitcher.switchToDataMode();
    }

    @Override // com.android.volley.Response.ErrorListener
    public void onErrorResponse(Response.ErrorCode error, String message) {
        String errorMessage = ErrorStrings.get(this.mActivity, error, message);
        switchToError(errorMessage);
    }
}
