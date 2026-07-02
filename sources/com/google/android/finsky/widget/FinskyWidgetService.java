package com.google.android.finsky.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.android.volley.Response;
import com.google.android.finsky.FinskyApp;
import com.google.android.finsky.R;
import com.google.android.finsky.activities.AuthenticatedActivity;
import com.google.android.finsky.analytics.Analytics;
import com.google.android.finsky.api.DfeApi;
import com.google.android.finsky.api.model.DfeList;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.api.model.OnDataChangedListener;
import com.google.android.finsky.config.G;
import com.google.android.finsky.utils.BitmapLoader;
import com.google.android.finsky.utils.FinskyLog;
import com.google.android.finsky.utils.Maps;
import com.google.android.finsky.utils.ThumbnailUtils;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class FinskyWidgetService extends RemoteViewsService {
    @Override // android.widget.RemoteViewsService
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FinskyWidgetFactory(getApplicationContext(), intent);
    }

    private static class FinskyWidgetFactory implements RemoteViewsService.RemoteViewsFactory, Response.ErrorListener, OnDataChangedListener {
        private int IMAGE_MIN_HEIGHT;
        private int IMAGE_MIN_WIDTH;
        private final int mAppWidgetId;
        BitmapLoader mBitmapLoader;
        private final Context mContext;
        private DfeList mItemReq;
        private boolean mRetried = false;
        private HashMap<Integer, BitmapLoader.BitmapContainer> mBitmapContainers = Maps.newHashMap();
        private Handler mUpdateHandler = new Handler() { // from class: com.google.android.finsky.widget.FinskyWidgetService.FinskyWidgetFactory.1
            @Override // android.os.Handler
            public void dispatchMessage(Message msg) {
                AppWidgetManager.getInstance(FinskyWidgetFactory.this.mContext).notifyAppWidgetViewDataChanged(FinskyWidgetFactory.this.mAppWidgetId, R.id.widget_stack);
            }
        };

        FinskyWidgetFactory(Context context, Intent intent) {
            this.mContext = context;
            this.mAppWidgetId = intent.getIntExtra("appWidgetId", 0);
            Resources res = this.mContext.getResources();
            this.IMAGE_MIN_WIDTH = res.getDimensionPixelSize(R.dimen.widget_image_min_width);
            this.IMAGE_MIN_HEIGHT = res.getDimensionPixelSize(R.dimen.widget_image_min_height);
        }

        @Override // android.widget.RemoteViewsService.RemoteViewsFactory
        public void onCreate() {
            setupRequest();
        }

        private void setupRequest() {
            DfeApi dfeApi = FinskyApp.get().getDfeApi();
            this.mBitmapLoader = FinskyApp.get().getBitmapLoader();
            if (dfeApi == null) {
                AuthenticatedActivity.setupAccountFromPreferences(this.mContext);
                return;
            }
            this.mItemReq = new DfeList(dfeApi, G.widgetUrl.get(), false, FinskyApp.get().getAnalytics(), Analytics.Event.WIDGET);
            this.mItemReq.addErrorListener(this);
            if (this.mItemReq.getCount() == 0) {
                this.mItemReq.startLoadItems();
            }
            this.mItemReq.addDataChangedListener(this);
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(Response.ErrorCode error, String message) {
            if (!this.mRetried) {
                this.mRetried = true;
                FinskyLog.d("Retrying once in 20s", new Object[0]);
                this.mUpdateHandler.postDelayed(new Runnable() { // from class: com.google.android.finsky.widget.FinskyWidgetService.FinskyWidgetFactory.2
                    @Override // java.lang.Runnable
                    public void run() {
                        FinskyLog.d("Retrying", new Object[0]);
                        FinskyWidgetFactory.this.mItemReq.retryLoadItems(true);
                    }
                }, 20000L);
            }
        }

        @Override // com.google.android.finsky.api.model.OnDataChangedListener
        public void onDataChanged() {
            notifyDataSetChanged();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifyDataSetChanged() {
            this.mUpdateHandler.removeMessages(0);
            this.mUpdateHandler.sendEmptyMessageDelayed(0, 1000L);
        }

        @Override // android.widget.RemoteViewsService.RemoteViewsFactory
        public void onDataSetChanged() {
            if (this.mItemReq == null) {
                setupRequest();
            }
        }

        @Override // android.widget.RemoteViewsService.RemoteViewsFactory
        public void onDestroy() {
            if (this.mItemReq != null) {
                this.mItemReq.detachAll();
            }
        }

        @Override // android.widget.RemoteViewsService.RemoteViewsFactory
        public int getCount() {
            int count = this.mItemReq == null ? 0 : this.mItemReq.getCount();
            if (count > 0) {
                while (count < 4) {
                    count += this.mItemReq.getCount();
                }
            }
            return count;
        }

        @Override // android.widget.RemoteViewsService.RemoteViewsFactory
        public RemoteViews getViewAt(int position) {
            final int adjustedPosition = position % this.mItemReq.getCount();
            Document doc = this.mItemReq.getItem(adjustedPosition);
            RemoteViews remoteViews = new RemoteViews(this.mContext.getPackageName(), R.layout.widget_item);
            boolean shouldFetch = true;
            String imageUrl = ThumbnailUtils.getBitmapUrlFromDocument(doc, 2);
            BitmapLoader.BitmapContainer container = this.mBitmapContainers.get(Integer.valueOf(adjustedPosition));
            if (container != null) {
                if (container.getRequestUrl() != null && container.getRequestUrl().equals(imageUrl)) {
                    shouldFetch = false;
                } else {
                    container.cancelRequest();
                    this.mBitmapContainers.remove(Integer.valueOf(adjustedPosition));
                    container = null;
                }
            }
            if (shouldFetch) {
                container = this.mBitmapLoader.get(imageUrl, (Bitmap) null, new BitmapLoader.BitmapLoadedHandler() { // from class: com.google.android.finsky.widget.FinskyWidgetService.FinskyWidgetFactory.3
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // com.android.volley.Response.Listener
                    public void onResponse(BitmapLoader.BitmapContainer result) {
                        if (result.getBitmap() != null) {
                            FinskyWidgetFactory.this.mBitmapContainers.put(Integer.valueOf(adjustedPosition), result);
                            FinskyWidgetFactory.this.notifyDataSetChanged();
                        }
                    }
                }, this.IMAGE_MIN_WIDTH, this.IMAGE_MIN_HEIGHT);
            }
            if (container != null && container.getBitmap() != null) {
                remoteViews.setImageViewBitmap(R.id.widget_image, container.getBitmap());
            }
            remoteViews.setTextViewText(R.id.widget_name, doc.getTitle());
            remoteViews.setTextViewText(R.id.widget_creator, doc.getCreator());
            remoteViews.setTextViewText(R.id.widget_price, doc.getFormattedPrice());
            float rating = doc.hasRating() ? doc.getStarRating() : 0.0f;
            remoteViews.setInt(R.id.widget_rating_bar, "setImageLevel", (int) (2.0f * rating));
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setPackage(this.mContext.getPackageName());
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http");
            builder.authority("market.android.com");
            builder.appendEncodedPath("details");
            builder.appendQueryParameter("id", doc.getDocId());
            intent.setData(builder.build());
            remoteViews.setOnClickFillInIntent(R.id.widget_image, intent);
            return remoteViews;
        }

        @Override // android.widget.RemoteViewsService.RemoteViewsFactory
        public RemoteViews getLoadingView() {
            RemoteViews remoteViews = new RemoteViews(this.mContext.getPackageName(), R.layout.widget_item);
            remoteViews.setViewVisibility(R.id.widget_loading, 0);
            remoteViews.setProgressBar(R.id.widget_loading, 0, 0, true);
            return remoteViews;
        }

        @Override // android.widget.RemoteViewsService.RemoteViewsFactory
        public int getViewTypeCount() {
            return 1;
        }

        @Override // android.widget.RemoteViewsService.RemoteViewsFactory
        public long getItemId(int position) {
            return position;
        }

        @Override // android.widget.RemoteViewsService.RemoteViewsFactory
        public boolean hasStableIds() {
            return true;
        }
    }
}
