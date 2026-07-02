package com.google.android.finsky.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import com.google.android.finsky.R;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.layout.MinTimeThumbnailListener;
import com.google.android.finsky.utils.BitmapLoader;

/* JADX INFO: loaded from: classes.dex */
public class DetailsPromoViewBinder extends DetailsViewBinder {
    private BitmapLoader mBitmapLoader;
    private int mPromoHeight;
    private int mPromoWidth;

    public void init(Context context, BitmapLoader bitmapLoader) {
        super.init(context, null, null);
        this.mBitmapLoader = bitmapLoader;
        Resources res = context.getResources();
        this.mPromoWidth = res.getDimensionPixelOffset(R.dimen.promo_width);
        this.mPromoHeight = res.getDimensionPixelOffset(R.dimen.promo_height);
    }

    public void bind(View view, Document doc) {
        super.bind(view, doc, -1, -1);
        String imageUrl = doc.getFirstImageUrl(2);
        ImageView imageView = (ImageView) this.mLayout.findViewById(R.id.hero_art);
        if (imageUrl == null) {
            imageView.setVisibility(8);
        } else {
            imageView.setVisibility(0);
            this.mBitmapLoader.getOrBindImmediately(imageUrl, imageView, new MinTimeThumbnailListener(imageView, true, 300L), this.mPromoWidth, this.mPromoHeight);
        }
    }
}
