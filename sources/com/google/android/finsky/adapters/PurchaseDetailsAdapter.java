package com.google.android.finsky.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.google.android.finsky.R;

/* JADX INFO: loaded from: classes.dex */
public abstract class PurchaseDetailsAdapter extends BaseAdapter {
    private Context mContext;

    public static class DetailsEntry {
        public String contentText;
        public String headerText;
    }

    @Override // android.widget.Adapter
    public abstract DetailsEntry getItem(int i);

    private static class DetailsEntryViewHolder {
        TextView contentView;
        TextView headerView;

        private DetailsEntryViewHolder() {
        }
    }

    public PurchaseDetailsAdapter(Context context) {
        this.mContext = context;
    }

    @Override // android.widget.BaseAdapter, android.widget.ListAdapter
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override // android.widget.BaseAdapter, android.widget.ListAdapter
    public boolean isEnabled(int position) {
        return false;
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return position;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View view, ViewGroup parent) {
        DetailsEntryViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.purchase_detail_row, (ViewGroup) null);
            holder = new DetailsEntryViewHolder();
            holder.headerView = (TextView) view.findViewById(R.id.header);
            holder.contentView = (TextView) view.findViewById(R.id.content);
            view.setTag(holder);
        } else {
            holder = (DetailsEntryViewHolder) view.getTag();
        }
        DetailsEntry entry = getItem(position);
        holder.headerView.setText(entry.headerText);
        holder.contentView.setText(entry.contentText);
        return view;
    }
}
