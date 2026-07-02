package com.google.android.finsky.remoting.protos;

import com.google.android.finsky.remoting.protos.Doc;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class BookInfo {
    private BookInfo() {
    }

    public static final class BookSubject extends MessageMicro {
        private boolean hasName;
        private boolean hasQuery;
        private String name_ = "";
        private String query_ = "";
        private int cachedSize = -1;

        public String getName() {
            return this.name_;
        }

        public boolean hasName() {
            return this.hasName;
        }

        public BookSubject setName(String value) {
            this.hasName = true;
            this.name_ = value;
            return this;
        }

        public String getQuery() {
            return this.query_;
        }

        public boolean hasQuery() {
            return this.hasQuery;
        }

        public BookSubject setQuery(String value) {
            this.hasQuery = true;
            this.query_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasName()) {
                output.writeString(1, getName());
            }
            if (hasQuery()) {
                output.writeString(2, getQuery());
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
            if (hasQuery()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getQuery());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public BookSubject mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        setName(input.readString());
                        break;
                    case 18:
                        setQuery(input.readString());
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

    public static final class BookAuthor extends MessageMicro {
        private boolean hasDeprecatedQuery;
        private boolean hasDocid;
        private boolean hasName;
        private String name_ = "";
        private Doc.Docid docid_ = null;
        private String deprecatedQuery_ = "";
        private int cachedSize = -1;

        public String getName() {
            return this.name_;
        }

        public boolean hasName() {
            return this.hasName;
        }

        public BookAuthor setName(String value) {
            this.hasName = true;
            this.name_ = value;
            return this;
        }

        public boolean hasDocid() {
            return this.hasDocid;
        }

        public Doc.Docid getDocid() {
            return this.docid_;
        }

        public BookAuthor setDocid(Doc.Docid value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasDocid = true;
            this.docid_ = value;
            return this;
        }

        public String getDeprecatedQuery() {
            return this.deprecatedQuery_;
        }

        public boolean hasDeprecatedQuery() {
            return this.hasDeprecatedQuery;
        }

        public BookAuthor setDeprecatedQuery(String value) {
            this.hasDeprecatedQuery = true;
            this.deprecatedQuery_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasName()) {
                output.writeString(1, getName());
            }
            if (hasDeprecatedQuery()) {
                output.writeString(2, getDeprecatedQuery());
            }
            if (hasDocid()) {
                output.writeMessage(3, getDocid());
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
            if (hasDeprecatedQuery()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getDeprecatedQuery());
            }
            if (hasDocid()) {
                size += CodedOutputStreamMicro.computeMessageSize(3, getDocid());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public BookAuthor mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        setName(input.readString());
                        break;
                    case 18:
                        setDeprecatedQuery(input.readString());
                        break;
                    case 26:
                        Doc.Docid value = new Doc.Docid();
                        input.readMessage(value);
                        setDocid(value);
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

    public static final class BookDetails extends MessageMicro {
        private boolean hasIsbn;
        private boolean hasNumberOfPages;
        private boolean hasPublicationDate;
        private boolean hasPublisher;
        private boolean hasSubtitle;
        private List<BookAuthor> author_ = Collections.emptyList();
        private List<BookSubject> subject_ = Collections.emptyList();
        private String publisher_ = "";
        private String publicationDate_ = "";
        private String isbn_ = "";
        private int numberOfPages_ = 0;
        private String subtitle_ = "";
        private int cachedSize = -1;

        public List<BookAuthor> getAuthorList() {
            return this.author_;
        }

        public BookDetails addAuthor(BookAuthor value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.author_.isEmpty()) {
                this.author_ = new ArrayList();
            }
            this.author_.add(value);
            return this;
        }

        public List<BookSubject> getSubjectList() {
            return this.subject_;
        }

        public BookDetails addSubject(BookSubject value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.subject_.isEmpty()) {
                this.subject_ = new ArrayList();
            }
            this.subject_.add(value);
            return this;
        }

        public String getPublisher() {
            return this.publisher_;
        }

        public boolean hasPublisher() {
            return this.hasPublisher;
        }

        public BookDetails setPublisher(String value) {
            this.hasPublisher = true;
            this.publisher_ = value;
            return this;
        }

        public String getPublicationDate() {
            return this.publicationDate_;
        }

        public boolean hasPublicationDate() {
            return this.hasPublicationDate;
        }

        public BookDetails setPublicationDate(String value) {
            this.hasPublicationDate = true;
            this.publicationDate_ = value;
            return this;
        }

        public String getIsbn() {
            return this.isbn_;
        }

        public boolean hasIsbn() {
            return this.hasIsbn;
        }

        public BookDetails setIsbn(String value) {
            this.hasIsbn = true;
            this.isbn_ = value;
            return this;
        }

        public int getNumberOfPages() {
            return this.numberOfPages_;
        }

        public boolean hasNumberOfPages() {
            return this.hasNumberOfPages;
        }

        public BookDetails setNumberOfPages(int value) {
            this.hasNumberOfPages = true;
            this.numberOfPages_ = value;
            return this;
        }

        public String getSubtitle() {
            return this.subtitle_;
        }

        public boolean hasSubtitle() {
            return this.hasSubtitle;
        }

        public BookDetails setSubtitle(String value) {
            this.hasSubtitle = true;
            this.subtitle_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            for (BookSubject element : getSubjectList()) {
                output.writeMessage(3, element);
            }
            if (hasPublisher()) {
                output.writeString(4, getPublisher());
            }
            if (hasPublicationDate()) {
                output.writeString(5, getPublicationDate());
            }
            if (hasIsbn()) {
                output.writeString(6, getIsbn());
            }
            if (hasNumberOfPages()) {
                output.writeInt32(7, getNumberOfPages());
            }
            if (hasSubtitle()) {
                output.writeString(8, getSubtitle());
            }
            for (BookAuthor element2 : getAuthorList()) {
                output.writeMessage(9, element2);
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
            for (BookSubject element : getSubjectList()) {
                size += CodedOutputStreamMicro.computeMessageSize(3, element);
            }
            if (hasPublisher()) {
                size += CodedOutputStreamMicro.computeStringSize(4, getPublisher());
            }
            if (hasPublicationDate()) {
                size += CodedOutputStreamMicro.computeStringSize(5, getPublicationDate());
            }
            if (hasIsbn()) {
                size += CodedOutputStreamMicro.computeStringSize(6, getIsbn());
            }
            if (hasNumberOfPages()) {
                size += CodedOutputStreamMicro.computeInt32Size(7, getNumberOfPages());
            }
            if (hasSubtitle()) {
                size += CodedOutputStreamMicro.computeStringSize(8, getSubtitle());
            }
            for (BookAuthor element2 : getAuthorList()) {
                size += CodedOutputStreamMicro.computeMessageSize(9, element2);
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public BookDetails mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 26:
                        BookSubject value = new BookSubject();
                        input.readMessage(value);
                        addSubject(value);
                        break;
                    case 34:
                        setPublisher(input.readString());
                        break;
                    case 42:
                        setPublicationDate(input.readString());
                        break;
                    case 50:
                        setIsbn(input.readString());
                        break;
                    case 56:
                        setNumberOfPages(input.readInt32());
                        break;
                    case 66:
                        setSubtitle(input.readString());
                        break;
                    case 74:
                        BookAuthor value2 = new BookAuthor();
                        input.readMessage(value2);
                        addAuthor(value2);
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
