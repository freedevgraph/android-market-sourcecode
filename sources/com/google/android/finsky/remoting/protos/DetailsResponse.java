package com.google.android.finsky.remoting.protos;

import com.google.android.finsky.remoting.protos.DeviceDoc;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class DetailsResponse extends MessageMicro {
    private boolean hasAnalyticsCookie;
    private boolean hasDoc;
    private DeviceDoc.DeviceDocument doc_ = null;
    private String analyticsCookie_ = "";
    private int cachedSize = -1;

    public boolean hasDoc() {
        return this.hasDoc;
    }

    public DeviceDoc.DeviceDocument getDoc() {
        return this.doc_;
    }

    public DetailsResponse setDoc(DeviceDoc.DeviceDocument value) {
        if (value == null) {
            throw new NullPointerException();
        }
        this.hasDoc = true;
        this.doc_ = value;
        return this;
    }

    public String getAnalyticsCookie() {
        return this.analyticsCookie_;
    }

    public boolean hasAnalyticsCookie() {
        return this.hasAnalyticsCookie;
    }

    public DetailsResponse setAnalyticsCookie(String value) {
        this.hasAnalyticsCookie = true;
        this.analyticsCookie_ = value;
        return this;
    }

    @Override // com.google.protobuf.micro.MessageMicro
    public void writeTo(CodedOutputStreamMicro output) throws IOException {
        if (hasDoc()) {
            output.writeMessage(1, getDoc());
        }
        if (hasAnalyticsCookie()) {
            output.writeString(2, getAnalyticsCookie());
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
        int size = hasDoc() ? 0 + CodedOutputStreamMicro.computeMessageSize(1, getDoc()) : 0;
        if (hasAnalyticsCookie()) {
            size += CodedOutputStreamMicro.computeStringSize(2, getAnalyticsCookie());
        }
        this.cachedSize = size;
        return size;
    }

    @Override // com.google.protobuf.micro.MessageMicro
    public DetailsResponse mergeFrom(CodedInputStreamMicro input) throws IOException {
        while (true) {
            int tag = input.readTag();
            switch (tag) {
                case 0:
                    break;
                case 10:
                    DeviceDoc.DeviceDocument value = new DeviceDoc.DeviceDocument();
                    input.readMessage(value);
                    setDoc(value);
                    break;
                case 18:
                    setAnalyticsCookie(input.readString());
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
