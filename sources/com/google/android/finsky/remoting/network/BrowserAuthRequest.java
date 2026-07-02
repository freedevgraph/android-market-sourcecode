package com.google.android.finsky.remoting.network;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.android.finsky.config.G;
import com.google.android.finsky.utils.FinskyLog;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class BrowserAuthRequest implements Response.Listener<String>, Response.ErrorListener {
    private Account account;
    private final String mAuthTokenService;
    private Listener<Uri> mListener;
    private final RequestQueue mRequestQueue;
    private String mUrl;
    private static final String PARAM_AUTH_TOKEN_SOURCE = G.authTokenType.get();
    private static final Uri ISSUE_AUTH_TOKEN_URL = Uri.parse("https://www.google.com/accounts/IssueAuthToken?service=gaia&Session=false");
    private static final Uri TOKEN_AUTH_URL = Uri.parse("https://www.google.com/accounts/TokenAuth");

    public interface Listener<Uri> {
        Activity getActivity();

        void onResponse(Uri uri, Response.ErrorCode errorCode, String str);
    }

    public BrowserAuthRequest(RequestQueue queue, String url, String accountName, String authTokenService, Listener<Uri> listener) {
        this.mRequestQueue = queue;
        this.mListener = listener;
        this.mUrl = url;
        this.mAuthTokenService = authTokenService;
        getSidToken(accountName);
    }

    private void getSidToken(String accountName) {
        this.account = new Account(accountName, "com.google");
        SidTokenHandler callback = new SidTokenHandler(this.account);
        AccountManager am = AccountManager.get(this.mListener.getActivity());
        am.getAuthToken(this.account, "SID", (Bundle) null, this.mListener.getActivity(), callback, (Handler) null);
    }

    private class SidTokenHandler implements AccountManagerCallback<Bundle> {
        private final Account mAccount;

        SidTokenHandler(Account account) {
            this.mAccount = account;
        }

        @Override // android.accounts.AccountManagerCallback
        public void run(AccountManagerFuture<Bundle> value) {
            try {
                String sid = value.getResult().getString("authtoken");
                LsidTokenHandler callback = BrowserAuthRequest.this.new LsidTokenHandler(sid);
                AccountManager am = AccountManager.get(BrowserAuthRequest.this.mListener.getActivity());
                am.getAuthToken(this.mAccount, "LSID", (Bundle) null, BrowserAuthRequest.this.mListener.getActivity(), callback, (Handler) null);
            } catch (AuthenticatorException e) {
                FinskyLog.e("Authentication error while acquiring token: %s", e);
                BrowserAuthRequest.this.deliverUri(null, Response.ErrorCode.AUTH, null);
            } catch (OperationCanceledException e2) {
                FinskyLog.e("Cancelled while acquiring token: %s", e2);
                BrowserAuthRequest.this.deliverUri(null, Response.ErrorCode.AUTH, null);
            } catch (IOException e3) {
                FinskyLog.e("IO error while acquiring token: %s", e3);
                BrowserAuthRequest.this.deliverUri(null, Response.ErrorCode.AUTH, null);
            }
        }
    }

    private class LsidTokenHandler implements AccountManagerCallback<Bundle> {
        private final String mSid;

        LsidTokenHandler(String sid) {
            this.mSid = sid;
        }

        @Override // android.accounts.AccountManagerCallback
        public void run(AccountManagerFuture<Bundle> value) {
            try {
                String lsid = value.getResult().getString("authtoken");
                BrowserAuthRequest.this.getUberToken(this.mSid, lsid);
            } catch (AuthenticatorException e) {
                FinskyLog.e("Authentication error while acquiring token: %s", e);
                BrowserAuthRequest.this.deliverUri(null, Response.ErrorCode.AUTH, null);
            } catch (OperationCanceledException e2) {
                FinskyLog.e("Cancelled while acquiring token: %s", e2);
                BrowserAuthRequest.this.deliverUri(null, Response.ErrorCode.AUTH, null);
            } catch (IOException e3) {
                FinskyLog.e("IO error while acquiring token: %s", e3);
                BrowserAuthRequest.this.deliverUri(null, Response.ErrorCode.AUTH, null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getUberToken(String sid, String lsid) {
        String url = ISSUE_AUTH_TOKEN_URL.buildUpon().appendQueryParameter("SID", sid).appendQueryParameter("LSID", lsid).build().toString();
        StringRequest request = new StringRequest(url, this, this);
        request.setShouldCache(false);
        this.mRequestQueue.add(request);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deliverUri(Uri uri, Response.ErrorCode error, String message) {
        this.mListener.onResponse(uri, error, message);
    }

    @Override // com.android.volley.Response.Listener
    public void onResponse(String token) {
        Uri.Builder builder = TOKEN_AUTH_URL.buildUpon().appendQueryParameter("service", this.mAuthTokenService).appendQueryParameter("source", PARAM_AUTH_TOKEN_SOURCE).appendQueryParameter("auth", token.trim()).appendQueryParameter("continue", this.mUrl);
        Uri uri = builder.build();
        deliverUri(uri, Response.ErrorCode.OK, null);
    }

    @Override // com.android.volley.Response.ErrorListener
    public void onErrorResponse(Response.ErrorCode error, String message) {
        deliverUri(null, error, message);
    }
}
