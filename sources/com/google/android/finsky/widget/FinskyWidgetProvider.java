package com.google.android.finsky.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import com.google.android.finsky.FinskyApp;
import com.google.android.finsky.R;
import com.google.android.finsky.activities.AuthenticatedActivity;
import com.google.android.finsky.activities.MainActivity;

/* JADX INFO: loaded from: classes.dex */
public class FinskyWidgetProvider extends AppWidgetProvider {
    @Override // android.appwidget.AppWidgetProvider, android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        if (FinskyApp.get().getDfeApi() == null) {
            AuthenticatedActivity.setupAccountFromPreferences(context);
        }
        if ("com.google.android.finsky.action.DFE_API_CONTEXT_CHANGED".equals(action)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(new ComponentName(context, (Class<?>) FinskyWidgetProvider.class)), R.id.widget_stack);
        }
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] iArr) {
        super.onUpdate(context, appWidgetManager, iArr);
        for (int i : iArr) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            Intent intent = new Intent(context, (Class<?>) FinskyWidgetService.class);
            intent.putExtra("appWidgetId", i);
            intent.setData(Uri.parse(intent.toUri(1)));
            remoteViews.setRemoteAdapter(i, R.id.widget_stack, intent);
            remoteViews.setEmptyView(R.id.widget_stack, R.id.widget_empty);
            remoteViews.setOnClickPendingIntent(R.id.widget_empty, PendingIntent.getActivity(context, 0, new Intent(context, (Class<?>) MainActivity.class), 134217728));
            remoteViews.setPendingIntentTemplate(R.id.widget_stack, PendingIntent.getActivity(context, 0, new Intent("android.intent.action.VIEW"), 134217728));
            appWidgetManager.updateAppWidget(i, remoteViews);
        }
    }
}
