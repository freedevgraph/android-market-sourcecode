package com.google.android.finsky.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.finsky.R;
import com.google.android.finsky.analytics.Analytics;
import com.google.android.finsky.api.model.OnDataChangedListener;
import com.google.android.finsky.navigationmanager.NavigationManager;

/* JADX INFO: loaded from: classes.dex */
public abstract class PaginatedListAdapter extends BaseAdapter implements OnDataChangedListener {
    protected final Context mContext;
    private FooterMode mFooterMode;
    private final LayoutInflater mLayoutInflater;
    protected final NavigationManager mNavigationManager;
    protected View.OnClickListener mRetryClickListener = new View.OnClickListener() { // from class: com.google.android.finsky.adapters.PaginatedListAdapter.1
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (PaginatedListAdapter.this.mFooterMode == FooterMode.ERROR) {
                PaginatedListAdapter.this.retryLoadingItems();
            }
            PaginatedListAdapter.this.mFooterMode = FooterMode.LOADING;
            PaginatedListAdapter.this.notifyDataSetChanged();
        }
    };

    protected enum FooterMode {
        NONE,
        LOADING,
        ERROR
    }

    protected abstract String getLastRequestError();

    protected abstract boolean isMoreDataAvailable();

    protected abstract void retryLoadingItems();

    public PaginatedListAdapter(Context context, NavigationManager navManager, Analytics.Event event, boolean isRequestInErrorState) {
        this.mContext = context;
        this.mNavigationManager = navManager;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mFooterMode = isRequestInErrorState ? FooterMode.ERROR : FooterMode.NONE;
    }

    protected View getLoadingFooterView(View convertView, ViewGroup parent) {
        return convertView != null ? convertView : inflate(R.layout.loading_footer, parent, false);
    }

    protected View getErrorFooterView(View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflate(R.layout.error_footer, parent, false);
            Button retryButton = (Button) convertView.findViewById(R.id.retry_button);
            retryButton.setOnClickListener(this.mRetryClickListener);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.error_msg);
        textView.setText(getLastRequestError());
        return convertView;
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return position;
    }

    @Override // android.widget.BaseAdapter, android.widget.ListAdapter
    public boolean isEnabled(int position) {
        return false;
    }

    protected View inflate(int resource, ViewGroup root, boolean attachToRoot) {
        return this.mLayoutInflater.inflate(resource, root, attachToRoot);
    }

    private void setFooterMode(FooterMode mode) {
        this.mFooterMode = mode;
        notifyDataSetChanged();
    }

    public void triggerFooterErrorMode() {
        setFooterMode(FooterMode.ERROR);
    }

    protected FooterMode getFooterMode() {
        return this.mFooterMode;
    }

    @Override // com.google.android.finsky.api.model.OnDataChangedListener
    public void onDataChanged() {
        if (isMoreDataAvailable()) {
            setFooterMode(FooterMode.LOADING);
        } else {
            setFooterMode(FooterMode.NONE);
        }
    }
}
