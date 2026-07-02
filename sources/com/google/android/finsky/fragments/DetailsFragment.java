package com.google.android.finsky.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.android.volley.Response;
import com.google.android.finsky.FinskyApp;
import com.google.android.finsky.R;
import com.google.android.finsky.analytics.Analytics;
import com.google.android.finsky.api.DfeApi;
import com.google.android.finsky.api.model.DfeDetails;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.remoting.protos.DetailsResponse;
import com.google.android.finsky.utils.CorpusMetadata;

/* JADX INFO: loaded from: classes.dex */
public class DetailsFragment extends PageFragment<DfeDetails> implements Response.Listener<DetailsResponse> {
    private DetailsPageState mCurrentState;
    private Document mDoc;
    private final DetailsSummaryViewBinder mItemDetailsViewBinder = new DetailsSummaryViewBinder();
    private final DetailsRelatedViewBinder mCreatorRelatedViewBinder = new DetailsRelatedViewBinder();
    private final DetailsAllReviewsViewBinder mAllReviewsViewBinder = new DetailsAllReviewsViewBinder();
    private final DetailsPromoViewBinder mPromoViewBinder = new DetailsPromoViewBinder();
    private final DetailsTextViewBinder mDescriptionViewBinder = new DetailsTextViewBinder();
    private final DetailsTextViewBinder mWhatsNewViewBinder = new DetailsTextViewBinder();
    private final DetailsScreenshotsViewBinder mScreenshotsViewBinder = new DetailsScreenshotsViewBinder();
    private final DetailsVideoViewBinder mVideoViewBinder = new DetailsVideoViewBinder();
    private final DetailsTrailerViewBinder mTrailerViewBinder = new DetailsTrailerViewBinder();
    private final DetailsCastCrewViewBinder mCastCrewViewBinder = new DetailsCastCrewViewBinder();
    private final DetailsReviewsViewBinder mReviewsViewBinder = new DetailsReviewsViewBinder();
    private final DetailsDeveloperViewBinder mDeveloperViewBinder = new DetailsDeveloperViewBinder();
    private final DetailsRelatedViewBinder mRelatedViewBinder = new DetailsRelatedViewBinder();

    public enum DetailsPageState {
        ALL_DETAILS(Integer.valueOf(R.id.details_scroll_panel)),
        ALL_REVIEWS(Integer.valueOf(R.id.all_reviews_panel));

        private Integer mLayoutId;

        DetailsPageState(Integer layoutId) {
            this.mLayoutId = layoutId;
        }

        private View getView(View parent) {
            if (parent == null) {
                return null;
            }
            return parent.findViewById(this.mLayoutId.intValue());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onEnterState(View parent) {
            View view = getView(parent);
            if (view != null) {
                view.setVisibility(0);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onExitState(View parent) {
            View view = getView(parent);
            if (view != null) {
                view.setVisibility(8);
            }
        }
    }

    public static DetailsFragment newInstance(String url, Document doc) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString("finsky.PageFragment.url", getDetailsUrl(url, doc));
        args.putParcelable("finsky.DetailsFragment.document", doc);
        detailsFragment.setArguments(args);
        return detailsFragment;
    }

    @Override // com.google.android.finsky.fragments.PageFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mDoc = (Document) getArguments().getParcelable("finsky.DetailsFragment.document");
        this.mCurrentState = DetailsPageState.ALL_DETAILS;
    }

    @Override // com.google.android.finsky.fragments.PageFragment
    protected int getLayoutRes() {
        return R.layout.details_frame;
    }

    @Override // com.google.android.finsky.fragments.PageFragment, android.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mCurrentState.onEnterState(getView());
        switchToBlank();
        if (this.mDoc == null) {
            switchToLoading();
            requestData();
        } else if (!this.mDoc.hasDetails()) {
            onDataChanged();
            requestData();
        } else {
            onDataChanged();
        }
    }

