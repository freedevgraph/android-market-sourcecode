package com.google.android.finsky.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import com.android.volley.Response;
import com.android.volley.toolbox.AndroidAuthenticator;
import com.google.android.finsky.FinskyApp;
import com.google.android.finsky.R;
import com.google.android.finsky.api.AccountHandler;
import com.google.android.finsky.api.DfeApiContext;
import com.google.android.finsky.api.model.DfeToc;
import com.google.android.finsky.config.G;
import com.google.android.finsky.remoting.protos.Toc;
import com.google.android.finsky.utils.FinskyLog;
import com.google.android.vending.remoting.api.VendingApiContext;
import java.lang.reflect.Method;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public abstract class AuthenticatedActivity extends FragmentActivity {
    private boolean mTosJustAccepted = false;
    private final DialogInterface.OnClickListener mDeclineCreateAccountListener = new DialogInterface.OnClickListener() { // from class: com.google.android.finsky.activities.AuthenticatedActivity.7
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int id) {
            AuthenticatedActivity.this.finish();
        }
    };
    private final DialogInterface.OnClickListener mOnClickCreateAccountListener = new DialogInterface.OnClickListener() { // from class: com.google.android.finsky.activities.AuthenticatedActivity.8
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int id) {
            AccountManager am = AccountManager.get(AuthenticatedActivity.this);
            am.addAccount("com.google", "androidmarket", null, null, AuthenticatedActivity.this, new AccountManagerCallback<Bundle>() { // from class: com.google.android.finsky.activities.AuthenticatedActivity.8.1
                @Override // android.accounts.AccountManagerCallback
                public void run(AccountManagerFuture<Bundle> future) {
                    Account[] accounts = AccountHandler.getAccounts(AuthenticatedActivity.this.getApplicationContext());
                    if (accounts.length != 0) {
                        AuthenticatedActivity.this.restart();
                    } else {
                        AuthenticatedActivity.this.finish();
                    }
                }
            }, null);
            dialog.cancel();
        }
    };

    protected abstract void handleAuthenticationError(Response.ErrorCode errorCode, String str);

    protected abstract void onApisChanged();

    protected abstract void onCleanup();

    protected abstract void onReady(boolean z);

    protected abstract void onTocLoaded(DfeToc dfeToc);

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean dfeChanged = setupAccountFromPreferences(getApplicationContext());
        if (dfeChanged) {
            onApisChanged();
        }
        if (hasDiffVersionCode()) {
            if (FinskyLog.DEBUG) {
                FinskyLog.v("Diff version code, clear cache", new Object[0]);
            }
            FinskyApp.get().clearCacheAsync(new Runnable() { // from class: com.google.android.finsky.activities.AuthenticatedActivity.1
                @Override // java.lang.Runnable
                public void run() {
                    AuthenticatedActivity.this.setupSessionForCorrectUser(true);
                }
            });
        } else {
            if (FinskyLog.DEBUG) {
                FinskyLog.v("Same version code as before", new Object[0]);
            }
            setupSessionForCorrectUser(true);
        }
    }

    public static boolean setupAccountFromPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("finsky", 0);
        Account account = AccountHandler.getAccountFromPreferences(context, prefs);
        if (account == null) {
            return false;
        }
        AccountHandler.setCurrentAccount(account.name, prefs);
        FinskyApp.get().setApiContexts(createApiContext(context, account), createVendingApiContext(context, account));
        return true;
    }

    protected void authenticateOnNewIntent(boolean shouldHandleIntent) {
        setupSessionForCorrectUser(shouldHandleIntent);
    }

    @Override // android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 20) {
            if (resultCode == 0) {
                finish();
            } else {
                setTosJustAccepted(true);
            }
        }
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int id, Bundle args) {
        switch (id) {
            case 0:
                return setupAccountDialog();
            case 1:
            default:
                throw new IllegalStateException("Invalid dialog type id " + id);
            case 2:
                return setupAccountCreationDialog(true);
        }
    }

    protected boolean getTosJustAccepted() {
        return this.mTosJustAccepted;
    }

    protected void setTosJustAccepted(boolean b) {
        this.mTosJustAccepted = b;
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        boolean shouldHandleIntent = true;
        if ("android.intent.action.MAIN".equals(intent.getAction()) && intent.hasCategory("android.intent.category.LAUNCHER")) {
            shouldHandleIntent = false;
        }
        authenticateOnNewIntent(shouldHandleIntent);
    }

    protected void restart(Account account) {
        if (account == null) {
            account = AccountHandler.findAccount(AccountHandler.getCurrentAccount(), this);
        }
        onCleanup();
        FinskyApp.get().setToc(null);
        FinskyApp.get().setApiContexts(createApiContext(getApplicationContext(), account), createVendingApiContext(getApplicationContext(), account));
        onApisChanged();
        SharedPreferences settings = getSharedPreferences("finsky", 0);
        AccountHandler.setCurrentAccount(account.name, settings);
        setupSessionForCorrectUser(true);
    }

    private static String[] convertToStringArray(Account[] accounts) {
        String[] accountNames = new String[accounts.length];
        for (int i = 0; i < accounts.length; i++) {
            accountNames[i] = accounts[i].name;
        }
        return accountNames;
    }

    private static DfeApiContext createApiContext(Context context, Account account) {
        AndroidAuthenticator authenticator = new AndroidAuthenticator(context, account, G.authTokenType.get());
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            String appVersionName = pi.versionName;
            int appVersionCode = pi.versionCode;
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            return new DfeApiContext(context, authenticator, FinskyApp.get().getCache(), appVersionName, appVersionCode, Locale.getDefault(), telephonyManager.getSimOperator(), G.clientId.get(), G.loggingId.get());
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Can't find our own package", e);
        }
    }

    private static VendingApiContext createVendingApiContext(Context context, Account account) {
        new AndroidAuthenticator(context, account);
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            int appVersionCode = pi.versionCode;
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            return new VendingApiContext(context, account, Locale.getDefault(), Long.toHexString(G.androidId.get().longValue()), appVersionCode, telephonyManager.getNetworkOperatorName(), telephonyManager.getSimOperatorName(), telephonyManager.getNetworkOperator(), telephonyManager.getSimOperator(), Build.DEVICE, Build.VERSION.SDK, G.clientId.get(), G.loggingId.get());
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Can't find our own package", e);
        }
    }

    private String determineAccount() {
        String accountNameFromIntent = getIntent().getStringExtra("authAccount");
        if (accountNameFromIntent != null) {
            Account accountFromIntent = AccountHandler.findAccount(accountNameFromIntent, this);
            if (accountFromIntent == null) {
                FinskyLog.wtf("This app was called with an intent that specified the account %s, which is not a valid account on this device", accountNameFromIntent);
                finish();
            }
            return accountFromIntent.name;
        }
        String currentAccount = AccountHandler.getCurrentAccount();
        if (AccountHandler.hasAccount(currentAccount, this)) {
            return currentAccount;
        }
        Account account = AccountHandler.getFirstAccount(this);
        if (account != null) {
            return account.name;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean displayTos(String tosContent) {
        String account = AccountHandler.getCurrentAccount();
        if (!TosActivity.requiresAcceptance(this, account, tosContent)) {
            return false;
        }
        Intent showTos = TosActivity.getIntent(this, account, tosContent);
        showTos.setFlags(67108864);
        startActivityForResult(showTos, 20);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireOnReadyRunnable(final boolean shouldHandleIntent) {
        Runnable onReadyRunnable = new Runnable() { // from class: com.google.android.finsky.activities.AuthenticatedActivity.2
            @Override // java.lang.Runnable
            public void run() {
                AuthenticatedActivity.this.onReady(shouldHandleIntent);
            }
        };
        onReadyRunnable.run();
    }

    private static int getIndexOfAccount(String[] accountNames, String accountToFind) {
        for (int i = 0; i < accountNames.length; i++) {
            if (accountNames[i].equals(accountToFind)) {
                return i;
            }
        }
        return -1;
    }

    public boolean hasDiffVersionCode() {
        int currentVersionCode = FinskyApp.get().getPackageInfoCache().getPackageVersion(getPackageName());
        SharedPreferences settings = getSharedPreferences("finsky", 0);
        int lastVersionCode = settings.getInt("last_version_code", -1);
        if (lastVersionCode == currentVersionCode) {
            return false;
        }
        settings.edit().putInt("last_version_code", currentVersionCode).commit();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAccountSwitchNeeded(String accountName) {
        return !accountName.equals(AccountHandler.getCurrentAccount());
    }

    private void requestToc(final boolean shouldHandleIntent) {
        FinskyApp.get().getDfeApi().getChannels(new Response.Listener<Toc.TocResponse>() { // from class: com.google.android.finsky.activities.AuthenticatedActivity.3
            @Override // com.android.volley.Response.Listener
            public void onResponse(Toc.TocResponse response) {
                DfeToc newToc = new DfeToc(response);
                FinskyApp.get().setToc(newToc);
                AuthenticatedActivity.this.onTocLoaded(newToc);
                if (!AuthenticatedActivity.this.isExtraInitializationNeeded() && !AuthenticatedActivity.this.displayTos(response.getTosContent())) {
                    AuthenticatedActivity.this.fireOnReadyRunnable(shouldHandleIntent);
                }
            }
        }, new Response.ErrorListener() { // from class: com.google.android.finsky.activities.AuthenticatedActivity.4
            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(Response.ErrorCode error, String message) {
                AuthenticatedActivity.this.handleAuthenticationError(error, message);
            }
        });
    }

    private Dialog setupAccountCreationDialog(boolean shouldHandleIntent) {
        Resources resources = getResources();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(resources.getString(R.string.account_required)).setCancelable(false).setPositiveButton(resources.getString(R.string.yes), this.mOnClickCreateAccountListener).setNegativeButton(resources.getString(R.string.no), this.mDeclineCreateAccountListener);
        AlertDialog alert = builder.create();
        return alert;
    }

    private AlertDialog setupAccountDialog() {
        final Account[] accounts = AccountHandler.getAccounts(getApplicationContext());
        String[] accountNames = convertToStringArray(accounts);
        int currentAccountIndex = getIndexOfAccount(accountNames, AccountHandler.getCurrentAccount());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_account));
        builder.setSingleChoiceItems(accountNames, currentAccountIndex, new DialogInterface.OnClickListener() { // from class: com.google.android.finsky.activities.AuthenticatedActivity.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int item) {
                String newAccount = accounts[item].name;
                if (AuthenticatedActivity.this.isAccountSwitchNeeded(newAccount)) {
                    AuthenticatedActivity.this.setIntent(new Intent());
                    AuthenticatedActivity.this.switchAccount(newAccount);
                }
                AuthenticatedActivity.this.removeDialog(0);
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.google.android.finsky.activities.AuthenticatedActivity.6
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialog) {
                AuthenticatedActivity.this.removeDialog(0);
            }
        });
        AlertDialog alert = builder.create();
        return alert;
    }

    protected void setupSessionForCorrectUser(boolean shouldHandleIntent) {
        String accountName = determineAccount();
        if (accountName == null) {
            showDialog(2);
            return;
        }
        if (isAccountSwitchNeeded(accountName)) {
            switchAccount(accountName);
            return;
        }
        DfeToc dfeToc = FinskyApp.get().getToc();
        if (dfeToc == null) {
            requestToc(shouldHandleIntent);
        } else if (!isExtraInitializationNeeded() && !displayTos(dfeToc.getTosContent())) {
            onTocLoaded(FinskyApp.get().getToc());
            fireOnReadyRunnable(shouldHandleIntent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchAccount(String accountName) {
        Account account = AccountHandler.findAccount(accountName, this);
        if (account == null) {
            throw new IllegalStateException("Error, could not switch to %s because the accountcould not be found on the device");
        }
        restart(account);
    }

    protected boolean isExtraInitializationNeeded() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restart() {
        Class<?> clazz = getClass();
        try {
            Method recreate = clazz.getDeclaredMethod("recreate", new Class[0]);
            recreate.invoke(this, new Object[0]);
        } catch (NoSuchMethodException e) {
            finish();
            startActivity(getIntent());
        } catch (Exception e2) {
            throw new RuntimeException("Could not restart activity after account setup.", e2);
        }
    }
}
