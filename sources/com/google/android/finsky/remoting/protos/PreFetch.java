package com.google.android.finsky.remoting.protos;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class PreFetch extends MessageMicro {
    private boolean hasCacheUrl;
    private boolean hasEtag;
    private boolean hasImmediate;
    private String cacheUrl_ = "";
    private String etag_ = "";
    private boolean immediate_ = false;
    private int cachedSize = -1;

    public String getCacheUrl() {
        return this.cacheUrl_;
    }

    public boolean hasCacheUrl() {
        return this.hasCacheUrl;
    }

    public PreFetch setCacheUrl(String value) {
        this.hasCacheUrl = true;
        this.cacheUrl_ = value;
        return this;
    }

    public String getEtag() {
        return this.etag_;
    }

    public boolean hasEtag() {
        return this.hasEtag;
    }

    public PreFetch setEtag(String value) {
        this.hasEtag = true;
        this.etag_ = value;
        return this;
    }

    public boolean getImmediate() {
        return this.immediate_;
    }

    public boolean hasImmediate() {
        return this.hasImmediate;
    }

    public PreFetch setImmediate(boolean value) {
        this.hasImmediate = true;
        this.immediate_ = value;
        return this;
    }

    @Override // com.google.protobuf.micro.MessageMicro
    public void writeTo(CodedOutputStreamMicro output) throws IOException {
        if (hasCacheUrl()) {
            output.writeString(1, getCacheUrl());
        }
        if (hasEtag()) {
            output.writeString(2, getEtag());
        }
        if (hasImmediate()) {
            output.writeBool(3, getImmediate());
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
        int size = hasCacheUrl() ? 0 + CodedOutputStreamMicro.computeStringSize(1, getCacheUrl()) : 0;
        if (hasEtag()) {
            size += CodedOutputStreamMicro.computeStringSize(2, getEtag());
        }
        if (hasImmediate()) {
            size += CodedOutputStreamMicro.computeBoolSize(3, getImmediate());
        }
        this.cachedSize = size;
        return size;
    }

    @Override // com.google.protobuf.micro.MessageMicro
    public PreFetch mergeFrom(CodedInputStreamMicro input) throws IOException {
        while (true) {
            int tag = input.readTag();
            switch (tag) {
                case 0:
                    break;
                case 10:
                    setCacheUrl(input.readString());
                    break;
                case 18:
                    setEtag(input.readString());
                    break;
                case 24:
                    setImmediate(input.readBool());
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
