package com.google.android.finsky.remoting.protos;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class Toc {
    private Toc() {
    }

    public static final class TocResponse extends MessageMicro {
        private boolean hasTosContent;
        private boolean hasTosVersion;
        private List<CorpusMetadata> corpus_ = Collections.emptyList();
        private int tosVersion_ = 0;
        private String tosContent_ = "";
        private int cachedSize = -1;

        public List<CorpusMetadata> getCorpusList() {
            return this.corpus_;
        }

        public int getCorpusCount() {
            return this.corpus_.size();
        }

        public TocResponse addCorpus(CorpusMetadata value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.corpus_.isEmpty()) {
                this.corpus_ = new ArrayList();
            }
            this.corpus_.add(value);
            return this;
        }

        public int getTosVersion() {
            return this.tosVersion_;
        }

        public boolean hasTosVersion() {
            return this.hasTosVersion;
        }

        public TocResponse setTosVersion(int value) {
            this.hasTosVersion = true;
            this.tosVersion_ = value;
            return this;
        }

        public String getTosContent() {
            return this.tosContent_;
        }

        public boolean hasTosContent() {
            return this.hasTosContent;
        }

        public TocResponse setTosContent(String value) {
            this.hasTosContent = true;
            this.tosContent_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            for (CorpusMetadata element : getCorpusList()) {
                output.writeMessage(1, element);
            }
            if (hasTosVersion()) {
                output.writeInt32(2, getTosVersion());
            }
            if (hasTosContent()) {
                output.writeString(3, getTosContent());
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
            int size = 0;
            for (CorpusMetadata element : getCorpusList()) {
                size += CodedOutputStreamMicro.computeMessageSize(1, element);
            }
            if (hasTosVersion()) {
                size += CodedOutputStreamMicro.computeInt32Size(2, getTosVersion());
            }
            if (hasTosContent()) {
                size += CodedOutputStreamMicro.computeStringSize(3, getTosContent());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public TocResponse mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        CorpusMetadata value = new CorpusMetadata();
                        input.readMessage(value);
                        addCorpus(value);
                        break;
                    case 16:
                        setTosVersion(input.readInt32());
                        break;
                    case 26:
                        setTosContent(input.readString());
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

    public static final class CorpusMetadata extends MessageMicro {
        private boolean hasBackend;
        private boolean hasLandingUrl;
        private boolean hasLibraryName;
        private boolean hasLibraryUrl;
        private boolean hasName;
        private int backend_ = 0;
        private String name_ = "";
        private String landingUrl_ = "";
        private String libraryName_ = "";
        private String libraryUrl_ = "";
        private int cachedSize = -1;

        public boolean hasBackend() {
            return this.hasBackend;
        }

        public int getBackend() {
            return this.backend_;
        }

        public CorpusMetadata setBackend(int value) {
            this.hasBackend = true;
            this.backend_ = value;
            return this;
        }

        public String getName() {
            return this.name_;
        }

        public boolean hasName() {
            return this.hasName;
        }

        public CorpusMetadata setName(String value) {
            this.hasName = true;
            this.name_ = value;
            return this;
        }

        public String getLandingUrl() {
            return this.landingUrl_;
        }

        public boolean hasLandingUrl() {
            return this.hasLandingUrl;
        }

        public CorpusMetadata setLandingUrl(String value) {
            this.hasLandingUrl = true;
            this.landingUrl_ = value;
            return this;
        }

        public String getLibraryName() {
            return this.libraryName_;
        }

        public boolean hasLibraryName() {
            return this.hasLibraryName;
        }

        public CorpusMetadata setLibraryName(String value) {
            this.hasLibraryName = true;
            this.libraryName_ = value;
            return this;
        }

        public String getLibraryUrl() {
            return this.libraryUrl_;
        }

        public boolean hasLibraryUrl() {
            return this.hasLibraryUrl;
        }

        public CorpusMetadata setLibraryUrl(String value) {
            this.hasLibraryUrl = true;
            this.libraryUrl_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasBackend()) {
                output.writeInt32(1, getBackend());
            }
            if (hasName()) {
                output.writeString(2, getName());
            }
            if (hasLandingUrl()) {
                output.writeString(3, getLandingUrl());
            }
            if (hasLibraryName()) {
                output.writeString(4, getLibraryName());
            }
            if (hasLibraryUrl()) {
                output.writeString(5, getLibraryUrl());
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
            int size = hasBackend() ? 0 + CodedOutputStreamMicro.computeInt32Size(1, getBackend()) : 0;
            if (hasName()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getName());
            }
            if (hasLandingUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(3, getLandingUrl());
            }
            if (hasLibraryName()) {
                size += CodedOutputStreamMicro.computeStringSize(4, getLibraryName());
            }
            if (hasLibraryUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(5, getLibraryUrl());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public CorpusMetadata mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 8:
                        setBackend(input.readInt32());
                        break;
                    case 18:
                        setName(input.readString());
                        break;
                    case 26:
                        setLandingUrl(input.readString());
                        break;
                    case 34:
                        setLibraryName(input.readString());
                        break;
                    case 42:
                        setLibraryUrl(input.readString());
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
