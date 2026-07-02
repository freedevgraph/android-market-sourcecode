package com.google.android.finsky.model;

import com.google.android.finsky.api.model.DfeToc;
import com.google.android.finsky.remoting.protos.Toc;
import com.google.android.finsky.utils.Lists;
import com.google.android.finsky.utils.Maps;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class ChannelList {
    private int mSelectedTabId = 3;
    private final Map<Integer, ChannelTab> mTabs = Maps.newLinkedHashMap();
    private final DfeToc mToc;

    public ChannelList(DfeToc response) {
        this.mToc = response;
        for (Toc.CorpusMetadata metadata : this.mToc.getCorpusList()) {
            ChannelTab tab = new ChannelTab(metadata.getName(), metadata.getBackend(), metadata.getLandingUrl(), null, null);
            this.mTabs.put(Integer.valueOf(metadata.getBackend()), tab);
        }
    }

    public List<ChannelTab> getTabs() {
        List<ChannelTab> list = Lists.newArrayList();
        list.addAll(this.mTabs.values());
        return list;
    }

    public ChannelTab getTab(int id) {
        return this.mTabs.get(Integer.valueOf(id));
    }

    public int getSelectedTabId() {
        return this.mSelectedTabId;
    }

    public void selectTabWithId(int id) {
        this.mSelectedTabId = id;
    }

    public int getIndexForBackendId(int id) {
        int index = 0;
        for (Toc.CorpusMetadata metadata : this.mToc.getCorpusList()) {
            if (metadata.getBackend() == id) {
                return index;
            }
            index++;
        }
        return -1;
    }
}
