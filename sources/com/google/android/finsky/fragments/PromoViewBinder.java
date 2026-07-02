package com.google.android.finsky.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.android.finsky.R;
import com.google.android.finsky.adapters.ArrayObserver;
import com.google.android.finsky.adapters.PromoAdapter;
import com.google.android.finsky.api.model.DfeList;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.layout.PromoStrip;
import com.google.android.finsky.layout.PromoStripManager;
import com.google.android.finsky.model.Bucket;
import com.google.android.finsky.navigationmanager.NavigationManager;
import com.google.android.finsky.utils.BitmapLoader;
import com.google.android.finsky.utils.CorpusMetadata;
import com.google.android.finsky.utils.PurchaseButtonWrapper;

/* JADX INFO: loaded from: classes.dex */
public class PromoViewBinder implements ArrayObserver {
    private PromoAdapter mAdapter;
    private BitmapLoader mBitmapLoader;
    private Context mContext;
    private DfeList mData;
    private NavigationManager mNavigationManager;
    private PromoStripManager mPromoStripManager;
    private PromoStrip mStrip;

    public void init(Context context, NavigationManager navManager, BitmapLoader bitmapLoader) {
        if (this.mContext != context) {
            this.mContext = context;
            this.mNavigationManager = navManager;
            this.mBitmapLoader = bitmapLoader;
            this.mData = null;
        }
    }

    public void setData(DfeList promoList) {
        this.mData = promoList;
    }

    private boolean hasPromos() {
        return this.mData != null && this.mData.getBucketCount() > 0 && this.mData.getBucket(0).getDocumentCount() >= 4;
    }

    public void bind(ViewGroup promoView) {
        if (hasPromos()) {
            this.mPromoStripManager = new PromoStripManager(promoView, this.mContext.getResources());
            setupStrip(promoView);
            if (this.mAdapter != null) {
                this.mAdapter.destroy();
            }
            Resources res = this.mContext.getResources();
            this.mAdapter = new PromoAdapter(this.mBitmapLoader, this.mData, 0, res.getDimensionPixelSize(R.dimen.promo_item_height), res.getDimensionPixelSize(R.dimen.promo_flyover_contents_width), res.getDimensionPixelSize(R.dimen.promo_flyover_contents_height), this, 10);
            this.mStrip.setAdapter(this.mAdapter);
            this.mStrip.resetStrip(this.mAdapter.getCount());
            ImageView promoShadowView = (ImageView) promoView.findViewById(R.id.strip_shadow);
            Bucket firstBucket = new Bucket(this.mData.getBucket(0));
            promoShadowView.setBackgroundResource(CorpusMetadata.getPromoShadowResource(this.mContext, firstBucket.getBackend()));
            this.mPromoStripManager.showWhenReady();
        }
    }

    private void setupStrip(ViewGroup view) {
        this.mStrip = (PromoStrip) view.findViewById(R.id.strip);
        this.mStrip.setTapListener(new PromoStrip.PromoStripTapListener() { // from class: com.google.android.finsky.fragments.PromoViewBinder.1
            @Override // com.google.android.finsky.layout.PromoStrip.PromoStripTapListener
            public void onItemTap(Document item) {
                PromoViewBinder.this.mNavigationManager.showDetails(item.getDetailsUrl());
            }

            @Override // com.google.android.finsky.layout.PromoStrip.PromoStripTapListener
            public void onPriceTap(PurchaseButtonWrapper button) {
                button.performDefaultAction(PromoViewBinder.this.mNavigationManager);
            }
        });
    }

    public void onDestroyView() {
        if (this.mAdapter != null) {
            this.mAdapter.destroy();
            this.mAdapter = null;
        }
        this.mStrip = null;
        this.mPromoStripManager = null;
    }

    @Override // com.google.android.finsky.adapters.ArrayObserver
    public void onItemChanged(int index) {
        if (this.mStrip != null) {
            this.mStrip.setItem(index, this.mAdapter.getDocument(index), this.mAdapter.getFlyover(index), this.mAdapter.getPromo(index));
            this.mPromoStripManager.showWhenReady();
        }
    }
}