    @Override // com.google.android.finsky.fragments.PageFragment
    protected void onInitViewBinders() {
        this.mItemDetailsViewBinder.init(this.mContext, this.mDfeApi, this.mNavigationManager, this.mBitmapLoader);
        this.mCreatorRelatedViewBinder.init(this.mContext, this.mDfeApi, this.mNavigationManager, this.mBitmapLoader);
        this.mAllReviewsViewBinder.init(this.mContext, this.mDfeApi, this.mNavigationManager);
        this.mPromoViewBinder.init(this.mContext, this.mBitmapLoader);
        this.mDescriptionViewBinder.init(this.mContext, this.mDfeApi, this.mNavigationManager);
        this.mWhatsNewViewBinder.init(this.mContext, this.mDfeApi, this.mNavigationManager);
        this.mScreenshotsViewBinder.init(this.mContext, this.mBitmapLoader);
        this.mVideoViewBinder.init(this.mContext, this.mDfeApi, this.mNavigationManager);
        this.mTrailerViewBinder.init(this.mContext, this.mDfeApi, this.mNavigationManager, this.mBitmapLoader);
        this.mCastCrewViewBinder.init(this.mContext, this.mDfeApi, this.mNavigationManager);
        this.mReviewsViewBinder.init(this.mContext, this.mDfeApi, this.mNavigationManager);
        this.mDeveloperViewBinder.init(this.mContext, this.mDfeApi, this.mNavigationManager);
        this.mRelatedViewBinder.init(this.mContext, this.mDfeApi, this.mNavigationManager, this.mBitmapLoader);
    }

    @Override // com.google.android.finsky.fragments.PageFragment
    protected void requestData() {
        this.mDfeApi.getDetails(this.mUrl, this, this);
    }

    private void setupHeaderBackground() {
        int backgroundRes = CorpusMetadata.getDetailsHeaderBackgroundResource(this.mDoc.getBackend());
        getView().findViewById(R.id.header_background).setBackgroundResource(backgroundRes);
        getView().findViewById(R.id.promo_panel).setBackgroundResource(backgroundRes);
    }

    private void updateStatusBar() {
        this.mActivity.getFinskyActionBar().setCurrentCollectionType(this.mDoc.getBackend());
        this.mActivity.invalidateOptionsMenu();
    }

    private static String getDetailsUrl(String url, Document doc) {
        return (doc == null || doc.getCookie() == null) ? url : url + "&ac=" + doc.getCookie();
    }

