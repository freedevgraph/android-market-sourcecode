package com.google.android.finsky.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.Response;
import com.google.android.finsky.api.DfeApi;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.layout.LayoutSwitcher;
import com.google.android.finsky.navigationmanager.NavigationManager;
import com.google.android.finsky.utils.CorpusMetadata;
import com.google.android.finsky.utils.ErrorStrings;

/* JADX INFO: loaded from: classes.dex */
public abstract class DetailsViewBinder implements Response.ErrorListener {
    protected Context mContext;
    protected DfeApi mDfeApi;
    protected Document mDoc;
    protected LayoutInflater mInflater;
    protected View mLayout;
    private LayoutSwitcher mLayoutSwitcher;
    protected NavigationManager mNavigationManager;

    public void init(Context context, DfeApi api, NavigationManager navManager) {
        this.mContext = context;
        this.mDfeApi = api;
        this.mNavigationManager = navManager;
        this.mInflater = LayoutInflater.from(this.mContext);
    }

    public void bind(View view, Document doc, int headerLayoutId, int headerStringId) {
        this.mLayout = view;
        this.mDoc = doc;
        setupHeader(headerLayoutId, headerStringId);
    }

    public void setLayoutSwitcher(LayoutSwitcher layoutSwitcher) {
        this.mLayoutSwitcher = layoutSwitcher;
    }

    private void setupHeader(int headerLayoutId, int headerStringId) {
        TextView headerView = (TextView) this.mLayout.findViewById(headerLayoutId);
        if (headerView != null) {
            headerView.setText(this.mContext.getString(headerStringId).toUpperCase());
            int color = CorpusMetadata.getBackendHintColor(this.mContext, this.mDoc.getBackend());
            headerView.setTextColor(color);
        }
    }

    protected void setButtonVisibility(int buttonLayoutId, int visibility, int stringId) {
        if (visibility != 8 && visibility != 4 && visibility != 0) {
            throw new IllegalArgumentException("Invalid visibility value for a view " + visibility);
        }
        Button button = (Button) this.mLayout.findViewById(buttonLayoutId);
        button.setVisibility(visibility);
        if (visibility == 0) {
            button.setText(stringId);
        }
    }

    protected void setButtonClickListener(int buttonLayoutId, View.OnClickListener listener) {
        View footerButton = this.mLayout.findViewById(buttonLayoutId);
        footerButton.setOnClickListener(listener);
    }

    protected LayoutSwitcher getLayoutSwitcher() {
        return this.mLayoutSwitcher;
    }

    @Override // com.android.volley.Response.ErrorListener
    public void onErrorResponse(Response.ErrorCode error, String message) {
        if (this.mLayoutSwitcher != null) {
            String errorMessage = ErrorStrings.get(this.mContext, error, message);
            getLayoutSwitcher().switchToErrorMode(errorMessage);
        }
    }

    public void onDestroyView() {
    }
}
