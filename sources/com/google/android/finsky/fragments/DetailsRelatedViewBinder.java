package com.google.android.finsky.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.android.volley.Response;
import com.google.android.finsky.FinskyApp;
import com.google.android.finsky.R;
import com.google.android.finsky.analytics.Analytics;
import com.google.android.finsky.api.DfeApi;
import com.google.android.finsky.api.model.DfeList;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.api.model.OnDataChangedListener;
import com.google.android.finsky.layout.LayoutSwitcher;
import com.google.android.finsky.layout.ThumbnailListener;
import com.google.android.finsky.navigationmanager.NavigationManager;
import com.google.android.finsky.utils.BitmapLoader;
import com.google.android.finsky.utils.CorpusMetadata;
import com.google.android.finsky.utils.ErrorStrings;
import com.google.android.finsky.utils.ThumbnailUtils;
import java.text.NumberFormat;

/* JADX INFO: loaded from: classes.dex */
public class DetailsRelatedViewBinder extends TableLayoutViewBinder<Document> implements OnDataChangedListener {
    private BitmapLoader mBitmapLoader;
    private int mColumnCount;
    private int mIconHeight;
    private int mIconWidth;
    private DfeList mItemListRequest;
    private int mMaxItemsCount;
    private String mMoreUrl;
    private NumberFormat mNumberFormatInstance;
    private String mUrl;

    private static class DetailsItemViewHolder {
        public BitmapLoader.BitmapContainer item;
        public final TextView ratingCount;
        public final RatingBar ratingStars;
        public final ImageView thumbnail;
        public final TextView title;

        public DetailsItemViewHolder(View view) {
            this.title = (TextView) view.findViewById(R.id.title);
            this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            this.ratingStars = (RatingBar) view.findViewById(R.id.rating_stars);
            this.ratingCount = (TextView) view.findViewById(R.id.rating_count);
        }
    }

    public void init(Context context, DfeApi api, NavigationManager navManager, BitmapLoader bitmapLoader) {
        super.init(context, api, navManager);
        this.mBitmapLoader = bitmapLoader;
        this.mNumberFormatInstance = NumberFormat.getInstance();
        Resources res = context.getResources();
        this.mIconHeight = res.getDimensionPixelOffset(R.dimen.list_panel_basic_item_icon_height);
    }

    public void bind(View view, Document doc, String header, String url, String moreUrl, int columnCount, int maxItemsCount) {
        super.bind(view, doc, -1, -1);
        TextView headerView = (TextView) this.mLayout.findViewById(R.id.header);
        headerView.setText(header.toUpperCase());
        headerView.setTextColor(CorpusMetadata.getBackendHintColor(this.mContext, this.mDoc.getBackend()));
        this.mUrl = url;
        this.mMoreUrl = moreUrl;
        this.mColumnCount = columnCount;
        this.mIconWidth = CorpusMetadata.getRelatedIconWidth(this.mContext, this.mDoc.getBackend());
        this.mMaxItemsCount = maxItemsCount;
        setupView();
    }

