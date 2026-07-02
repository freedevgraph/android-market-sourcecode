package com.google.android.finsky.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.finsky.R;
import com.google.android.finsky.api.DfeApi;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.layout.ThumbnailListener;
import com.google.android.finsky.navigationmanager.NavigationManager;
import com.google.android.finsky.remoting.protos.DeviceDoc;
import com.google.android.finsky.utils.BitmapLoader;
import com.google.android.finsky.utils.IntentUtils;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DetailsTrailerViewBinder extends TableLayoutViewBinder<DeviceDoc.Trailer> {
    private BitmapLoader mBitmapLoader;
    private int mColumns;
    private List<DeviceDoc.Trailer> mMovieTrailers;

    public void init(Context context, DfeApi api, NavigationManager navManager, BitmapLoader bitmapLoader) {
        super.init(context, api, navManager);
        this.mBitmapLoader = bitmapLoader;
    }

    public void bind(View view, Document doc, int columns) {
        super.bind(view, doc, R.id.header, R.string.details_trailers);
        this.mMovieTrailers = doc.getMovieTrailers();
        if (this.mMovieTrailers == null || this.mMovieTrailers.size() == 0) {
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
        return this.mMovieTrailers.size();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    public DeviceDoc.Trailer getData(int position) {
        return this.mMovieTrailers.get(position);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    public void setupTableCell(final DeviceDoc.Trailer data, ViewGroup view) {
        if (data != null) {
            TextView titleView = (TextView) view.findViewById(R.id.video_title);
            titleView.setText(data.getTitle());
            TextView durationView = (TextView) view.findViewById(R.id.video_duration);
            durationView.setText(data.getDuration());
            String thumbnailUrl = data.getThumbnailUrl();
            if (thumbnailUrl != null) {
                ImageView thumbnailView = (ImageView) view.findViewById(R.id.preview_image);
                this.mBitmapLoader.getOrBindImmediately(thumbnailUrl, thumbnailView, new ThumbnailListener(thumbnailView, true));
            }
            view.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.finsky.fragments.DetailsTrailerViewBinder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    Intent intent = IntentUtils.createYouTubeIntentForUrl(DetailsTrailerViewBinder.this.mContext.getPackageManager(), Uri.parse(data.getWatchUrl()));
                    DetailsTrailerViewBinder.this.mContext.startActivity(intent);
                }
            });
        }
    }

    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    protected int getTableCellLayoutId() {
        return R.layout.video_item;
    }
}
