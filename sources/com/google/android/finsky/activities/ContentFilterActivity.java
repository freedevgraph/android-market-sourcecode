package com.google.android.finsky.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.finsky.FinskyApp;
import com.google.android.finsky.R;
import com.google.android.finsky.analytics.Analytics;
import com.google.android.finsky.config.G;
import com.google.android.finsky.utils.Utils;

/* JADX INFO: loaded from: classes.dex */
public class ContentFilterActivity extends Activity {
    private InputMethodManager mInputMethodManager;
    private int mLevel;
    private ListView mListView;
    private boolean mLock;
    private ImageView mLockView;
    private int mMode = 0;
    private TextView mMoreInfoView;
    private String mPasscode;
    private EditText mPasscodeView;
    private SharedPreferences mSettings;
    private TextView mTitleView;

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(7);
        setContentView(R.layout.content_filter_activity);
        getWindow().setFeatureInt(7, R.layout.content_filter_title);
        this.mSettings = getSharedPreferences("finsky", 0);
        this.mInputMethodManager = (InputMethodManager) getSystemService("input_method");
        this.mLevel = this.mSettings.getInt(Utils.getPreferenceKey("content-filter-level"), G.defaultContentFilterLevel.get().intValue());
        this.mPasscode = this.mSettings.getString(Utils.getPreferenceKey("content-filter-passcode"), null);
        if (this.mPasscode != null) {
            this.mLock = true;
        }
        this.mTitleView = (TextView) findViewById(R.id.title);
        this.mLockView = (ImageView) findViewById(R.id.lock);
        this.mListView = (ListView) findViewById(R.id.list);
        this.mMoreInfoView = (TextView) findViewById(R.id.more_info);
        this.mPasscodeView = (EditText) findViewById(R.id.passcode);
        setupViews();
        setResult(0);
    }

    private void setupViews() {
        this.mLockView.setImageResource(this.mLock ? R.drawable.ic_locked_holo_light : R.drawable.ic_unlocked_holo_light);
        this.mLockView.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.finsky.activities.ContentFilterActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ContentFilterActivity.this.switchMode();
            }
        });
        String[] contentFilters = new String[5];
        for (int i = 0; i < 5; i++) {
            contentFilters[i] = getLabel(this, i);
        }
        this.mListView.setAdapter((ListAdapter) new ArrayAdapter(this, R.layout.list_item_multiple_choice, contentFilters));
        this.mListView.setChoiceMode(2);
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.google.android.finsky.activities.ContentFilterActivity.2
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (ContentFilterActivity.this.mLock) {
                    position = ContentFilterActivity.this.mLevel;
                } else if (position == 4) {
                    position--;
                }
                for (int i2 = position + 1; i2 < 5; i2++) {
                    ContentFilterActivity.this.mListView.setItemChecked(i2, false);
                }
                for (int i3 = 0; i3 <= position; i3++) {
                    ContentFilterActivity.this.mListView.setItemChecked(i3, true);
                }
                ContentFilterActivity.this.mListView.setItemChecked(4, ContentFilterActivity.this.mListView.isItemChecked(3));
                ContentFilterActivity.this.mLevel = position;
            }
        });
        for (int i2 = 0; i2 <= this.mLevel; i2++) {
            this.mListView.setItemChecked(i2, true);
        }
        this.mListView.setItemChecked(4, this.mListView.isItemChecked(3));
        String moreInfoText = String.format("%s <a href='%s'>%s</a>", getString(R.string.info_about_content_filter), G.contentFilterInfoUrl.get(), getString(R.string.more_about_content_filter));
        this.mMoreInfoView.setText(Html.fromHtml(moreInfoText));
        this.mMoreInfoView.setMovementMethod(LinkMovementMethod.getInstance());
        View okButton = findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.finsky.activities.ContentFilterActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ContentFilterActivity.this.performPositiveAction();
            }
        });
        View cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.finsky.activities.ContentFilterActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                switch (ContentFilterActivity.this.mMode) {
                    case 0:
                        ContentFilterActivity.this.finish();
                        break;
                    case 1:
                        ContentFilterActivity.this.switchMode();
                        break;
                }
            }
        });
        this.mPasscodeView.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.google.android.finsky.activities.ContentFilterActivity.5
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != 0) {
                    return false;
                }
                ContentFilterActivity.this.performPositiveAction();
                return true;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performPositiveAction() {
        switch (this.mMode) {
            case 0:
                this.mSettings.edit().putInt(Utils.getPreferenceKey("content-filter-level"), this.mLevel).apply();
                FinskyApp.get().getAnalytics().reportEvent(Analytics.Event.CONTENT_FILTER, "", Integer.toString(this.mLevel));
                setResult(-1);
                finish();
                break;
            case 1:
                if (this.mLock) {
                    if (this.mPasscode.equals(this.mPasscodeView.getText().toString())) {
                        this.mLock = false;
                        switchMode();
                    } else {
                        this.mPasscodeView.setText("");
                        this.mPasscodeView.setHint(R.string.content_filter_wrong_passcode);
                    }
                } else {
                    this.mPasscode = this.mPasscodeView.getText().toString();
                    this.mSettings.edit().putString(Utils.getPreferenceKey("content-filter-passcode"), this.mPasscode).apply();
                    this.mLock = true;
                    switchMode();
                }
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchMode() {
        if (this.mMode == 0) {
            this.mMode = 1;
        } else {
            this.mMode = 0;
        }
        if (this.mMode == 0) {
            this.mInputMethodManager.hideSoftInputFromWindow(this.mPasscodeView.getWindowToken(), 0);
            this.mTitleView.setText(R.string.content_filter_title);
            this.mLockView.setImageResource(this.mLock ? R.drawable.ic_locked_holo_light : R.drawable.ic_unlocked_holo_light);
            this.mListView.setVisibility(0);
            this.mMoreInfoView.setVisibility(0);
            this.mPasscodeView.setText("");
            this.mPasscodeView.setVisibility(8);
            return;
        }
        this.mTitleView.setText(this.mLock ? R.string.content_filter_unlock : R.string.content_filter_lock);
        this.mLockView.setImageResource(this.mLock ? R.drawable.ic_unlocked_holo_light : R.drawable.ic_locked_holo_light);
        this.mListView.setVisibility(8);
        this.mMoreInfoView.setVisibility(8);
        this.mPasscodeView.setVisibility(0);
        this.mPasscodeView.setHint(R.string.content_filter_enter_passcode);
        this.mPasscodeView.requestFocus();
    }

    public static String getLabel(Context context, int level) {
        Resources res = context.getResources();
        switch (level) {
            case -1:
                return res.getString(R.string.no_movie_rating);
            case 0:
                return res.getString(R.string.content_filter_everyone);
            case 1:
                return res.getString(R.string.content_filter_low_maturity);
            case 2:
                return res.getString(R.string.content_filter_medium_maturity);
            case 3:
                return res.getString(R.string.content_filter_high_maturity);
            case 4:
                return res.getString(R.string.content_filter_show_all_apps);
            default:
                return null;
        }
    }
}
