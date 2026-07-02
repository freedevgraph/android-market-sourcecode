package com.google.android.finsky.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.google.android.finsky.FinskyApp;

/* JADX INFO: loaded from: classes.dex */
public class ClearCacheReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        FinskyApp.get().clearCacheAsync(new Runnable() { // from class: com.google.android.finsky.receivers.ClearCacheReceiver.1
            @Override // java.lang.Runnable
            public void run() {
                System.exit(0);
            }
        });
    }
}
