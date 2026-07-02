package com.google.android.apps.analytics;

/* JADX INFO: loaded from: classes.dex */
interface EventStore {
    void deleteEvent(long j);

    int getNumStoredEvents();

    String getReferrer();

    int getStoreId();

    Event[] peekEvents();

    void putEvent(Event event);

    void startNewVisit();
}
