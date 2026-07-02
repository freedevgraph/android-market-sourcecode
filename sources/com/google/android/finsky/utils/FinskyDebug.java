package com.google.android.finsky.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.google.android.common.http.UrlRules;
import com.google.android.finsky.api.DfeApi;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class FinskyDebug {
    private static final String ORIGINAL_DFE_URL = DfeApi.BASE_URI.toString();
    public static final Map<String, String> SERVER_INSTANCES = Maps.newLinkedHashMap();

    static {
        SERVER_INSTANCES.put("Production", ORIGINAL_DFE_URL);
        SERVER_INSTANCES.put("Dogfood", "https://android.clients.google.com/dogfood/fdfe/");
        SERVER_INSTANCES.put("Staging", "https://android.clients.google.com/staging/fdfe/");
        SERVER_INSTANCES.put("Development", "https://android.clients.google.com/devel/fdfe/");
        SERVER_INSTANCES.put("Dave's Debug Hut", "http://santoro.mtv.corp.google.com:8000/dfe/");
        SERVER_INSTANCES.put("Ficustown", "http://ficus.mtv.corp.google.com:8000/dfe/");
        SERVER_INSTANCES.put("Demo", "https://android.clients.google.com/demo/fdfe/");
    }

    public static void selectServer(Context context, String newUrl) {
        Intent intent = new Intent("com.google.gservices.intent.action.GSERVICES_OVERRIDE");
        String rewriteRule = ORIGINAL_DFE_URL + " rewrite " + newUrl;
        intent.putExtra("url:finsky_dfe_url", newUrl == null ? null : rewriteRule);
        context.sendBroadcast(intent);
    }

    public static boolean isServerSelected(Context context, String url) {
        UrlRules rules = UrlRules.getRules(context.getContentResolver());
        UrlRules.Rule rule = rules.matchRule(ORIGINAL_DFE_URL);
        String rewritten = rule == UrlRules.Rule.DEFAULT ? ORIGINAL_DFE_URL : rule.apply(ORIGINAL_DFE_URL);
        return TextUtils.equals(url, rewritten);
    }
}
