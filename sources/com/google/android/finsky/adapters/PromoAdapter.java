package com.google.android.finsky.adapters;

import android.graphics.Bitmap;
import com.google.android.finsky.api.model.DfeList;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.remoting.protos.DocList;
import com.google.android.finsky.utils.BitmapLoader;
import com.google.android.finsky.utils.ThumbnailUtils;

/* JADX INFO: loaded from: classes.dex */
public class PromoAdapter {
    private int mBannerHeight;
    private int mBannerWidth;
    private final BitmapLoader mBitmapLoader;
    private int mFlyoverHeight;
    private int mFlyoverWidth;
    private PromoItemWrapper[] mItems = new PromoItemWrapper[0];
    private final int mMaxImages;
    private ArrayObserver mObserver;

    private class PromoItemWrapper implements BitmapLoader.BitmapLoadedHandler {
        private int mCurrentPosition;
        private Document mDocument;
        private BitmapLoader.BitmapContainer mLogoContainer;
        private BitmapLoader.BitmapContainer mPromoContainer;

        public PromoItemWrapper(Document document) {
            this.mDocument = document;
            this.mLogoContainer = PromoAdapter.this.mBitmapLoader.get(ThumbnailUtils.getBitmapUrlFromDocument(document, 4), (Bitmap) null, this, PromoAdapter.this.mFlyoverWidth, PromoAdapter.this.mFlyoverHeight);
            this.mPromoContainer = PromoAdapter.this.mBitmapLoader.get(ThumbnailUtils.getBitmapUrlFromDocument(document, 2), (Bitmap) null, this, PromoAdapter.this.mBannerWidth, PromoAdapter.this.mBannerHeight);
        }

        public void destroy() {
            this.mLogoContainer.cancelRequest();
            this.mPromoContainer.cancelRequest();
            this.mLogoContainer = null;
            this.mPromoContainer = null;
        }

        public Document getDocument() {
            return this.mDocument;
        }

        public Bitmap getLogoBitmap() {
            return this.mLogoContainer.getBitmap();
        }

        public Bitmap getPromoBitmap() {
            return this.mPromoContainer.getBitmap();
        }

        public void setCurrentPosition(int newPosition) {
            this.mCurrentPosition = newPosition;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.android.volley.Response.Listener
        public void onResponse(BitmapLoader.BitmapContainer result) {
            PromoAdapter.this.mObserver.onItemChanged(this.mCurrentPosition);
        }
    }

    public PromoAdapter(BitmapLoader bitmapLoader, DfeList promoList, int maxBannerWidth, int maxBannerHeight, int maxFlyoverWidth, int maxFlyoverHeight, ArrayObserver observer, int maxImages) {
        this.mBannerWidth = 0;
        this.mBannerHeight = 0;
        this.mFlyoverWidth = 0;
        this.mFlyoverHeight = 0;
        this.mBitmapLoader = bitmapLoader;
        this.mBannerWidth = maxBannerWidth;
        this.mBannerHeight = maxBannerHeight;
        this.mFlyoverWidth = maxFlyoverWidth;
        this.mFlyoverHeight = maxFlyoverHeight;
        this.mObserver = observer;
        this.mMaxImages = maxImages;
        syncWrapperItems(promoList.getBucket(0));
    }

    public Bitmap getFlyover(int index) {
        return this.mItems[index].getLogoBitmap();
    }

    public Bitmap getPromo(int index) {
        return this.mItems[index].getPromoBitmap();
    }

    public Document getDocument(int index) {
        return this.mItems[index].getDocument();
    }

    private void syncWrapperItems(DocList.Bucket bucket) {
        PromoItemWrapper[] items = new PromoItemWrapper[Math.min(this.mMaxImages, bucket.getDocumentCount())];
        for (int i = 0; i < items.length; i++) {
            Document document = new Document(bucket.getDocument(i), bucket.getAnalyticsCookie());
            boolean reused = false;
            int j = 0;
            while (true) {
                if (j >= this.mItems.length) {
                    break;
                }
                if (this.mItems[j].getDocument() != document) {
                    j++;
                } else {
                    items[i] = this.mItems[j];
                    reused = true;
                    break;
                }
            }
            if (!reused) {
                PromoItemWrapper newWrapper = new PromoItemWrapper(document);
                items[i] = newWrapper;
            }
            items[i].setCurrentPosition(i);
        }
        this.mItems = items;
    }

    public void destroy() {
        destroyItems();
        this.mObserver = null;
    }

    private void destroyItems() {
        PromoItemWrapper[] arr$ = this.mItems;
        for (PromoItemWrapper item : arr$) {
            item.destroy();
        }
        this.mItems = null;
    }

    public int getCount() {
        return this.mItems.length;
    }
}
