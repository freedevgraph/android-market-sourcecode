package com.google.android.finsky.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.google.android.finsky.utils.FinskyLog;

/* JADX INFO: loaded from: classes.dex */
public abstract class PackageMonitorReceiver extends BroadcastReceiver {
    protected abstract void onPackageAdded(String str);

    protected abstract void onPackageChanged(String str);

    protected abstract void onPackageRemoved(String str);

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String packageName = getPackageName(intent);
        if ("android.intent.action.PACKAGE_REMOVED".equals(action)) {
            onPackageRemoved(packageName);
            return;
        }
        if ("android.intent.action.PACKAGE_ADDED".equals(action)) {
            onPackageAdded(packageName);
        } else if ("android.intent.action.PACKAGE_CHANGED".equals(action)) {
            onPackageChanged(packageName);
        } else {
            FinskyLog.w("Unhandled intent type action type: %s", action);
        }
    }

    private String getPackageName(Intent intent) {
        return intent.getData().getSchemeSpecificPart();
    }

    public void register(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_CHANGED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addDataScheme("package");
        context.registerReceiver(this, filter);
    }
}
