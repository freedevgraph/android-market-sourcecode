package com.google.android.finsky.api.model;

import com.google.android.finsky.remoting.protos.Toc;
import com.google.android.finsky.utils.Lists;
import com.google.android.finsky.utils.Maps;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class DfeToc extends DfeModel {
    private final Toc.TocResponse mToc;
    private int mSelectedBackendId = 3;
    private final Map<Integer, Toc.CorpusMetadata> mCorpusMap = Maps.newLinkedHashMap();

    public DfeToc(Toc.TocResponse response) {
        this.mToc = response;
        for (Toc.CorpusMetadata metadata : this.mToc.getCorpusList()) {
            this.mCorpusMap.put(Integer.valueOf(metadata.getBackend()), metadata);
        }
    }

    public List<Toc.CorpusMetadata> getCorpusList() {
        List<Toc.CorpusMetadata> list = Lists.newArrayList();
        list.addAll(this.mCorpusMap.values());
        return list;
    }

    public String getTosContent() {
        return this.mToc.getTosContent();
    }
}
