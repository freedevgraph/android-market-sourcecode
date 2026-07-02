package com.google.android.finsky.adapters;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.remoting.protos.Doc;

/* JADX INFO: loaded from: classes.dex */
public class ImageStripAdapter {
    private final DataSetObservable mDataSetObservable = new DataSetObservable();
    private final String[] mIds;
    private final int mImageCount;
    private final Doc.Image.Dimension[] mImageDimensions;
    private final Drawable[] mImages;
    private final Document[] mTags;

    public ImageStripAdapter(int imageCount) {
        this.mImageCount = imageCount;
        this.mIds = new String[this.mImageCount];
        this.mImages = new Drawable[this.mImageCount];
        this.mImageDimensions = new Doc.Image.Dimension[this.mImageCount];
        this.mTags = new Document[this.mImageCount];
    }

    public void setDimensionAt(int index, Doc.Image.Dimension dim) {
        this.mImageDimensions[index] = dim;
    }

    public void setImageAt(int index, Drawable drawable) {
        this.mImages[index] = drawable;
        notifyDataSetChanged();
    }

    public int getImageCount() {
        return this.mImageCount;
    }

    public Drawable getImageAt(int index) {
        return this.mImages[index];
    }

    public Document getTagAt(int index) {
        return this.mTags[index];
    }

    public void getDimensionAt(int index, Doc.Image.Dimension dimension) {
        Drawable drawable = getImageAt(index);
        if (drawable != null) {
            dimension.setWidth(drawable.getIntrinsicWidth());
            dimension.setHeight(drawable.getIntrinsicHeight());
        } else if (this.mImageDimensions[index] != null) {
            dimension.setWidth(this.mImageDimensions[index].getWidth());
            dimension.setHeight(this.mImageDimensions[index].getHeight());
        } else {
            dimension.setWidth(0);
            dimension.setHeight(0);
        }
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        this.mDataSetObservable.registerObserver(observer);
    }

    public void unregisterAll() {
        this.mDataSetObservable.unregisterAll();
    }

    public void notifyDataSetChanged() {
        this.mDataSetObservable.notifyChanged();
    }
}
