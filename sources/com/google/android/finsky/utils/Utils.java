package com.google.android.finsky.utils;

import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import com.google.android.finsky.FinskyApp;
import com.google.android.finsky.api.AccountHandler;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.navigationmanager.NavigationManager;
import java.net.MalformedURLException;
import java.net.URL;

/* JADX INFO: loaded from: classes.dex */
public class Utils {
    public static void ensureOnMainThread() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
        } else {
            throw new IllegalStateException("This method must be called from the UI thread.");
        }
    }

    public static String getPreferenceKey(String key) {
        return AccountHandler.getCurrentAccount() + ":" + key;
    }

    public static void checkUrlIsSecure(String url) {
        try {
            URL parsed = new URL(url);
            if (parsed.getProtocol().toLowerCase().equals("https")) {
                return;
            }
            if (parsed.getHost().toLowerCase().endsWith("corp.google.com")) {
                return;
            }
        } catch (MalformedURLException e) {
            FinskyLog.d("Cannot parse URL: " + url, new Object[0]);
        }
        throw new RuntimeException("Insecure URL: " + url);
    }

    public static void bindPurchaseButton(TextView button, Document doc, final NavigationManager nm) {
        final PurchaseButtonWrapper wrapper = new PurchaseButtonWrapper(doc, FinskyApp.get().getPackageInfoCache());
        int visibility = wrapper.getVisibility();
        button.setVisibility(visibility);
        if (visibility == 0) {
            button.setText(wrapper.getDisplayText(button.getContext()));
            button.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.finsky.utils.Utils.1
                @Override // android.view.View.OnClickListener
                public void onClick(View arg0) {
                    wrapper.performDefaultAction(nm);
                }
            });
        }
    }
}
