package com.google.android.finsky.remoting.protos;

import com.google.android.finsky.remoting.protos.Browse;
import com.google.android.finsky.remoting.protos.Buy;
import com.google.android.finsky.remoting.protos.BuyFops;
import com.google.android.finsky.remoting.protos.DocList;
import com.google.android.finsky.remoting.protos.Rev;
import com.google.android.finsky.remoting.protos.Toc;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class Response {
    private Response() {
    }

    public static final class ResponseWrapper extends MessageMicro {
        private boolean hasCommands;
        private boolean hasPayload;
        private Payload payload_ = null;
        private ServerCommands commands_ = null;
        private List<PreFetch> preFetch_ = Collections.emptyList();
        private int cachedSize = -1;

        public boolean hasPayload() {
            return this.hasPayload;
        }

        public Payload getPayload() {
            return this.payload_;
        }

        public ResponseWrapper setPayload(Payload value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasPayload = true;
            this.payload_ = value;
            return this;
        }

        public boolean hasCommands() {
            return this.hasCommands;
        }

        public ServerCommands getCommands() {
            return this.commands_;
        }

        public ResponseWrapper setCommands(ServerCommands value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasCommands = true;
            this.commands_ = value;
            return this;
        }

        public List<PreFetch> getPreFetchList() {
            return this.preFetch_;
        }

        public ResponseWrapper addPreFetch(PreFetch value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.preFetch_.isEmpty()) {
                this.preFetch_ = new ArrayList();
            }
            this.preFetch_.add(value);
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasPayload()) {
                output.writeMessage(1, getPayload());
            }
            if (hasCommands()) {
                output.writeMessage(2, getCommands());
            }
            for (PreFetch element : getPreFetchList()) {
                output.writeMessage(3, element);
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
            int size = hasPayload() ? 0 + CodedOutputStreamMicro.computeMessageSize(1, getPayload()) : 0;
            if (hasCommands()) {
                size += CodedOutputStreamMicro.computeMessageSize(2, getCommands());
            }
            for (PreFetch element : getPreFetchList()) {
                size += CodedOutputStreamMicro.computeMessageSize(3, element);
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public ResponseWrapper mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        Payload value = new Payload();
                        input.readMessage(value);
                        setPayload(value);
                        break;
                    case 18:
                        ServerCommands value2 = new ServerCommands();
                        input.readMessage(value2);
                        setCommands(value2);
                        break;
                    case 26:
                        PreFetch value3 = new PreFetch();
                        input.readMessage(value3);
                        addPreFetch(value3);
                        break;
                    default:
                        if (!parseUnknownField(input, tag)) {
                        }
                        break;
                }
            }
            return this;
        }

        public static ResponseWrapper parseFrom(byte[] data) throws InvalidProtocolBufferMicroException {
            return (ResponseWrapper) new ResponseWrapper().mergeFrom(data);
        }
    }

    public static final class Payload extends MessageMicro {
        private boolean hasBrowseResponse;
        private boolean hasBuyResponse;
        private boolean hasDetailsResponse;
        private boolean hasListResponse;
        private boolean hasPurchaseStatusResponse;
        private boolean hasReviewResponse;
        private boolean hasSearchResponse;
        private boolean hasTocResponse;
        private boolean hasUpdateFopResponse;
        private DocList.ListResponse listResponse_ = null;
        private DetailsResponse detailsResponse_ = null;
        private Rev.ReviewResponse reviewResponse_ = null;
        private Buy.BuyResponse buyResponse_ = null;
        private SearchResponse searchResponse_ = null;
        private Toc.TocResponse tocResponse_ = null;
        private Browse.BrowseResponse browseResponse_ = null;
        private Buy.PurchaseStatusResponse purchaseStatusResponse_ = null;
        private BuyFops.UpdateFopResponse updateFopResponse_ = null;
        private int cachedSize = -1;

        public boolean hasListResponse() {
            return this.hasListResponse;
        }

        public DocList.ListResponse getListResponse() {
            return this.listResponse_;
        }

        public Payload setListResponse(DocList.ListResponse value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasListResponse = true;
            this.listResponse_ = value;
            return this;
        }

        public boolean hasDetailsResponse() {
            return this.hasDetailsResponse;
        }

        public DetailsResponse getDetailsResponse() {
            return this.detailsResponse_;
        }

        public Payload setDetailsResponse(DetailsResponse value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasDetailsResponse = true;
            this.detailsResponse_ = value;
            return this;
        }

        public boolean hasReviewResponse() {
            return this.hasReviewResponse;
        }

        public Rev.ReviewResponse getReviewResponse() {
            return this.reviewResponse_;
        }

        public Payload setReviewResponse(Rev.ReviewResponse value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasReviewResponse = true;
            this.reviewResponse_ = value;
            return this;
        }

        public boolean hasBuyResponse() {
            return this.hasBuyResponse;
        }

        public Buy.BuyResponse getBuyResponse() {
            return this.buyResponse_;
        }

        public Payload setBuyResponse(Buy.BuyResponse value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasBuyResponse = true;
            this.buyResponse_ = value;
            return this;
        }

        public boolean hasSearchResponse() {
            return this.hasSearchResponse;
        }

        public SearchResponse getSearchResponse() {
            return this.searchResponse_;
        }

        public Payload setSearchResponse(SearchResponse value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasSearchResponse = true;
            this.searchResponse_ = value;
            return this;
        }

        public boolean hasTocResponse() {
            return this.hasTocResponse;
        }

        public Toc.TocResponse getTocResponse() {
            return this.tocResponse_;
        }

        public Payload setTocResponse(Toc.TocResponse value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasTocResponse = true;
            this.tocResponse_ = value;
            return this;
        }

        public boolean hasBrowseResponse() {
            return this.hasBrowseResponse;
        }

        public Browse.BrowseResponse getBrowseResponse() {
            return this.browseResponse_;
        }

        public Payload setBrowseResponse(Browse.BrowseResponse value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasBrowseResponse = true;
            this.browseResponse_ = value;
            return this;
        }

        public boolean hasPurchaseStatusResponse() {
            return this.hasPurchaseStatusResponse;
        }

        public Buy.PurchaseStatusResponse getPurchaseStatusResponse() {
            return this.purchaseStatusResponse_;
        }

        public Payload setPurchaseStatusResponse(Buy.PurchaseStatusResponse value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasPurchaseStatusResponse = true;
            this.purchaseStatusResponse_ = value;
            return this;
        }

        public boolean hasUpdateFopResponse() {
            return this.hasUpdateFopResponse;
        }

        public BuyFops.UpdateFopResponse getUpdateFopResponse() {
            return this.updateFopResponse_;
        }

        public Payload setUpdateFopResponse(BuyFops.UpdateFopResponse value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasUpdateFopResponse = true;
            this.updateFopResponse_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasListResponse()) {
                output.writeMessage(1, getListResponse());
            }
            if (hasDetailsResponse()) {
                output.writeMessage(2, getDetailsResponse());
            }
            if (hasReviewResponse()) {
                output.writeMessage(3, getReviewResponse());
            }
            if (hasBuyResponse()) {
                output.writeMessage(4, getBuyResponse());
            }
            if (hasSearchResponse()) {
                output.writeMessage(5, getSearchResponse());
            }
            if (hasTocResponse()) {
                output.writeMessage(6, getTocResponse());
            }
            if (hasBrowseResponse()) {
                output.writeMessage(7, getBrowseResponse());
            }
            if (hasPurchaseStatusResponse()) {
                output.writeMessage(8, getPurchaseStatusResponse());
            }
            if (hasUpdateFopResponse()) {
                output.writeMessage(9, getUpdateFopResponse());
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
            int size = hasListResponse() ? 0 + CodedOutputStreamMicro.computeMessageSize(1, getListResponse()) : 0;
            if (hasDetailsResponse()) {
                size += CodedOutputStreamMicro.computeMessageSize(2, getDetailsResponse());
            }
            if (hasReviewResponse()) {
                size += CodedOutputStreamMicro.computeMessageSize(3, getReviewResponse());
            }
            if (hasBuyResponse()) {
                size += CodedOutputStreamMicro.computeMessageSize(4, getBuyResponse());
            }
            if (hasSearchResponse()) {
                size += CodedOutputStreamMicro.computeMessageSize(5, getSearchResponse());
            }
            if (hasTocResponse()) {
                size += CodedOutputStreamMicro.computeMessageSize(6, getTocResponse());
            }
            if (hasBrowseResponse()) {
                size += CodedOutputStreamMicro.computeMessageSize(7, getBrowseResponse());
            }
            if (hasPurchaseStatusResponse()) {
                size += CodedOutputStreamMicro.computeMessageSize(8, getPurchaseStatusResponse());
            }
            if (hasUpdateFopResponse()) {
                size += CodedOutputStreamMicro.computeMessageSize(9, getUpdateFopResponse());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public Payload mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        DocList.ListResponse value = new DocList.ListResponse();
                        input.readMessage(value);
                        setListResponse(value);
                        break;
                    case 18:
                        DetailsResponse value2 = new DetailsResponse();
                        input.readMessage(value2);
                        setDetailsResponse(value2);
                        break;
                    case 26:
                        Rev.ReviewResponse value3 = new Rev.ReviewResponse();
                        input.readMessage(value3);
                        setReviewResponse(value3);
                        break;
                    case 34:
                        Buy.BuyResponse value4 = new Buy.BuyResponse();
                        input.readMessage(value4);
                        setBuyResponse(value4);
                        break;
                    case 42:
                        SearchResponse value5 = new SearchResponse();
                        input.readMessage(value5);
                        setSearchResponse(value5);
                        break;
                    case 50:
                        Toc.TocResponse value6 = new Toc.TocResponse();
                        input.readMessage(value6);
                        setTocResponse(value6);
                        break;
                    case 58:
                        Browse.BrowseResponse value7 = new Browse.BrowseResponse();
                        input.readMessage(value7);
                        setBrowseResponse(value7);
                        break;
                    case 66:
                        Buy.PurchaseStatusResponse value8 = new Buy.PurchaseStatusResponse();
                        input.readMessage(value8);
                        setPurchaseStatusResponse(value8);
                        break;
                    case 74:
                        BuyFops.UpdateFopResponse value9 = new BuyFops.UpdateFopResponse();
                        input.readMessage(value9);
                        setUpdateFopResponse(value9);
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

    public static final class ServerCommands extends MessageMicro {
        private boolean hasClearCache;
        private boolean hasDisplayErrorMessage;
        private boolean hasLogErrorStacktrace;
        private boolean clearCache_ = false;
        private String displayErrorMessage_ = "";
        private String logErrorStacktrace_ = "";
        private int cachedSize = -1;

        public boolean getClearCache() {
            return this.clearCache_;
        }

        public boolean hasClearCache() {
            return this.hasClearCache;
        }

        public ServerCommands setClearCache(boolean value) {
            this.hasClearCache = true;
            this.clearCache_ = value;
            return this;
        }

        public String getDisplayErrorMessage() {
            return this.displayErrorMessage_;
        }

        public boolean hasDisplayErrorMessage() {
            return this.hasDisplayErrorMessage;
        }

        public ServerCommands setDisplayErrorMessage(String value) {
            this.hasDisplayErrorMessage = true;
            this.displayErrorMessage_ = value;
            return this;
        }

        public String getLogErrorStacktrace() {
            return this.logErrorStacktrace_;
        }

        public boolean hasLogErrorStacktrace() {
            return this.hasLogErrorStacktrace;
        }

        public ServerCommands setLogErrorStacktrace(String value) {
            this.hasLogErrorStacktrace = true;
            this.logErrorStacktrace_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasClearCache()) {
                output.writeBool(1, getClearCache());
            }
            if (hasDisplayErrorMessage()) {
                output.writeString(2, getDisplayErrorMessage());
            }
            if (hasLogErrorStacktrace()) {
                output.writeString(3, getLogErrorStacktrace());
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
            int size = hasClearCache() ? 0 + CodedOutputStreamMicro.computeBoolSize(1, getClearCache()) : 0;
            if (hasDisplayErrorMessage()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getDisplayErrorMessage());
            }
            if (hasLogErrorStacktrace()) {
                size += CodedOutputStreamMicro.computeStringSize(3, getLogErrorStacktrace());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public ServerCommands mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 8:
                        setClearCache(input.readBool());
                        break;
                    case 18:
                        setDisplayErrorMessage(input.readString());
                        break;
                    case 26:
                        setLogErrorStacktrace(input.readString());
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
