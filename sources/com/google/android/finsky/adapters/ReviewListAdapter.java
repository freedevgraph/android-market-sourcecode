package com.google.android.finsky.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.android.finsky.R;
import com.google.android.finsky.adapters.PaginatedListAdapter;
import com.google.android.finsky.api.DfeApi;
import com.google.android.finsky.model.ReviewRequest;
import com.google.android.finsky.remoting.protos.Rev;
import com.google.android.finsky.utils.DateUtils;
import com.google.android.finsky.utils.IntentUtils;
import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
public class ReviewListAdapter extends PaginatedListAdapter {
    private final ReviewRequest mReviewRequest;

    private static final class ReviewHolder {
        public final TextView author;
        public final RatingBar ratingBar;
        public final View reviewBy;
        public final TextView reviewDate;
        public final View reviewFrom;
        public final View reviewOn;
        public final TextView reviewText;
        public final TextView source;

        private ReviewHolder(View reviewItem) {
            this.reviewBy = reviewItem.findViewById(R.id.review_by);
            this.author = (TextView) reviewItem.findViewById(R.id.review_author);
            this.reviewFrom = reviewItem.findViewById(R.id.review_from);
            this.source = (TextView) reviewItem.findViewById(R.id.review_source);
            this.reviewText = (TextView) reviewItem.findViewById(R.id.review_text);
            this.reviewOn = reviewItem.findViewById(R.id.review_on);
            this.reviewDate = (TextView) reviewItem.findViewById(R.id.review_date);
            this.ratingBar = (RatingBar) reviewItem.findViewById(R.id.review_rating);
        }
    }

    public ReviewListAdapter(Context context, DfeApi api, ReviewRequest request) {
        super(context, null, null, request.hadRequestError());
        this.mReviewRequest = request;
        this.mReviewRequest.attach(this);
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case 0:
                return getItemView(position, convertView, parent);
            case 1:
                return getLoadingFooterView(convertView, parent);
            case 2:
                return getErrorFooterView(convertView, parent);
            default:
                throw new IllegalStateException("Unknown type for getView " + getItemViewType(position));
        }
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int position) {
        if (hasItem(position)) {
            return 0;
        }
        switch (getFooterMode()) {
            case LOADING:
                return 1;
            case ERROR:
                return 2;
            default:
                throw new IllegalStateException("No footer or item at position " + position);
        }
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return 3;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return getItemCount() + (getFooterMode() == PaginatedListAdapter.FooterMode.NONE ? 0 : 1);
    }

    private View getItemView(int position, View convertView, ViewGroup parent) {
        View v = setupView(convertView, parent, R.layout.review_item);
        return bindView(v, position);
    }

    private View setupView(View convertView, ViewGroup parent, int resourceId) {
        if (convertView == null) {
            return inflate(resourceId, parent, false);
        }
        return convertView;
    }

    private View bindView(View convertView, int position) {
        if (convertView == null) {
            throw new IllegalStateException("Must initialize View first in setupViewAndHolder()");
        }
        Rev.Review review = getItem(position);
        if (review == null) {
            throw new IllegalStateException("Cannot create a view for a null review located at position " + position);
        }
        fillReviewItem(review, convertView);
        return convertView;
    }

    @Override // android.widget.Adapter
    public Rev.Review getItem(int position) {
        if (position < this.mReviewRequest.getCount()) {
            return this.mReviewRequest.getItem(position);
        }
        return null;
    }

    private boolean hasItem(int position) {
        return this.mReviewRequest.hasItem(position);
    }

    private int getItemCount() {
        return this.mReviewRequest.getCount();
    }

    public static void fillReviewItem(Rev.Review review, View reviewItem) {
        if (reviewItem.getTag() == null) {
            reviewItem.setTag(new ReviewHolder(reviewItem));
        }
        ReviewHolder reviewHolder = (ReviewHolder) reviewItem.getTag();
        String authorName = review.getAuthorName();
        String reviewSource = review.getSource();
        final String sourceUrl = review.getUrl();
        if (!TextUtils.isEmpty(authorName)) {
            reviewHolder.author.setText(authorName);
            reviewHolder.author.setVisibility(0);
            reviewHolder.reviewBy.setVisibility(0);
        } else {
            reviewHolder.author.setVisibility(8);
            reviewHolder.reviewBy.setVisibility(8);
        }
        if (!TextUtils.isEmpty(reviewSource)) {
            reviewHolder.source.setText(reviewSource.toUpperCase());
            reviewHolder.source.setVisibility(0);
            reviewHolder.reviewFrom.setVisibility(0);
            reviewHolder.source.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.finsky.adapters.ReviewListAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    v.getContext().startActivity(IntentUtils.createViewIntentForUrl(Uri.parse(sourceUrl)));
                }
            });
        } else {
            reviewHolder.source.setVisibility(8);
            reviewHolder.reviewFrom.setVisibility(8);
        }
        String reviewComment = review.getComment();
        if (!TextUtils.isEmpty(reviewComment)) {
            reviewHolder.reviewText.setText(reviewComment);
        }
        if (review.hasStarRating()) {
            reviewHolder.ratingBar.setVisibility(0);
            reviewHolder.ratingBar.setRating(review.getStarRating());
        } else {
            reviewHolder.ratingBar.setVisibility(8);
        }
        if (review.hasTimestampMsec()) {
            long milliseconds = review.getTimestampMsec();
            reviewHolder.reviewDate.setText(DateUtils.formatDisplayDate(new Date(milliseconds)));
            reviewHolder.reviewOn.setVisibility(0);
            reviewHolder.reviewDate.setVisibility(0);
            return;
        }
        reviewHolder.reviewOn.setVisibility(8);
        reviewHolder.reviewDate.setVisibility(8);
    }

    @Override // com.google.android.finsky.adapters.PaginatedListAdapter
    protected String getLastRequestError() {
        return this.mReviewRequest.getErrorMessage();
    }

    @Override // com.google.android.finsky.adapters.PaginatedListAdapter
    protected boolean isMoreDataAvailable() {
        return this.mReviewRequest.isMoreAvailable();
    }

    @Override // com.google.android.finsky.adapters.PaginatedListAdapter
    protected void retryLoadingItems() {
        this.mReviewRequest.retryLoadItems();
    }
}
