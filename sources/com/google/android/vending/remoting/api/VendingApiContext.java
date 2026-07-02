package com.google.android.vending.remoting.api;

import android.accounts.Account;
import android.content.Context;
import com.android.volley.toolbox.AndroidAuthenticator;
import com.android.volley.toolbox.UrlTools;
import com.google.android.finsky.utils.Utils;
import com.google.android.vending.remoting.protos.VendingProtos;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class VendingApiContext {
    private final AndroidAuthenticator mAuthenticator;
    private final Context mContext;
    private VendingProtos.RequestPropertiesProto mRequestProperties = new VendingProtos.RequestPropertiesProto();

    public VendingApiContext(Context context, Account account, Locale userLocale, String aid, int softwareVersion, String operatorName, String simOperatorName, String operatorNumericName, String simOperatorNumericName, String deviceName, String sdkVersion, String clientId, String loggingId) {
        this.mContext = context;
        this.mAuthenticator = new AndroidAuthenticator(context, account);
        this.mRequestProperties.setAid(aid);
        this.mRequestProperties.setUserCountry(userLocale.getCountry());
        this.mRequestProperties.setUserLanguage(userLocale.getLanguage());
        this.mRequestProperties.setOperatorName(operatorName);
        this.mRequestProperties.setSoftwareVersion(softwareVersion);
        this.mRequestProperties.setSimOperatorName(simOperatorName);
        this.mRequestProperties.setOperatorNumericName(operatorNumericName);
        this.mRequestProperties.setSimOperatorNumericName(simOperatorNumericName);
        this.mRequestProperties.setProductNameAndVersion(deviceName + ":" + sdkVersion);
        this.mRequestProperties.setClientId(clientId);
        this.mRequestProperties.setLoggingId(loggingId);
        checkUrlRewrites();
    }

    private void checkUrlRewrites() {
        checkRewrittenToSecureUrl("https://android.clients.google.com/vending/api/ApiRequest");
        checkRewrittenToSecureUrl("https://android_efe.clients.google.com/vending/api/efeApiRequest");
    }

    private void checkRewrittenToSecureUrl(String url) {
        String rewritten = UrlTools.rewrite(this.mContext, url);
        if (rewritten == null) {
            throw new RuntimeException("URL blocked: " + url);
        }
        Utils.checkUrlIsSecure(rewritten);
    }

    public String toString() {
        return "[VendingApiContext]";
    }
}
