package com.google.android.finsky.remoting.protos;

import com.google.android.finsky.remoting.protos.DocList;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class SearchResponse extends MessageMicro {
    private boolean hasAggregateQuery;
    private boolean hasOriginalQuery;
    private boolean hasSuggestedQuery;
    private String originalQuery_ = "";
    private String suggestedQuery_ = "";
    private boolean aggregateQuery_ = false;
    private List<DocList.Bucket> bucket_ = Collections.emptyList();
    private int cachedSize = -1;

    public String getOriginalQuery() {
        return this.originalQuery_;
    }

    public boolean hasOriginalQuery() {
        return this.hasOriginalQuery;
    }

    public SearchResponse setOriginalQuery(String value) {
        this.hasOriginalQuery = true;
        this.originalQuery_ = value;
        return this;
    }

    public String getSuggestedQuery() {
        return this.suggestedQuery_;
    }

    public boolean hasSuggestedQuery() {
        return this.hasSuggestedQuery;
    }

    public SearchResponse setSuggestedQuery(String value) {
        this.hasSuggestedQuery = true;
        this.suggestedQuery_ = value;
        return this;
    }

    public boolean getAggregateQuery() {
        return this.aggregateQuery_;
    }

    public boolean hasAggregateQuery() {
        return this.hasAggregateQuery;
    }

    public SearchResponse setAggregateQuery(boolean value) {
        this.hasAggregateQuery = true;
        this.aggregateQuery_ = value;
        return this;
    }

    public List<DocList.Bucket> getBucketList() {
        return this.bucket_;
    }

    public int getBucketCount() {
        return this.bucket_.size();
    }

    public DocList.Bucket getBucket(int index) {
        return this.bucket_.get(index);
    }

    public SearchResponse addBucket(DocList.Bucket value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (this.bucket_.isEmpty()) {
            this.bucket_ = new ArrayList();
        }
        this.bucket_.add(value);
        return this;
    }

    @Override // com.google.protobuf.micro.MessageMicro
    public void writeTo(CodedOutputStreamMicro output) throws IOException {
        if (hasOriginalQuery()) {
            output.writeString(1, getOriginalQuery());
        }
        if (hasSuggestedQuery()) {
            output.writeString(2, getSuggestedQuery());
        }
        if (hasAggregateQuery()) {
            output.writeBool(3, getAggregateQuery());
        }
        for (DocList.Bucket element : getBucketList()) {
            output.writeMessage(4, element);
        }
    }

    @Override // com.google.protobuf.micro.MessageMicro
    public int getCachedSize() {
        if (this.cachedSize < 0) {
            getSerializedSize();
        }
        return this.cachedSize;
    }

    @Override // com.google.protobuf.micro.MessageMicro
    public int getSerializedSize() {
        int size = hasOriginalQuery() ? 0 + CodedOutputStreamMicro.computeStringSize(1, getOriginalQuery()) : 0;
        if (hasSuggestedQuery()) {
            size += CodedOutputStreamMicro.computeStringSize(2, getSuggestedQuery());
        }
        if (hasAggregateQuery()) {
            size += CodedOutputStreamMicro.computeBoolSize(3, getAggregateQuery());
        }
        for (DocList.Bucket element : getBucketList()) {
            size += CodedOutputStreamMicro.computeMessageSize(4, element);
        }
        this.cachedSize = size;
        return size;
    }

    @Override // com.google.protobuf.micro.MessageMicro
    public SearchResponse mergeFrom(CodedInputStreamMicro input) throws IOException {
        while (true) {
            int tag = input.readTag();
            switch (tag) {
                case 0:
                    break;
                case 10:
                    setOriginalQuery(input.readString());
                    break;
                case 18:
                    setSuggestedQuery(input.readString());
                    break;
                case 24:
                    setAggregateQuery(input.readBool());
                    break;
                case 34:
                    DocList.Bucket value = new DocList.Bucket();
                    input.readMessage(value);
                    addBucket(value);
                    break;
                default:
                    if (!parseUnknownField(input, tag)) {
                    }
                    break;
            }
        }
        return this;
    }
}
