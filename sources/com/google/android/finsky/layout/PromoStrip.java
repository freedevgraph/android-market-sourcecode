package com.google.android.finsky.layout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.finsky.FinskyApp;
import com.google.android.finsky.R;
import com.google.android.finsky.adapters.PromoAdapter;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.utils.CorpusMetadata;
import com.google.android.finsky.utils.PurchaseButtonWrapper;

/* JADX INFO: loaded from: classes.dex */
public class PromoStrip extends DraggableHorizontalStrip {
    private PromoAdapter mAdapter;
    private final Handler mAutoAdvanceHandler;
    private final Runnable mAutoAdvanceRunnable;
    private boolean mAutoAdvanceScheduled;
    private int mFlyoverHeight;
    private final int mFlyoverInsetLeft;
    private final int mFlyoverInsetTop;
    private int mFlyoverMeasureHeight;
    private int mFlyoverMeasureWidth;
    private int mFlyoverWidth;
    private int mItemHeight;
    private int mItemWidth;
    private PromoItemView[] mItems;
    private PromoStripTapListener mOnChildViewTapListener;
    private int mVirtualItemWidth;

    public interface PromoStripTapListener {
        void onItemTap(Document document);

        void onPriceTap(PurchaseButtonWrapper purchaseButtonWrapper);
    }

    private class PromoItemView {
        private final TextView mBuyLink;
        private final View mFlyover;
        private final ImageView mIcon;
        private final ImageView mLargePromoImage;
        private final LayoutInflater mLayoutInflater;
        PurchaseButtonWrapper mPurchaseButton;
        private final ImageView mStripSideHologram;
        private boolean isPromoSet = false;
        private boolean isLogoSet = false;

        public PromoItemView() {
            this.mLayoutInflater = (LayoutInflater) PromoStrip.this.mContext.getSystemService("layout_inflater");
            this.mFlyover = this.mLayoutInflater.inflate(R.layout.promo_flyover, (ViewGroup) PromoStrip.this, false);
            this.mFlyover.setVisibility(4);
            this.mIcon = (ImageView) this.mFlyover.findViewById(R.id.icon);
            this.mBuyLink = (TextView) this.mFlyover.findViewById(R.id.buy_link);
            this.mLargePromoImage = new ImageView(PromoStrip.this.mContext);
            this.mLargePromoImage.setScaleType(ImageView.ScaleType.FIT_XY);
            this.mLargePromoImage.setVisibility(4);
            this.mStripSideHologram = new ImageView(PromoStrip.this.mContext);
            this.mStripSideHologram.setScaleType(ImageView.ScaleType.FIT_XY);
            this.mStripSideHologram.setLayoutParams(new ViewGroup.LayoutParams(-2, -1));
        }

        public void setFlyoverIcon(Bitmap bm) {
            if (bm != null) {
                this.mIcon.setImageDrawable(new BitmapDrawable(PromoStrip.this.mContext.getResources(), bm));
                if (!this.isLogoSet) {
                    this.mFlyover.setVisibility(0);
                }
            } else {
                this.mIcon.setImageDrawable(null);
                this.mFlyover.setVisibility(4);
            }
            this.isLogoSet = bm != null;
        }

        public void setPromo(Bitmap bm) {
            if (bm != null) {
                this.mLargePromoImage.setImageDrawable(new BitmapDrawable(PromoStrip.this.mContext.getResources(), bm));
                PromoStrip.this.updateItemWidth(Math.max(PromoStrip.this.mItemWidth, bm.getWidth()));
                PromoStrip.this.updateItemHeight(Math.max(PromoStrip.this.mItemHeight, bm.getHeight()));
                if (!this.isPromoSet) {
                    this.mLargePromoImage.setVisibility(0);
                }
            } else {
                this.mLargePromoImage.setImageDrawable(null);
                this.mLargePromoImage.setVisibility(4);
            }
            this.isPromoSet = bm != null;
        }

        public boolean isReadyForDisplay() {
            return this.isPromoSet && this.isLogoSet && isDocumentSet();
        }

        private boolean isDocumentSet() {
            return this.mLargePromoImage.getTag() != null;
        }

        public View getFlyover() {
            return this.mFlyover;
        }

        public View getPromo() {
            return this.mLargePromoImage;
        }