    private void detachListener() {
        if (this.mItemListRequest != null) {
            this.mItemListRequest.removeDataChangedListener(this);
        }
    }

    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    protected int getTableColumnCount() {
        return this.mColumnCount;
    }

    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    protected int getCellCount() {
        return Math.min(this.mItemListRequest.getCount(), this.mMaxItemsCount);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    public Document getData(int position) {
        return this.mItemListRequest.getItem(position);
    }

    public void setupView() {
        if (this.mItemListRequest != null) {
            this.mItemListRequest.clearTransientState();
        }
        LayoutSwitcher layoutSwitcher = new LayoutSwitcher(this.mLayout, getTableLayoutId(), new LayoutSwitcher.RetryButtonListener() { // from class: com.google.android.finsky.fragments.DetailsRelatedViewBinder.1
            @Override // com.google.android.finsky.layout.LayoutSwitcher.RetryButtonListener
            public void onRetry() {
                DetailsRelatedViewBinder.this.mItemListRequest.retryLoadItems(false);
            }
        });
        setLayoutSwitcher(layoutSwitcher);
        layoutSwitcher.switchToLoadingDelayed(350);
        if (this.mUrl != null) {
            this.mLayout.setVisibility(0);
            setupItemListRequest();
        }
    }

    private void attachToInternalRequest() {
        if (this.mItemListRequest == null) {
            throw new IllegalStateException("Cannot attach when no request is held internally");
        }
        this.mItemListRequest.addDataChangedListener(this);
        if (this.mItemListRequest.getCount() != 0) {
            this.mLayout.setVisibility(0);
            getLayoutSwitcher().switchToDataMode();
            prepareAndPopulateTable();
        } else if (!this.mItemListRequest.isMoreAvailable()) {
            this.mLayout.setVisibility(8);
        } else if (this.mItemListRequest.inErrorState()) {
            getLayoutSwitcher().switchToErrorMode(ErrorStrings.get(this.mContext, this.mItemListRequest.getErrorCode(), this.mItemListRequest.getErrorMessage()));
        }
    }

    private void setupItemListRequest() {
        detachListener();
        this.mItemListRequest = new DfeList(this.mDfeApi, this.mUrl, false, FinskyApp.get().getAnalytics(), Analytics.Event.RELATED);
        attachToInternalRequest();
        this.mItemListRequest.setPageSize(this.mMaxItemsCount);
        this.mItemListRequest.addErrorListener(this);
        this.mItemListRequest.startLoadItems();
    }

    private void prepareAndPopulateTable() {
        setButtonVisibility(R.id.more_button, 0, R.string.more_related);
        setButtonClickListener(R.id.more_button, new View.OnClickListener() { // from class: com.google.android.finsky.fragments.DetailsRelatedViewBinder.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DetailsRelatedViewBinder.this.mNavigationManager.goBrowse(DetailsRelatedViewBinder.this.mMoreUrl);
            }
        });
        populateTable();
    }

    @Override // com.google.android.finsky.api.model.OnDataChangedListener
    public void onDataChanged() {
        getLayoutSwitcher().switchToDataMode();
        if (this.mItemListRequest.getCount() == 0) {
            this.mLayout.setVisibility(8);
        } else {
            this.mLayout.setVisibility(0);
            prepareAndPopulateTable();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    public void setupTableCell(Document data, ViewGroup view) {
        if (data != null) {
            DetailsItemViewHolder holder = new DetailsItemViewHolder(view);
            holder.title.setText(data.getTitle());
            if (data.hasRating()) {
                holder.ratingStars.setRating(data.getStarRating());
                holder.ratingCount.setText(this.mNumberFormatInstance.format(data.getRatingCount()));
            }
            holder.thumbnail.getLayoutParams().width = this.mIconWidth;
            String imageUrl = ThumbnailUtils.getBitmapUrlFromDocument(data, 0);
            if (holder.item != null && holder.item.getRequestUrl() != null && !holder.item.getRequestUrl().equals(imageUrl)) {
                holder.item.cancelRequest();
            }
            holder.item = this.mBitmapLoader.getOrBindImmediately(imageUrl, holder.thumbnail, new ThumbnailListener(holder.thumbnail, true), this.mIconWidth, this.mIconHeight);
            view.setTag(holder);
            view.setOnClickListener(this.mNavigationManager.getDetailsClickListener(data));
        }
    }

    @Override // com.google.android.finsky.fragments.DetailsViewBinder, com.android.volley.Response.ErrorListener
    public void onErrorResponse(Response.ErrorCode error, String message) {
        super.onErrorResponse(error, message);
        ErrorStrings.get(this.mContext, error, message);
    }

    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    protected int getTableCellLayoutId() {
        return R.layout.list_panel_basic_item;
    }

    @Override // com.google.android.finsky.fragments.DetailsViewBinder
    public void onDestroyView() {
        super.onDestroyView();
        detachListener();
    }
}
