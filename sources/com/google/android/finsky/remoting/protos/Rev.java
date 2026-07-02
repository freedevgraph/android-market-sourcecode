package com.google.android.finsky.remoting.protos;

import com.google.android.finsky.remoting.protos.Doc;
import com.google.android.finsky.remoting.protos.Rating;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class Rev {
    private Rev() {
    }

    public static final class Review extends MessageMicro {
        private boolean hasAuthorGaiaId;
        private boolean hasAuthorName;
        private boolean hasAuthorType;
        private boolean hasComment;
        private boolean hasCommentId;
        private boolean hasCommentRating;
        private boolean hasDocid;
        private boolean hasDocumentVersion;
        private boolean hasImageUrl;
        private boolean hasSource;
        private boolean hasSourceUrl;
        private boolean hasStarRating;
        private boolean hasTimestampMsec;
        private boolean hasTitle;
        private boolean hasUrl;
        private Doc.Docid docid_ = null;
        private String authorName_ = "";
        private long authorGaiaId_ = 0;
        private int authorType_ = 0;
        private String url_ = "";
        private String source_ = "";
        private String sourceUrl_ = "";
        private String imageUrl_ = "";
        private String documentVersion_ = "";
        private long timestampMsec_ = 0;
        private int starRating_ = 0;
        private String title_ = "";
        private String comment_ = "";
        private String commentId_ = "";
        private Rating.AggregateRating commentRating_ = null;
        private List<String> pro_ = Collections.emptyList();
        private List<String> con_ = Collections.emptyList();
        private int cachedSize = -1;

        public boolean hasDocid() {
            return this.hasDocid;
        }

        public Doc.Docid getDocid() {
            return this.docid_;
        }

        public Review setDocid(Doc.Docid value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasDocid = true;
            this.docid_ = value;
            return this;
        }

        public String getAuthorName() {
            return this.authorName_;
        }

        public boolean hasAuthorName() {
            return this.hasAuthorName;
        }

        public Review setAuthorName(String value) {
            this.hasAuthorName = true;
            this.authorName_ = value;
            return this;
        }

        public long getAuthorGaiaId() {
            return this.authorGaiaId_;
        }

        public boolean hasAuthorGaiaId() {
            return this.hasAuthorGaiaId;
        }

        public Review setAuthorGaiaId(long value) {
            this.hasAuthorGaiaId = true;
            this.authorGaiaId_ = value;
            return this;
        }

        public boolean hasAuthorType() {
            return this.hasAuthorType;
        }

        public int getAuthorType() {
            return this.authorType_;
        }

        public Review setAuthorType(int value) {
            this.hasAuthorType = true;
            this.authorType_ = value;
            return this;
        }

        public String getUrl() {
            return this.url_;
        }

        public boolean hasUrl() {
            return this.hasUrl;
        }

        public Review setUrl(String value) {
            this.hasUrl = true;
            this.url_ = value;
            return this;
        }

        public String getSource() {
            return this.source_;
        }

        public boolean hasSource() {
            return this.hasSource;
        }

        public Review setSource(String value) {
            this.hasSource = true;
            this.source_ = value;
            return this;
        }

        public String getSourceUrl() {
            return this.sourceUrl_;
        }

        public boolean hasSourceUrl() {
            return this.hasSourceUrl;
        }

        public Review setSourceUrl(String value) {
            this.hasSourceUrl = true;
            this.sourceUrl_ = value;
            return this;
        }

        public String getImageUrl() {
            return this.imageUrl_;
        }

        public boolean hasImageUrl() {
            return this.hasImageUrl;
        }

        public Review setImageUrl(String value) {
            this.hasImageUrl = true;
            this.imageUrl_ = value;
            return this;
        }

        public String getDocumentVersion() {
            return this.documentVersion_;
        }

        public boolean hasDocumentVersion() {
            return this.hasDocumentVersion;
        }

        public Review setDocumentVersion(String value) {
            this.hasDocumentVersion = true;
            this.documentVersion_ = value;
            return this;
        }

        public long getTimestampMsec() {
            return this.timestampMsec_;
        }

        public boolean hasTimestampMsec() {
            return this.hasTimestampMsec;
        }

        public Review setTimestampMsec(long value) {
            this.hasTimestampMsec = true;
            this.timestampMsec_ = value;
            return this;
        }

        public int getStarRating() {
            return this.starRating_;
        }

        public boolean hasStarRating() {
            return this.hasStarRating;
        }

        public Review setStarRating(int value) {
            this.hasStarRating = true;
            this.starRating_ = value;
            return this;
        }

        public String getTitle() {
            return this.title_;
        }

        public boolean hasTitle() {
            return this.hasTitle;
        }

        public Review setTitle(String value) {
            this.hasTitle = true;
            this.title_ = value;
            return this;
        }

        public String getComment() {
            return this.comment_;
        }

        public boolean hasComment() {
            return this.hasComment;
        }

        public Review setComment(String value) {
            this.hasComment = true;
            this.comment_ = value;
            return this;
        }

        public String getCommentId() {
            return this.commentId_;
        }

        public boolean hasCommentId() {
            return this.hasCommentId;
        }

        public Review setCommentId(String value) {
            this.hasCommentId = true;
            this.commentId_ = value;
            return this;
        }

        public boolean hasCommentRating() {
            return this.hasCommentRating;
        }

        public Rating.AggregateRating getCommentRating() {
            return this.commentRating_;
        }

        public Review setCommentRating(Rating.AggregateRating value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasCommentRating = true;
            this.commentRating_ = value;
            return this;
        }

        public List<String> getProList() {
            return this.pro_;
        }

        public Review addPro(String value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.pro_.isEmpty()) {
                this.pro_ = new ArrayList();
            }
            this.pro_.add(value);
            return this;
        }

        public List<String> getConList() {
            return this.con_;
        }

        public Review addCon(String value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.con_.isEmpty()) {
                this.con_ = new ArrayList();
            }
            this.con_.add(value);
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasAuthorName()) {
                output.writeString(1, getAuthorName());
            }
            if (hasUrl()) {
                output.writeString(2, getUrl());
            }
            if (hasSource()) {
                output.writeString(3, getSource());
            }
            if (hasDocumentVersion()) {
                output.writeString(4, getDocumentVersion());
            }
            if (hasTimestampMsec()) {
                output.writeInt64(5, getTimestampMsec());
            }
            if (hasStarRating()) {
                output.writeInt32(6, getStarRating());
            }
            if (hasTitle()) {
                output.writeString(7, getTitle());
            }
            if (hasComment()) {
                output.writeString(8, getComment());
            }
            if (hasCommentId()) {
                output.writeString(9, getCommentId());
            }
            if (hasCommentRating()) {
                output.writeMessage(10, getCommentRating());
            }
            for (String element : getProList()) {
                output.writeString(11, element);
            }
            for (String element2 : getConList()) {
                output.writeString(12, element2);
            }
            if (hasAuthorGaiaId()) {
                output.writeFixed64(13, getAuthorGaiaId());
            }
            if (hasAuthorType()) {
                output.writeInt32(14, getAuthorType());
            }
            if (hasImageUrl()) {
                output.writeString(15, getImageUrl());
            }
            if (hasSourceUrl()) {
                output.writeString(16, getSourceUrl());
            }
            if (hasDocid()) {
                output.writeMessage(17, getDocid());
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
            int size = hasAuthorName() ? 0 + CodedOutputStreamMicro.computeStringSize(1, getAuthorName()) : 0;
            if (hasUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getUrl());
            }
            if (hasSource()) {
                size += CodedOutputStreamMicro.computeStringSize(3, getSource());
            }
            if (hasDocumentVersion()) {
                size += CodedOutputStreamMicro.computeStringSize(4, getDocumentVersion());
            }
            if (hasTimestampMsec()) {
                size += CodedOutputStreamMicro.computeInt64Size(5, getTimestampMsec());
            }
            if (hasStarRating()) {
                size += CodedOutputStreamMicro.computeInt32Size(6, getStarRating());
            }
            if (hasTitle()) {
                size += CodedOutputStreamMicro.computeStringSize(7, getTitle());
            }
            if (hasComment()) {
                size += CodedOutputStreamMicro.computeStringSize(8, getComment());
            }
            if (hasCommentId()) {
                size += CodedOutputStreamMicro.computeStringSize(9, getCommentId());
            }
            if (hasCommentRating()) {
                size += CodedOutputStreamMicro.computeMessageSize(10, getCommentRating());
            }
            int dataSize = 0;
            for (String element : getProList()) {
                dataSize += CodedOutputStreamMicro.computeStringSizeNoTag(element);
            }
            int size2 = size + dataSize + (getProList().size() * 1);
            int dataSize2 = 0;
            for (String element2 : getConList()) {
                dataSize2 += CodedOutputStreamMicro.computeStringSizeNoTag(element2);
            }
            int size3 = size2 + dataSize2 + (getConList().size() * 1);
            if (hasAuthorGaiaId()) {
                size3 += CodedOutputStreamMicro.computeFixed64Size(13, getAuthorGaiaId());
            }
            if (hasAuthorType()) {
                size3 += CodedOutputStreamMicro.computeInt32Size(14, getAuthorType());
            }
            if (hasImageUrl()) {
                size3 += CodedOutputStreamMicro.computeStringSize(15, getImageUrl());
            }
            if (hasSourceUrl()) {
                size3 += CodedOutputStreamMicro.computeStringSize(16, getSourceUrl());
            }
            if (hasDocid()) {
                size3 += CodedOutputStreamMicro.computeMessageSize(17, getDocid());
            }
            this.cachedSize = size3;
            return size3;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public Review mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        setAuthorName(input.readString());
                        break;
                    case 18:
                        setUrl(input.readString());
                        break;
                    case 26:
                        setSource(input.readString());
                        break;
                    case 34:
                        setDocumentVersion(input.readString());
                        break;
                    case 40:
                        setTimestampMsec(input.readInt64());
                        break;
                    case 48:
                        setStarRating(input.readInt32());
                        break;
                    case 58:
                        setTitle(input.readString());
                        break;
                    case 66:
                        setComment(input.readString());
                        break;
                    case 74:
                        setCommentId(input.readString());
                        break;
                    case 82:
                        Rating.AggregateRating value = new Rating.AggregateRating();
                        input.readMessage(value);
                        setCommentRating(value);
                        break;
                    case 90:
                        addPro(input.readString());
                        break;
                    case 98:
                        addCon(input.readString());
                        break;
                    case 105:
                        setAuthorGaiaId(input.readFixed64());
                        break;
                    case 112:
                        setAuthorType(input.readInt32());
                        break;
                    case 122:
                        setImageUrl(input.readString());
                        break;
                    case 130:
                        setSourceUrl(input.readString());
                        break;
                    case 138:
                        Doc.Docid value2 = new Doc.Docid();
                        input.readMessage(value2);
                        setDocid(value2);
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

    public static final class GetReviewsResponse extends MessageMicro {
        private boolean hasMatchingCount;
        private List<Review> review_ = Collections.emptyList();
        private long matchingCount_ = 0;
        private int cachedSize = -1;

        public List<Review> getReviewList() {
            return this.review_;
        }

        public GetReviewsResponse addReview(Review value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.review_.isEmpty()) {
                this.review_ = new ArrayList();
            }
            this.review_.add(value);
            return this;
        }

        public long getMatchingCount() {
            return this.matchingCount_;
        }

        public boolean hasMatchingCount() {
            return this.hasMatchingCount;
        }

        public GetReviewsResponse setMatchingCount(long value) {
            this.hasMatchingCount = true;
            this.matchingCount_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            for (Review element : getReviewList()) {
                output.writeMessage(1, element);
            }
            if (hasMatchingCount()) {
                output.writeInt64(2, getMatchingCount());
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
            for (Review element : getReviewList()) {
                size += CodedOutputStreamMicro.computeMessageSize(1, element);
            }
            if (hasMatchingCount()) {
                size += CodedOutputStreamMicro.computeInt64Size(2, getMatchingCount());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public GetReviewsResponse mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        Review value = new Review();
                        input.readMessage(value);
                        addReview(value);
                        break;
                    case 16:
                        setMatchingCount(input.readInt64());
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

    public static final class ReviewResponse extends MessageMicro {
        private boolean hasGetResponse;
        private GetReviewsResponse getResponse_ = null;
        private int cachedSize = -1;

        public boolean hasGetResponse() {
            return this.hasGetResponse;
        }

        public GetReviewsResponse getGetResponse() {
            return this.getResponse_;
        }

        public ReviewResponse setGetResponse(GetReviewsResponse value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasGetResponse = true;
            this.getResponse_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasGetResponse()) {
                output.writeMessage(1, getGetResponse());
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
            int size = hasGetResponse() ? 0 + CodedOutputStreamMicro.computeMessageSize(1, getGetResponse()) : 0;
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public ReviewResponse mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        GetReviewsResponse value = new GetReviewsResponse();
                        input.readMessage(value);
                        setGetResponse(value);
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
