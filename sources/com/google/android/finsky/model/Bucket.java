package com.google.android.finsky.model;

import android.text.TextUtils;
import com.google.android.finsky.remoting.protos.DeviceDoc;
import com.google.android.finsky.remoting.protos.DocList;
import com.google.android.finsky.utils.Lists;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class Bucket {
    private final DocList.Bucket mBucket;

    public static List<Bucket> fromProtos(List<DocList.Bucket> bucketList) {
        List<Bucket> list = Lists.newArrayList();
        for (DocList.Bucket bucket : bucketList) {
            list.add(new Bucket(bucket));
        }
        return list;
    }

    public Bucket(DocList.Bucket bucket) {
        this.mBucket = bucket;
    }

    public int getBackend() {
        if (this.mBucket.getMultiCorpus()) {
            return 0;
        }
        if (this.mBucket.getDocumentCount() == 0) {
            return -1;
        }
        return this.mBucket.getDocument(0).getFinskyDoc().getDocid().getBackend();
    }

    public int getEstimatedResults() {
        return (int) this.mBucket.getEstimatedResults();
    }

    public String getHeaderText() {
        return this.mBucket.getTitle();
    }

    public boolean isSongsList() {
        return this.mBucket.getDocumentCount() != 0 && this.mBucket.getDocument(0).getFinskyDoc().getDocid().getType() == 4;
    }

    public String getHeaderUrl() {
        return this.mBucket.getFullContentsUrl();
    }

    public int getItemCount() {
        return this.mBucket.getDocumentCount();
    }

    public DeviceDoc.DeviceDocument getItem(int index) {
        return this.mBucket.getDocument(index);
    }

    public boolean hasMoreItems() {
        return !TextUtils.isEmpty(this.mBucket.getFullContentsUrl());
    }

    public String getIconUrl() {
        return this.mBucket.getIconUrl();
    }

    public String getCookie() {
        return this.mBucket.getAnalyticsCookie();
    }
}
