package com.google.android.apps.analytics;

/* JADX INFO: loaded from: classes.dex */
interface Dispatcher {

    public interface Callbacks {
        void dispatchFinished();

        void eventDispatched(long j);
    }

    void dispatchEvents(Event[] eventArr);

    void init(Callbacks callbacks, String str);
}
