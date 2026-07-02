package com.google.android.finsky.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.google.android.finsky.utils.FinskyLog;
import com.google.android.finsky.utils.Sets;
import java.util.HashSet;

/* JADX INFO: loaded from: classes.dex */
public class PurchaseStatusTracker extends BroadcastReceiver {
    private final HashSet<PurchaseStatusListener> mListeners = Sets.newHashSet();
    public final HashSet<String> mPendingPurchases = Sets.newHashSet();

    public enum PurchaseState {
        PURCHASE_INITIATED,
        PURCHASE_COMPLETED,
        PURCHASE_COMPLETED_WITH_ERROR
    }

    public interface PurchaseStatusListener {
        void onPackageInstalled(String str);

        void onPurchaseCompleted(String str, boolean z);

        void onPurchaseInitiated(String str, int i);
    }

    public PurchaseStatusTracker(Context context) {
        IntentFilter filter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        filter.addDataScheme("package");
        context.registerReceiver(this, filter);
    }

    public void attach(PurchaseStatusListener listener) {
        this.mListeners.add(listener);
    }

    public void detach(PurchaseStatusListener listener) {
        this.mListeners.remove(listener);
    }

    private void notifyPurchaseInitiated(String docId, int offerType) {
        for (PurchaseStatusListener listener : this.mListeners) {
            listener.onPurchaseInitiated(docId, offerType);
        }
    }

    private void notifyPurchaseCompleted(String docId, boolean hadError) {
        for (PurchaseStatusListener listener : this.mListeners) {
            listener.onPurchaseCompleted(docId, hadError);
        }
    }

    private void notifyPackageAdded(String packageName) {
        for (PurchaseStatusListener listener : this.mListeners) {
            listener.onPackageInstalled(packageName);
        }
    }

    public void switchState(String docId, int offerType, PurchaseState stateChange) {
        if (docId == null) {
            throw new IllegalStateException("Cannot track the purchase of an item with a null doc ID");
        }
        switch (stateChange) {
            case PURCHASE_INITIATED:
                if (offerType == 0) {
                    offerType = 1;
                }
                this.mPendingPurchases.add(docId);
                notifyPurchaseInitiated(docId, offerType);
                return;
            case PURCHASE_COMPLETED:
                this.mPendingPurchases.remove(docId);
                notifyPurchaseCompleted(docId, false);
                return;
            case PURCHASE_COMPLETED_WITH_ERROR:
                this.mPendingPurchases.remove(docId);
                notifyPurchaseCompleted(docId, true);
                return;
            default:
                throw new IllegalStateException("Invalid state change for PurchaseStatusTracker " + stateChange.toString());
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.PACKAGE_ADDED".equals(intent.getAction())) {
            String packageName = getPackageName(intent);
            notifyPackageAdded(packageName);
        }
    }

    private static String getPackageName(Intent intent) {
        String packageName = intent.getData().getSchemeSpecificPart();
        if (FinskyLog.DEBUG) {
            FinskyLog.v("Package successfully installed: %s", packageName);
        }
        return packageName;
    }

    public boolean isPendingPurchase(String docId) {
        return this.mPendingPurchases.contains(docId);
    }

    public void clearPendingPurchases() {
        this.mPendingPurchases.clear();
    }
}
