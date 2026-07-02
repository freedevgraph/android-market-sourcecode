package com.google.android.finsky.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.finsky.R;
import com.google.android.finsky.adapters.CategoryPanelAdapter;
import com.google.android.finsky.api.model.DfeBrowse;
import com.google.android.finsky.navigationmanager.NavigationManager;
import com.google.android.finsky.utils.BitmapLoader;
import com.google.android.finsky.utils.CorpusMetadata;

/* JADX INFO: loaded from: classes.dex */
public class CategoriesViewBinder implements View.OnClickListener {
    private BitmapLoader mBitmapLoader;
    private int mChannelId;
    private Context mContext;
    private DfeBrowse mData;
    private NavigationManager mNavManager;

    public void init(Context context, NavigationManager nm, BitmapLoader bitmapLoader) {
        this.mContext = context;
        this.mNavManager = nm;
        this.mBitmapLoader = bitmapLoader;
        this.mData = null;
    }

    public void setData(DfeBrowse data, int channelId) {
        this.mData = data;
        this.mChannelId = channelId;
    }

    public void bind(ViewGroup view) {
        if (!this.mData.hasCategories()) {
            view.setVisibility(8);
            return;
        }
        int color = CorpusMetadata.getBackendHintColor(this.mContext, this.mChannelId);
        TextView textView = (TextView) view.findViewById(R.id.category_title);
        textView.setText(textView.getText().toString().toUpperCase());
        textView.setTextColor(color);
        CategoryPanelAdapter adapter = new CategoryPanelAdapter(this.mContext, this.mBitmapLoader, this.mChannelId, R.layout.category_panel_item);
        adapter.addAll(this.mData.getCategoryList());
        populateList(view, adapter);
        view.setVisibility(0);
    }

    private void populateList(ViewGroup root, CategoryPanelAdapter adapter) {
        ViewGroup contentBox = (ViewGroup) root.findViewById(R.id.category_list);
        int scrollPos = root.getScrollY();
        contentBox.setBackgroundResource(CorpusMetadata.getCategoryBackground(this.mChannelId));
        contentBox.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this.mContext);
        int n = adapter.getCount();
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                inflater.inflate(R.layout.category_list_divider, contentBox);
            }
            View child = adapter.getView(i, null, contentBox);
            child.setTag(adapter.getCategoryUrl(i));
            child.setOnClickListener(this);
            contentBox.addView(child);
        }
        root.scrollTo(0, scrollPos);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.mNavManager != null) {
            String url = (String) view.getTag();
            this.mNavManager.goBrowse(url);
        }
    }
}
