package com.google.android.finsky.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import com.google.android.finsky.R;
import com.google.android.finsky.utils.FinskyLog;

/* JADX INFO: loaded from: classes.dex */
public class TosActivity extends Activity implements View.OnClickListener {
    private String mAccount = null;
    private String mContent = null;

    public static Intent getIntent(Context context, String str, String str2) {
        Intent intent = new Intent(context, (Class<?>) TosActivity.class);
        intent.putExtra("finsky.TosActivity.account", str);
        intent.putExtra("finsky.TosActivity.tos", str2);
        return intent;
    }

    private static String makeTosKey(String account, String tosContent) {
        return account + ":" + tosContent.hashCode();
    }

    public static boolean requiresAcceptance(Context context, String account, String tosContent) {
        SharedPreferences settings = context.getSharedPreferences("finsky", 0);
        return !settings.getBoolean(makeTosKey(account, tosContent), false);
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        Bundle initBundle = savedState != null ? savedState : getIntent().getExtras();
        if (initBundle != null) {
            this.mAccount = initBundle.getString("finsky.TosActivity.account");
            this.mContent = initBundle.getString("finsky.TosActivity.tos");
        }
        if (this.mAccount == null || this.mContent == null) {
            FinskyLog.w("Bad request to Terms of Service activity.", new Object[0]);
            finish();
            return;
        }
        setContentView(R.layout.terms_of_service);
        findViewById(R.id.accept_button).setOnClickListener(this);
        findViewById(R.id.decline_button).setOnClickListener(this);
        TextView mainContent = (TextView) findViewById(R.id.content);
        mainContent.setMovementMethod(LinkMovementMethod.getInstance());
        mainContent.setText(Html.fromHtml(this.mContent));
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("finsky.TosActivity.account", this.mAccount);
        outState.putString("finsky.TosActivity.tos", this.mContent);
    }

    private void onAccepted() {
        SharedPreferences settings = getSharedPreferences("finsky", 0);
        settings.edit().putBoolean(makeTosKey(this.mAccount, this.mContent), true).commit();
        setResult(-1);
        finish();
    }

    private void onDeclined() {
        setResult(0);
        finish();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View target) {
        switch (target.getId()) {
            case R.id.accept_button /* 2131296421 */:
                onAccepted();
                break;
            case R.id.decline_button /* 2131296422 */:
                onDeclined();
                break;
        }
    }
}
