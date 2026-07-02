package com.google.android.finsky.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.android.finsky.R;
import com.google.android.finsky.analytics.Analytics;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.model.Bucket;
import com.google.android.finsky.navigationmanager.NavigationManager;
import com.google.android.finsky.utils.BitmapLoader;
import com.google.android.finsky.utils.CorpusMetadata;
import com.google.android.finsky.utils.ThumbnailUtils;
import com.google.android.finsky.utils.Utils;
import java.text.NumberFormat;

/* JADX INFO: loaded from: classes.dex */
public abstract class DocumentBasedAdapter extends PaginatedListAdapter {
    private final boolean mIncludeLinkToAllResults;
    protected final BitmapLoader mLoader;
    private final boolean mShowIndividualRatings;
    private final boolean mShowResultCount;

    public DocumentBasedAdapter(Context context, NavigationManager navManager, Analytics.Event event, boolean showIndividualRatings, boolean showAllResultsLink, BitmapLoader loader, boolean isRequestInErrorState, boolean showResultCount) {
        super(context, navManager, event, isRequestInErrorState);
        this.mShowIndividualRatings = showIndividualRatings;
        this.mShowResultCount = showResultCount;
        this.mIncludeLinkToAllResults = showAllResultsLink;
        this.mLoader = loader;
    }

    protected int getHintColor(int backend) {
        return CorpusMetadata.getBackendHintColor(this.mContext, backend);
    }

    protected Drawable getEntryBackground(Document doc) {
        return CorpusMetadata.getBucketEntryBackground(this.mContext, doc.getBackend());
    }

    protected void bindDocument(Document doc, ViewGroup docEntry, int imageWidth, int imageHeight) {
        RatingBar ratingBar = (RatingBar) docEntry.findViewById(R.id.li_rating);
        TextView ratingCount = (TextView) docEntry.findViewById(R.id.li_rating_count);
        TextView priceView = (TextView) docEntry.findViewById(R.id.li_price);
        ImageView bitmapView = (ImageView) docEntry.findViewById(R.id.li_thumbnail);
        View.OnClickListener openDetails = this.mNavigationManager.getDetailsClickListener(doc);
        docEntry.setOnClickListener(openDetails);
        docEntry.setBackgroundDrawable(getEntryBackground(doc));
        ((TextView) docEntry.findViewById(R.id.li_title)).setText(doc.getTitle());
        ((TextView) docEntry.findViewById(R.id.li_creator)).setText(doc.getCreator());
        Utils.bindPurchaseButton(priceView, doc, this.mNavigationManager);
        if (doc.hasRating() && this.mShowIndividualRatings) {
            ratingBar.setVisibility(0);
            ratingCount.setVisibility(0);
            ratingBar.setRating(doc.getStarRating());
            ratingCount.setText(NumberFormat.getNumberInstance().format(doc.getRatingCount()));
        } else {
            ratingBar.setVisibility(8);
            ratingCount.setVisibility(8);
        }
        bindImage(bitmapView, ThumbnailUtils.getBitmapUrlFromDocument(doc), ThumbnailUtils.getDefaultIcon(doc.getBackend(), this.mContext.getResources()), imageWidth, imageHeight);
    }

