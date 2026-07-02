package com.google.android.finsky.remoting.protos;

import com.google.android.finsky.remoting.protos.Common;
import com.google.android.finsky.remoting.protos.Rating;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class Doc {
    private Doc() {
    }

    public static final class Availability extends MessageMicro {
        private boolean hasOfferType;
        private boolean hasRestriction;
        private boolean hasUserHasPurchased;
        private int restriction_ = 1;
        private boolean userHasPurchased_ = false;
        private int offerType_ = 1;
        private int cachedSize = -1;

        public boolean hasRestriction() {
            return this.hasRestriction;
        }

        public int getRestriction() {
            return this.restriction_;
        }

        public Availability setRestriction(int value) {
            this.hasRestriction = true;
            this.restriction_ = value;
            return this;
        }

        public boolean getUserHasPurchased() {
            return this.userHasPurchased_;
        }

        public boolean hasUserHasPurchased() {
            return this.hasUserHasPurchased;
        }

        public Availability setUserHasPurchased(boolean value) {
            this.hasUserHasPurchased = true;
            this.userHasPurchased_ = value;
            return this;
        }

        public boolean hasOfferType() {
            return this.hasOfferType;
        }

        public int getOfferType() {
            return this.offerType_;
        }

        public Availability setOfferType(int value) {
            this.hasOfferType = true;
            this.offerType_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasUserHasPurchased()) {
                output.writeBool(4, getUserHasPurchased());
            }
            if (hasRestriction()) {
                output.writeInt32(5, getRestriction());
            }
            if (hasOfferType()) {
                output.writeInt32(6, getOfferType());
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
            int size = hasUserHasPurchased() ? 0 + CodedOutputStreamMicro.computeBoolSize(4, getUserHasPurchased()) : 0;
            if (hasRestriction()) {
                size += CodedOutputStreamMicro.computeInt32Size(5, getRestriction());
            }
            if (hasOfferType()) {
                size += CodedOutputStreamMicro.computeInt32Size(6, getOfferType());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public Availability mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 32:
                        setUserHasPurchased(input.readBool());
                        break;
                    case 40:
                        setRestriction(input.readInt32());
                        break;
                    case 48:
                        setOfferType(input.readInt32());
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

    public static final class Image extends MessageMicro {
        private boolean hasAltTextLocalized;
        private boolean hasDimension;
        private boolean hasImageType;
        private boolean hasImageUrl;
        private boolean hasSecureUrl;
        private int imageType_ = 0;
        private Dimension dimension_ = null;
        private String imageUrl_ = "";
        private String secureUrl_ = "";
        private String altTextLocalized_ = "";
        private int cachedSize = -1;

        public static final class Dimension extends MessageMicro {
            private boolean hasHeight;
            private boolean hasWidth;
            private int width_ = 0;
            private int height_ = 0;
            private int cachedSize = -1;

            public int getWidth() {
                return this.width_;
            }

            public boolean hasWidth() {
                return this.hasWidth;
            }

            public Dimension setWidth(int value) {
                this.hasWidth = true;
                this.width_ = value;
                return this;
            }

            public int getHeight() {
                return this.height_;
            }

            public boolean hasHeight() {
                return this.hasHeight;
            }

            public Dimension setHeight(int value) {
                this.hasHeight = true;
                this.height_ = value;
                return this;
            }

            @Override // com.google.protobuf.micro.MessageMicro
            public void writeTo(CodedOutputStreamMicro output) throws IOException {
                if (hasWidth()) {
                    output.writeInt32(3, getWidth());
                }
                if (hasHeight()) {
                    output.writeInt32(4, getHeight());
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
                int size = hasWidth() ? 0 + CodedOutputStreamMicro.computeInt32Size(3, getWidth()) : 0;
                if (hasHeight()) {
                    size += CodedOutputStreamMicro.computeInt32Size(4, getHeight());
                }
                this.cachedSize = size;
                return size;
            }

            @Override // com.google.protobuf.micro.MessageMicro
            public Dimension mergeFrom(CodedInputStreamMicro input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0:
                            break;
                        case 24:
                            setWidth(input.readInt32());
                            break;
                        case 32:
                            setHeight(input.readInt32());
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

        public boolean hasImageType() {
            return this.hasImageType;
        }

        public int getImageType() {
            return this.imageType_;
        }

        public Image setImageType(int value) {
            this.hasImageType = true;
            this.imageType_ = value;
            return this;
        }

        public boolean hasDimension() {
            return this.hasDimension;
        }

        public Dimension getDimension() {
            return this.dimension_;
        }

        public Image setDimension(Dimension value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasDimension = true;
            this.dimension_ = value;
            return this;
        }

        public String getImageUrl() {
            return this.imageUrl_;
        }

        public boolean hasImageUrl() {
            return this.hasImageUrl;
        }

        public Image setImageUrl(String value) {
            this.hasImageUrl = true;
            this.imageUrl_ = value;
            return this;
        }

        public String getSecureUrl() {
            return this.secureUrl_;
        }

        public boolean hasSecureUrl() {
            return this.hasSecureUrl;
        }

        public Image setSecureUrl(String value) {
            this.hasSecureUrl = true;
            this.secureUrl_ = value;
            return this;
        }

        public String getAltTextLocalized() {
            return this.altTextLocalized_;
        }

        public boolean hasAltTextLocalized() {
            return this.hasAltTextLocalized;
        }

        public Image setAltTextLocalized(String value) {
            this.hasAltTextLocalized = true;
            this.altTextLocalized_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasImageType()) {
                output.writeInt32(1, getImageType());
            }
            if (hasDimension()) {
                output.writeGroup(2, getDimension());
            }
            if (hasImageUrl()) {
                output.writeString(5, getImageUrl());
            }
            if (hasAltTextLocalized()) {
                output.writeString(6, getAltTextLocalized());
            }
            if (hasSecureUrl()) {
                output.writeString(7, getSecureUrl());
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
            int size = hasImageType() ? 0 + CodedOutputStreamMicro.computeInt32Size(1, getImageType()) : 0;
            if (hasDimension()) {
                size += CodedOutputStreamMicro.computeGroupSize(2, getDimension());
            }
            if (hasImageUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(5, getImageUrl());
            }
            if (hasAltTextLocalized()) {
                size += CodedOutputStreamMicro.computeStringSize(6, getAltTextLocalized());
            }
            if (hasSecureUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(7, getSecureUrl());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public Image mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 8:
                        setImageType(input.readInt32());
                        break;
                    case 19:
                        Dimension value = new Dimension();
                        input.readGroup(value, 2);
                        setDimension(value);
                        break;
                    case 42:
                        setImageUrl(input.readString());
                        break;
                    case 50:
                        setAltTextLocalized(input.readString());
                        break;
                    case 58:
                        setSecureUrl(input.readString());
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

    public static final class TranslatedText extends MessageMicro {
        private boolean hasSourceLocale;
        private boolean hasTargetLocale;
        private boolean hasText;
        private String text_ = "";
        private String sourceLocale_ = "";
        private String targetLocale_ = "";
        private int cachedSize = -1;

        public String getText() {
            return this.text_;
        }

        public boolean hasText() {
            return this.hasText;
        }

        public TranslatedText setText(String value) {
            this.hasText = true;
            this.text_ = value;
            return this;
        }

        public String getSourceLocale() {
            return this.sourceLocale_;
        }

        public boolean hasSourceLocale() {
            return this.hasSourceLocale;
        }

        public TranslatedText setSourceLocale(String value) {
            this.hasSourceLocale = true;
            this.sourceLocale_ = value;
            return this;
        }

        public String getTargetLocale() {
            return this.targetLocale_;
        }

        public boolean hasTargetLocale() {
            return this.hasTargetLocale;
        }

        public TranslatedText setTargetLocale(String value) {
            this.hasTargetLocale = true;
            this.targetLocale_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasText()) {
                output.writeString(1, getText());
            }
            if (hasSourceLocale()) {
                output.writeString(2, getSourceLocale());
            }
            if (hasTargetLocale()) {
                output.writeString(3, getTargetLocale());
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
            int size = hasText() ? 0 + CodedOutputStreamMicro.computeStringSize(1, getText()) : 0;
            if (hasSourceLocale()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getSourceLocale());
            }
            if (hasTargetLocale()) {
                size += CodedOutputStreamMicro.computeStringSize(3, getTargetLocale());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public TranslatedText mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        setText(input.readString());
                        break;
                    case 18:
                        setSourceLocale(input.readString());
                        break;
                    case 26:
                        setTargetLocale(input.readString());
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

    public static final class Docid extends MessageMicro {
        private boolean hasBackend;
        private boolean hasBackendDocid;
        private boolean hasType;
        private String backendDocid_ = "";
        private int type_ = 1;
        private int backend_ = 0;
        private int cachedSize = -1;

        public String getBackendDocid() {
            return this.backendDocid_;
        }

        public boolean hasBackendDocid() {
            return this.hasBackendDocid;
        }

        public Docid setBackendDocid(String value) {
            this.hasBackendDocid = true;
            this.backendDocid_ = value;
            return this;
        }

        public boolean hasType() {
            return this.hasType;
        }

        public int getType() {
            return this.type_;
        }

        public Docid setType(int value) {
            this.hasType = true;
            this.type_ = value;
            return this;
        }

        public boolean hasBackend() {
            return this.hasBackend;
        }

        public int getBackend() {
            return this.backend_;
        }

        public Docid setBackend(int value) {
            this.hasBackend = true;
            this.backend_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasBackendDocid()) {
                output.writeString(1, getBackendDocid());
            }
            if (hasType()) {
                output.writeInt32(2, getType());
            }
            if (hasBackend()) {
                output.writeInt32(3, getBackend());
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
            int size = hasBackendDocid() ? 0 + CodedOutputStreamMicro.computeStringSize(1, getBackendDocid()) : 0;
            if (hasType()) {
                size += CodedOutputStreamMicro.computeInt32Size(2, getType());
            }
            if (hasBackend()) {
                size += CodedOutputStreamMicro.computeInt32Size(3, getBackend());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public Docid mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        setBackendDocid(input.readString());
                        break;
                    case 16:
                        setType(input.readInt32());
                        break;
                    case 24:
                        setBackend(input.readInt32());
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

    public static final class Document extends MessageMicro {
        private boolean hasAggregateRating;
        private boolean hasAvailability;
        private boolean hasDocid;
        private boolean hasFetchDocid;
        private boolean hasFullPriceDeprecated;
        private boolean hasPrice;
        private boolean hasSampleDocid;
        private boolean hasTitle;
        private boolean hasUrl;
        private Docid docid_ = null;
        private Docid fetchDocid_ = null;
        private Docid sampleDocid_ = null;
        private String title_ = "";
        private String url_ = "";
        private List<String> snippet_ = Collections.emptyList();
        private List<TranslatedText> translatedSnippet_ = Collections.emptyList();
        private Common.Offer price_ = null;
        private List<Common.Offer> offer_ = Collections.emptyList();
        private Common.Offer fullPriceDeprecated_ = null;
        private Availability availability_ = null;
        private List<Image> image_ = Collections.emptyList();
        private List<Document> child_ = Collections.emptyList();
        private Rating.AggregateRating aggregateRating_ = null;
        private int cachedSize = -1;

        public boolean hasDocid() {
            return this.hasDocid;
        }

        public Docid getDocid() {
            return this.docid_;
        }

        public Document setDocid(Docid value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasDocid = true;
            this.docid_ = value;
            return this;
        }

        public boolean hasFetchDocid() {
            return this.hasFetchDocid;
        }

        public Docid getFetchDocid() {
            return this.fetchDocid_;
        }

        public Document setFetchDocid(Docid value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasFetchDocid = true;
            this.fetchDocid_ = value;
            return this;
        }

        public boolean hasSampleDocid() {
            return this.hasSampleDocid;
        }

        public Docid getSampleDocid() {
            return this.sampleDocid_;
        }

        public Document setSampleDocid(Docid value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasSampleDocid = true;
            this.sampleDocid_ = value;
            return this;
        }

        public String getTitle() {
            return this.title_;
        }

        public boolean hasTitle() {
            return this.hasTitle;
        }

        public Document setTitle(String value) {
            this.hasTitle = true;
            this.title_ = value;
            return this;
        }

        public String getUrl() {
            return this.url_;
        }

        public boolean hasUrl() {
            return this.hasUrl;
        }

        public Document setUrl(String value) {
            this.hasUrl = true;
            this.url_ = value;
            return this;
        }

        public List<String> getSnippetList() {
            return this.snippet_;
        }

        public Document addSnippet(String value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.snippet_.isEmpty()) {
                this.snippet_ = new ArrayList();
            }
            this.snippet_.add(value);
            return this;
        }

        public List<TranslatedText> getTranslatedSnippetList() {
            return this.translatedSnippet_;
        }

        public Document addTranslatedSnippet(TranslatedText value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.translatedSnippet_.isEmpty()) {
                this.translatedSnippet_ = new ArrayList();
            }
            this.translatedSnippet_.add(value);
            return this;
        }

        public boolean hasPrice() {
            return this.hasPrice;
        }

        public Common.Offer getPrice() {
            return this.price_;
        }

        public Document setPrice(Common.Offer value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasPrice = true;
            this.price_ = value;
            return this;
        }

        public List<Common.Offer> getOfferList() {
            return this.offer_;
        }

        public Document addOffer(Common.Offer value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.offer_.isEmpty()) {
                this.offer_ = new ArrayList();
            }
            this.offer_.add(value);
            return this;
        }

        public boolean hasFullPriceDeprecated() {
            return this.hasFullPriceDeprecated;
        }

        public Common.Offer getFullPriceDeprecated() {
            return this.fullPriceDeprecated_;
        }

        public Document setFullPriceDeprecated(Common.Offer value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasFullPriceDeprecated = true;
            this.fullPriceDeprecated_ = value;
            return this;
        }

        public boolean hasAvailability() {
            return this.hasAvailability;
        }

        public Availability getAvailability() {
            return this.availability_;
        }

        public Document setAvailability(Availability value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasAvailability = true;
            this.availability_ = value;
            return this;
        }

        public List<Image> getImageList() {
            return this.image_;
        }

        public Document addImage(Image value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.image_.isEmpty()) {
                this.image_ = new ArrayList();
            }
            this.image_.add(value);
            return this;
        }

        public List<Document> getChildList() {
            return this.child_;
        }

        public Document addChild(Document value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.child_.isEmpty()) {
                this.child_ = new ArrayList();
            }
            this.child_.add(value);
            return this;
        }

        public boolean hasAggregateRating() {
            return this.hasAggregateRating;
        }

        public Rating.AggregateRating getAggregateRating() {
            return this.aggregateRating_;
        }

        public Document setAggregateRating(Rating.AggregateRating value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasAggregateRating = true;
            this.aggregateRating_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasDocid()) {
                output.writeMessage(1, getDocid());
            }
            if (hasFetchDocid()) {
                output.writeMessage(2, getFetchDocid());
            }
            if (hasSampleDocid()) {
                output.writeMessage(3, getSampleDocid());
            }
            if (hasTitle()) {
                output.writeString(4, getTitle());
            }
            if (hasUrl()) {
                output.writeString(5, getUrl());
            }
            for (String element : getSnippetList()) {
                output.writeString(6, element);
            }
            if (hasPrice()) {
                output.writeMessage(7, getPrice());
            }
            if (hasFullPriceDeprecated()) {
                output.writeMessage(8, getFullPriceDeprecated());
            }
            if (hasAvailability()) {
                output.writeMessage(9, getAvailability());
            }
            for (Image element2 : getImageList()) {
                output.writeMessage(10, element2);
            }
            for (Document element3 : getChildList()) {
                output.writeMessage(11, element3);
            }
            if (hasAggregateRating()) {
                output.writeMessage(13, getAggregateRating());
            }
            for (Common.Offer element4 : getOfferList()) {
                output.writeMessage(14, element4);
            }
            for (TranslatedText element5 : getTranslatedSnippetList()) {
                output.writeMessage(15, element5);
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
            int size = hasDocid() ? 0 + CodedOutputStreamMicro.computeMessageSize(1, getDocid()) : 0;
            if (hasFetchDocid()) {
                size += CodedOutputStreamMicro.computeMessageSize(2, getFetchDocid());
            }
            if (hasSampleDocid()) {
                size += CodedOutputStreamMicro.computeMessageSize(3, getSampleDocid());
            }
            if (hasTitle()) {
                size += CodedOutputStreamMicro.computeStringSize(4, getTitle());
            }
            if (hasUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(5, getUrl());
            }
            int dataSize = 0;
            for (String element : getSnippetList()) {
                dataSize += CodedOutputStreamMicro.computeStringSizeNoTag(element);
            }
            int size2 = size + dataSize + (getSnippetList().size() * 1);
            if (hasPrice()) {
                size2 += CodedOutputStreamMicro.computeMessageSize(7, getPrice());
            }
            if (hasFullPriceDeprecated()) {
                size2 += CodedOutputStreamMicro.computeMessageSize(8, getFullPriceDeprecated());
            }
            if (hasAvailability()) {
                size2 += CodedOutputStreamMicro.computeMessageSize(9, getAvailability());
            }
            for (Image element2 : getImageList()) {
                size2 += CodedOutputStreamMicro.computeMessageSize(10, element2);
            }
            for (Document element3 : getChildList()) {
                size2 += CodedOutputStreamMicro.computeMessageSize(11, element3);
            }
            if (hasAggregateRating()) {
                size2 += CodedOutputStreamMicro.computeMessageSize(13, getAggregateRating());
            }
            for (Common.Offer element4 : getOfferList()) {
                size2 += CodedOutputStreamMicro.computeMessageSize(14, element4);
            }
            for (TranslatedText element5 : getTranslatedSnippetList()) {
                size2 += CodedOutputStreamMicro.computeMessageSize(15, element5);
            }
            this.cachedSize = size2;
            return size2;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public Document mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        Docid value = new Docid();
                        input.readMessage(value);
                        setDocid(value);
                        break;
                    case 18:
                        Docid value2 = new Docid();
                        input.readMessage(value2);
                        setFetchDocid(value2);
                        break;
                    case 26:
                        Docid value3 = new Docid();
                        input.readMessage(value3);
                        setSampleDocid(value3);
                        break;
                    case 34:
                        setTitle(input.readString());
                        break;
                    case 42:
                        setUrl(input.readString());
                        break;
                    case 50:
                        addSnippet(input.readString());
                        break;
                    case 58:
                        Common.Offer value4 = new Common.Offer();
                        input.readMessage(value4);
                        setPrice(value4);
                        break;
                    case 66:
                        Common.Offer value5 = new Common.Offer();
                        input.readMessage(value5);
                        setFullPriceDeprecated(value5);
                        break;
                    case 74:
                        Availability value6 = new Availability();
                        input.readMessage(value6);
                        setAvailability(value6);
                        break;
                    case 82:
                        Image value7 = new Image();
                        input.readMessage(value7);
                        addImage(value7);
                        break;
                    case 90:
                        Document value8 = new Document();
                        input.readMessage(value8);
                        addChild(value8);
                        break;
                    case 106:
                        Rating.AggregateRating value9 = new Rating.AggregateRating();
                        input.readMessage(value9);
                        setAggregateRating(value9);
                        break;
                    case 114:
                        Common.Offer value10 = new Common.Offer();
                        input.readMessage(value10);
                        addOffer(value10);
                        break;
                    case 122:
                        TranslatedText value11 = new TranslatedText();
                        input.readMessage(value11);
                        addTranslatedSnippet(value11);
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
