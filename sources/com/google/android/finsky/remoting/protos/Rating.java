package com.google.android.finsky.remoting.protos;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class Rating {
    private Rating() {
    }

    public static final class AggregateRating extends MessageMicro {
        private boolean hasFiveStarRatings;
        private boolean hasFourStarRatings;
        private boolean hasOneStarRatings;
        private boolean hasRatingsCount;
        private boolean hasStarRating;
        private boolean hasThreeStarRatings;
        private boolean hasThumbsDownCount;
        private boolean hasThumbsUpCount;
        private boolean hasTwoStarRatings;
        private boolean hasType;
        private int type_ = 1;
        private float starRating_ = 0.0f;
        private long ratingsCount_ = 0;
        private long oneStarRatings_ = 0;
        private long twoStarRatings_ = 0;
        private long threeStarRatings_ = 0;
        private long fourStarRatings_ = 0;
        private long fiveStarRatings_ = 0;
        private long thumbsUpCount_ = 0;
        private long thumbsDownCount_ = 0;
        private int cachedSize = -1;

        public boolean hasType() {
            return this.hasType;
        }

        public int getType() {
            return this.type_;
        }

        public AggregateRating setType(int value) {
            this.hasType = true;
            this.type_ = value;
            return this;
        }

        public float getStarRating() {
            return this.starRating_;
        }

        public boolean hasStarRating() {
            return this.hasStarRating;
        }

        public AggregateRating setStarRating(float value) {
            this.hasStarRating = true;
            this.starRating_ = value;
            return this;
        }

        public long getRatingsCount() {
            return this.ratingsCount_;
        }

        public boolean hasRatingsCount() {
            return this.hasRatingsCount;
        }

        public AggregateRating setRatingsCount(long value) {
            this.hasRatingsCount = true;
            this.ratingsCount_ = value;
            return this;
        }

        public long getOneStarRatings() {
            return this.oneStarRatings_;
        }

        public boolean hasOneStarRatings() {
            return this.hasOneStarRatings;
        }

        public AggregateRating setOneStarRatings(long value) {
            this.hasOneStarRatings = true;
            this.oneStarRatings_ = value;
            return this;
        }

        public long getTwoStarRatings() {
            return this.twoStarRatings_;
        }

        public boolean hasTwoStarRatings() {
            return this.hasTwoStarRatings;
        }

        public AggregateRating setTwoStarRatings(long value) {
            this.hasTwoStarRatings = true;
            this.twoStarRatings_ = value;
            return this;
        }

        public long getThreeStarRatings() {
            return this.threeStarRatings_;
        }

        public boolean hasThreeStarRatings() {
            return this.hasThreeStarRatings;
        }

        public AggregateRating setThreeStarRatings(long value) {
            this.hasThreeStarRatings = true;
            this.threeStarRatings_ = value;
            return this;
        }

        public long getFourStarRatings() {
            return this.fourStarRatings_;
        }

        public boolean hasFourStarRatings() {
            return this.hasFourStarRatings;
        }

        public AggregateRating setFourStarRatings(long value) {
            this.hasFourStarRatings = true;
            this.fourStarRatings_ = value;
            return this;
        }

        public long getFiveStarRatings() {
            return this.fiveStarRatings_;
        }

        public boolean hasFiveStarRatings() {
            return this.hasFiveStarRatings;
        }

        public AggregateRating setFiveStarRatings(long value) {
            this.hasFiveStarRatings = true;
            this.fiveStarRatings_ = value;
            return this;
        }

        public long getThumbsUpCount() {
            return this.thumbsUpCount_;
        }

        public boolean hasThumbsUpCount() {
            return this.hasThumbsUpCount;
        }

        public AggregateRating setThumbsUpCount(long value) {
            this.hasThumbsUpCount = true;
            this.thumbsUpCount_ = value;
            return this;
        }

        public long getThumbsDownCount() {
            return this.thumbsDownCount_;
        }

        public boolean hasThumbsDownCount() {
            return this.hasThumbsDownCount;
        }

        public AggregateRating setThumbsDownCount(long value) {
            this.hasThumbsDownCount = true;
            this.thumbsDownCount_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasType()) {
                output.writeInt32(1, getType());
            }
            if (hasStarRating()) {
                output.writeFloat(2, getStarRating());
            }
            if (hasRatingsCount()) {
                output.writeUInt64(3, getRatingsCount());
            }
            if (hasOneStarRatings()) {
                output.writeUInt64(4, getOneStarRatings());
            }
            if (hasTwoStarRatings()) {
                output.writeUInt64(5, getTwoStarRatings());
            }
            if (hasThreeStarRatings()) {
                output.writeUInt64(6, getThreeStarRatings());
            }
            if (hasFourStarRatings()) {
                output.writeUInt64(7, getFourStarRatings());
            }
            if (hasFiveStarRatings()) {
                output.writeUInt64(8, getFiveStarRatings());
            }
            if (hasThumbsUpCount()) {
                output.writeUInt64(9, getThumbsUpCount());
            }
            if (hasThumbsDownCount()) {
                output.writeUInt64(10, getThumbsDownCount());
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
            int size = hasType() ? 0 + CodedOutputStreamMicro.computeInt32Size(1, getType()) : 0;
            if (hasStarRating()) {
                size += CodedOutputStreamMicro.computeFloatSize(2, getStarRating());
            }
            if (hasRatingsCount()) {
                size += CodedOutputStreamMicro.computeUInt64Size(3, getRatingsCount());
            }
            if (hasOneStarRatings()) {
                size += CodedOutputStreamMicro.computeUInt64Size(4, getOneStarRatings());
            }
            if (hasTwoStarRatings()) {
                size += CodedOutputStreamMicro.computeUInt64Size(5, getTwoStarRatings());
            }
            if (hasThreeStarRatings()) {
                size += CodedOutputStreamMicro.computeUInt64Size(6, getThreeStarRatings());
            }
            if (hasFourStarRatings()) {
                size += CodedOutputStreamMicro.computeUInt64Size(7, getFourStarRatings());
            }
            if (hasFiveStarRatings()) {
                size += CodedOutputStreamMicro.computeUInt64Size(8, getFiveStarRatings());
            }
            if (hasThumbsUpCount()) {
                size += CodedOutputStreamMicro.computeUInt64Size(9, getThumbsUpCount());
            }
            if (hasThumbsDownCount()) {
                size += CodedOutputStreamMicro.computeUInt64Size(10, getThumbsDownCount());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public AggregateRating mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 8:
                        setType(input.readInt32());
                        break;
                    case 21:
                        setStarRating(input.readFloat());
                        break;
                    case 24:
                        setRatingsCount(input.readUInt64());
                        break;
                    case 32:
                        setOneStarRatings(input.readUInt64());
                        break;
                    case 40:
                        setTwoStarRatings(input.readUInt64());
                        break;
                    case 48:
                        setThreeStarRatings(input.readUInt64());
                        break;
                    case 56:
                        setFourStarRatings(input.readUInt64());
                        break;
                    case 64:
                        setFiveStarRatings(input.readUInt64());
                        break;
                    case 72:
                        setThumbsUpCount(input.readUInt64());
                        break;
                    case 80:
                        setThumbsDownCount(input.readUInt64());
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
