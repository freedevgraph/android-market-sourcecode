package com.google.android.finsky.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.android.finsky.R;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.layout.TrackList;
import com.google.android.finsky.model.Bucket;
import com.google.android.finsky.model.Track;
import com.google.android.finsky.navigationmanager.NavigationManager;
import com.google.android.finsky.remoting.protos.DocList;
import com.google.android.finsky.utils.BitmapLoader;
import com.google.android.finsky.utils.CorpusMetadata;
import com.google.android.finsky.utils.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class BucketAdapter extends DocumentBasedAdapter {
    private final int mBucketEntryIconHeight;
    private final List<Bucket> mBuckets;
    private final int mColumns;
    private final int mHeaderIconHeight;
    private final int mHeaderIconWidth;
    private final String mOriginalQuery;
    private final int mRows;

    public BucketAdapter(Context context, NavigationManager navManager, BitmapLoader loader, List<DocList.Bucket> list, int columns, int rows, String searchQueryTerm, boolean showIndividualRatings, boolean showResultCount) {
        super(context, navManager, null, showIndividualRatings, searchQueryTerm != null, loader, false, showResultCount);
        this.mColumns = columns;
        this.mRows = rows;
        Resources res = context.getResources();
        this.mBucketEntryIconHeight = res.getDimensionPixelSize(R.dimen.bucket_entry_icon_height);
        this.mHeaderIconWidth = res.getDimensionPixelSize(R.dimen.bucket_header_icon_width);
        this.mHeaderIconHeight = res.getDimensionPixelSize(R.dimen.bucket_header_icon_height);
        this.mBuckets = Bucket.fromProtos(list);
        this.mOriginalQuery = searchQueryTerm;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mBuckets.size();
    }

    @Override // android.widget.Adapter
    public Bucket getItem(int position) {
        return this.mBuckets.get(position);
    }

    private void bindSongsListBucket(Bucket bucketInfo, ViewGroup bucket) {
        ViewGroup bucketItemHolder = (ViewGroup) bucket.findViewById(R.id.bucket_row_holder);
        TrackList tlist = new TrackList(this.mContext, null);
        List<Track> tracks = getMockTracks();
        tlist.setTracks(tracks);
        bucketItemHolder.removeAllViews();
        bucketItemHolder.addView(tlist);
    }

    private List<Track> getMockTracks() {
        IOException e;
        List<Track> tracks = Lists.newArrayList();
        int count = 1;
        InputStream is = this.mContext.getResources().openRawResource(R.raw.tracks);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while (true) {
            try {
                int count2 = count;
                String readLine = br.readLine();
                if (readLine == null) {
                    break;
                }
                Track ti = new Track();
                ti.docId = "1234";
                ti.title = readLine;
                ti.album = "This Is It";
                ti.artist = br.readLine();
                count = count2 + 1;
                try {
                    ti.trackNo = count2;
                    ti.year = Integer.parseInt(br.readLine());
                    ti.length = br.readLine();
                    ti.price = br.readLine();
                    ti.url = br.readLine();
                    ti.mode = Track.TrackMode.READY;
                    tracks.add(ti);
                    br.readLine();
                } catch (IOException e2) {
                    e = e2;
                    e.printStackTrace();
                    return tracks;
                }
            } catch (IOException e3) {
                e = e3;
            }
        }
        return tracks;
    }

    private void bindBucketEntry(Bucket bucket, int itemIndex, ViewGroup holder) {
        Document doc = new Document(bucket.getItem(itemIndex), bucket.getCookie());
        int iconWidth = CorpusMetadata.getIconWidth(this.mContext, doc.getBackend());
        bindDocument(doc, holder, iconWidth, this.mBucketEntryIconHeight);
    }

    private int getDisplayedRows(Bucket bucket) {
        return Math.min((int) Math.ceil(((double) bucket.getItemCount()) / ((double) this.mColumns)), this.mRows);
    }

    private int getDisplayedColumns(Bucket bucket, int row) {
        if (row < getDisplayedRows(bucket)) {
            return Math.min(bucket.getItemCount() - (this.mColumns * row), this.mColumns);
        }
        return 0;
    }

    private void bindBucketEntries(Bucket bucket, ViewGroup view) {
        LinearLayout bucketRowHolder = (LinearLayout) view.findViewById(R.id.bucket_row_holder);
        for (int row = 0; row < this.mRows; row++) {
            LinearLayout rowOfDocuments = (LinearLayout) bucketRowHolder.getChildAt(row);
            if (row >= getDisplayedRows(bucket)) {
                rowOfDocuments.setVisibility(8);
            } else {
                rowOfDocuments.setVisibility(0);
                int displayedColumns = getDisplayedColumns(bucket, row);
                for (int column = 0; column < this.mColumns; column++) {
                    View docEntry = rowOfDocuments.getChildAt(column);
                    if (column < displayedColumns) {
                        bindBucketEntry(bucket, (this.mColumns * row) + column, (ViewGroup) docEntry);
                        docEntry.setVisibility(0);
                    } else {
                        docEntry.setVisibility(4);
                    }
                }
            }
        }
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Bucket bucket = getItem(position);
        if (convertView == null) {
            convertView = inflateViewForBucket(bucket, parent);
        }
        bindView(bucket, (ViewGroup) convertView);
        return convertView;
    }

    private void bindView(Bucket bucket, ViewGroup view) {
        if (bucket.isSongsList()) {
            bindSongsListBucket(bucket, view);
        } else {
            bindBucketEntries(bucket, view);
        }
        bindBucketHeader(bucket, view, this.mOriginalQuery, this.mHeaderIconWidth, this.mHeaderIconHeight);
    }

    private View inflateViewForBucket(Bucket bucket, ViewGroup parent) {
        View view = inflate(R.layout.bucket_single, parent, false);
        ViewGroup viewGroup = (ViewGroup) view.findViewById(R.id.bucket_row_holder);
        if (bucket.isSongsList()) {
            TrackList tlist = new TrackList(this.mContext, null);
            List<Track> tracks = getMockTracks();
            tlist.setTracks(tracks);
            viewGroup.addView(tlist);
        } else {
            for (int row = 0; row < this.mRows; row++) {
                LinearLayout rowOfBucketEntries = (LinearLayout) inflate(R.layout.bucket_row, null, false);
                for (int i = 0; i < this.mColumns; i++) {
                    ViewGroup emptyEntry = (ViewGroup) inflate(R.layout.search_bucket_entry, rowOfBucketEntries, false);
                    emptyEntry.setVisibility(8);
                    rowOfBucketEntries.addView(emptyEntry);
                }
                viewGroup.addView(rowOfBucketEntries);
            }
        }
        return view;
    }

    @Override // com.google.android.finsky.adapters.PaginatedListAdapter
    protected String getLastRequestError() {
        return null;
    }

    @Override // com.google.android.finsky.adapters.PaginatedListAdapter
    protected boolean isMoreDataAvailable() {
        return false;
    }

    @Override // com.google.android.finsky.adapters.PaginatedListAdapter
    protected void retryLoadingItems() {
    }
}
