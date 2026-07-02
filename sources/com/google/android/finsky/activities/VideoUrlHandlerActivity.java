package com.google.android.finsky.activities;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.finsky.utils.IntentUtils;

/* JADX INFO: loaded from: classes.dex */
public class VideoUrlHandlerActivity extends Activity {
    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentUtils.forwardIntentToMainActivity(this, getIntent());
        finish();
    }
}
