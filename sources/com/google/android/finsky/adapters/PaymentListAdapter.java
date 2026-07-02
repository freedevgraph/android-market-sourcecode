package com.google.android.finsky.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.google.android.finsky.model.FormOfPayment;

/* JADX INFO: loaded from: classes.dex */
public class PaymentListAdapter extends ArrayAdapter<FormOfPayment> {
    public PaymentListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override // android.widget.BaseAdapter, android.widget.ListAdapter
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override // android.widget.BaseAdapter, android.widget.ListAdapter
    public boolean isEnabled(int position) {
        FormOfPayment option = getItem(position);
        return option.hasCart();
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return 1;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int position) {
        return 1;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        checkEnabled(position, view);
        return view;
    }

    @Override // android.widget.ArrayAdapter, android.widget.BaseAdapter, android.widget.SpinnerAdapter
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        checkEnabled(position, view);
        return view;
    }

    private void checkEnabled(int position, View view) {
        FormOfPayment option = getItem(position);
        view.setEnabled(option.hasCart());
    }
}
