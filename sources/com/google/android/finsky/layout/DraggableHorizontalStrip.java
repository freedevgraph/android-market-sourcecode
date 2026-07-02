package com.google.android.finsky.layout;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

/* JADX INFO: loaded from: classes.dex */
public abstract class DraggableHorizontalStrip extends ViewGroup {
    protected Context mContext;
    protected final float mDeceleration;
    private float mDistanceScrolledSinceLastDown;
    private float mLastMotionX;
    private float mLastMotionY;
    protected float mOriginalPixelOffsetOfFirstChild;
    private float mPixelOffsetOfFirstChild;
    private Animator mScrollAnimation;
    private final int mScrollThreshold;
    protected float mTotalChildrenWidth;
    private VelocityTracker mVelocityTracker;

    abstract float getLeftEdgeOfChildOnLeft(float f);

    abstract float getLeftEdgeOfChildOnRight(float f);

    public DraggableHorizontalStrip(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet);
        this.mPixelOffsetOfFirstChild = 0.0f;
        this.mOriginalPixelOffsetOfFirstChild = 0.0f;
        this.mContext = context;
        this.mScrollThreshold = ViewConfiguration.get(context).getScaledTouchSlop();
        float ppi = context.getResources().getDisplayMetrics().density * 160.0f;
        this.mDeceleration = 1158.2634f * ppi * ViewConfiguration.getScrollFriction();
    }

    protected Animator createScrollAnimation(final float velocity, long scrollDurationMs) {
        this.mOriginalPixelOffsetOfFirstChild = this.mPixelOffsetOfFirstChild;
        ValueAnimator scrolling = ValueAnimator.ofFloat(0.0f, scrollDurationMs).setDuration(scrollDurationMs);
        scrolling.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.finsky.layout.DraggableHorizontalStrip.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                float timePassed = ((Float) animation.getAnimatedValue()).floatValue() / 1000.0f;
                float travelled = (Math.abs(velocity) * timePassed) - (((DraggableHorizontalStrip.this.mDeceleration * timePassed) * timePassed) / 2.0f);
                if (velocity < 0.0f) {
                    travelled = -travelled;
                }
                DraggableHorizontalStrip.this.updateFirstChildOffset(DraggableHorizontalStrip.this.mOriginalPixelOffsetOfFirstChild + travelled);
                DraggableHorizontalStrip.this.requestLayout();
            }
        });
        scrolling.setInterpolator(new LinearInterpolator());
        return scrolling;
    }

    protected float clampToTotalStripWidth(float value) {
        if (this.mTotalChildrenWidth == 0.0f) {
            return value;
        }
        while (value < 0.0f) {
            value += this.mTotalChildrenWidth;
        }
        while (value >= this.mTotalChildrenWidth) {
            value -= this.mTotalChildrenWidth;
        }
        return value;
    }

    private void onTouchEventDown(float x, float y) {
        this.mLastMotionX = x;
        this.mLastMotionY = y;
        this.mDistanceScrolledSinceLastDown = 0.0f;
    }

    private void onTouchEventMove(float x, float y) {
        float scrollX = this.mLastMotionX - x;
        float scrollY = this.mLastMotionY - y;
        this.mLastMotionX = x;
        this.mLastMotionY = y;
        float distTravelledInPixels = (float) Math.sqrt((scrollX * scrollX) + (scrollY * scrollY));
        this.mDistanceScrolledSinceLastDown += distTravelledInPixels;
        if (distTravelledInPixels > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
            requestDisallowInterceptTouchEvent(true);
        }
        if (this.mTotalChildrenWidth > getWidth()) {
            updateFirstChildOffset(this.mPixelOffsetOfFirstChild - scrollX);
            requestLayout();
        }
    }

    private void onTouchEventUp(float x, float velocity) {
        float velocity2;
        float absVelocity = Math.abs(velocity);
        if (absVelocity > ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity() && this.mTotalChildrenWidth > getWidth()) {
            float duration = absVelocity / this.mDeceleration;
            float totalDistance = (absVelocity * duration) - (((this.mDeceleration * duration) * duration) / 2.0f);
            boolean movingToLeft = velocity < 0.0f;
            float offsetAtStop = clampToTotalStripWidth(this.mPixelOffsetOfFirstChild + (movingToLeft ? -totalDistance : totalDistance));
            if (movingToLeft) {
                float target = getLeftEdgeOfChildOnRight(clampToTotalStripWidth(this.mTotalChildrenWidth - offsetAtStop));
                velocity2 = -((float) Math.sqrt(2.0f * this.mDeceleration * (totalDistance + (target - clampToTotalStripWidth(this.mTotalChildrenWidth - offsetAtStop)))));
            } else {
                float target2 = getLeftEdgeOfChildOnLeft(clampToTotalStripWidth(this.mTotalChildrenWidth - offsetAtStop));
                velocity2 = (float) Math.sqrt(2.0f * this.mDeceleration * (totalDistance + (clampToTotalStripWidth(this.mTotalChildrenWidth - offsetAtStop) - target2)));
            }
            runScrollAnimation(velocity2, Math.abs(velocity2) / this.mDeceleration);
            return;
        }
        if (this.mDistanceScrolledSinceLastDown <= this.mScrollThreshold && onTouchEventTriggeredTap(x)) {
            this.mPixelOffsetOfFirstChild = 0.0f;
            this.mOriginalPixelOffsetOfFirstChild = 0.0f;
            return;
        }
        if (this.mTotalChildrenWidth > getWidth()) {
            float currPos = clampToTotalStripWidth(this.mTotalChildrenWidth - this.mPixelOffsetOfFirstChild);
            float totalDistance2 = 0.0f;
            int currLeft = 0;
            int i = 0;
            while (true) {
                if (i >= getChildCount()) {
                    break;
                }
                View childView = getChildAt(i);
                int currRight = currLeft + childView.getWidth();
                if (currRight >= currPos) {
                    float distToLeftEdge = currPos - currLeft;
                    float distToRightEdge = currRight - currPos;
                    totalDistance2 = distToRightEdge > distToLeftEdge ? distToLeftEdge : -distToRightEdge;
                } else {
                    currLeft = currRight;
                    i++;
                }
            }
            float absVelocity2 = (float) Math.sqrt(Math.abs(2.0f * this.mDeceleration * totalDistance2));
            runScrollAnimation(totalDistance2 < 0.0f ? -absVelocity2 : absVelocity2, absVelocity2 / this.mDeceleration);
        }
    }

    @Override // android.view.View
    public synchronized boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        float x = event.getX();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case 0:
                onTouchEventDown(x, y);
                break;
            case 1:
                this.mVelocityTracker.computeCurrentVelocity(1000, 1250.0f);
                onTouchEventUp(x, this.mVelocityTracker.getXVelocity());
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
                break;
            case 2:
                onTouchEventMove(x, y);
                break;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFirstChildOffset(float targetValue) {
        this.mPixelOffsetOfFirstChild = limitScrollPosition(targetValue);
    }

    protected void runScrollAnimation(float velocity, float duration) {
        this.mScrollAnimation = createScrollAnimation(velocity, (long) Math.abs(1000.0f * duration));
        this.mScrollAnimation.start();
    }

    protected void cancelScrolling() {
        if (this.mScrollAnimation != null && this.mScrollAnimation.isRunning()) {
            this.mScrollAnimation.cancel();
        }
    }

    protected float getScrollPosition() {
        return this.mPixelOffsetOfFirstChild;
    }

    protected boolean onTouchEventTriggeredTap(float x) {
        return false;
    }

    protected float limitScrollPosition(float targetValue) {
        if (targetValue > 0.0f) {
            targetValue = 0.0f;
        }
        int maxFromRight = (int) (this.mTotalChildrenWidth - getWidth());
        if ((-targetValue) > maxFromRight) {
            return -maxFromRight;
        }
        return targetValue;
    }
}
