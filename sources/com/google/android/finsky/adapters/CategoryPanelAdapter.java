package com.google.android.finsky.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.google.android.finsky.R;
import com.google.android.finsky.remoting.protos.Browse;
import com.google.android.finsky.utils.BitmapLoader;
import com.google.android.finsky.utils.CorpusMetadata;

/* JADX INFO: loaded from: classes.dex */
public class CategoryPanelAdapter extends ArrayAdapter<Browse.BrowseLink> {
    private final LayoutInflater mLayoutInflater;
    private final int mTextColor;

    public CategoryPanelAdapter(Context context, BitmapLoader bitmapLoader, int channelId, int textViewResourceId) {
        super(context, textViewResourceId);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mTextColor = CorpusMetadata.getBackendHintColor(context, channelId);
    }

    public String getCategoryUrl(int position) {
        return getItem(position).getDataUrl();
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.mLayoutInflater.inflate(R.layout.category_panel_item, parent, false);
        }
        Browse.BrowseLink category = getItem(position);
        TextView title = (TextView) convertView.findViewById(R.id.category_title);
        title.setText(category.getName().toUpperCase());
        title.setTextColor(this.mTextColor);
        return convertView;
    }
}
