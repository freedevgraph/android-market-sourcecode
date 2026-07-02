package com.google.android.finsky.layout;

import android.os.Handler;
import android.widget.ImageView;
import com.google.android.finsky.utils.BitmapLoader;

/* JADX INFO: loaded from: classes.dex */
public class MinTimeThumbnailListener extends HidingThumbnailListener {
    private long mEndTime;
    private final Handler mHandler;
    private final long mMinTime;
    private long mStartTime;

    public MinTimeThumbnailListener(ImageView imageView, boolean fadeIn, long minTime) {
        super(imageView, fadeIn);
        this.mMinTime = minTime;
        this.mHandler = new Handler();
        this.mStartTime = System.currentTimeMillis();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.android.finsky.layout.ThumbnailListener, com.android.volley.Response.Listener
    public void onResponse(final BitmapLoader.BitmapContainer bitmapContainer) {
        this.mEndTime = System.currentTimeMillis();
        long delay = this.mMinTime - (this.mEndTime - this.mStartTime);
        if (delay <= 0) {
            super.onResponse(bitmapContainer);
        } else {
            this.mHandler.postDelayed(new Runnable() { // from class: com.google.android.finsky.layout.MinTimeThumbnailListener.1
                @Override // java.lang.Runnable
                public void run() {
                    MinTimeThumbnailListener.super.onResponse(bitmapContainer);
                }
            }, delay);
        }
    }
}
