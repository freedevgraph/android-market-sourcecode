package com.google.android.vending.remoting.protos;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class VendingProtos {
    private VendingProtos() {
    }

    public static final class RequestPropertiesProto extends MessageMicro {
        private boolean hasAid;
        private boolean hasClientId;
        private boolean hasLoggingId;
        private boolean hasOperatorName;
        private boolean hasOperatorNumericName;
        private boolean hasProductNameAndVersion;
        private boolean hasSimOperatorName;
        private boolean hasSimOperatorNumericName;
        private boolean hasSoftwareVersion;
        private boolean hasUserAuthToken;
        private boolean hasUserAuthTokenSecure;
        private boolean hasUserCountry;
        private boolean hasUserLanguage;
        private String userAuthToken_ = "";
        private boolean userAuthTokenSecure_ = false;
        private int softwareVersion_ = 0;
        private String aid_ = "";
        private String productNameAndVersion_ = "";
        private String userLanguage_ = "";
        private String userCountry_ = "";
        private String operatorName_ = "";
        private String simOperatorName_ = "";
        private String operatorNumericName_ = "";
        private String simOperatorNumericName_ = "";
        private String clientId_ = "";
        private String loggingId_ = "";
        private int cachedSize = -1;

        public String getUserAuthToken() {
            return this.userAuthToken_;
        }

        public boolean hasUserAuthToken() {
            return this.hasUserAuthToken;
        }

        public RequestPropertiesProto setUserAuthToken(String value) {
            this.hasUserAuthToken = true;
            this.userAuthToken_ = value;
            return this;
        }

        public boolean getUserAuthTokenSecure() {
            return this.userAuthTokenSecure_;
        }

        public boolean hasUserAuthTokenSecure() {
            return this.hasUserAuthTokenSecure;
        }

        public RequestPropertiesProto setUserAuthTokenSecure(boolean value) {
            this.hasUserAuthTokenSecure = true;
            this.userAuthTokenSecure_ = value;
            return this;
        }

        public int getSoftwareVersion() {
            return this.softwareVersion_;
        }

        public boolean hasSoftwareVersion() {
            return this.hasSoftwareVersion;
        }

        public RequestPropertiesProto setSoftwareVersion(int value) {
            this.hasSoftwareVersion = true;
            this.softwareVersion_ = value;
            return this;
        }

        public String getAid() {
            return this.aid_;
        }

        public boolean hasAid() {
            return this.hasAid;
        }

        public RequestPropertiesProto setAid(String value) {
            this.hasAid = true;
            this.aid_ = value;
            return this;
        }

        public String getProductNameAndVersion() {
            return this.productNameAndVersion_;
        }

        public boolean hasProductNameAndVersion() {
            return this.hasProductNameAndVersion;
        }

        public RequestPropertiesProto setProductNameAndVersion(String value) {
            this.hasProductNameAndVersion = true;
            this.productNameAndVersion_ = value;
            return this;
        }

        public String getUserLanguage() {
            return this.userLanguage_;
        }

        public boolean hasUserLanguage() {
            return this.hasUserLanguage;
        }

        public RequestPropertiesProto setUserLanguage(String value) {
            this.hasUserLanguage = true;
            this.userLanguage_ = value;
            return this;
        }

        public String getUserCountry() {
            return this.userCountry_;
        }

        public boolean hasUserCountry() {
            return this.hasUserCountry;
        }

        public RequestPropertiesProto setUserCountry(String value) {
            this.hasUserCountry = true;
            this.userCountry_ = value;
            return this;
        }

        public String getOperatorName() {
            return this.operatorName_;
        }

        public boolean hasOperatorName() {
            return this.hasOperatorName;
        }

        public RequestPropertiesProto setOperatorName(String value) {
            this.hasOperatorName = true;
            this.operatorName_ = value;
            return this;
        }

        public String getSimOperatorName() {
            return this.simOperatorName_;
        }

        public boolean hasSimOperatorName() {
            return this.hasSimOperatorName;
        }

        public RequestPropertiesProto setSimOperatorName(String value) {
            this.hasSimOperatorName = true;
            this.simOperatorName_ = value;
            return this;
        }

        public String getOperatorNumericName() {
            return this.operatorNumericName_;
        }

        public boolean hasOperatorNumericName() {
            return this.hasOperatorNumericName;
        }

        public RequestPropertiesProto setOperatorNumericName(String value) {
            this.hasOperatorNumericName = true;
            this.operatorNumericName_ = value;
            return this;
        }

        public String getSimOperatorNumericName() {
            return this.simOperatorNumericName_;
        }

        public boolean hasSimOperatorNumericName() {
            return this.hasSimOperatorNumericName;
        }

        public RequestPropertiesProto setSimOperatorNumericName(String value) {
            this.hasSimOperatorNumericName = true;
            this.simOperatorNumericName_ = value;
            return this;
        }

        public String getClientId() {
            return this.clientId_;
        }

        public boolean hasClientId() {
            return this.hasClientId;
        }

        public RequestPropertiesProto setClientId(String value) {
            this.hasClientId = true;
            this.clientId_ = value;
            return this;
        }

        public String getLoggingId() {
            return this.loggingId_;
        }

        public boolean hasLoggingId() {
            return this.hasLoggingId;
        }

        public RequestPropertiesProto setLoggingId(String value) {
            this.hasLoggingId = true;
            this.loggingId_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasUserAuthToken()) {
                output.writeString(1, getUserAuthToken());
            }
            if (hasUserAuthTokenSecure()) {
                output.writeBool(2, getUserAuthTokenSecure());
            }
            if (hasSoftwareVersion()) {
                output.writeInt32(3, getSoftwareVersion());
            }
            if (hasAid()) {
                output.writeString(4, getAid());
            }
            if (hasProductNameAndVersion()) {
                output.writeString(5, getProductNameAndVersion());
            }
            if (hasUserLanguage()) {
                output.writeString(6, getUserLanguage());
            }
            if (hasUserCountry()) {
                output.writeString(7, getUserCountry());
            }
            if (hasOperatorName()) {
                output.writeString(8, getOperatorName());
            }
            if (hasSimOperatorName()) {
                output.writeString(9, getSimOperatorName());
            }
            if (hasOperatorNumericName()) {
                output.writeString(10, getOperatorNumericName());
            }
            if (hasSimOperatorNumericName()) {
                output.writeString(11, getSimOperatorNumericName());
            }
            if (hasClientId()) {
                output.writeString(12, getClientId());
            }
            if (hasLoggingId()) {
                output.writeString(13, getLoggingId());
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
            int size = hasUserAuthToken() ? 0 + CodedOutputStreamMicro.computeStringSize(1, getUserAuthToken()) : 0;
            if (hasUserAuthTokenSecure()) {
                size += CodedOutputStreamMicro.computeBoolSize(2, getUserAuthTokenSecure());
            }
            if (hasSoftwareVersion()) {
                size += CodedOutputStreamMicro.computeInt32Size(3, getSoftwareVersion());
            }
            if (hasAid()) {
                size += CodedOutputStreamMicro.computeStringSize(4, getAid());
            }
            if (hasProductNameAndVersion()) {
                size += CodedOutputStreamMicro.computeStringSize(5, getProductNameAndVersion());
            }
            if (hasUserLanguage()) {
                size += CodedOutputStreamMicro.computeStringSize(6, getUserLanguage());
            }
            if (hasUserCountry()) {
                size += CodedOutputStreamMicro.computeStringSize(7, getUserCountry());
            }
            if (hasOperatorName()) {
                size += CodedOutputStreamMicro.computeStringSize(8, getOperatorName());
            }
            if (hasSimOperatorName()) {
                size += CodedOutputStreamMicro.computeStringSize(9, getSimOperatorName());
            }
            if (hasOperatorNumericName()) {
                size += CodedOutputStreamMicro.computeStringSize(10, getOperatorNumericName());
            }
            if (hasSimOperatorNumericName()) {
                size += CodedOutputStreamMicro.computeStringSize(11, getSimOperatorNumericName());
            }
            if (hasClientId()) {
                size += CodedOutputStreamMicro.computeStringSize(12, getClientId());
            }
            if (hasLoggingId()) {
                size += CodedOutputStreamMicro.computeStringSize(13, getLoggingId());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public RequestPropertiesProto mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        setUserAuthToken(input.readString());
                        break;
                    case 16:
                        setUserAuthTokenSecure(input.readBool());
                        break;
                    case 24:
                        setSoftwareVersion(input.readInt32());
                        break;
                    case 34:
                        setAid(input.readString());
                        break;
                    case 42:
                        setProductNameAndVersion(input.readString());
                        break;
                    case 50:
                        setUserLanguage(input.readString());
                        break;
                    case 58:
                        setUserCountry(input.readString());
                        break;
                    case 66:
                        setOperatorName(input.readString());
                        break;
                    case 74:
                        setSimOperatorName(input.readString());
                        break;
                    case 82:
                        setOperatorNumericName(input.readString());
                        break;
                    case 90:
                        setSimOperatorNumericName(input.readString());
                        break;
                    case 98:
                        setClientId(input.readString());
                        break;
                    case 106:
                        setLoggingId(input.readString());
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
