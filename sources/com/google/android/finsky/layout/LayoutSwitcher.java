package com.google.android.finsky.layout;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.finsky.R;

/* JADX INFO: loaded from: classes.dex */
public class LayoutSwitcher {
    private final View mContentLayout;
    private int mDataLayoutId;
    private final int mErrorLayoutId;
    private final Handler mHandler;
    private final int mLoadingLayoutId;
    private int mMode;
    private boolean mPendingLoad;
    private final RetryButtonListener mRetryListener;
    private final View.OnClickListener retryClickListener;

    public interface RetryButtonListener {
        void onRetry();
    }

    public LayoutSwitcher(View pageLayout, int dataLayoutId, int errorLayoutId, int loadingLayoutId, RetryButtonListener listener) {
        this.mHandler = new Handler();
        this.retryClickListener = new View.OnClickListener() { // from class: com.google.android.finsky.layout.LayoutSwitcher.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                LayoutSwitcher.this.switchToLoadingMode();
                LayoutSwitcher.this.mRetryListener.onRetry();
            }
        };
        this.mPendingLoad = false;
        this.mDataLayoutId = dataLayoutId;
        this.mErrorLayoutId = errorLayoutId;
        this.mLoadingLayoutId = loadingLayoutId;
        this.mContentLayout = pageLayout;
        this.mRetryListener = listener;
        resetMode();
    }

    public LayoutSwitcher(View pageLayout, int dataLayoutId, int errorLayoutId, int loadingLayoutId, RetryButtonListener listener, int initialState) {
        this.mHandler = new Handler();
        this.retryClickListener = new View.OnClickListener() { // from class: com.google.android.finsky.layout.LayoutSwitcher.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                LayoutSwitcher.this.switchToLoadingMode();
                LayoutSwitcher.this.mRetryListener.onRetry();
            }
        };
        this.mPendingLoad = false;
        this.mDataLayoutId = dataLayoutId;
        this.mErrorLayoutId = errorLayoutId;
        this.mLoadingLayoutId = loadingLayoutId;
        this.mContentLayout = pageLayout;
        this.mRetryListener = listener;
        this.mMode = initialState;
    }

    public LayoutSwitcher(View pageLayout, int dataLayoutId, RetryButtonListener listener) {
        this(pageLayout, dataLayoutId, R.id.error_indicator, R.id.loading_indicator, listener);
    }

    private void resetMode() {
        this.mMode = 3;
        setLoadingVisible(false);
        setErrorVisible(false, null);
        setDataVisible(false);
    }

    public boolean isDataMode() {
        return this.mMode == 2;
    }

    public void switchToBlankMode() {
        performSwitch(3, null);
    }

    public void switchToLoadingMode() {
        performSwitch(0, null);
    }

    public void switchToLoadingDelayed(int delayMillis) {
        this.mPendingLoad = true;
        this.mHandler.postDelayed(new Runnable() { // from class: com.google.android.finsky.layout.LayoutSwitcher.2
            @Override // java.lang.Runnable
            public void run() {
                if (LayoutSwitcher.this.mPendingLoad) {
                    LayoutSwitcher.this.switchToLoadingMode();
                }
            }
        }, delayMillis);
    }

    public void switchToDataMode() {
        performSwitch(2, null);
    }

    public void switchToErrorMode(String error) {
        performSwitch(1, error);
    }

    private void performSwitch(int newMode, String errorMessage) {
        this.mPendingLoad = false;
        if (this.mMode != newMode) {
            switch (this.mMode) {
                case 0:
                    setLoadingVisible(false);
                    break;
                case 1:
                    setErrorVisible(false, null);
                    break;
                case 2:
                    setDataVisible(false);
                    break;
            }
            switch (newMode) {
                case 0:
                    setLoadingVisible(true);
                    break;
                case 1:
                    setErrorVisible(true, errorMessage);
                    break;
                case 2:
                    setDataVisible(true);
                    break;
                case 3:
                    break;
                default:
                    throw new IllegalStateException("Invalid mode " + newMode + "should be LOADING_MODE, ERROR_MODE, DATA_MODE, or BLANK_MODE");
            }
            this.mMode = newMode;
        }
    }

    private void setLoadingVisible(boolean show) {
        View loadingIndicators = this.mContentLayout.findViewById(this.mLoadingLayoutId);
        loadingIndicators.setVisibility(show ? 0 : 8);
    }

    private void setErrorVisible(boolean show, String errorMessage) {
        View errorIndicator = this.mContentLayout.findViewById(this.mErrorLayoutId);
        errorIndicator.setVisibility(show ? 0 : 8);
        if (show) {
            TextView textView = (TextView) errorIndicator.findViewById(R.id.error_msg);
            textView.setText(errorMessage);
        }
        Button retryButton = (Button) errorIndicator.findViewById(R.id.retry_button);
        retryButton.setOnClickListener(show ? this.retryClickListener : null);
    }

    private void setDataVisible(boolean show) {
        ViewGroup listView = (ViewGroup) this.mContentLayout.findViewById(this.mDataLayoutId);
        listView.setVisibility(show ? 0 : 8);
    }
}
