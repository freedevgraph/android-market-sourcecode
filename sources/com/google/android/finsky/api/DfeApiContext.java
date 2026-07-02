package com.google.android.finsky.api;

import android.accounts.Account;
import android.content.Context;
import android.text.TextUtils;
import com.android.volley.AuthFailureException;
import com.android.volley.Cache;
import com.android.volley.toolbox.AndroidAuthenticator;
import com.android.volley.toolbox.UrlTools;
import com.google.android.finsky.config.G;
import com.google.android.finsky.utils.Maps;
import com.google.android.finsky.utils.Utils;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class DfeApiContext {
    private final AndroidAuthenticator mAuthenticator;
    private final Cache mCache;
    private final Context mContext;
    private final Map<String, String> mHeaders = Maps.newHashMap();
    private String mLastAuthToken;

    public DfeApiContext(Context context, AndroidAuthenticator authenticator, Cache cache, String appVersionString, int appVersionCode, Locale locale, String mccmnc, String clientId, String loggingId) {
        this.mContext = context;
        this.mAuthenticator = authenticator;
        this.mCache = cache;
        this.mHeaders.put("X-DFE-Device-Id", Long.toHexString(G.androidId.get().longValue()));
        this.mHeaders.put("Accept-Language", locale.getLanguage() + "-" + locale.getCountry());
        if (!TextUtils.isEmpty(mccmnc)) {
            this.mHeaders.put("X-DFE-MCCMNC", mccmnc);
        }
        if (!TextUtils.isEmpty(clientId)) {
            this.mHeaders.put("X-DFE-Client-Id", clientId);
        }
        if (!TextUtils.isEmpty(clientId)) {
            this.mHeaders.put("X-DFE-Logging-Id", loggingId);
        }
        this.mHeaders.put("User-Agent", makeUserAgentString(appVersionString, appVersionCode));
        if (this.mContext != null) {
            int contentFilterLevel = this.mContext.getSharedPreferences("finsky", 0).getInt(Utils.getPreferenceKey("content-filter-level"), G.defaultContentFilterLevel.get().intValue());
            this.mHeaders.put("X-DFE-Filter-Level", String.valueOf(contentFilterLevel));
            checkUrlRules();
        }
    }

    private void checkUrlRules() {
        String uriString = DfeApi.BASE_URI.toString();
        String rewritten = UrlTools.rewrite(this.mContext, uriString);
        if (rewritten == null) {
            throw new RuntimeException("BASE_URI blocked by UrlRules: " + uriString);
        }
        Utils.checkUrlIsSecure(rewritten);
    }

    private String makeUserAgentString(String versionString, int versionCode) {
        return String.format(Locale.US, "Android-Finsky/%s (api=%d,versionCode=%d)", versionString, 2, Integer.valueOf(versionCode));
    }

    public Account getAccount() {
        return this.mAuthenticator.getAccount();
    }

    public void invalidateAuthToken() {
        if (this.mLastAuthToken != null) {
            this.mAuthenticator.invalidateAuthToken(this.mLastAuthToken);
            this.mLastAuthToken = null;
        }
    }

    public void clearCache() {
        this.mCache.clear();
    }

    public Map<String, String> getHeaders() throws AuthFailureException {
        this.mLastAuthToken = this.mAuthenticator.getAuthToken();
        this.mHeaders.put("Authorization", "GoogleLogin auth=" + this.mLastAuthToken);
        return this.mHeaders;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[DfeApiContext headers={");
        boolean first = true;
        for (String header : this.mHeaders.keySet()) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(header).append(": ").append(this.mHeaders.get(header));
        }
        sb.append("}]");
        return sb.toString();
    }
}
