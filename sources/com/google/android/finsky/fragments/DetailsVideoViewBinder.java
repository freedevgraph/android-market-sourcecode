package com.google.android.finsky.fragments;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.finsky.R;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.remoting.protos.Doc;
import com.google.android.finsky.utils.IntentUtils;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DetailsVideoViewBinder extends TableLayoutViewBinder<String> {
    private int mColumns;
    private List<Doc.Image> mVideoPreviews;

    public void bind(View view, Document doc, int columns) {
        super.bind(view, doc, R.id.header, R.string.details_videos);
        this.mVideoPreviews = doc.hasVideos() ? doc.getImages(3) : null;
        if (this.mVideoPreviews == null) {
            this.mLayout.setVisibility(8);
            return;
        }
        this.mLayout.setVisibility(0);
        this.mColumns = columns;
        populateTable();
    }

    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    protected int getTableColumnCount() {
        return this.mColumns;
    }

    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    protected int getCellCount() {
        return this.mVideoPreviews.size();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    public String getData(int position) {
        return this.mVideoPreviews.get(position).getImageUrl();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    public void setupTableCell(final String data, ViewGroup view) {
        if (!TextUtils.isEmpty(data)) {
            view.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.finsky.fragments.DetailsVideoViewBinder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    Intent intent = IntentUtils.createYouTubeIntentForUrl(DetailsVideoViewBinder.this.mContext.getPackageManager(), Uri.parse(data));
                    DetailsVideoViewBinder.this.mContext.startActivity(intent);
                }
            });
        }
    }

    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    protected int getTableCellLayoutId() {
        return R.layout.video_item;
    }
}