    protected View.OnClickListener makeHeaderClickListener(final Bucket bucket, String originalQuery) {
        if (originalQuery != null) {
            return this.mNavigationManager.createSearchMoreClickListener(bucket.getHeaderUrl(), originalQuery, bucket.getBackend());
        }
        if (!TextUtils.isEmpty(bucket.getHeaderUrl())) {
            return new View.OnClickListener() { // from class: com.google.android.finsky.adapters.DocumentBasedAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    DocumentBasedAdapter.this.mNavigationManager.goBrowse(bucket.getHeaderUrl());
                }
            };
        }
        return null;
    }

    private void bindImage(final ImageView safeView, String urlToLoad, Bitmap temporaryDisplay, int maxWidth, int maxHeight) {
        BitmapLoader.BitmapContainer oldContainer = (BitmapLoader.BitmapContainer) safeView.getTag();
        if (oldContainer != null && oldContainer.getRequestUrl() != null) {
            if (!oldContainer.getRequestUrl().equals(urlToLoad)) {
                oldContainer.cancelRequest();
            } else {
                return;
            }
        }
        BitmapLoader.BitmapContainer newContainer = this.mLoader.get(urlToLoad, temporaryDisplay, new BitmapLoader.BitmapLoadedHandler() { // from class: com.google.android.finsky.adapters.DocumentBasedAdapter.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.android.volley.Response.Listener
            public void onResponse(BitmapLoader.BitmapContainer result) {
                if (result.getBitmap() != null) {
                    ThumbnailUtils.setImageBitmapWithFade(safeView, result.getBitmap());
                }
            }
        }, maxWidth, maxHeight);
        safeView.getLayoutParams().width = maxWidth;
        safeView.setTag(newContainer);
        safeView.setImageBitmap(newContainer.getBitmap());
    }

    private void setHeaderIcon(Bucket bucket, ImageView bucketIconView, int maxIconWidth, int maxIconHeight) {
        if (!TextUtils.isEmpty(bucket.getIconUrl())) {
            bindImage(bucketIconView, bucket.getIconUrl(), BitmapFactory.decodeResource(this.mContext.getResources(), CorpusMetadata.getIconResource(bucket.getBackend())), maxIconWidth, maxIconHeight);
        } else {
            bucketIconView.setImageResource(CorpusMetadata.getIconResource(bucket.getBackend()));
        }
    }

    protected void bindBucketHeader(Bucket bucket, ViewGroup bucketView, String originalQuery, int maxIconWidth, int maxIconHeight) {
        View bucketHeader = bucketView.findViewById(R.id.bucket_header);
        View headerContents = bucketView.findViewById(R.id.header_contents);
        if (bucket.getItemCount() == 0) {
            bucketHeader.setVisibility(8);
            return;
        }
        bucketHeader.setVisibility(0);
        ImageView bucketIconView = (ImageView) bucketView.findViewById(R.id.header_icon);
        ImageView bucketMoreIconView = (ImageView) bucketView.findViewById(R.id.big_section_more_arrow);
        TextView headerView = (TextView) bucketView.findViewById(R.id.section_header);
        TextView searchResultsView = (TextView) bucketView.findViewById(R.id.section_results);
        TextView allResults = (TextView) bucketView.findViewById(R.id.header_more_results_link);
        int backend = bucket.getBackend();
        View.OnClickListener headerClickHandler = makeHeaderClickListener(bucket, originalQuery);
        headerView.setTextColor(getHintColor(backend));
        headerView.setText(bucket.getHeaderText().toUpperCase());
        setHeaderIcon(bucket, bucketIconView, maxIconWidth, maxIconHeight);
        if (bucket.hasMoreItems()) {
            if (this.mIncludeLinkToAllResults) {
                bucketMoreIconView.setVisibility(8);
            } else {
                bucketMoreIconView.setVisibility(0);
                bucketMoreIconView.setImageResource(CorpusMetadata.getMoreArrowResource(backend));
            }
            bucketMoreIconView.setVisibility(0);
            headerContents.setOnClickListener(headerClickHandler);
            headerContents.setBackgroundResource(R.drawable.details_page_button);
        } else {
            bucketMoreIconView.setVisibility(8);
            headerContents.setOnClickListener(null);
            headerContents.setBackgroundDrawable(null);
        }
        if (this.mShowResultCount) {
            int numMatchingAssets = bucket.getEstimatedResults();
            String template = numMatchingAssets > 0 ? this.mContext.getResources().getQuantityText(R.plurals.search_results_estimated_in_bucket, numMatchingAssets).toString() : "";
            searchResultsView.setText(String.format(template, Integer.valueOf(numMatchingAssets)));
            searchResultsView.setVisibility(0);
        } else {
            searchResultsView.setVisibility(8);
        }
        if (this.mIncludeLinkToAllResults) {
            allResults.setOnClickListener(headerClickHandler);
            allResults.setVisibility(0);
        } else {
            allResults.setVisibility(8);
            allResults.setOnClickListener(null);
        }
    }
}
