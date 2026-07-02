package com.google.android.finsky.remoting.protos;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class Purchase {
    private Purchase() {
    }

    public static final class PurchaseNotificationResponse extends MessageMicro {
        private boolean hasDebugInfo;
        private boolean hasLocalizedErrorMessage;
        private boolean hasPurchaseId;
        private boolean hasStatus;
        private int status_ = 0;
        private String purchaseId_ = "";
        private String localizedErrorMessage_ = "";
        private DebugInfo debugInfo_ = null;
        private int cachedSize = -1;

        public boolean hasStatus() {
            return this.hasStatus;
        }

        public int getStatus() {
            return this.status_;
        }

        public PurchaseNotificationResponse setStatus(int value) {
            this.hasStatus = true;
            this.status_ = value;
            return this;
        }

        public String getPurchaseId() {
            return this.purchaseId_;
        }

        public boolean hasPurchaseId() {
            return this.hasPurchaseId;
        }

        public PurchaseNotificationResponse setPurchaseId(String value) {
            this.hasPurchaseId = true;
            this.purchaseId_ = value;
            return this;
        }

        public String getLocalizedErrorMessage() {
            return this.localizedErrorMessage_;
        }

        public boolean hasLocalizedErrorMessage() {
            return this.hasLocalizedErrorMessage;
        }

        public PurchaseNotificationResponse setLocalizedErrorMessage(String value) {
            this.hasLocalizedErrorMessage = true;
            this.localizedErrorMessage_ = value;
            return this;
        }

        public boolean hasDebugInfo() {
            return this.hasDebugInfo;
        }

        public DebugInfo getDebugInfo() {
            return this.debugInfo_;
        }

        public PurchaseNotificationResponse setDebugInfo(DebugInfo value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasDebugInfo = true;
            this.debugInfo_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasStatus()) {
                output.writeInt32(1, getStatus());
            }
            if (hasDebugInfo()) {
                output.writeMessage(2, getDebugInfo());
            }
            if (hasLocalizedErrorMessage()) {
                output.writeString(3, getLocalizedErrorMessage());
            }
            if (hasPurchaseId()) {
                output.writeString(4, getPurchaseId());
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
            int size = hasStatus() ? 0 + CodedOutputStreamMicro.computeInt32Size(1, getStatus()) : 0;
            if (hasDebugInfo()) {
                size += CodedOutputStreamMicro.computeMessageSize(2, getDebugInfo());
            }
            if (hasLocalizedErrorMessage()) {
                size += CodedOutputStreamMicro.computeStringSize(3, getLocalizedErrorMessage());
            }
            if (hasPurchaseId()) {
                size += CodedOutputStreamMicro.computeStringSize(4, getPurchaseId());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public PurchaseNotificationResponse mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 8:
                        setStatus(input.readInt32());
                        break;
                    case 18:
                        DebugInfo value = new DebugInfo();
                        input.readMessage(value);
                        setDebugInfo(value);
                        break;
                    case 26:
                        setLocalizedErrorMessage(input.readString());
                        break;
                    case 34:
                        setPurchaseId(input.readString());
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
}
