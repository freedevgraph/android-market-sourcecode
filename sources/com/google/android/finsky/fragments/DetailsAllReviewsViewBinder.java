package com.google.android.finsky.fragments;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.android.volley.Response;
import com.google.android.finsky.R;
import com.google.android.finsky.adapters.ReviewListAdapter;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.api.model.OnDataChangedListener;
import com.google.android.finsky.fragments.DetailsFragment;
import com.google.android.finsky.layout.LayoutSwitcher;
import com.google.android.finsky.model.ReviewRequest;
import com.google.android.finsky.utils.ErrorStrings;

/* JADX INFO: loaded from: classes.dex */
public class DetailsAllReviewsViewBinder extends DetailsViewBinder implements Response.ErrorListener, OnDataChangedListener, LayoutSwitcher.RetryButtonListener {
    private ReviewListAdapter mReviewAdapter;
    private ReviewRequest mReviewRequest;

    public void bind(View view, Document doc) {
        super.bind(view, doc, R.id.header, R.string.details_reviews);
        detachAll();
        String reviewUrl = doc.getReviewsUrl();
        ImageView backButton = (ImageView) this.mLayout.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.finsky.fragments.DetailsAllReviewsViewBinder.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DetailsAllReviewsViewBinder.this.mNavigationManager.switchDetailsPageState(DetailsFragment.DetailsPageState.ALL_DETAILS);
            }
        });
        ListView reviewList = (ListView) this.mLayout.findViewById(R.id.review_list);
        this.mReviewRequest = ReviewRequest.getReviewRequest(this.mDfeApi, reviewUrl);
        this.mReviewRequest.setErrorListener(this);
        this.mReviewRequest.attach(this);
        this.mReviewAdapter = new ReviewListAdapter(this.mContext, this.mDfeApi, this.mReviewRequest);
        reviewList.setAdapter((ListAdapter) this.mReviewAdapter);
        LayoutSwitcher layoutSwitcher = new LayoutSwitcher(this.mLayout, R.id.review_list, this);
        layoutSwitcher.switchToLoadingDelayed(350);
        setLayoutSwitcher(layoutSwitcher);
        if (this.mReviewRequest.hadRequestError()) {
            getLayoutSwitcher().switchToErrorMode(this.mReviewRequest.getErrorMessage());
        } else if (this.mReviewRequest.getCount() > 0) {
            getLayoutSwitcher().switchToDataMode();
        } else {
            this.mReviewRequest.startLoadItems();
        }
    }

    private void detachAll() {
        if (this.mReviewRequest != null) {
            this.mReviewRequest.detachAll();
        }
    }

    @Override // com.google.android.finsky.api.model.OnDataChangedListener
    public void onDataChanged() {
        if (!getLayoutSwitcher().isDataMode()) {
            getLayoutSwitcher().switchToDataMode();
        }
    }

    @Override // com.google.android.finsky.fragments.DetailsViewBinder, com.android.volley.Response.ErrorListener
    public void onErrorResponse(Response.ErrorCode error, String message) {
        String errorMessage = ErrorStrings.get(this.mContext, error, message);
        if (!getLayoutSwitcher().isDataMode()) {
            super.onErrorResponse(error, errorMessage);
        } else {
            this.mReviewAdapter.triggerFooterErrorMode();
        }
        this.mReviewRequest.setErrorMessage(errorMessage);
    }

    @Override // com.google.android.finsky.fragments.DetailsViewBinder
    public void onDestroyView() {
        super.onDestroyView();
        detachAll();
    }

    @Override // com.google.android.finsky.layout.LayoutSwitcher.RetryButtonListener
    public void onRetry() {
        this.mReviewRequest.retryLoadItems();
    }
}
