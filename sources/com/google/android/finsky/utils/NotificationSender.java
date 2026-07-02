package com.google.android.finsky.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.finsky.R;

/* JADX INFO: loaded from: classes.dex */
public class NotificationSender {
    public static void send(Context context, String notificationTitle, String docTitle, String message, String pendingIntentUrl, String detailsUrl) {
        int id = detailsUrl.hashCode();
        String customTitle = notificationTitle + " - " + docTitle;
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.stat_notify_market_error);
        builder.setTicker(customTitle);
        builder.setContentTitle(customTitle);
        builder.setContentText(message);
        if (!TextUtils.isEmpty(pendingIntentUrl)) {
            Intent notificationIntent = new Intent();
            notificationIntent.setAction("android.intent.action.VIEW");
            notificationIntent.setData(Uri.parse(pendingIntentUrl));
            PendingIntent pendingIntent = PendingIntent.getActivity(context, id, notificationIntent, 1073741824);
            builder.setContentIntent(pendingIntent);
        }
        Notification notification = builder.getNotification();
        notification.flags = 16;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService("notification");
        mNotificationManager.notify(id, notification);
    }
}
