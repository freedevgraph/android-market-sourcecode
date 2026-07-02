package com.android.volley.toolbox;

import android.content.Context;
import com.google.android.common.http.UrlRules;

/* JADX INFO: loaded from: classes.dex */
public class UrlTools {
    public static String rewrite(Context context, String url) {
        UrlRules rules = UrlRules.getRules(context.getContentResolver());
        UrlRules.Rule rule = rules.matchRule(url);
        return rule.apply(url);
    }
}
