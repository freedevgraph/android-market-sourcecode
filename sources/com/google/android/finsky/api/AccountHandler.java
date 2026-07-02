package com.google.android.finsky.api;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public class AccountHandler {
    private static String sCurrentAccount;

    public static Account[] getAccounts(Context context) {
        AccountManager am = AccountManager.get(context);
        return am.getAccountsByType("com.google");
    }

    public static String getCurrentAccount() {
        return sCurrentAccount;
    }

    public static void setCurrentAccount(String accountName, SharedPreferences settings) {
        sCurrentAccount = accountName;
        if (sCurrentAccount != null) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("account", sCurrentAccount);
            editor.commit();
        }
    }

    public static Account getFirstAccount(Context context) {
        Account[] accounts = getAccounts(context);
        if (accounts.length > 0) {
            return accounts[0];
        }
        return null;
    }

    public static Account findAccount(String accountName, Context context) {
        if (TextUtils.isEmpty(accountName)) {
            return null;
        }
        Account[] accounts = getAccounts(context);
        for (Account a : accounts) {
            if (a.name.equals(accountName)) {
                return a;
            }
        }
        return null;
    }

    public static boolean hasAccount(String accountName, Context context) {
        return findAccount(accountName, context) != null;
    }

    public static Account getAccountFromPreferences(Context context, SharedPreferences settings) {
        String currentAccountName = settings.getString("account", null);
        return hasAccount(currentAccountName, context) ? new Account(currentAccountName, "com.google") : getFirstAccount(context);
    }
}
