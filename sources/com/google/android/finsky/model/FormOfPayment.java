package com.google.android.finsky.model;

/* JADX INFO: loaded from: classes.dex */
public class FormOfPayment {
    private final String mCart;
    private boolean mHasCart;
    private final String mInstrumentId;
    private final String mName;

    public FormOfPayment(String name, String instrumentId, boolean hasCart, String cart) {
        this.mName = name;
        this.mInstrumentId = instrumentId;
        this.mCart = cart;
        this.mHasCart = hasCart;
    }

    public String getInstrumentId() {
        return this.mInstrumentId;
    }

    public String getCart() {
        return this.mCart;
    }

    public boolean hasCart() {
        return this.mHasCart;
    }

    public String toString() {
        return this.mName;
    }
}
