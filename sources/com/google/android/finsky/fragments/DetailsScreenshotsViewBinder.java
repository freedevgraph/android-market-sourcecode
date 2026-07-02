package com.google.android.finsky.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import com.google.android.finsky.R;
import com.google.android.finsky.adapters.ImageStripAdapter;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.layout.HorizontalStrip;
import com.google.android.finsky.layout.LayoutSwitcher;
import com.google.android.finsky.remoting.protos.Doc;
import com.google.android.finsky.utils.BitmapLoader;
import com.google.android.finsky.utils.FinskyLog;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DetailsScreenshotsViewBinder extends DetailsViewBinder implements LayoutSwitcher.RetryButtonListener {
    private BitmapLoader mBitmapLoader;
    private HorizontalStrip mImageStrip;
    private ImageStripAdapter mImageStripAdapter;
    private int mNumImagesFailed;
    private List<Doc.Image> mPreviewImages;
    private int mScreenshotsRightPadding;
    private int mScreenshotsSpacing;
    private int mMaxImageHeight = 0;
    private int mLoadRequestCount = 0;

    static /* synthetic */ int access$104(DetailsScreenshotsViewBinder x0) {
        int i = x0.mNumImagesFailed + 1;
        x0.mNumImagesFailed = i;
        return i;
    }

    public void init(Context context, BitmapLoader bitmapLoader) {
        super.init(context, null, null);
        this.mBitmapLoader = bitmapLoader;
        Resources res = this.mContext.getResources();
        this.mScreenshotsSpacing = res.getDimensionPixelOffset(R.dimen.screenshots_spacing);
        this.mScreenshotsRightPadding = res.getDimensionPixelOffset(R.dimen.screenshots_right_padding);
        this.mMaxImageHeight = res.getDimensionPixelOffset(R.dimen.screenshots_max_height);
    }

    public void bind(View view, Document doc) {
        super.bind(view, doc, R.id.header, R.string.details_screenshots);
        this.mPreviewImages = this.mDoc.hasScreenshots() ? this.mDoc.getImages(1) : null;
        if (this.mPreviewImages == null) {
            this.mLayout.setVisibility(8);
            return;
        }
        this.mLayout.setVisibility(0);
        this.mImageStrip = (HorizontalStrip) this.mLayout.findViewById(R.id.strip);
        this.mImageStrip.setBackgroundColor(-1);
        this.mImageStrip.setLayoutMargin(this.mScreenshotsSpacing);
        if (this.mImageStripAdapter != null) {
            this.mImageStripAdapter.unregisterAll();
        }
        this.mImageStripAdapter = new ImageStripAdapter(this.mPreviewImages.size() + 2);
        this.mImageStrip.setAdapter(this.mImageStripAdapter);
        this.mImageStripAdapter.setDimensionAt(this.mPreviewImages.size() + 1, new Doc.Image.Dimension().setWidth(this.mScreenshotsRightPadding));
        LayoutSwitcher layoutSwitcher = new LayoutSwitcher(this.mLayout, R.id.strip, this);
        setLayoutSwitcher(layoutSwitcher);
        layoutSwitcher.switchToDataMode();
        loadImages();
    }

    private void loadImages() {
        int currIndex = 1;
        final int loadId = this.mLoadRequestCount + 1;
        this.mLoadRequestCount = loadId;
        final int numImagesToLoad = this.mPreviewImages.size();
        this.mNumImagesFailed = 0;
        for (Doc.Image img : this.mPreviewImages) {
            this.mImageStripAdapter.setDimensionAt(currIndex, img.getDimension());
            final int index = currIndex;
            BitmapLoader.BitmapContainer bitmapContainer = this.mBitmapLoader.get(img.getImageUrl(), (Bitmap) null, new BitmapLoader.BitmapLoadedHandler() { // from class: com.google.android.finsky.fragments.DetailsScreenshotsViewBinder.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.android.volley.Response.Listener
                public void onResponse(BitmapLoader.BitmapContainer bitmapContainer2) {
                    if (DetailsScreenshotsViewBinder.this.mLoadRequestCount != loadId) {
                        FinskyLog.w("Expected response for load %s but got %s", Integer.valueOf(DetailsScreenshotsViewBinder.this.mLoadRequestCount), Integer.valueOf(loadId));
                        return;
                    }
                    Bitmap bitmap = bitmapContainer2.getBitmap();
                    if (bitmap != null) {
                        DetailsScreenshotsViewBinder.this.mImageStripAdapter.setImageAt(index, new BitmapDrawable(bitmap));
                    } else if (DetailsScreenshotsViewBinder.access$104(DetailsScreenshotsViewBinder.this) == numImagesToLoad) {
                        DetailsScreenshotsViewBinder.this.getLayoutSwitcher().switchToErrorMode(null);
                    }
                }
            }, 0, this.mMaxImageHeight);
            if (bitmapContainer.getBitmap() != null) {
                this.mImageStripAdapter.setImageAt(index, new BitmapDrawable(bitmapContainer.getBitmap()));
            }
            currIndex++;
        }
    }

    @Override // com.google.android.finsky.layout.LayoutSwitcher.RetryButtonListener
    public void onRetry() {
        loadImages();
    }
}
