package com.google.android.finsky.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.finsky.R;
import com.google.android.finsky.model.Track;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class TrackListAdapter extends ArrayAdapter<Track> {
    private View.OnClickListener mOnClickListener;
    private Drawable mPauseIcon;
    private Drawable mPlayIcon;

    protected static final class TrackViewHolder {
        public final TextView album;
        public final TextView artist;
        public final TextView length;
        public final ProgressBar loadingSpinner;
        public final FrameLayout playButtonLayout;
        public final ImageView playIcon;
        public final TextView priceButton;
        public final TextView title;
        public final TextView trackNumber;
        public String url;
        public final TextView year;

        protected TrackViewHolder(View view) {
            this.playButtonLayout = (FrameLayout) view.findViewById(R.id.play_button_layout);
            this.loadingSpinner = (ProgressBar) view.findViewById(R.id.loading_spinner);
            this.playIcon = (ImageView) view.findViewById(R.id.play_button);
            this.trackNumber = (TextView) view.findViewById(R.id.song_number);
            this.title = (TextView) view.findViewById(R.id.song_title);
            this.album = (TextView) view.findViewById(R.id.song_album);
            this.year = (TextView) view.findViewById(R.id.song_year);
            this.artist = (TextView) view.findViewById(R.id.song_artist);
            this.length = (TextView) view.findViewById(R.id.song_length);
            this.priceButton = (TextView) view.findViewById(R.id.song_buy);
        }
    }

    public TrackListAdapter(Context context, int textViewResourceId, List<Track> objects, View.OnClickListener onClickListener) {
        super(context, textViewResourceId, 0, objects);
        this.mOnClickListener = onClickListener;
        Resources res = context.getResources();
        this.mPlayIcon = res.getDrawable(R.drawable.play);
        this.mPauseIcon = res.getDrawable(R.drawable.pause);
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService("layout_inflater");
            v = vi.inflate(R.layout.list_song_item, parent, false);
        }
        Track track = (Track) getItem(position);
        TrackViewHolder holder = (TrackViewHolder) v.getTag();
        if (holder == null) {
            holder = new TrackViewHolder(v);
            v.setTag(holder);
        }
        setTrackView(v, track.mode);
        holder.url = track.url;
        holder.trackNumber.setText(Integer.toString(track.trackNo));
        holder.title.setText(track.title);
        holder.album.setText(track.album);
        holder.year.setText(Integer.toString(track.year));
        holder.artist.setText(track.artist);
        holder.length.setText(track.length);
        holder.priceButton.setText(track.price);
        holder.priceButton.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.finsky.adapters.TrackListAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v2) {
            }
        });
        holder.playButtonLayout.setOnClickListener(this.mOnClickListener);
        holder.trackNumber.setOnClickListener(this.mOnClickListener);
        holder.title.setOnClickListener(this.mOnClickListener);
        holder.album.setOnClickListener(this.mOnClickListener);
        holder.year.setOnClickListener(this.mOnClickListener);
        holder.artist.setOnClickListener(this.mOnClickListener);
        holder.length.setOnClickListener(this.mOnClickListener);
        holder.album.setOnClickListener(this.mOnClickListener);
        return v;
    }

    private void setTrackView(View trackView, Track.TrackMode mode) {
        if (trackView != null) {
            TrackViewHolder holder = (TrackViewHolder) trackView.getTag();
            switch (mode) {
                case READY:
                    holder.loadingSpinner.setVisibility(8);
                    holder.playIcon.setVisibility(0);
                    holder.playIcon.setImageDrawable(this.mPlayIcon);
                    trackView.setBackgroundColor(0);
                    return;
                case PAUSE:
                    holder.loadingSpinner.setVisibility(8);
                    holder.playIcon.setVisibility(0);
                    holder.playIcon.setImageDrawable(this.mPlayIcon);
                    trackView.setBackgroundColor(-7829368);
                    return;
                case LOADING:
                    holder.loadingSpinner.setVisibility(0);
                    holder.playIcon.setVisibility(8);
                    trackView.setBackgroundColor(-7829368);
                    return;
                case PLAYING:
                    holder.loadingSpinner.setVisibility(8);
                    holder.playIcon.setVisibility(0);
                    holder.playIcon.setImageDrawable(this.mPauseIcon);
                    trackView.setBackgroundColor(-7829368);
                    return;
                default:
                    throw new IllegalStateException("Cannot have TrackDisplayMode: " + mode);
            }
        }
    }
}
