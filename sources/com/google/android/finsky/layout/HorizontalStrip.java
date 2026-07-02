package com.google.android.finsky.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.google.android.finsky.R;
import com.google.android.finsky.adapters.ImageStripAdapter;
import com.google.android.finsky.remoting.protos.Doc;
import com.google.android.finsky.utils.ThumbnailUtils;

/* JADX INFO: loaded from: classes.dex */
public class HorizontalStrip extends DraggableHorizontalStrip {
    private ImageStripAdapter mAdapter;
    private final Doc.Image.Dimension mDimension;
    protected int mLayoutMargin;
    private final int mMaxHeight;

    public HorizontalStrip(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public HorizontalStrip(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.HorizontalStrip, defStyle, 0);
        this.mMaxHeight = a.getDimensionPixelSize(0, Integer.MAX_VALUE);
        a.recycle();
        this.mDimension = new Doc.Image.Dimension();
    }

    public void setAdapter(ImageStripAdapter adapter) {
        this.mAdapter = adapter;
        this.mAdapter.registerDataSetObserver(new DataSetObserver() { // from class: com.google.android.finsky.layout.HorizontalStrip.1
            @Override // android.database.DataSetObserver
            public void onChanged() {
                HorizontalStrip.this.syncChildViews();
            }

            @Override // android.database.DataSetObserver
            public void onInvalidated() {
                HorizontalStrip.this.recreateChildViews();
            }
        });
        recreateChildViews();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recreateChildViews() {
        removeAllViews();
        for (int i = 0; i < this.mAdapter.getImageCount(); i++) {
            ImageView childView = new ImageView(getContext());
            childView.setScaleType(ImageView.ScaleType.FIT_XY);
            childView.setBackgroundResource(R.drawable.background_shadow_gray_filled);
            addView(childView);
        }
        syncChildViews();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncChildViews() {
        boolean hasAllImages = true;
        for (int i = 0; i < this.mAdapter.getImageCount(); i++) {
            ImageView childView = (ImageView) getChildAt(i);
            Drawable drawable = this.mAdapter.getImageAt(i);
            if (drawable != null) {
                ThumbnailUtils.setImageDrawableWithFade(childView, drawable);
            } else {
                hasAllImages = false;
            }
            childView.setTag(this.mAdapter.getTagAt(i));
        }
        if (hasAllImages) {
            requestLayout();
        }
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = View.MeasureSpec.getMode(heightMeasureSpec);
        if (mode == 1073741824) {
            setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), View.MeasureSpec.getSize(heightMeasureSpec));
            return;
        }
        int maxChildHeight = 0;
        int i = 0;
        int len = getChildCount();
        while (true) {
            if (i >= len) {
                break;
            }
            int childHeight = getChildHeight(i);
            if (maxChildHeight < childHeight) {
                maxChildHeight = childHeight;
            }
            if (maxChildHeight <= this.mMaxHeight) {
                i++;
            } else {
                maxChildHeight = this.mMaxHeight;
                break;
            }
        }
        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), maxChildHeight);
    }

    public void setLayoutMargin(int layoutMargin) {
        this.mLayoutMargin = layoutMargin;
    }

    private int getChildWidth(int childIndex) {
        this.mAdapter.getDimensionAt(childIndex, this.mDimension);
        return this.mDimension.getWidth();
    }

    private int getChildHeight(int childIndex) {
        this.mAdapter.getDimensionAt(childIndex, this.mDimension);
        return this.mDimension.getHeight();
    }

    private float computeTotalImagesWidth(int stripHeight) {
        float childrenWidth = 0.0f;
        for (int i = 0; i < getChildCount(); i++) {
            int childWidth = getChildWidth(i);
            int childHeight = getChildHeight(i);
            float scalingFactor = stripHeight / childHeight;
            if (scalingFactor < 1.0d) {
                childWidth = (int) (childWidth * scalingFactor);
            }
            childrenWidth += childWidth;
        }
        if (getChildCount() > 0) {
            return (childrenWidth + (getChildCount() * this.mLayoutMargin)) - this.mLayoutMargin;
        }
        return childrenWidth;
    }

    private void layoutImages(int childLeft, int childTop, int stripWidth, int stripHeight) {
        int x = (int) (childLeft + getScrollPosition());
        for (int i = 0; i < getChildCount(); i++) {
            ImageView child = (ImageView) getChildAt(i);
            int childWidth = getChildWidth(i);
            int childHeight = getChildHeight(i);
            float scalingFactor = stripHeight / childHeight;
            if (scalingFactor < 1.0d) {
                childWidth = (int) (childWidth * scalingFactor);
                childHeight = stripHeight;
            }
            child.layout(x - 0, ((stripHeight - childHeight) / 2) + childTop, (x + childWidth) - 0, ((stripHeight + childHeight) / 2) + childTop);
            x = x + childWidth + this.mLayoutMargin;
            if (x - 0 >= stripWidth) {
                x = (int) (x - this.mTotalChildrenWidth);
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int h = b - t;
        int w = r - l;
        this.mTotalChildrenWidth = computeTotalImagesWidth(h);
        layoutImages(getPaddingLeft(), getPaddingTop(), w, h);
    }

    @Override // com.google.android.finsky.layout.DraggableHorizontalStrip
    protected float getLeftEdgeOfChildOnLeft(float xOffset) {
        int result = 0;
        int currLeft = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            currLeft += childView.getWidth() + this.mLayoutMargin;
            if (currLeft > xOffset) {
                return result;
            }
            result = currLeft;
        }
        return result;
    }

    @Override // com.google.android.finsky.layout.DraggableHorizontalStrip
    protected float getLeftEdgeOfChildOnRight(float xOffset) {
        int result = 0;
        int currLeft = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            currLeft += childView.getWidth() + this.mLayoutMargin;
            result = currLeft;
            if (currLeft > xOffset) {
                return result;
            }
        }
        return result;
    }

    @Override // android.view.View
    protected float getLeftFadingEdgeStrength() {
        if (getScrollPosition() >= 0.0f) {
            return 0.0f;
        }
        float pixelsBeyondLeft = (-1.0f) * getScrollPosition();
        int fadeLength = getHorizontalFadingEdgeLength();
        if (pixelsBeyondLeft > fadeLength) {
            return 1.0f;
        }
        return pixelsBeyondLeft / fadeLength;
    }

    @Override // android.view.View
    protected float getRightFadingEdgeStrength() {
        float pixelsBeyondRight = (getScrollPosition() + this.mTotalChildrenWidth) - getWidth();
        if (pixelsBeyondRight <= 0.0f) {
            return 0.0f;
        }
        int fadeLength = getHorizontalFadingEdgeLength();
        if (pixelsBeyondRight > fadeLength) {
            return 1.0f;
        }
        return pixelsBeyondRight / fadeLength;
    }
}
