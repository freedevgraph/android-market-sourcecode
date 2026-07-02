package com.google.android.finsky.services;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.RemoteException;
import com.android.vending.billing.IMarketBillingService;
import com.google.android.finsky.utils.FinskyLog;

/* JADX INFO: loaded from: classes.dex */
public class InAppBillingProxyService extends Service {
    private volatile boolean mConnectedOrConnecting;
    private volatile IMarketBillingService mRemoteStub;
    private UidProvider mUidProvider = new UidProvider();
    private ServiceConnection mConnection = new ServiceConnection() { // from class: com.google.android.finsky.services.InAppBillingProxyService.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder service) {
            InAppBillingProxyService.this.mRemoteStub = IMarketBillingService.Stub.asInterface(service);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName className) {
            InAppBillingProxyService.this.mRemoteStub = null;
            InAppBillingProxyService.this.mConnectedOrConnecting = false;
            InAppBillingProxyService.this.stopSelf();
        }
    };
    private IMarketBillingService.Stub mStub = new Stub();

    public static class UidProvider {
        public int getCallingUid() {
            return Binder.getCallingUid();
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        bindService();
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        unbindService();
    }

    private void bindService() {
        this.mConnectedOrConnecting = true;
        boolean bindResult = bindService(new Intent("com.android.vending.billing.MarketBillingService.BIND_INTERNAL"), this.mConnection, 1);
        if (!bindResult) {
            FinskyLog.e("Failed to bind to MarketBillingService", new Object[0]);
            stopSelf();
        }
    }

    private void unbindService() {
        this.mConnectedOrConnecting = false;
        unbindService(this.mConnection);
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mStub;
    }

    private class Stub extends IMarketBillingService.Stub {
        private Stub() {
        }

        @Override // com.android.vending.billing.IMarketBillingService
        public Bundle sendBillingRequest(Bundle arguments) {
            return sendBillingRequest(arguments, 1);
        }

        private Bundle sendBillingRequest(Bundle arguments, int numRetries) {
            int numAttempts = 10;
            while (InAppBillingProxyService.this.mRemoteStub == null && numAttempts > 0) {
                numAttempts--;
                if (!InAppBillingProxyService.this.mConnectedOrConnecting) {
                    InAppBillingProxyService.this.stopSelf();
                    return makeErrorResult();
                }
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    FinskyLog.e("InterruptedException occured. Continuing.", new Object[0]);
                }
            }
            try {
                if (InAppBillingProxyService.this.verifyPackageName(arguments.getString("PACKAGE_NAME"))) {
                    return InAppBillingProxyService.this.mRemoteStub.sendBillingRequest(arguments);
                }
            } catch (RemoteException e2) {
                if (e2 instanceof DeadObjectException) {
                    InAppBillingProxyService.this.stopSelf();
                }
                FinskyLog.e("RemoteException occured when proxying request.", new Object[0]);
                if (numRetries > 0) {
                    return sendBillingRequest(arguments, numRetries - 1);
                }
            }
            return makeErrorResult();
        }

        private Bundle makeErrorResult() {
            Bundle result = new Bundle();
            result.putInt("RESPONSE_CODE", 7);
            return result;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean verifyPackageName(String packageName) {
        int uid = this.mUidProvider.getCallingUid();
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, 0);
            if (packageInfo.applicationInfo.uid == uid) {
                return true;
            }
            FinskyLog.w("package uid %s does not match caller's uid %s", Integer.valueOf(packageInfo.applicationInfo.uid), Integer.valueOf(uid));
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            FinskyLog.w("cannot find package name: %s", packageName);
            return false;
        }
    }
}
