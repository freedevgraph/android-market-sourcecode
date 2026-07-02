package com.google.android.finsky.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/* JADX INFO: loaded from: classes.dex */
public class QsbSearchActivity extends Activity {
    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        if (intent != null && "android.intent.action.PICK".equals(intent.getAction())) {
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
