package com.google.android.finsky.model;

import com.android.volley.Response;
import com.google.android.finsky.api.DfeApi;
import com.google.android.finsky.api.PaginatedDfeRequest;
import com.google.android.finsky.api.model.ItemListObservable;
import com.google.android.finsky.api.model.OnDataChangedListener;
import com.google.android.finsky.remoting.protos.Rev;
import com.google.android.finsky.utils.Lists;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ReviewRequest extends ItemListObservable implements PaginatedDfeRequest.PaginatedListener<Rev.ReviewResponse> {
    private DfeApi mDfeApi;
    protected Response.ErrorListener mErrorListener;
    private String mErrorMsg;
    protected final String mUrl;
    private final List<Rev.Review> mItems = Lists.newArrayList();
    private boolean mMoreAvailable = true;
    private int mPageSize = 12;
    private boolean mAlreadyStartedLoadingItems = false;
    private int mItemsRequestedCount = 0;

    protected ReviewRequest(DfeApi dfeApi, String url) {
        this.mUrl = url;
        this.mDfeApi = dfeApi;
    }

    public static synchronized ReviewRequest getReviewRequest(DfeApi api, String url) {
        ReviewRequest req;
        req = new ReviewRequest(api, url);
        return req;
    }

    public int getCount() {
        return this.mItems.size();
    }

    public final boolean hasItem(int position) {
        return position < getCount();
    }

    public void setErrorListener(Response.ErrorListener e) {
        this.mErrorListener = e;
    }

    private void requestMoreItems(int offset, int count) {
        this.mDfeApi.getReviews(this.mUrl, this.mItemsRequestedCount, count, this, this.mErrorListener);
        this.mItemsRequestedCount += count;
    }

    public void startLoadItems() {
        if (!this.mAlreadyStartedLoadingItems) {
            this.mAlreadyStartedLoadingItems = true;
            requestMoreItems(0, this.mPageSize);
        }
    }

    public final Rev.Review getItem(int position) {
        Rev.Review result = null;
        if (position < 0) {
            throw new IllegalArgumentException("Can't return an item with a negative index: " + position);
        }
        if (position < getCount()) {
            Rev.Review result2 = this.mItems.get(position);
            result = result2;
            if (this.mMoreAvailable && position >= getCount() - 4) {
                requestMoreItems(getCount(), this.mPageSize);
            }
        }
        return result;
    }

    public boolean hadRequestError() {
        return this.mErrorMsg != null;
    }

    public String getErrorMessage() {
        return this.mErrorMsg;
    }

    public void setErrorMessage(String message) {
        this.mErrorMsg = message;
    }

    public void retryLoadItems() {
        if (hadRequestError()) {
            requestMoreItems(getCount(), this.mPageSize);
        }
    }

    public boolean isMoreAvailable() {
        return this.mMoreAvailable;
    }

    public void attach(OnDataChangedListener o) {
        registerObserver(o);
    }

    public void detachAll() {
        unregisterAll();
        this.mErrorListener = null;
    }

    @Override // com.google.android.finsky.api.PaginatedDfeRequest.PaginatedListener
    public void onResponse(Rev.ReviewResponse response, int requestOffset, int requestCount) {
        this.mErrorMsg = null;
        if (requestOffset < this.mItems.size()) {
            int end = requestOffset + requestCount;
            this.mItems.subList(requestOffset, Math.min(end, this.mItems.size())).clear();
        }
        Rev.GetReviewsResponse reviewsResponse = response.getGetResponse();
        if (reviewsResponse == null || reviewsResponse.getReviewList().size() == 0) {
            this.mMoreAvailable = false;
        } else {
            this.mItems.addAll(reviewsResponse.getReviewList());
        }
        notifyListChanged();
    }
}
