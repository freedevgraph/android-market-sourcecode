package com.google.android.finsky.fragments;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.android.finsky.R;
import com.google.android.finsky.adapters.ReviewListAdapter;
import com.google.android.finsky.api.PaginatedDfeRequest;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.fragments.DetailsFragment;
import com.google.android.finsky.layout.LayoutSwitcher;
import com.google.android.finsky.remoting.protos.Rev;
import com.google.android.finsky.utils.CorpusMetadata;

/* JADX INFO: loaded from: classes.dex */
public class DetailsReviewsViewBinder extends DetailsViewBinder implements PaginatedDfeRequest.PaginatedListener<Rev.ReviewResponse>, LayoutSwitcher.RetryButtonListener {
    private LinearLayout mReviewList;
    private String mReviewsUrl;

    public void bind(View view, Document doc) {
        super.bind(view, doc, R.id.header, CorpusMetadata.getReviewsHeaderStringId(doc.getBackend()));
        this.mReviewsUrl = doc.getReviewsUrl();
        if (this.mReviewsUrl == null) {
            this.mLayout.setVisibility(8);
            return;
        }
        this.mLayout.setVisibility(0);
        this.mReviewList = (LinearLayout) this.mLayout.findViewById(R.id.review_panel);
        LayoutSwitcher layoutSwitcher = new LayoutSwitcher(this.mLayout, R.id.review_panel, this);
        setLayoutSwitcher(layoutSwitcher);
        layoutSwitcher.switchToLoadingDelayed(350);
        setButtonVisibility(R.id.more_button, 0, R.string.more_reviews);
        setButtonClickListener(R.id.more_button, new View.OnClickListener() { // from class: com.google.android.finsky.fragments.DetailsReviewsViewBinder.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DetailsReviewsViewBinder.this.mNavigationManager.switchDetailsPageState(DetailsFragment.DetailsPageState.ALL_REVIEWS);
            }
        });
        loadReviews();
    }

    private void loadReviews() {
        this.mDfeApi.getReviews(this.mReviewsUrl, 0, 5, this, this);
    }

    @Override // com.google.android.finsky.layout.LayoutSwitcher.RetryButtonListener
    public void onRetry() {
        loadReviews();
    }

    @Override // com.google.android.finsky.api.PaginatedDfeRequest.PaginatedListener
    public void onResponse(Rev.ReviewResponse response, int requestOffset, int requestCount) {
        if (response == null) {
            this.mLayout.setVisibility(8);
            return;
        }
        Rev.GetReviewsResponse reviewsResponse = response.getGetResponse();
        if (reviewsResponse == null || reviewsResponse.getReviewList().size() == 0) {
            this.mLayout.setVisibility(8);
            return;
        }
        this.mReviewList.removeAllViews();
        getLayoutSwitcher().switchToDataMode();
        for (Rev.Review review : reviewsResponse.getReviewList()) {
            LinearLayout reviewItem = (LinearLayout) this.mInflater.inflate(R.layout.review_item, (ViewGroup) this.mReviewList, false);
            ReviewListAdapter.fillReviewItem(review, reviewItem);
            this.mReviewList.addView(reviewItem);
        }
    }
}
