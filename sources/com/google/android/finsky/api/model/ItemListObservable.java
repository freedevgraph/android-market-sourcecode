package com.google.android.finsky.api.model;

import android.database.Observable;

/* JADX INFO: loaded from: classes.dex */
public class ItemListObservable extends Observable<OnDataChangedListener> {
    public void notifyListChanged() {
        for (int i = this.mObservers.size() - 1; i >= 0; i--) {
            ((OnDataChangedListener) this.mObservers.get(i)).onDataChanged();
        }
    }
}
