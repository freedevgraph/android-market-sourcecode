package com.google.android.finsky.layout;

import android.content.res.Resources;
import android.view.View;
import com.google.android.finsky.R;
import com.google.android.finsky.utils.ThumbnailUtils;

/* JADX INFO: loaded from: classes.dex */
public class PromoStripManager {
    private final View mHolder;
    private final PromoStrip mPromo;

    public PromoStripManager(View promoStripView, Resources resources) {
        this.mPromo = (PromoStrip) promoStripView.findViewById(R.id.strip);
        this.mHolder = promoStripView;
        this.mHolder.setVisibility(4);
    }

    public void showWhenReady() {
        if (this.mHolder.getVisibility() != 0 && this.mPromo.allImagesExist()) {
            ThumbnailUtils.fadeInView(this.mHolder);
            this.mHolder.setVisibility(0);
        }
    }
}
