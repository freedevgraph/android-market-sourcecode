package com.google.android.finsky.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.google.android.finsky.activities.PurchaseActivity;

/* JADX INFO: loaded from: classes.dex */
public class WebViewLayout extends FrameLayout {
    public WebViewLayout(Context context) {
        super(context);
    }

    public WebViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebViewLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int newHeightMeasureSpec = heightMeasureSpec;
        if (View.MeasureSpec.getMode(heightMeasureSpec) == Integer.MIN_VALUE) {
            int measureHeight = View.MeasureSpec.getSize(heightMeasureSpec);
            newHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(Math.min(PurchaseActivity.MAX_WEB_VIEW_HEIGHT, measureHeight), 1073741824);
        }
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
    }
}