        public void setDocument(Document document) {
            this.mPurchaseButton = new PurchaseButtonWrapper(document, FinskyApp.get().getPackageInfoCache());
            this.mLargePromoImage.setTag(document);
            this.mFlyover.setTag(document);
            int visibility = this.mPurchaseButton.getVisibility();
            this.mBuyLink.setVisibility(visibility);
            if (visibility == 0) {
                this.mBuyLink.setText(this.mPurchaseButton.getDisplayText(PromoStrip.this.mContext));
            }
            this.mFlyover.setBackgroundResource(CorpusMetadata.getFlyoverBackgroundResource(document.getBackend()));
            this.mStripSideHologram.setImageResource(CorpusMetadata.getPromoHolographicStrip(document.getBackend()));
            this.mBuyLink.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.finsky.layout.PromoStrip.PromoItemView.1
                @Override // android.view.View.OnClickListener
                public void onClick(View arg0) {
                    PromoStrip.this.mOnChildViewTapListener.onPriceTap(PromoItemView.this.mPurchaseButton);
                }
            });
        }

        public void clearBitmaps() {
            setPromo(null);
            setFlyoverIcon(null);
        }

        public void updateFade() {
            if (isReadyForDisplay()) {
                int promoMiddleX = this.mLargePromoImage.getLeft() + (PromoStrip.this.mVirtualItemWidth / 2);
                float imageAlpha = 1.0f - getComponentStrengthForPosition(promoMiddleX, 0.7f);
                int multiplyComponentValue = (int) (255.0f * imageAlpha);
                int greyComponentValue = (int) (51.0f * (1.0f - imageAlpha));
                LightingColorFilter filter = new LightingColorFilter((multiplyComponentValue << 16) | (multiplyComponentValue << 8) | multiplyComponentValue, (greyComponentValue << 16) | (greyComponentValue << 8) | greyComponentValue);
                this.mStripSideHologram.setAlpha(1.0f - getComponentStrengthForPosition(promoMiddleX, 1.0f));
                this.mLargePromoImage.setColorFilter(filter);
                this.mStripSideHologram.setColorFilter(filter);
                updateFlyoverFade();
            }
        }

        private void updateFlyoverFade() {
            int widgetWidth = PromoStrip.this.getWidth();
            if (widgetWidth > 0) {
                int halfWayMark = widgetWidth / 2;
                int edge = this.mFlyover.getRight();
                this.mFlyover.setAlpha(edge > halfWayMark ? 1.0f : 1.0f - ((halfWayMark - edge) / (halfWayMark * 1.4f)));
            }
        }

        private float getComponentStrengthForPosition(int x, float intensity) {
            int viewWidth = PromoStrip.this.getWidth();
            if (viewWidth <= 1) {
                return 0.0f;
            }
            int middleX = viewWidth / 2;
            int distanceFromCenter = Math.abs(middleX - Math.min(Math.max(0, x), viewWidth));
            return (distanceFromCenter / middleX) * intensity;
        }

        public View getHologram() {
            return this.mStripSideHologram;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateItemHeight(int height) {
        this.mItemHeight = height;
        this.mFlyoverHeight = (this.mFlyoverInsetTop * 2) + height;
        this.mFlyoverMeasureHeight = View.MeasureSpec.makeMeasureSpec(this.mFlyoverHeight, 1073741824);
    }

    public PromoStrip(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PromoStrip(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        this.mVirtualItemWidth = 0;
        this.mItemWidth = 0;
        this.mItemHeight = 0;
        this.mItems = new PromoItemView[0];
        this.mAutoAdvanceRunnable = new Runnable() { // from class: com.google.android.finsky.layout.PromoStrip.1
            @Override // java.lang.Runnable
            public void run() {
                float totalDistance = PromoStrip.this.mItemWidth;
                float absVelocity = (float) Math.sqrt(Math.abs(2.0f * PromoStrip.this.mDeceleration * totalDistance));
                float duration = absVelocity / PromoStrip.this.mDeceleration;
                PromoStrip.this.runScrollAnimation(-absVelocity, duration);
            }
        };
        this.mAutoAdvanceHandler = new Handler();
        Resources resources = this.mContext.getResources();
        this.mVirtualItemWidth = (resources.getDimensionPixelSize(R.dimen.browse_item_width) * 2) - resources.getDimensionPixelSize(R.dimen.bucket_entry_right_margin);
        this.mFlyoverInsetLeft = resources.getDimensionPixelSize(R.dimen.promo_flyover_left_overlap);
        this.mFlyoverInsetTop = resources.getDimensionPixelSize(R.dimen.promo_flyover_top_overlap);
        updateItemHeight(0);
        updateItemWidth(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateItemWidth(int width) {
        this.mItemWidth = width;
        this.mFlyoverWidth = (this.mFlyoverInsetLeft * 2) + (this.mVirtualItemWidth - this.mItemWidth);
        this.mFlyoverMeasureWidth = View.MeasureSpec.makeMeasureSpec(this.mFlyoverWidth, 1073741824);
    }

    public void setAdapter(PromoAdapter adapter) {
        this.mAdapter = adapter;
    }

    public void setTapListener(PromoStripTapListener listener) {
        this.mOnChildViewTapListener = listener;
    }

    private void recycleBitmaps() {
        PromoItemView[] arr$ = this.mItems;
        for (PromoItemView item : arr$) {
            item.clearBitmaps();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelScrolling();
        this.mItems = new PromoItemView[0];
        this.mAdapter = null;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int height = b - t;
        int width = r - l;
        if (width > 0 && height > 0 && this.mItemWidth > 0 && this.mItemHeight > 0 && this.mItems.length > 0) {
            int virtualLeftEdgeTranslation = (width / 2) - (this.mVirtualItemWidth / 2);
            int virtualLeftEdgeFlyoverTranslation = ((width / 2) + (this.mVirtualItemWidth / 2)) - (this.mFlyoverWidth - this.mFlyoverInsetLeft);
            int virtualTotalFlyoverWidth = (int) (3.3f * this.mTotalChildrenWidth);
            int virtualFlyoverWidth = virtualTotalFlyoverWidth / this.mItems.length;
            int scrollPositionFlyover = (int) ((getScrollPosition() / this.mTotalChildrenWidth) * virtualTotalFlyoverWidth);
            int leftEdge = (int) getScrollPosition();
            int leftEdgeFlyover = scrollPositionFlyover;
            PromoItemView[] arr$ = this.mItems;
            for (PromoItemView item : arr$) {
                View promo = item.getPromo();
                View holograph = item.getHologram();
                View flyover = item.getFlyover();
                if (leftEdge + virtualLeftEdgeTranslation >= width) {
                    leftEdge = (int) (leftEdge - this.mTotalChildrenWidth);
                }
                if (leftEdgeFlyover + virtualLeftEdgeFlyoverTranslation >= width) {
                    leftEdgeFlyover -= virtualTotalFlyoverWidth;
                }
                holograph.layout((leftEdge + virtualLeftEdgeTranslation) - holograph.getMeasuredWidth(), t, leftEdge + virtualLeftEdgeTranslation, this.mItemHeight + t);
                promo.layout(leftEdge + virtualLeftEdgeTranslation, t, this.mItemWidth + leftEdge + virtualLeftEdgeTranslation, this.mItemHeight + t);
                flyover.layout(leftEdgeFlyover + virtualLeftEdgeFlyoverTranslation, t - this.mFlyoverInsetTop, leftEdgeFlyover + virtualLeftEdgeFlyoverTranslation + this.mFlyoverWidth, this.mFlyoverHeight + t);
                item.updateFade();
                leftEdge += this.mItemWidth;
                leftEdgeFlyover += virtualFlyoverWidth;
            }
        }
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mItemWidth <= 0 || this.mItemHeight <= 0 || this.mItems.length <= 0) {
            setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), View.MeasureSpec.getSize(heightMeasureSpec));
            return;
        }
        this.mTotalChildrenWidth = this.mItemWidth * this.mItems.length;
        setChildrenMeasurements();
        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), View.MeasureSpec.getSize(heightMeasureSpec));
    }

    private void setChildrenMeasurements() {
        PromoItemView[] arr$ = this.mItems;
        for (PromoItemView view : arr$) {
            view.getFlyover().measure(this.mFlyoverMeasureWidth, this.mFlyoverMeasureHeight);
            view.getHologram().measure(this.mItemWidth, this.mItemHeight);
        }
    }

    @Override // com.google.android.finsky.layout.DraggableHorizontalStrip
    protected Animator createScrollAnimation(float velocity, long scrollDurationMs) {
        AnimatorSet scrollAnimatorSet = new AnimatorSet();
        Animator scrolling = super.createScrollAnimation(velocity, scrollDurationMs);
        scrollAnimatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.finsky.layout.PromoStrip.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                PromoStrip.this.mAutoAdvanceScheduled = false;
                PromoStrip.this.scheduleAutoAdvance();
            }
        });
        scrollAnimatorSet.play(scrolling);
        return scrollAnimatorSet;
    }

    @Override // com.google.android.finsky.layout.DraggableHorizontalStrip
    protected float limitScrollPosition(float targetValue) {
        return clampToTotalStripWidth(targetValue);
    }

    @Override // com.google.android.finsky.layout.DraggableHorizontalStrip
    protected void cancelScrolling() {
        super.cancelScrolling();
        this.mAutoAdvanceHandler.removeCallbacks(this.mAutoAdvanceRunnable);
        this.mAutoAdvanceScheduled = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleAutoAdvance() {
        if (allImagesExist() && !this.mAutoAdvanceScheduled) {
            this.mAutoAdvanceScheduled = true;
            this.mAutoAdvanceHandler.postDelayed(this.mAutoAdvanceRunnable, 5000L);
        }
    }

    @Override // com.google.android.finsky.layout.DraggableHorizontalStrip, android.view.View
    public synchronized boolean onTouchEvent(MotionEvent event) {
        cancelScrolling();
        return super.onTouchEvent(event);
    }

    private boolean isXWithinView(View view, float x) {
        return view.getVisibility() == 0 && x >= ((float) view.getLeft()) && x < ((float) view.getRight());
    }

    private View findViewAtX(float x) {
        PromoItemView[] arr$ = this.mItems;
        for (PromoItemView item : arr$) {
            if (isXWithinView(item.getFlyover(), x)) {
                return item.getFlyover();
            }
        }
        PromoItemView[] arr$2 = this.mItems;
        for (PromoItemView item2 : arr$2) {
            if (isXWithinView(item2.getPromo(), x)) {
                return item2.getPromo();
            }
        }
        return null;
    }

    @Override // com.google.android.finsky.layout.DraggableHorizontalStrip
    protected float getLeftEdgeOfChildOnLeft(float clampToTotalStripWidth) {
        if (this.mItemWidth == 0) {
            return 0.0f;
        }
        return this.mItemWidth * (((int) clampToTotalStripWidth) / this.mItemWidth);
    }

    @Override // com.google.android.finsky.layout.DraggableHorizontalStrip
    protected float getLeftEdgeOfChildOnRight(float clampToTotalStripWidth) {
        return getLeftEdgeOfChildOnLeft(clampToTotalStripWidth) + this.mItemWidth;
    }

    public void resetStrip(int newTotalSize) {
        updateItemWidth(0);
        updateItemHeight(0);
        recycleBitmaps();
        removeAllViews();
        resyncItems(newTotalSize);
        invalidate();
    }

    public boolean allImagesExist() {
        PromoItemView[] arr$ = this.mItems;
        for (PromoItemView item : arr$) {
            if (item == null || !item.isReadyForDisplay()) {
                return false;
            }
        }
        return this.mItems.length > 0;
    }

    private void resyncItems(int size) {
        this.mItems = new PromoItemView[size];
        for (int i = 0; i < this.mItems.length; i++) {
            this.mItems[i] = new PromoItemView();
            setItem(i, this.mAdapter.getDocument(i), this.mAdapter.getFlyover(i), this.mAdapter.getPromo(i));
            addView(this.mItems[i].getPromo());
            addView(this.mItems[i].getFlyover());
            addView(this.mItems[i].getHologram());
        }
        PromoItemView[] arr$ = this.mItems;
        for (PromoItemView item : arr$) {
            item.getHologram().bringToFront();
        }
        PromoItemView[] arr$2 = this.mItems;
        for (PromoItemView item2 : arr$2) {
            item2.getFlyover().bringToFront();
        }
        scheduleAutoAdvance();
    }

    public void setItem(int index, Document document, Bitmap flyover, Bitmap promo) {
        this.mItems[index].setPromo(promo);
        this.mItems[index].setFlyoverIcon(flyover);
        this.mItems[index].setDocument(document);
        scheduleAutoAdvance();
        invalidate();
    }

    @Override // com.google.android.finsky.layout.DraggableHorizontalStrip
    protected boolean onTouchEventTriggeredTap(float x) {
        View tappedView = findViewAtX(x);
        if (tappedView == null || tappedView.getTag() == null || this.mOnChildViewTapListener == null) {
            return false;
        }
        this.mOnChildViewTapListener.onItemTap((Document) tappedView.getTag());
        return true;
    }
}
