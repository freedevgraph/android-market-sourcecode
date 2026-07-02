package com.android.volley.toolbox;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Handler;
import com.android.volley.AuthFailureException;

/* JADX INFO: loaded from: classes.dex */
public class AndroidAuthenticator {
    private final Account mAccount;
    private final Context mContext;
    private final String mDefaultAuthTokenType;

    public interface AuthTokenListener {
        void onAuthTokenReceived(String str);

        void onErrorReceived(AuthFailureException authFailureException);
    }

    public AndroidAuthenticator(Context context, Account account) {
        this(context, account, null);
    }

    public AndroidAuthenticator(Context context, Account account, String defaultAuthTokenType) {
        this.mContext = context;
        this.mAccount = account;
        this.mDefaultAuthTokenType = defaultAuthTokenType;
    }

    public Account getAccount() {
        return this.mAccount;
    }

    public String getAuthToken() throws AuthFailureException {
        if (this.mDefaultAuthTokenType == null) {
            throw new UnsupportedOperationException("No default auth type.");
        }
        return getAuthToken(this.mDefaultAuthTokenType);
    }

    public String getAuthToken(String authTokenType) throws AuthFailureException {
        AccountManager accountManager = AccountManager.get(this.mContext);
        try {
            String authToken = accountManager.blockingGetAuthToken(this.mAccount, authTokenType, true);
            if (authToken == null) {
                throw new AuthFailureException("Got null auth token for type: " + authTokenType);
            }
            return authToken;
        } catch (Exception e) {
            throw new AuthFailureException(e.getMessage(), e);
        }
    }

    public void getAuthTokenAsync(AuthTokenListener listener, Handler handler, boolean forceReauth) {
        getAuthTokenAsync(listener, handler, this.mDefaultAuthTokenType, forceReauth);
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [com.android.volley.toolbox.AndroidAuthenticator$1] */
    public void getAuthTokenAsync(final AuthTokenListener listener, final Handler handler, final String authTokenType, boolean forceReauth) {
        String cachedAuthToken;
        if (listener != null) {
            if (forceReauth && (cachedAuthToken = AccountManager.get(this.mContext).peekAuthToken(this.mAccount, authTokenType)) != null) {
                invalidateAuthToken(cachedAuthToken);
            }
            new Thread() { // from class: com.android.volley.toolbox.AndroidAuthenticator.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    Runnable runnable;
                    try {
                        final String authToken = AndroidAuthenticator.this.getAuthToken(authTokenType);
                        runnable = new Runnable() { // from class: com.android.volley.toolbox.AndroidAuthenticator.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                listener.onAuthTokenReceived(authToken);
                            }
                        };
                    } catch (AuthFailureException afe) {
                        runnable = new Runnable() { // from class: com.android.volley.toolbox.AndroidAuthenticator.1.2
                            @Override // java.lang.Runnable
                            public void run() {
                                listener.onErrorReceived(afe);
                            }
                        };
                    }
                    handler.post(runnable);
                }
            }.start();
        }
    }

    public void invalidateAuthToken(String authToken) {
        AccountManager.get(this.mContext).invalidateAuthToken(this.mAccount.type, authToken);
    }
}
