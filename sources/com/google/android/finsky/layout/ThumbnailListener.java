package com.google.android.finsky.layout;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import com.google.android.finsky.utils.BitmapLoader;
import com.google.android.finsky.utils.ThumbnailUtils;

/* JADX INFO: loaded from: classes.dex */
public class ThumbnailListener implements BitmapLoader.BitmapLoadedHandler {
    private final boolean mFadeIn;
    protected final ImageView mImageView;
    private final View mViewToBeUpdated;

    public ThumbnailListener(ImageView imageView, boolean fadeIn) {
        this.mImageView = imageView;
        this.mViewToBeUpdated = null;
        this.mFadeIn = fadeIn;
    }

    public ThumbnailListener(ImageView imageView, View viewToBeUpdated, boolean fadeIn) {
        this.mImageView = imageView;
        this.mViewToBeUpdated = viewToBeUpdated;
        this.mFadeIn = fadeIn;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.volley.Response.Listener
    public void onResponse(BitmapLoader.BitmapContainer bitmapContainer) {
        Bitmap response = bitmapContainer.getBitmap();
        if (response == null) {
            onImageFailed();
            return;
        }
        if (this.mViewToBeUpdated != null) {
            this.mImageView.getLayoutParams().width = -2;
            this.mImageView.getLayoutParams().height = -2;
            this.mViewToBeUpdated.getLayoutParams().width = response.getWidth() + this.mImageView.getPaddingLeft() + this.mImageView.getPaddingRight();
        }
        if (this.mFadeIn) {
            ThumbnailUtils.setImageBitmapWithFade(this.mImageView, response);
        } else {
            this.mImageView.setImageBitmap(response);
        }
    }

    protected void onImageFailed() {
    }
}
