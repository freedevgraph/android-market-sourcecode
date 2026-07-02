package com.google.android.finsky.layout;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.finsky.R;
import com.google.android.finsky.adapters.TrackListAdapter;
import com.google.android.finsky.model.Track;
import com.google.android.finsky.utils.FinskyLog;
import java.io.IOException;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class TrackList extends LinearLayout implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private TrackListAdapter mAdapter;
    private PlaybackMode mCurrentPlaybackMode;
    private Track mCurrentTrack;
    private int mCurrentTrackIndex;
    private boolean mIsPreparing;
    private ListView mListView;
    private final View.OnClickListener mOnTrackClickListener;
    private MediaPlayer mPlayer;

    private enum PlaybackMode {
        PLAY_ONE_TRACK,
        PLAY_ALL_TRACKS
    }

    public TrackList(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mCurrentTrackIndex = -1;
        this.mIsPreparing = false;
        this.mOnTrackClickListener = new View.OnClickListener() { // from class: com.google.android.finsky.layout.TrackList.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                int position = TrackList.this.mListView.getPositionForView(v);
                TrackList.this.mCurrentPlaybackMode = PlaybackMode.PLAY_ONE_TRACK;
                if (position == TrackList.this.mCurrentTrackIndex) {
                    if (TrackList.this.mPlayer.isPlaying()) {
                        TrackList.this.mPlayer.pause();
                        TrackList.this.updateCurrentTrackMode(Track.TrackMode.PAUSE);
                        return;
                    } else {
                        if (!TrackList.this.mIsPreparing) {
                            TrackList.this.mPlayer.start();
                            TrackList.this.updateCurrentTrackMode(Track.TrackMode.PLAYING);
                            return;
                        }
                        return;
                    }
                }
                TrackList.this.playOneTrack(position);
            }
        };
        setOrientation(1);
    }

    public void setTracks(List<Track> tracks) {
        this.mCurrentPlaybackMode = PlaybackMode.PLAY_ONE_TRACK;
        setupLayout();
        this.mAdapter = new TrackListAdapter(getContext(), R.layout.list_song_item, tracks, this.mOnTrackClickListener);
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
    }

    private void setupLayout() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
        LinearLayout mPlayerControls = (LinearLayout) inflater.inflate(R.layout.media_player_controls, (ViewGroup) null);
        Button playAllButton = (Button) mPlayerControls.findViewById(R.id.play_all_button);
        playAllButton.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.finsky.layout.TrackList.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                TrackList.this.playAllTracks();
            }
        });
        Button prevButton = (Button) mPlayerControls.findViewById(R.id.prev_button);
        prevButton.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.finsky.layout.TrackList.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                TrackList.this.skipToSong(TrackList.this.mCurrentTrackIndex - 1);
            }
        });
        Button nextButton = (Button) mPlayerControls.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.finsky.layout.TrackList.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                TrackList.this.skipToSong(TrackList.this.mCurrentTrackIndex + 1);
            }
        });
        this.mListView = new ListView(getContext());
        this.mListView.setDivider(null);
        this.mListView.setFadingEdgeLength(0);
        this.mListView.setCacheColorHint(-3355444);
        addView(mPlayerControls);
        addView(this.mListView);
    }

    public void playAllTracks() {
        if (this.mCurrentPlaybackMode == PlaybackMode.PLAY_ALL_TRACKS) {
            resetMediaPlayer();
        }
        this.mCurrentPlaybackMode = PlaybackMode.PLAY_ALL_TRACKS;
        playOneTrack(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void skipToSong(int newIndex) {
        if (this.mCurrentPlaybackMode == PlaybackMode.PLAY_ALL_TRACKS) {
            if (newIndex >= 0 && newIndex < this.mAdapter.getCount()) {
                playOneTrack(newIndex);
                return;
            }
            resetMediaPlayer();
            updateCurrentTrackMode(Track.TrackMode.READY);
            this.mCurrentPlaybackMode = PlaybackMode.PLAY_ONE_TRACK;
            this.mCurrentTrack = null;
            this.mCurrentTrackIndex = -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playOneTrack(int newTrack) {
        if (!this.mIsPreparing || this.mCurrentTrackIndex != newTrack) {
            resetMediaPlayer();
            if (this.mCurrentTrackIndex >= 0) {
                updateCurrentTrackMode(Track.TrackMode.READY);
                this.mCurrentTrackIndex = -1;
                this.mCurrentTrack = null;
            }
            this.mCurrentTrackIndex = newTrack;
            this.mCurrentTrack = this.mAdapter.getItem(newTrack);
            String previewUrl = this.mCurrentTrack.url;
            try {
                this.mPlayer.setDataSource(previewUrl);
                this.mPlayer.prepareAsync();
                this.mIsPreparing = true;
            } catch (IOException e) {
                FinskyLog.e("Music preview playback error: %s", e.toString());
                Toast.makeText(getContext(), getContext().getString(R.string.song_playback_error), 0).show();
            } catch (IllegalStateException e2) {
                return;
            }
            updateCurrentTrackMode(Track.TrackMode.LOADING);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCurrentTrackMode(Track.TrackMode newMode) {
        if (this.mCurrentTrack != null) {
            if (newMode == Track.TrackMode.LOADING) {
                this.mListView.setSelection(this.mCurrentTrackIndex);
            }
            this.mCurrentTrack.mode = newMode;
            this.mAdapter.notifyDataSetChanged();
        }
    }

    private void resetMediaPlayer() {
        if (this.mPlayer.isPlaying()) {
            updateCurrentTrackMode(Track.TrackMode.READY);
            this.mPlayer.stop();
        }
        this.mPlayer.reset();
    }

    @Override // android.media.MediaPlayer.OnPreparedListener
    public void onPrepared(MediaPlayer mp) {
        this.mIsPreparing = false;
        mp.start();
        updateCurrentTrackMode(Track.TrackMode.PLAYING);
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mp) {
        if (this.mCurrentPlaybackMode == PlaybackMode.PLAY_ONE_TRACK) {
            updateCurrentTrackMode(Track.TrackMode.READY);
        } else {
            skipToSong(this.mCurrentTrackIndex + 1);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        this.mPlayer = new MediaPlayer();
        this.mPlayer.setOnPreparedListener(this);
        if (this.mAdapter != null) {
            this.mAdapter.notifyDataSetChanged();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        this.mPlayer.release();
        this.mIsPreparing = false;
        if (this.mCurrentTrack != null) {
            this.mCurrentTrack.mode = Track.TrackMode.READY;
        }
        this.mCurrentTrack = null;
        this.mCurrentTrackIndex = -1;
    }
}
