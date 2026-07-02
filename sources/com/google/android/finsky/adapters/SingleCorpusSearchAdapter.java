package com.google.android.finsky.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.finsky.R;
import com.google.android.finsky.adapters.PaginatedListAdapter;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.api.model.OnDataChangedListener;
import com.google.android.finsky.api.model.PaginatedList;
import com.google.android.finsky.model.Bucket;
import com.google.android.finsky.navigationmanager.NavigationManager;
import com.google.android.finsky.utils.BitmapLoader;
import com.google.android.finsky.utils.CorpusMetadata;
import com.google.android.finsky.utils.ErrorStrings;

/* JADX INFO: loaded from: classes.dex */
public class SingleCorpusSearchAdapter extends DocumentBasedAdapter implements OnDataChangedListener {
    private final Bucket mBucket;
    private final int mBucketEntryIconHeight;
    private final int mBucketEntryIconWidth;
    private final int mColumnCount;
    private final int mHeaderIconHeight;
    private final int mHeaderIconWidth;
    private final PaginatedList<?> mListRequest;

    public SingleCorpusSearchAdapter(Context context, BitmapLoader bitmapLoader, NavigationManager navManager, PaginatedList<?> request, int columns, Bucket bucket, boolean showIndividualRatings) {
        super(context, navManager, null, showIndividualRatings, false, bitmapLoader, request.inErrorState(), true);
        this.mBucket = bucket;
        this.mColumnCount = columns;
        this.mListRequest = request;
        this.mListRequest.addDataChangedListener(this);
        request.setPageSize(this.mColumnCount * 5);
        Resources res = context.getResources();
        float dimensionPixelSize = res.getDimensionPixelSize(R.dimen.bucket_entry_width) + res.getDimensionPixelSize(R.dimen.bucket_entry_right_margin);
        this.mHeaderIconWidth = res.getDimensionPixelSize(R.dimen.bucket_header_icon_width);
        this.mHeaderIconHeight = res.getDimensionPixelSize(R.dimen.bucket_header_icon_height);
        this.mBucketEntryIconHeight = res.getDimensionPixelSize(R.dimen.bucket_entry_icon_height);
        this.mBucketEntryIconWidth = CorpusMetadata.getIconWidth(context, bucket.getBackend());
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case 0:
                return getHeaderView(convertView, parent);
            case 1:
                return getItemView(position - 1, convertView, parent);
            case 2:
                return getLoadingFooterView(convertView, parent);
            case 3:
                return getErrorFooterView(convertView, parent);
            default:
                throw new IllegalStateException("Unknown type for getView " + getItemViewType(position));
        }
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int position) {
        int lastRow = getCount() - 1;
        if (position == 0) {
            return 0;
        }
        int position2 = position - 1;
        if (position2 != lastRow - 1) {
            return 1;
        }
        switch (getFooterMode()) {
            case LOADING:
                return 2;
            case ERROR:
                return 3;
            case NONE:
                return 1;
            default:
                throw new IllegalStateException("No footer or item at row " + position2);
        }
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return 4;
    }

    @Override // com.google.android.finsky.adapters.DocumentBasedAdapter
    protected View.OnClickListener makeHeaderClickListener(Bucket bucket, String originalQuery) {
        return null;
    }

    private View getHeaderView(View convertView, ViewGroup parent) {
        View headerView = convertView == null ? inflate(R.layout.bucket_header, parent, false) : convertView;
        bindBucketHeader(this.mBucket, (ViewGroup) headerView, null, this.mHeaderIconWidth, this.mHeaderIconHeight);
        return headerView;
    }

    private View getItemView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflate(R.layout.bucket_row, parent, false);
            for (int column = 0; column < this.mColumnCount; column++) {
                ((ViewGroup) convertView).addView(inflate(R.layout.search_bucket_entry, (ViewGroup) convertView, false));
            }
        }
        for (int column2 = 0; column2 < this.mColumnCount; column2++) {
            bindBucketEntry((this.mColumnCount * position) + column2, (ViewGroup) ((ViewGroup) convertView).getChildAt(column2));
        }
        return convertView;
    }

    private void bindBucketEntry(int itemIndex, ViewGroup docEntry) {
        if (getItem(itemIndex) == null) {
            docEntry.setVisibility(4);
        } else {
            docEntry.setVisibility(0);
            bindDocument(getItem(itemIndex), docEntry, this.mBucketEntryIconWidth, this.mBucketEntryIconHeight);
        }
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return (int) (Math.ceil(((double) this.mListRequest.getCount()) / ((double) this.mColumnCount)) + 1.0d + ((double) (getFooterMode() == PaginatedListAdapter.FooterMode.NONE ? 0 : 1)));
    }

    @Override // android.widget.Adapter
    public Document getItem(int position) {
        if (position < this.mListRequest.getCount()) {
            return this.mListRequest.getItem(position);
        }
        return null;
    }

    @Override // com.google.android.finsky.adapters.PaginatedListAdapter
    protected String getLastRequestError() {
        return ErrorStrings.get(this.mContext, this.mListRequest.getErrorCode(), this.mListRequest.getErrorMessage());
    }

    @Override // com.google.android.finsky.adapters.PaginatedListAdapter
    protected boolean isMoreDataAvailable() {
        return this.mListRequest.isMoreAvailable();
    }

    @Override // com.google.android.finsky.adapters.PaginatedListAdapter
    protected void retryLoadingItems() {
        this.mListRequest.retryLoadItems(false);
    }
}
