package com.google.android.finsky.remoting.protos;

import com.google.android.finsky.remoting.protos.DeviceDoc;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class DocList {
    private DocList() {
    }

    public static final class ListResponse extends MessageMicro {
        private List<Bucket> bucket_ = Collections.emptyList();
        private int cachedSize = -1;

        public List<Bucket> getBucketList() {
            return this.bucket_;
        }

        public int getBucketCount() {
            return this.bucket_.size();
        }

        public Bucket getBucket(int index) {
            return this.bucket_.get(index);
        }

        public ListResponse addBucket(Bucket value) {
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
            for (Bucket element : getBucketList()) {
                output.writeMessage(1, element);
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
            for (Bucket element : getBucketList()) {
                size += CodedOutputStreamMicro.computeMessageSize(1, element);
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public ListResponse mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        Bucket value = new Bucket();
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

    public static final class Bucket extends MessageMicro {
        private boolean hasAnalyticsCookie;
        private boolean hasEstimatedResults;
        private boolean hasFullContentsUrl;
        private boolean hasIconUrl;
        private boolean hasMultiCorpus;
        private boolean hasRelevance;
        private boolean hasTitle;
        private List<DeviceDoc.DeviceDocument> document_ = Collections.emptyList();
        private boolean multiCorpus_ = false;
        private String title_ = "";
        private String iconUrl_ = "";
        private String fullContentsUrl_ = "";
        private double relevance_ = 0.0d;
        private long estimatedResults_ = 0;
        private String analyticsCookie_ = "";
        private int cachedSize = -1;

        public List<DeviceDoc.DeviceDocument> getDocumentList() {
            return this.document_;
        }

        public int getDocumentCount() {
            return this.document_.size();
        }

        public DeviceDoc.DeviceDocument getDocument(int index) {
            return this.document_.get(index);
        }

        public Bucket addDocument(DeviceDoc.DeviceDocument value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.document_.isEmpty()) {
                this.document_ = new ArrayList();
            }
            this.document_.add(value);
            return this;
        }

        public boolean getMultiCorpus() {
            return this.multiCorpus_;
        }

        public boolean hasMultiCorpus() {
            return this.hasMultiCorpus;
        }

        public Bucket setMultiCorpus(boolean value) {
            this.hasMultiCorpus = true;
            this.multiCorpus_ = value;
            return this;
        }

        public String getTitle() {
            return this.title_;
        }

        public boolean hasTitle() {
            return this.hasTitle;
        }

        public Bucket setTitle(String value) {
            this.hasTitle = true;
            this.title_ = value;
            return this;
        }

        public String getIconUrl() {
            return this.iconUrl_;
        }

        public boolean hasIconUrl() {
            return this.hasIconUrl;
        }

        public Bucket setIconUrl(String value) {
            this.hasIconUrl = true;
            this.iconUrl_ = value;
            return this;
        }

        public String getFullContentsUrl() {
            return this.fullContentsUrl_;
        }

        public boolean hasFullContentsUrl() {
            return this.hasFullContentsUrl;
        }

        public Bucket setFullContentsUrl(String value) {
            this.hasFullContentsUrl = true;
            this.fullContentsUrl_ = value;
            return this;
        }

        public double getRelevance() {
            return this.relevance_;
        }

        public boolean hasRelevance() {
            return this.hasRelevance;
        }

        public Bucket setRelevance(double value) {
            this.hasRelevance = true;
            this.relevance_ = value;
            return this;
        }

        public long getEstimatedResults() {
            return this.estimatedResults_;
        }

        public boolean hasEstimatedResults() {
            return this.hasEstimatedResults;
        }

        public Bucket setEstimatedResults(long value) {
            this.hasEstimatedResults = true;
            this.estimatedResults_ = value;
            return this;
        }

        public String getAnalyticsCookie() {
            return this.analyticsCookie_;
        }

        public boolean hasAnalyticsCookie() {
            return this.hasAnalyticsCookie;
        }

        public Bucket setAnalyticsCookie(String value) {
            this.hasAnalyticsCookie = true;
            this.analyticsCookie_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            for (DeviceDoc.DeviceDocument element : getDocumentList()) {
                output.writeMessage(1, element);
            }
            if (hasMultiCorpus()) {
                output.writeBool(2, getMultiCorpus());
            }
            if (hasTitle()) {
                output.writeString(3, getTitle());
            }
            if (hasIconUrl()) {
                output.writeString(4, getIconUrl());
            }
            if (hasFullContentsUrl()) {
                output.writeString(5, getFullContentsUrl());
            }
            if (hasRelevance()) {
                output.writeDouble(6, getRelevance());
            }
            if (hasEstimatedResults()) {
                output.writeInt64(7, getEstimatedResults());
            }
            if (hasAnalyticsCookie()) {
                output.writeString(8, getAnalyticsCookie());
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
            for (DeviceDoc.DeviceDocument element : getDocumentList()) {
                size += CodedOutputStreamMicro.computeMessageSize(1, element);
            }
            if (hasMultiCorpus()) {
                size += CodedOutputStreamMicro.computeBoolSize(2, getMultiCorpus());
            }
            if (hasTitle()) {
                size += CodedOutputStreamMicro.computeStringSize(3, getTitle());
            }
            if (hasIconUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(4, getIconUrl());
            }
            if (hasFullContentsUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(5, getFullContentsUrl());
            }
            if (hasRelevance()) {
                size += CodedOutputStreamMicro.computeDoubleSize(6, getRelevance());
            }
            if (hasEstimatedResults()) {
                size += CodedOutputStreamMicro.computeInt64Size(7, getEstimatedResults());
            }
            if (hasAnalyticsCookie()) {
                size += CodedOutputStreamMicro.computeStringSize(8, getAnalyticsCookie());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public Bucket mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        DeviceDoc.DeviceDocument value = new DeviceDoc.DeviceDocument();
                        input.readMessage(value);
                        addDocument(value);
                        break;
                    case 16:
                        setMultiCorpus(input.readBool());
                        break;
                    case 26:
                        setTitle(input.readString());
                        break;
                    case 34:
                        setIconUrl(input.readString());
                        break;
                    case 42:
                        setFullContentsUrl(input.readString());
                        break;
                    case 49:
                        setRelevance(input.readDouble());
                        break;
                    case 56:
                        setEstimatedResults(input.readInt64());
                        break;
                    case 66:
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
}
