package com.google.android.finsky.layout;

import android.widget.ImageView;

/* JADX INFO: loaded from: classes.dex */
public class HidingThumbnailListener extends ThumbnailListener {
    public HidingThumbnailListener(ImageView imageView, boolean fadeIn) {
        super(imageView, fadeIn);
    }

    @Override // com.google.android.finsky.layout.ThumbnailListener
    public void onImageFailed() {
        this.mImageView.setVisibility(8);
    }
}
