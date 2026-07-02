package com.google.android.finsky.api.model;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.android.finsky.api.PaginatedDfeRequest;
import com.google.android.finsky.utils.Lists;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class PaginatedList<T> extends DfeModel implements PaginatedDfeRequest.PaginatedListener<T> {
    private final boolean mAutoLoadNextPage;
    private Request<?> mCurrentRequest;
    private final List<Document> mItems;
    protected T mLastResponse;
    private boolean mMoreAvailable;
    private int mPageSize;
    protected final String mUrl;

    protected abstract List<Document> getItemsFromResponse(T t);

    protected abstract Request<?> makeRequest(int i, int i2);

    protected PaginatedList(String url) {
        this(url, true);
    }

    protected PaginatedList(String url, boolean autoLoadNextPage) {
        this.mItems = Lists.newArrayList();
        this.mUrl = url;
        this.mMoreAvailable = true;
        this.mAutoLoadNextPage = autoLoadNextPage;
        this.mPageSize = 12;
    }

    public void clearTransientState() {
        this.mCurrentRequest = null;
    }

    public int getCount() {
        return this.mItems.size();
    }

    public boolean isReady() {
        return this.mLastResponse != null;
    }

    @Override // com.google.android.finsky.api.model.DfeModel, com.android.volley.Response.ErrorListener
    public void onErrorResponse(Response.ErrorCode error, String message) {
        this.mCurrentRequest = null;
        super.onErrorResponse(error, message);
    }

    public void startLoadItems() {
        if (this.mMoreAvailable && getCount() == 0) {
            requestMoreItemsIfNoRequestExists(0, this.mPageSize);
        }
    }

    public final Document getItem(int position) {
        Document result = null;
        if (position < 0) {
            throw new IllegalArgumentException("Can't return an item with a negative index: " + position);
        }
        if (position < getCount()) {
            Document result2 = this.mItems.get(position);
            result = result2;
            if (this.mAutoLoadNextPage && this.mMoreAvailable && position >= getCount() - 4) {
                requestMoreItemsIfNoRequestExists(getCount(), this.mPageSize);
            }
        }
        return result;
    }

    public void retryLoadItems(boolean forceRetry) {
        if (inErrorState()) {
            requestMoreItemsIfNoRequestExists(getCount(), this.mPageSize);
        }
    }

    public boolean isMoreAvailable() {
        return this.mMoreAvailable;
    }

    public void detachAll() {
        unregisterAll();
    }

    public void setPageSize(int itemCount) {
        if (itemCount <= 0) {
            throw new IllegalArgumentException("Can't set number of items to load per page to be <= 0.");
        }
        this.mPageSize = itemCount;
    }

    @Override // com.google.android.finsky.api.PaginatedDfeRequest.PaginatedListener
    public void onResponse(T response, int requestOffset, int requestCount) {
        clearErrors();
        this.mCurrentRequest = null;
        this.mLastResponse = response;
        if (requestOffset < this.mItems.size()) {
            int end = requestOffset + requestCount;
            this.mItems.subList(requestOffset, Math.min(end, this.mItems.size())).clear();
        }
        List<Document> items = getItemsFromResponse(response);
        this.mItems.addAll(requestOffset, items);
        this.mMoreAvailable = items.size() >= requestCount && this.mAutoLoadNextPage;
        notifyDataSetChanged();
    }

    private void requestMoreItemsIfNoRequestExists(int offset, int count) {
        if (this.mCurrentRequest == null) {
            this.mCurrentRequest = makeRequest(offset, count);
        }
    }
}
