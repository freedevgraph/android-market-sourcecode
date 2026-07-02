package com.google.android.finsky.model;

/* JADX INFO: loaded from: classes.dex */
public class Track {
    public String album;
    public String artist;
    public String docId;
    public String length;
    public TrackMode mode;
    public String price;
    public String title;
    public int trackNo;
    public String url;
    public int year;

    public enum TrackMode {
        READY,
        LOADING,
        PLAYING,
        PAUSE
    }
}
