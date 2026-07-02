package com.google.android.finsky.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import com.google.android.finsky.receivers.PackageMonitorReceiver;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class PackageInfoCache {
    private final Context mContext;
    private final PackageManager mPackageManager;
    private final Map<String, PackageInfo> mCache = Maps.newHashMap();
    private final PackageMonitorReceiver mPackageMonitor = new PackageMonitorReceiver() { // from class: com.google.android.finsky.utils.PackageInfoCache.1
        @Override // com.google.android.finsky.receivers.PackageMonitorReceiver
        protected void onPackageAdded(String packageName) {
            PackageInfoCache.this.mCache.remove(packageName);
            PackageInfoCache.this.updatePackageInfo(packageName);
        }

        @Override // com.google.android.finsky.receivers.PackageMonitorReceiver
        protected void onPackageRemoved(String packageName) {
            PackageInfoCache.this.mCache.remove(packageName);
        }

        @Override // com.google.android.finsky.receivers.PackageMonitorReceiver
        protected void onPackageChanged(String packageName) {
            PackageInfoCache.this.mCache.remove(packageName);
            PackageInfoCache.this.updatePackageInfo(packageName);
        }
    };

    public PackageInfoCache(Context context, PackageManager pm) {
        this.mContext = context;
        this.mPackageManager = pm;
        this.mPackageMonitor.register(context);
    }

    public synchronized boolean isPackageInstalled(String packageName) {
        return getPackageVersion(packageName) != -1;
    }

    public synchronized boolean isSystemPackage(String packageName) {
        updatePackageInfo(packageName);
        return this.mCache.get(packageName).firstInstallTime == getSystemFirstInstallTime(this.mContext);
    }

    public synchronized int getPackageVersion(String packageName) {
        updatePackageInfo(packageName);
        return this.mCache.get(packageName).versionCode;
    }

    public synchronized boolean canLaunch(String packageName) {
        updatePackageInfo(packageName);
        return this.mCache.get(packageName).canLaunch;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updatePackageInfo(String packageName) {
        PackageInfo packageInfo = this.mCache.get(packageName);
        if (packageInfo == null) {
            int versionCode = -1;
            long firstInstallTime = -1;
            boolean canLaunch = false;
            try {
                android.content.pm.PackageInfo pi = this.mPackageManager.getPackageInfo(packageName, 0);
                versionCode = pi.versionCode;
                firstInstallTime = pi.getClass().getDeclaredField("firstInstallTime").getLong(pi);
                canLaunch = this.mPackageManager.getLaunchIntentForPackage(packageName) != null;
            } catch (PackageManager.NameNotFoundException e) {
            } catch (IllegalAccessException e2) {
            } catch (IllegalArgumentException e3) {
            } catch (NoSuchFieldException e4) {
            } catch (SecurityException e5) {
            }
            PackageInfo packageInfo2 = new PackageInfo(versionCode, firstInstallTime, canLaunch);
            this.mCache.put(packageName, packageInfo2);
        }
    }

    public synchronized long getSystemFirstInstallTime(Context context) {
        String packageName;
        packageName = context.getPackageName();
        updatePackageInfo(packageName);
        return this.mCache.get(packageName).firstInstallTime;
    }

    private static final class PackageInfo {
        boolean canLaunch;
        long firstInstallTime;
        int versionCode;

        PackageInfo(int versionCode, long firstInstallTime, boolean canLaunch) {
            this.versionCode = versionCode;
            this.firstInstallTime = firstInstallTime;
            this.canLaunch = canLaunch;
        }
    }
}
