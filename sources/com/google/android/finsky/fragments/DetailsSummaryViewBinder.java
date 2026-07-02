package com.google.android.finsky.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.android.finsky.FinskyApp;
import com.google.android.finsky.R;
import com.google.android.finsky.activities.ContentFilterActivity;
import com.google.android.finsky.api.DfeApi;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.layout.ThumbnailListener;
import com.google.android.finsky.model.PurchaseStatusTracker;
import com.google.android.finsky.navigationmanager.NavigationManager;
import com.google.android.finsky.remoting.protos.BookInfo;
import com.google.android.finsky.remoting.protos.Common;
import com.google.android.finsky.remoting.protos.DeviceDoc;
import com.google.android.finsky.utils.BitmapLoader;
import com.google.android.finsky.utils.CorpusMetadata;
import com.google.android.finsky.utils.DateUtils;
import com.google.android.finsky.utils.FinskyLog;
import com.google.android.finsky.utils.PackageInfoCache;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DetailsSummaryViewBinder implements PurchaseStatusTracker.PurchaseStatusListener {
    private BitmapLoader mBitmapLoader;
    private Context mContext;
    private Document mDoc;
    private boolean mIsPendingDownload;
    private View mLayout;
    private NavigationManager mNavigationManager;
    private NumberFormat mNumberFormatInstance;
    private int mOfferTypeForPendingPurchase;
    private PurchaseStatusTracker mPurchaseStatusTracker;

    public void init(Context context, DfeApi api, NavigationManager navManager, BitmapLoader bitmapLoader) {
        this.mContext = context;
        this.mNavigationManager = navManager;
        this.mBitmapLoader = bitmapLoader;
        this.mNumberFormatInstance = NumberFormat.getNumberInstance();
    }

    public void bind(View view, Document document) {
        this.mLayout = view;
        this.mDoc = document;
        this.mPurchaseStatusTracker = FinskyApp.get().getPurchaseStatusTracker();
        this.mPurchaseStatusTracker.attach(this);
        setupItemDetails();
        if (this.mDoc.hasDetails()) {
            setupActionButtons();
        }
    }

    public void refresh() {
        bind(this.mLayout, this.mDoc);
    }

    private void setupItemDetails() {
        TextView title = (TextView) this.mLayout.findViewById(R.id.title);
        title.setText(this.mDoc.getTitle());
        TextView creator = (TextView) this.mLayout.findViewById(R.id.creator);
        creator.setText(this.mDoc.getCreator().toUpperCase());
        bindImage();
        bindPrice();
        bindRatingAndReviewsCount();
        bindDetails();
    }

    private void bindImage() {
        View divider = this.mLayout.findViewById(R.id.divider);
        int color = CorpusMetadata.getBackendHintColor(this.mContext, this.mDoc.getBackend());
        divider.setBackgroundColor(color);
        ImageView iv = (ImageView) this.mLayout.findViewById(R.id.thumbnail);
        String url = this.mDoc.getBestThumbnailUrl();
        if (url != null) {
            Resources res = this.mContext.getResources();
            int width = res.getDimensionPixelSize(R.dimen.thumbnail_width);
            int height = res.getDimensionPixelSize(R.dimen.thumbnail_height);
            BitmapLoader.BitmapContainer container = this.mBitmapLoader.get(url, (Bitmap) null, new ThumbnailListener(iv, divider, true), width, height);
            if (container.getBitmap() != null) {
                iv.getLayoutParams().width = -2;
                iv.getLayoutParams().height = -2;
                divider.getLayoutParams().width = container.getBitmap().getWidth() + iv.getPaddingLeft() + iv.getPaddingRight();
                iv.setImageBitmap(container.getBitmap());
            }
        }
    }

    private void bindPrice() {
        TextView priceView = (TextView) this.mLayout.findViewById(R.id.price);
        if (this.mDoc.getBackend() == 4) {
            priceView.setVisibility(4);
        } else {
            TextView convertedPriceView = (TextView) this.mLayout.findViewById(R.id.converted_price);
            this.mDoc.setPrice(priceView, convertedPriceView);
        }
    }

    private void bindRatingAndReviewsCount() {
        RatingBar ratingBar = (RatingBar) this.mLayout.findViewById(R.id.rating_stars);
        TextView reviewNumberView = (TextView) this.mLayout.findViewById(R.id.rating_count);
        if (this.mDoc.hasRating()) {
            ratingBar.setVisibility(0);
            reviewNumberView.setVisibility(0);
            ratingBar.setRating(this.mDoc.getStarRating());
            reviewNumberView.setText(this.mNumberFormatInstance.format(this.mDoc.getRatingCount()));
            return;
        }
        ratingBar.setVisibility(8);
        reviewNumberView.setVisibility(8);
    }

    private void bindDetails() {
        TextView firstLineText = (TextView) this.mLayout.findViewById(R.id.first_line_text);
        TextView secondLineText = (TextView) this.mLayout.findViewById(R.id.second_line_text);
        TextView thirdLineText = (TextView) this.mLayout.findViewById(R.id.third_line_text);
        DeviceDoc.AppDetails appDetails = this.mDoc.getAppDetails();
        if (appDetails != null) {
            String contentRating = ContentFilterActivity.getLabel(this.mContext, this.mDoc.getNormalizedContentRating());
            firstLineText.setText(this.mContext.getString(R.string.content_rated_for, contentRating));
            StringBuilder builder = new StringBuilder();
            if (appDetails.hasVersionString()) {
                builder.append(this.mContext.getString(R.string.version, appDetails.getVersionString())).append("   ");
            }
            if (appDetails.hasUploadDate()) {
                builder.append(appDetails.getUploadDate());
            }
            secondLineText.setText(builder.toString());
            StringBuilder builder2 = new StringBuilder();
            if (appDetails.hasInstallationSize()) {
                builder2.append(Formatter.formatFileSize(this.mContext, appDetails.getInstallationSize())).append("   ");
            }
            if (appDetails.hasNumDownloads()) {
                builder2.append(this.mContext.getString(R.string.downloads_count, appDetails.getNumDownloads()));
            }
            thirdLineText.setText(builder2.toString());
        }
        BookInfo.BookDetails bookDetails = this.mDoc.getBookDetails();
        if (bookDetails != null) {
            if (bookDetails.hasPublicationDate()) {
                try {
                    firstLineText.setText(DateUtils.formatIso8601Date(bookDetails.getPublicationDate()));
                } catch (ParseException e) {
                    FinskyLog.e("Cannot parse ISO 8601 date " + e, new Object[0]);
                }
            }
            if (bookDetails.hasPublisher()) {
                secondLineText.setText(bookDetails.getPublisher());
            }
            if (bookDetails.hasNumberOfPages()) {
                thirdLineText.setText(this.mContext.getString(R.string.pages_count, Integer.valueOf(bookDetails.getNumberOfPages())));
            }
        }
        DeviceDoc.VideoDetails videoDetails = this.mDoc.getVideoDetails();
        if (videoDetails != null) {
            if (videoDetails.hasContentRating()) {
                firstLineText.setText(this.mContext.getString(R.string.movie_rating, videoDetails.getContentRating()));
            } else {
                firstLineText.setText(this.mContext.getString(R.string.no_movie_rating));
            }
            if (videoDetails.hasReleaseDate()) {
                secondLineText.setText(videoDetails.getReleaseDate());
            }
            if (videoDetails.hasDuration()) {
                thirdLineText.setText(videoDetails.getDuration());
            }
        }
    }

    private void setupBuyButtons(Button buyButton, Button buyButtonSecondary) {
        switch (this.mDoc.getBackend()) {
            case 4:
                List<Common.Offer> offers = this.mDoc.getAvailableOffers();
                for (Common.Offer o : offers) {
                    int offerType = o.getOfferType();
                    if (offerType == 4) {
                        buyButton.setVisibility(0);
                        buyButton.setText(this.mContext.getString(R.string.rent_hd, o.getFormattedAmount()).toUpperCase());
                        buyButton.setOnClickListener(this.mNavigationManager.getBuyImmediateClickListener(this.mDoc, offerType));
                    }
                    if (offerType == 3) {
                        buyButtonSecondary.setVisibility(0);
                        buyButtonSecondary.setText(this.mContext.getString(R.string.rent_sd, o.getFormattedAmount()).toUpperCase());
                        buyButtonSecondary.setOnClickListener(this.mNavigationManager.getBuyImmediateClickListener(this.mDoc, offerType));
                    }
                }
                break;
            default:
                buyButton.setVisibility(0);
                buyButton.setText(getBuyButtonString());
                buyButton.setOnClickListener(this.mNavigationManager.getBuyImmediateClickListener(this.mDoc, 1));
                break;
        }
    }

    private String getBuyButtonString() {
        if (!this.mDoc.needsCheckoutFlow()) {
            if (this.mDoc.getBackend() == 3) {
                return this.mContext.getString(R.string.download);
            }
            if (this.mDoc.getBackend() == 1) {
                return this.mContext.getString(R.string.open);
            }
        }
        return this.mContext.getString(R.string.buy);
    }

    private String getPurchasePendingString(int offerType) {
        if (this.mDoc.needsCheckoutFlow() && offerType != 2) {
            return this.mContext.getString(R.string.checkout_purchase_pending);
        }
        switch (this.mDoc.getBackend()) {
            case 1:
                return this.mContext.getString(R.string.book_purchase_pending);
            default:
                return this.mContext.getString(R.string.app_download_pending);
        }
    }

    private int getOpenButtonStringId() {
        return this.mDoc.getBackend() == 4 ? R.string.play : R.string.open;
    }

    private void setupActionButtons() {
        Button tryButton = (Button) this.mLayout.findViewById(R.id.try_button);
        Button buyButton = (Button) this.mLayout.findViewById(R.id.buy_button);
        Button buyButton2 = (Button) this.mLayout.findViewById(R.id.buy_button2);
        Button launchButton = (Button) this.mLayout.findViewById(R.id.launch_button);
        Button manageButton = (Button) this.mLayout.findViewById(R.id.manage_button);
        View divider = this.mLayout.findViewById(R.id.divider);
        View purchaseProgress = this.mLayout.findViewById(R.id.purchase_progress_bar);
        TextView pendingStatus = (TextView) this.mLayout.findViewById(R.id.pending_status);
        ImageView ownershipStatus = (ImageView) this.mLayout.findViewById(R.id.ownership_status);
        launchButton.setVisibility(8);
        tryButton.setVisibility(8);
        buyButton.setVisibility(8);
        buyButton2.setVisibility(8);
        manageButton.setVisibility(8);
        divider.setVisibility(0);
        purchaseProgress.setVisibility(8);
        pendingStatus.setVisibility(8);
        if (this.mPurchaseStatusTracker.isPendingPurchase(this.mDoc.getDocId())) {
            String message = getPurchasePendingString(this.mOfferTypeForPendingPurchase);
            showPendingState(message, divider, purchaseProgress, pendingStatus);
            return;
        }
        if (this.mIsPendingDownload) {
            String message2 = this.mContext.getString(R.string.app_download_pending);
            showPendingState(message2, divider, purchaseProgress, pendingStatus);
            return;
        }
        PackageInfoCache packageInfoCache = FinskyApp.get().getPackageInfoCache();
        boolean isLocallyAvailable = this.mDoc.isLocallyAvailable(packageInfoCache);
        if (this.mDoc.ownedByUser(packageInfoCache) || isLocallyAvailable) {
            boolean showLaunchButton = (this.mDoc.getBackend() == 3 && isLocallyAvailable && !this.mDoc.canLaunch(packageInfoCache)) ? false : true;
            if (showLaunchButton) {
                launchButton.setVisibility(0);
                launchButton.setText(isLocallyAvailable ? getOpenButtonStringId() : R.string.install);
                launchButton.setOnClickListener(this.mNavigationManager.getOpenClickListener(this.mDoc));
            } else {
                launchButton.setVisibility(8);
            }
            if (isLocallyAvailable && this.mDoc.canManage(packageInfoCache)) {
                if (this.mDoc.getBackend() == 4) {
                    manageButton.setText(R.string.download);
                } else if (this.mDoc.isUpdateAvailable(packageInfoCache)) {
                    manageButton.setText(R.string.update);
                } else {
                    manageButton.setText(R.string.manage);
                }
                manageButton.setVisibility(0);
                manageButton.setOnClickListener(this.mNavigationManager.getManageClickListener(this.mDoc));
            }
        } else {
            setupBuyButtons(buyButton, buyButton2);
            if (this.mDoc.hasSample()) {
                tryButton.setVisibility(0);
                if (this.mDoc.sampleOwnedByUser()) {
                    tryButton.setOnClickListener(this.mNavigationManager.getOpenClickListener(this.mDoc));
                } else {
                    tryButton.setOnClickListener(this.mNavigationManager.getBuyImmediateClickListener(this.mDoc, 2));
                }
            }
        }
        if (isLocallyAvailable) {
            ownershipStatus.setImageResource(CorpusMetadata.getOwnedIconResource(this.mDoc.getBackend()));
        } else if (this.mDoc.ownedByUser(packageInfoCache)) {
            ownershipStatus.setImageResource(CorpusMetadata.getOwnedNotLocalIconResource(this.mDoc.getBackend()));
        }
        tryButton.setText(tryButton.getText().toString().toUpperCase());
        buyButton.setText(buyButton.getText().toString().toUpperCase());
        launchButton.setText(launchButton.getText().toString().toUpperCase());
        manageButton.setText(manageButton.getText().toString().toUpperCase());
    }

    private void showPendingState(String message, View divider, View purchaseProgressBar, TextView statusTextView) {
        divider.setVisibility(8);
        purchaseProgressBar.setVisibility(0);
        statusTextView.setVisibility(0);
        statusTextView.setText(message);
    }

    @Override // com.google.android.finsky.model.PurchaseStatusTracker.PurchaseStatusListener
    public void onPurchaseInitiated(String docId, int offerType) {
        if (this.mDoc.getDocId().equals(docId)) {
            this.mOfferTypeForPendingPurchase = offerType;
            refresh();
        }
    }

    @Override // com.google.android.finsky.model.PurchaseStatusTracker.PurchaseStatusListener
    public void onPurchaseCompleted(String docId, boolean hadError) {
        if (this.mDoc.getDocId().equals(docId)) {
            if (hadError) {
                this.mNavigationManager.refreshPage();
                return;
            }
            switch (this.mDoc.getBackend()) {
                case 1:
                    this.mNavigationManager.refreshPage();
                    if (!this.mDoc.needsCheckoutFlow() || this.mOfferTypeForPendingPurchase == 2) {
                        this.mNavigationManager.sampleItem(this.mDoc);
                    }
                    break;
                case 2:
                default:
                    this.mNavigationManager.refreshPage();
                    break;
                case 3:
                    this.mIsPendingDownload = true;
                    refresh();
                    break;
            }
            this.mOfferTypeForPendingPurchase = 0;
        }
    }

    @Override // com.google.android.finsky.model.PurchaseStatusTracker.PurchaseStatusListener
    public void onPackageInstalled(String packageName) {
        if (this.mDoc != null && this.mDoc.getAppDetails() != null && this.mDoc.getAppDetails().getPackageName().equals(packageName)) {
            this.mIsPendingDownload = false;
            this.mNavigationManager.refreshPage();
        }
    }

    public void onDestroyView() {
        if (this.mPurchaseStatusTracker != null) {
            this.mPurchaseStatusTracker.detach(this);
        }
    }
}
