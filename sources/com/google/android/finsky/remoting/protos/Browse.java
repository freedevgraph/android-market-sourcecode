package com.google.android.finsky.remoting.protos;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class Browse {
    private Browse() {
    }

    public static final class BrowseResponse extends MessageMicro {
        private boolean hasContentsUrl;
        private boolean hasPromoUrl;
        private String contentsUrl_ = "";
        private String promoUrl_ = "";
        private List<BrowseLink> category_ = Collections.emptyList();
        private List<BrowseLink> breadcrumb_ = Collections.emptyList();
        private int cachedSize = -1;

        public String getContentsUrl() {
            return this.contentsUrl_;
        }

        public boolean hasContentsUrl() {
            return this.hasContentsUrl;
        }

        public BrowseResponse setContentsUrl(String value) {
            this.hasContentsUrl = true;
            this.contentsUrl_ = value;
            return this;
        }

        public String getPromoUrl() {
            return this.promoUrl_;
        }

        public boolean hasPromoUrl() {
            return this.hasPromoUrl;
        }

        public BrowseResponse setPromoUrl(String value) {
            this.hasPromoUrl = true;
            this.promoUrl_ = value;
            return this;
        }

        public List<BrowseLink> getCategoryList() {
            return this.category_;
        }

        public BrowseResponse addCategory(BrowseLink value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.category_.isEmpty()) {
                this.category_ = new ArrayList();
            }
            this.category_.add(value);
            return this;
        }

        public List<BrowseLink> getBreadcrumbList() {
            return this.breadcrumb_;
        }

        public BrowseResponse addBreadcrumb(BrowseLink value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.breadcrumb_.isEmpty()) {
                this.breadcrumb_ = new ArrayList();
            }
            this.breadcrumb_.add(value);
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasContentsUrl()) {
                output.writeString(1, getContentsUrl());
            }
            if (hasPromoUrl()) {
                output.writeString(2, getPromoUrl());
            }
            for (BrowseLink element : getCategoryList()) {
                output.writeMessage(3, element);
            }
            for (BrowseLink element2 : getBreadcrumbList()) {
                output.writeMessage(4, element2);
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
            int size = hasContentsUrl() ? 0 + CodedOutputStreamMicro.computeStringSize(1, getContentsUrl()) : 0;
            if (hasPromoUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getPromoUrl());
            }
            for (BrowseLink element : getCategoryList()) {
                size += CodedOutputStreamMicro.computeMessageSize(3, element);
            }
            for (BrowseLink element2 : getBreadcrumbList()) {
                size += CodedOutputStreamMicro.computeMessageSize(4, element2);
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public BrowseResponse mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        setContentsUrl(input.readString());
                        break;
                    case 18:
                        setPromoUrl(input.readString());
                        break;
                    case 26:
                        BrowseLink value = new BrowseLink();
                        input.readMessage(value);
                        addCategory(value);
                        break;
                    case 34:
                        BrowseLink value2 = new BrowseLink();
                        input.readMessage(value2);
                        addBreadcrumb(value2);
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

    public static final class BrowseLink extends MessageMicro {
        private boolean hasDataUrl;
        private boolean hasIconUrl;
        private boolean hasName;
        private String name_ = "";
        private String iconUrl_ = "";
        private String dataUrl_ = "";
        private int cachedSize = -1;

        public String getName() {
            return this.name_;
        }

        public boolean hasName() {
            return this.hasName;
        }

        public BrowseLink setName(String value) {
            this.hasName = true;
            this.name_ = value;
            return this;
        }

        public String getIconUrl() {
            return this.iconUrl_;
        }

        public boolean hasIconUrl() {
            return this.hasIconUrl;
        }

        public BrowseLink setIconUrl(String value) {
            this.hasIconUrl = true;
            this.iconUrl_ = value;
            return this;
        }

        public String getDataUrl() {
            return this.dataUrl_;
        }

        public boolean hasDataUrl() {
            return this.hasDataUrl;
        }

        public BrowseLink setDataUrl(String value) {
            this.hasDataUrl = true;
            this.dataUrl_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasName()) {
                output.writeString(1, getName());
            }
            if (hasIconUrl()) {
                output.writeString(2, getIconUrl());
            }
            if (hasDataUrl()) {
                output.writeString(3, getDataUrl());
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
            int size = hasName() ? 0 + CodedOutputStreamMicro.computeStringSize(1, getName()) : 0;
            if (hasIconUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getIconUrl());
            }
            if (hasDataUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(3, getDataUrl());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public BrowseLink mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        setName(input.readString());
                        break;
                    case 18:
                        setIconUrl(input.readString());
                        break;
                    case 26:
                        setDataUrl(input.readString());
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
