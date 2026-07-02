package com.google.android.finsky.utils;

import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.widget.ImageView;
import com.google.android.finsky.R;
import com.google.android.finsky.api.model.Document;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class ThumbnailUtils {
    private static HashMap<Integer, Bitmap> sDefaultIcons;

    public static Bitmap getDefaultIcon(int channelId, Resources resources) {
        if (sDefaultIcons == null) {
            sDefaultIcons = new HashMap<>();
            resources.getDrawable(R.drawable.ic_vm_thumbnail_big);
            sDefaultIcons.put(3, BitmapFactory.decodeResource(resources, R.drawable.ic_vm_thumbnail_big_apps));
            sDefaultIcons.put(1, BitmapFactory.decodeResource(resources, R.drawable.ic_vm_thumbnail_big_books));
            sDefaultIcons.put(2, BitmapFactory.decodeResource(resources, R.drawable.ic_vm_thumbnail_big));
            sDefaultIcons.put(4, BitmapFactory.decodeResource(resources, R.drawable.ic_vm_thumbnail_big_movies));
        }
        return sDefaultIcons.get(Integer.valueOf(channelId));
    }

    public static String getBitmapUrlFromDocument(Document document) {
        String url = document.getFirstImageUrl(4);
        if (url == null) {
            return document.getFirstImageUrl(0);
        }
        return url;
    }

    public static String getBitmapUrlFromDocument(Document document, int imageType) {
        return document.getFirstImageUrl(imageType);
    }

    public static void setImageBitmapWithFade(ImageView imageView, Bitmap bitmap) {
        setImageDrawableWithFade(imageView, new BitmapDrawable(bitmap));
    }

    public static void setImageDrawableWithFade(ImageView imageView, Drawable drawable) {
        Drawable oldDrawable = imageView.getDrawable();
        if (oldDrawable != null) {
            TransitionDrawable newDrawable = new TransitionDrawable(new Drawable[]{oldDrawable, drawable});
            newDrawable.setCrossFadeEnabled(true);
            imageView.setImageDrawable(newDrawable);
            newDrawable.startTransition(250);
            return;
        }
        imageView.setImageDrawable(drawable);
        fadeInView(imageView);
    }

    public static void fadeInView(View view) {
        ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f).setDuration(250L).start();
    }
}
