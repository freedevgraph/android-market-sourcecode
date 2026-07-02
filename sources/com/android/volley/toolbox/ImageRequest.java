package com.android.volley.toolbox;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

/* JADX INFO: loaded from: classes.dex */
public class ImageRequest extends Request<Bitmap> {
    private final Bitmap.Config mDecodeConfig;
    private final Response.Listener<Bitmap> mListener;
    private final int mMaxHeight;
    private final int mMaxWidth;

    public ImageRequest(String url, Response.Listener<Bitmap> listener, int maxWidth, int maxHeight, Bitmap.Config decodeConfig, Response.ErrorListener errorListener) {
        super(url, errorListener);
        this.mListener = listener;
        this.mDecodeConfig = decodeConfig;
        this.mMaxWidth = maxWidth;
        this.mMaxHeight = maxHeight;
    }

    @Override // com.android.volley.Request
    public Request.Priority getPriority() {
        return Request.Priority.LOW;
    }

    private static int getResizedWidth(int maxWidth, int maxHeight, int actualWidth, int actualHeight) {
        if (maxWidth == 0 && maxHeight == 0) {
            return actualWidth;
        }
        if (maxWidth == 0) {
            return (int) (((double) actualWidth) * (((double) maxHeight) / ((double) actualHeight)));
        }
        if (maxHeight == 0) {
            return maxWidth;
        }
        double ratio = ((double) actualHeight) / ((double) actualWidth);
        int resized = maxWidth;
        if (((double) resized) * ratio > maxHeight) {
            resized = (int) (((double) maxHeight) / ratio);
        }
        return resized;
    }

    private static int getResizedHeight(int maxWidth, int maxHeight, int actualWidth, int actualHeight) {
        if (maxWidth == 0 && maxHeight == 0) {
            return actualHeight;
        }
        if (maxHeight == 0) {
            return (int) (((double) actualHeight) * (((double) maxWidth) / ((double) actualWidth)));
        }
        if (maxWidth == 0) {
            return maxHeight;
        }
        double ratio = ((double) actualWidth) / ((double) actualHeight);
        int resized = maxHeight;
        if (((double) resized) * ratio > maxWidth) {
            resized = (int) (((double) maxWidth) / ratio);
        }
        return resized;
    }

    @Override // com.android.volley.Request
    protected Response<Bitmap> parseNetworkResponse(NetworkResponse response) {
        Bitmap bitmap;
        byte[] data = response.data;
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        if (this.mMaxWidth == 0 && this.mMaxHeight == 0) {
            decodeOptions.inPreferredConfig = this.mDecodeConfig;
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
        } else {
            decodeOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
            int actualWidth = decodeOptions.outWidth;
            int actualHeight = decodeOptions.outHeight;
            int desiredWidth = getResizedWidth(this.mMaxWidth, this.mMaxHeight, actualWidth, actualHeight);
            int desiredHeight = getResizedHeight(this.mMaxWidth, this.mMaxHeight, actualWidth, actualHeight);
            decodeOptions.inJustDecodeBounds = false;
            decodeOptions.inSampleSize = findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight);
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
            if (bitmap != null && (bitmap.getWidth() != desiredWidth || bitmap.getHeight() != desiredHeight)) {
                bitmap = Bitmap.createScaledBitmap(bitmap, desiredWidth, desiredHeight, true);
            }
        }
        if (bitmap == null) {
            return Response.error(Response.ErrorCode.SERVER, null);
        }
        return Response.success(bitmap, HttpHeaderParser.parseCacheHeaders(response));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.volley.Request
    public void deliverResponse(Bitmap response) {
        this.mListener.onResponse(response);
    }

    static int findBestSampleSize(int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = ((double) actualWidth) / ((double) desiredWidth);
        double hr = ((double) actualHeight) / ((double) desiredHeight);
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while (n * 2.0f <= ratio) {
            n *= 2.0f;
        }
        return (int) n;
    }
}