    public void switchLayout(DetailsPageState state) {
        if (state != this.mCurrentState) {
            final DetailsPageState oldState = this.mCurrentState;
            this.mCurrentState = state;
            this.mCurrentState.onEnterState(getView());
            final View reviewsPanel = getView().findViewById(R.id.reviews_panel);
            final View developerPanel = getView().findViewById(R.id.developer_panel);
            final View relatedPanel = getView().findViewById(R.id.related_panel);
            View allReviewsPanel = getView().findViewById(R.id.all_reviews_panel);
            float alpha = oldState == DetailsPageState.ALL_DETAILS ? 1.0f : 0.0f;
            ValueAnimator allDetailsAnimator = ValueAnimator.ofFloat(alpha, 1.0f - alpha).setDuration(500L);
            allDetailsAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.finsky.fragments.DetailsFragment.1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator animation) {
                    float alpha2 = ((Float) animation.getAnimatedValue()).floatValue();
                    reviewsPanel.setAlpha(alpha2);
                    developerPanel.setAlpha(alpha2);
                    relatedPanel.setAlpha(alpha2);
                }
            });
            Animator allReviewsAnimator = ObjectAnimator.ofFloat(allReviewsPanel, "alpha", 1.0f - alpha, alpha).setDuration(500L);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(allDetailsAnimator, allReviewsAnimator);
            animatorSet.addListener(new Animator.AnimatorListener() { // from class: com.google.android.finsky.fragments.DetailsFragment.2
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animation) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    oldState.onExitState(DetailsFragment.this.getView());
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animation) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animation) {
                }
            });
            animatorSet.start();
        }
    }

    @Override // com.google.android.finsky.fragments.PageFragment
    public void refresh() {
        String cacheKey = Uri.withAppendedPath(DfeApi.BASE_URI, this.mUrl).toString();
        FinskyApp.get().getCache().invalidate(cacheKey, true);
        super.refresh();
    }

    @Override // com.google.android.finsky.fragments.PageFragment
    protected void rebindViews() {
        setupHeaderBackground();
        updateStatusBar();
        this.mItemDetailsViewBinder.bind(getView().findViewById(R.id.item_details_panel), this.mDoc);
        String creatorRelatedHeader = this.mDoc.getMoreByHeader();
        int creatorRelatedItemsRowCount = getResources().getInteger(R.integer.creator_related_items_row_count);
        this.mCreatorRelatedViewBinder.bind(getView().findViewById(R.id.creator_related_panel), this.mDoc, creatorRelatedHeader, this.mDoc.getMoreByListUrl(), this.mDoc.getMoreByBrowseUrl(), 1, creatorRelatedItemsRowCount * 1);
        this.mAllReviewsViewBinder.bind(getView().findViewById(R.id.all_reviews_panel), this.mDoc);
        this.mPromoViewBinder.bind(getView().findViewById(R.id.promo_panel), this.mDoc);
        CharSequence descriptionText = this.mDoc.getDescription();
        this.mDescriptionViewBinder.bind(getView().findViewById(R.id.description_panel), this.mDoc, CorpusMetadata.getDescriptionHeaderStringId(this.mDoc.getBackend()), descriptionText);
        CharSequence whatsNewText = this.mDoc.getWhatsNew();
        this.mWhatsNewViewBinder.bind(getView().findViewById(R.id.new_panel), this.mDoc, R.string.details_whats_new, whatsNewText);
        this.mScreenshotsViewBinder.bind(getView().findViewById(R.id.screenshots_panel), this.mDoc);
        this.mReviewsViewBinder.bind(getView().findViewById(R.id.reviews_panel), this.mDoc);
        this.mDeveloperViewBinder.bind(getView().findViewById(R.id.developer_panel), this.mDoc);
        if (this.mDoc.hasCreatorRelatedContent()) {
            this.mContext.getString(R.string.details_related);
            this.mRelatedViewBinder.bind(getView().findViewById(R.id.related_panel), this.mDoc, this.mDoc.getRelatedHeader(), this.mDoc.getRelatedUrl(), this.mDoc.getRelatedBrowseUrl(), 2, 4);
        }
        int videoColumns = getResources().getInteger(R.integer.video_column_count);
        this.mVideoViewBinder.bind(getView().findViewById(R.id.video_panel), this.mDoc, videoColumns);
        int trailerColumns = getResources().getInteger(R.integer.trailer_column_count);
        this.mTrailerViewBinder.bind(getView().findViewById(R.id.trailer_panel), this.mDoc, trailerColumns);
        this.mCastCrewViewBinder.bind(getView().findViewById(R.id.cast_crew_panel), this.mDoc);
    }

    @Override // com.android.volley.Response.Listener
    public void onResponse(DetailsResponse response) {
        String cookie = response.getAnalyticsCookie();
        FinskyApp.get().getAnalytics().reportVirtualPageView(Analytics.Event.DETAILS, cookie);
        if (isAdded()) {
            if (response.getDoc() == null) {
                this.mActivity.showErrorDialog(this.mContext.getString(R.string.details_page_error), true);
            } else {
                this.mDoc = new Document(response.getDoc(), cookie);
                onDataChanged();
            }
        }
    }

    @Override // android.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        this.mItemDetailsViewBinder.onDestroyView();
        this.mCreatorRelatedViewBinder.onDestroyView();
        this.mAllReviewsViewBinder.onDestroyView();
        this.mPromoViewBinder.onDestroyView();
        this.mDescriptionViewBinder.onDestroyView();
        this.mWhatsNewViewBinder.onDestroyView();
        this.mScreenshotsViewBinder.onDestroyView();
        this.mVideoViewBinder.onDestroyView();
        this.mTrailerViewBinder.onDestroyView();
        this.mCastCrewViewBinder.onDestroyView();
        this.mReviewsViewBinder.onDestroyView();
        this.mDeveloperViewBinder.onDestroyView();
        this.mRelatedViewBinder.onDestroyView();
    }

    public Document getDocument() {
        return this.mDoc;
    }
}
