package com.google.android.apps.analytics;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import com.google.android.apps.analytics.Dispatcher;
import com.google.android.apps.analytics.PersistentEventStore;

/* JADX INFO: loaded from: classes.dex */
public class GoogleAnalyticsTracker {
    private static final GoogleAnalyticsTracker INSTANCE = new GoogleAnalyticsTracker();
    private String accountId;
    private ConnectivityManager connetivityManager;
    private CustomVariableBuffer customVariableBuffer;
    private int dispatchPeriod;
    private Dispatcher dispatcher;
    private boolean dispatcherIsBusy;
    private EventStore eventStore;
    private Handler handler;
    private Context parent;
    private boolean powerSaveMode;
    private String userAgentProduct = "GoogleAnalytics";
    private String userAgentVersion = "1.1";
    private Runnable dispatchRunner = new Runnable() { // from class: com.google.android.apps.analytics.GoogleAnalyticsTracker.1
        @Override // java.lang.Runnable
        public void run() {
            GoogleAnalyticsTracker.this.dispatch();
        }
    };

    final class DispatcherCallbacks implements Dispatcher.Callbacks {
        DispatcherCallbacks() {
        }

        @Override // com.google.android.apps.analytics.Dispatcher.Callbacks
        public void dispatchFinished() {
            GoogleAnalyticsTracker.this.handler.post(new Runnable() { // from class: com.google.android.apps.analytics.GoogleAnalyticsTracker.DispatcherCallbacks.1
                @Override // java.lang.Runnable
                public void run() {
                    GoogleAnalyticsTracker.this.dispatchFinished();
                }
            });
        }

        @Override // com.google.android.apps.analytics.Dispatcher.Callbacks
        public void eventDispatched(long j) {
            GoogleAnalyticsTracker.this.eventStore.deleteEvent(j);
        }
    }

    private GoogleAnalyticsTracker() {
    }

    private void cancelPendingDispatches() {
        this.handler.removeCallbacks(this.dispatchRunner);
    }

    private void createEvent(String str, String str2, String str3, String str4, int i) {
        Event event = new Event(this.eventStore.getStoreId(), str, str2, str3, str4, i, this.parent.getResources().getDisplayMetrics().widthPixels, this.parent.getResources().getDisplayMetrics().heightPixels);
        event.setCustomVariableBuffer(this.customVariableBuffer);
        this.customVariableBuffer = new CustomVariableBuffer();
        this.eventStore.putEvent(event);
        resetPowerSaveMode();
    }

    public static GoogleAnalyticsTracker getInstance() {
        return INSTANCE;
    }

    private void maybeScheduleNextDispatch() {
        if (this.dispatchPeriod >= 0 && this.handler.postDelayed(this.dispatchRunner, this.dispatchPeriod * 1000)) {
        }
    }

    private void resetPowerSaveMode() {
        if (this.powerSaveMode) {
            this.powerSaveMode = false;
            maybeScheduleNextDispatch();
        }
    }

    public boolean dispatch() {
        if (this.dispatcherIsBusy) {
            maybeScheduleNextDispatch();
            return false;
        }
        NetworkInfo activeNetworkInfo = this.connetivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
            maybeScheduleNextDispatch();
            return false;
        }
        if (this.eventStore.getNumStoredEvents() == 0) {
            this.powerSaveMode = true;
            return false;
        }
        this.dispatcher.dispatchEvents(this.eventStore.peekEvents());
        this.dispatcherIsBusy = true;
        maybeScheduleNextDispatch();
        return true;
    }

    void dispatchFinished() {
        this.dispatcherIsBusy = false;
    }

    public void setDispatchPeriod(int i) {
        int i2 = this.dispatchPeriod;
        this.dispatchPeriod = i;
        if (i2 <= 0) {
            maybeScheduleNextDispatch();
        } else if (i2 > 0) {
            cancelPendingDispatches();
            maybeScheduleNextDispatch();
        }
    }

    public void start(String str, int i, Context context) {
        start(str, i, context, this.eventStore == null ? new PersistentEventStore(new PersistentEventStore.DataBaseHelper(context)) : this.eventStore, this.dispatcher == null ? new NetworkDispatcher(this.userAgentProduct, this.userAgentVersion) : this.dispatcher);
    }

    void start(String str, int i, Context context, EventStore eventStore, Dispatcher dispatcher) {
        start(str, i, context, eventStore, dispatcher, new DispatcherCallbacks());
    }

    void start(String str, int i, Context context, EventStore eventStore, Dispatcher dispatcher, Dispatcher.Callbacks callbacks) {
        this.accountId = str;
        this.parent = context;
        this.eventStore = eventStore;
        this.eventStore.startNewVisit();
        this.dispatcher = dispatcher;
        this.dispatcher.init(callbacks, this.eventStore.getReferrer());
        this.dispatcherIsBusy = false;
        if (this.connetivityManager == null) {
            this.connetivityManager = (ConnectivityManager) this.parent.getSystemService("connectivity");
        }
        if (this.handler == null) {
            this.handler = new Handler(context.getMainLooper());
        } else {
            cancelPendingDispatches();
        }
        setDispatchPeriod(i);
    }

    public void trackEvent(String str, String str2, String str3, int i) {
        createEvent(this.accountId, str, str2, str3, i);
    }

    public void trackPageView(String str) {
        createEvent(this.accountId, "__##GOOGLEPAGEVIEW##__", str, null, -1);
    }
}
