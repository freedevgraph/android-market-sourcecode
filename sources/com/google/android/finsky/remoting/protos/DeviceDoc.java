package com.google.android.finsky.remoting.protos;

import com.google.android.finsky.remoting.protos.BookInfo;
import com.google.android.finsky.remoting.protos.Common;
import com.google.android.finsky.remoting.protos.Doc;
import com.google.android.finsky.remoting.protos.MusicInfo;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class DeviceDoc {
    private DeviceDoc() {
    }

    public static final class DeviceDocument extends MessageMicro {
        private boolean hasCreator;
        private boolean hasDescriptionHtml;
        private boolean hasDetails;
        private boolean hasDetailsUrl;
        private boolean hasDocid;
        private boolean hasFinskyDoc;
        private boolean hasMoreByBrowseUrl;
        private boolean hasMoreByHeader;
        private boolean hasMoreByListUrl;
        private boolean hasRelatedBrowseUrl;
        private boolean hasRelatedHeader;
        private boolean hasRelatedListUrl;
        private boolean hasReviewsUrl;
        private boolean hasShareUrl;
        private Doc.Document finskyDoc_ = null;
        private String docid_ = "";
        private String detailsUrl_ = "";
        private String reviewsUrl_ = "";
        private String relatedListUrl_ = "";
        private String relatedBrowseUrl_ = "";
        private String relatedHeader_ = "";
        private String moreByListUrl_ = "";
        private String moreByBrowseUrl_ = "";
        private String moreByHeader_ = "";
        private String shareUrl_ = "";
        private String creator_ = "";
        private DocumentDetails details_ = null;
        private String descriptionHtml_ = "";
        private int cachedSize = -1;

        public boolean hasFinskyDoc() {
            return this.hasFinskyDoc;
        }

        public Doc.Document getFinskyDoc() {
            return this.finskyDoc_;
        }

        public DeviceDocument setFinskyDoc(Doc.Document value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasFinskyDoc = true;
            this.finskyDoc_ = value;
            return this;
        }

        public String getDocid() {
            return this.docid_;
        }

        public boolean hasDocid() {
            return this.hasDocid;
        }

        public DeviceDocument setDocid(String value) {
            this.hasDocid = true;
            this.docid_ = value;
            return this;
        }

        public String getDetailsUrl() {
            return this.detailsUrl_;
        }

        public boolean hasDetailsUrl() {
            return this.hasDetailsUrl;
        }

        public DeviceDocument setDetailsUrl(String value) {
            this.hasDetailsUrl = true;
            this.detailsUrl_ = value;
            return this;
        }

        public String getReviewsUrl() {
            return this.reviewsUrl_;
        }

        public boolean hasReviewsUrl() {
            return this.hasReviewsUrl;
        }

        public DeviceDocument setReviewsUrl(String value) {
            this.hasReviewsUrl = true;
            this.reviewsUrl_ = value;
            return this;
        }

        public String getRelatedListUrl() {
            return this.relatedListUrl_;
        }

        public boolean hasRelatedListUrl() {
            return this.hasRelatedListUrl;
        }

        public DeviceDocument setRelatedListUrl(String value) {
            this.hasRelatedListUrl = true;
            this.relatedListUrl_ = value;
            return this;
        }

        public String getRelatedBrowseUrl() {
            return this.relatedBrowseUrl_;
        }

        public boolean hasRelatedBrowseUrl() {
            return this.hasRelatedBrowseUrl;
        }

        public DeviceDocument setRelatedBrowseUrl(String value) {
            this.hasRelatedBrowseUrl = true;
            this.relatedBrowseUrl_ = value;
            return this;
        }

        public String getRelatedHeader() {
            return this.relatedHeader_;
        }

        public boolean hasRelatedHeader() {
            return this.hasRelatedHeader;
        }

        public DeviceDocument setRelatedHeader(String value) {
            this.hasRelatedHeader = true;
            this.relatedHeader_ = value;
            return this;
        }

        public String getMoreByListUrl() {
            return this.moreByListUrl_;
        }

        public boolean hasMoreByListUrl() {
            return this.hasMoreByListUrl;
        }

        public DeviceDocument setMoreByListUrl(String value) {
            this.hasMoreByListUrl = true;
            this.moreByListUrl_ = value;
            return this;
        }

        public String getMoreByBrowseUrl() {
            return this.moreByBrowseUrl_;
        }

        public boolean hasMoreByBrowseUrl() {
            return this.hasMoreByBrowseUrl;
        }

        public DeviceDocument setMoreByBrowseUrl(String value) {
            this.hasMoreByBrowseUrl = true;
            this.moreByBrowseUrl_ = value;
            return this;
        }

        public String getMoreByHeader() {
            return this.moreByHeader_;
        }

        public boolean hasMoreByHeader() {
            return this.hasMoreByHeader;
        }

        public DeviceDocument setMoreByHeader(String value) {
            this.hasMoreByHeader = true;
            this.moreByHeader_ = value;
            return this;
        }

        public String getShareUrl() {
            return this.shareUrl_;
        }

        public boolean hasShareUrl() {
            return this.hasShareUrl;
        }

        public DeviceDocument setShareUrl(String value) {
            this.hasShareUrl = true;
            this.shareUrl_ = value;
            return this;
        }

        public String getCreator() {
            return this.creator_;
        }

        public boolean hasCreator() {
            return this.hasCreator;
        }

        public DeviceDocument setCreator(String value) {
            this.hasCreator = true;
            this.creator_ = value;
            return this;
        }

        public boolean hasDetails() {
            return this.hasDetails;
        }

        public DocumentDetails getDetails() {
            return this.details_;
        }

        public DeviceDocument setDetails(DocumentDetails value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasDetails = true;
            this.details_ = value;
            return this;
        }

        public String getDescriptionHtml() {
            return this.descriptionHtml_;
        }

        public boolean hasDescriptionHtml() {
            return this.hasDescriptionHtml;
        }

        public DeviceDocument setDescriptionHtml(String value) {
            this.hasDescriptionHtml = true;
            this.descriptionHtml_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasFinskyDoc()) {
                output.writeMessage(1, getFinskyDoc());
            }
            if (hasDocid()) {
                output.writeString(2, getDocid());
            }
            if (hasDetailsUrl()) {
                output.writeString(3, getDetailsUrl());
            }
            if (hasReviewsUrl()) {
                output.writeString(4, getReviewsUrl());
            }
            if (hasRelatedListUrl()) {
                output.writeString(5, getRelatedListUrl());
            }
            if (hasMoreByListUrl()) {
                output.writeString(6, getMoreByListUrl());
            }
            if (hasShareUrl()) {
                output.writeString(7, getShareUrl());
            }
            if (hasCreator()) {
                output.writeString(8, getCreator());
            }
            if (hasDetails()) {
                output.writeMessage(9, getDetails());
            }
            if (hasDescriptionHtml()) {
                output.writeString(10, getDescriptionHtml());
            }
            if (hasRelatedBrowseUrl()) {
                output.writeString(11, getRelatedBrowseUrl());
            }
            if (hasMoreByBrowseUrl()) {
                output.writeString(12, getMoreByBrowseUrl());
            }
            if (hasRelatedHeader()) {
                output.writeString(13, getRelatedHeader());
            }
            if (hasMoreByHeader()) {
                output.writeString(14, getMoreByHeader());
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
            int size = hasFinskyDoc() ? 0 + CodedOutputStreamMicro.computeMessageSize(1, getFinskyDoc()) : 0;
            if (hasDocid()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getDocid());
            }
            if (hasDetailsUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(3, getDetailsUrl());
            }
            if (hasReviewsUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(4, getReviewsUrl());
            }
            if (hasRelatedListUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(5, getRelatedListUrl());
            }
            if (hasMoreByListUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(6, getMoreByListUrl());
            }
            if (hasShareUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(7, getShareUrl());
            }
            if (hasCreator()) {
                size += CodedOutputStreamMicro.computeStringSize(8, getCreator());
            }
            if (hasDetails()) {
                size += CodedOutputStreamMicro.computeMessageSize(9, getDetails());
            }
            if (hasDescriptionHtml()) {
                size += CodedOutputStreamMicro.computeStringSize(10, getDescriptionHtml());
            }
            if (hasRelatedBrowseUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(11, getRelatedBrowseUrl());
            }
            if (hasMoreByBrowseUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(12, getMoreByBrowseUrl());
            }
            if (hasRelatedHeader()) {
                size += CodedOutputStreamMicro.computeStringSize(13, getRelatedHeader());
            }
            if (hasMoreByHeader()) {
                size += CodedOutputStreamMicro.computeStringSize(14, getMoreByHeader());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public DeviceDocument mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        Doc.Document value = new Doc.Document();
                        input.readMessage(value);
                        setFinskyDoc(value);
                        break;
                    case 18:
                        setDocid(input.readString());
                        break;
                    case 26:
                        setDetailsUrl(input.readString());
                        break;
                    case 34:
                        setReviewsUrl(input.readString());
                        break;
                    case 42:
                        setRelatedListUrl(input.readString());
                        break;
                    case 50:
                        setMoreByListUrl(input.readString());
                        break;
                    case 58:
                        setShareUrl(input.readString());
                        break;
                    case 66:
                        setCreator(input.readString());
                        break;
                    case 74:
                        DocumentDetails value2 = new DocumentDetails();
                        input.readMessage(value2);
                        setDetails(value2);
                        break;
                    case 82:
                        setDescriptionHtml(input.readString());
                        break;
                    case 90:
                        setRelatedBrowseUrl(input.readString());
                        break;
                    case 98:
                        setMoreByBrowseUrl(input.readString());
                        break;
                    case 106:
                        setRelatedHeader(input.readString());
                        break;
                    case 114:
                        setMoreByHeader(input.readString());
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

    public static final class DocumentDetails extends MessageMicro {
        private boolean hasAlbumDetails;
        private boolean hasAppDetails;
        private boolean hasArtistDetails;
        private boolean hasBookDetails;
        private boolean hasSongDetails;
        private boolean hasVideoDetails;
        private AppDetails appDetails_ = null;
        private AlbumDetails albumDetails_ = null;
        private ArtistDetails artistDetails_ = null;
        private SongDetails songDetails_ = null;
        private BookInfo.BookDetails bookDetails_ = null;
        private VideoDetails videoDetails_ = null;
        private int cachedSize = -1;

        public boolean hasAppDetails() {
            return this.hasAppDetails;
        }

        public AppDetails getAppDetails() {
            return this.appDetails_;
        }

        public DocumentDetails setAppDetails(AppDetails value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasAppDetails = true;
            this.appDetails_ = value;
            return this;
        }

        public boolean hasAlbumDetails() {
            return this.hasAlbumDetails;
        }

        public AlbumDetails getAlbumDetails() {
            return this.albumDetails_;
        }

        public DocumentDetails setAlbumDetails(AlbumDetails value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasAlbumDetails = true;
            this.albumDetails_ = value;
            return this;
        }

        public boolean hasArtistDetails() {
            return this.hasArtistDetails;
        }

        public ArtistDetails getArtistDetails() {
            return this.artistDetails_;
        }

        public DocumentDetails setArtistDetails(ArtistDetails value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasArtistDetails = true;
            this.artistDetails_ = value;
            return this;
        }

        public boolean hasSongDetails() {
            return this.hasSongDetails;
        }

        public SongDetails getSongDetails() {
            return this.songDetails_;
        }

        public DocumentDetails setSongDetails(SongDetails value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasSongDetails = true;
            this.songDetails_ = value;
            return this;
        }

        public boolean hasBookDetails() {
            return this.hasBookDetails;
        }

        public BookInfo.BookDetails getBookDetails() {
            return this.bookDetails_;
        }

        public DocumentDetails setBookDetails(BookInfo.BookDetails value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasBookDetails = true;
            this.bookDetails_ = value;
            return this;
        }

        public boolean hasVideoDetails() {
            return this.hasVideoDetails;
        }

        public VideoDetails getVideoDetails() {
            return this.videoDetails_;
        }

        public DocumentDetails setVideoDetails(VideoDetails value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasVideoDetails = true;
            this.videoDetails_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasAppDetails()) {
                output.writeMessage(1, getAppDetails());
            }
            if (hasAlbumDetails()) {
                output.writeMessage(2, getAlbumDetails());
            }
            if (hasArtistDetails()) {
                output.writeMessage(3, getArtistDetails());
            }
            if (hasSongDetails()) {
                output.writeMessage(4, getSongDetails());
            }
            if (hasBookDetails()) {
                output.writeMessage(5, getBookDetails());
            }
            if (hasVideoDetails()) {
                output.writeMessage(6, getVideoDetails());
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
            int size = hasAppDetails() ? 0 + CodedOutputStreamMicro.computeMessageSize(1, getAppDetails()) : 0;
            if (hasAlbumDetails()) {
                size += CodedOutputStreamMicro.computeMessageSize(2, getAlbumDetails());
            }
            if (hasArtistDetails()) {
                size += CodedOutputStreamMicro.computeMessageSize(3, getArtistDetails());
            }
            if (hasSongDetails()) {
                size += CodedOutputStreamMicro.computeMessageSize(4, getSongDetails());
            }
            if (hasBookDetails()) {
                size += CodedOutputStreamMicro.computeMessageSize(5, getBookDetails());
            }
            if (hasVideoDetails()) {
                size += CodedOutputStreamMicro.computeMessageSize(6, getVideoDetails());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public DocumentDetails mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        AppDetails value = new AppDetails();
                        input.readMessage(value);
                        setAppDetails(value);
                        break;
                    case 18:
                        AlbumDetails value2 = new AlbumDetails();
                        input.readMessage(value2);
                        setAlbumDetails(value2);
                        break;
                    case 26:
                        ArtistDetails value3 = new ArtistDetails();
                        input.readMessage(value3);
                        setArtistDetails(value3);
                        break;
                    case 34:
                        SongDetails value4 = new SongDetails();
                        input.readMessage(value4);
                        setSongDetails(value4);
                        break;
                    case 42:
                        BookInfo.BookDetails value5 = new BookInfo.BookDetails();
                        input.readMessage(value5);
                        setBookDetails(value5);
                        break;
                    case 50:
                        VideoDetails value6 = new VideoDetails();
                        input.readMessage(value6);
                        setVideoDetails(value6);
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

    public static final class AppDetails extends MessageMicro {
        private boolean hasContentRating;
        private boolean hasDeveloperEmail;
        private boolean hasDeveloperName;
        private boolean hasDeveloperWebsite;
        private boolean hasInstallationSize;
        private boolean hasMajorVersionNumber;
        private boolean hasNumDownloads;
        private boolean hasPackageName;
        private boolean hasRecentChangesHtml;
        private boolean hasTitle;
        private boolean hasUploadDate;
        private boolean hasVersionCode;
        private boolean hasVersionString;
        private String developerName_ = "";
        private int majorVersionNumber_ = 0;
        private int versionCode_ = 0;
        private String versionString_ = "";
        private String title_ = "";
        private List<String> appCategory_ = Collections.emptyList();
        private int contentRating_ = 0;
        private long installationSize_ = 0;
        private List<String> permission_ = Collections.emptyList();
        private String developerEmail_ = "";
        private String developerWebsite_ = "";
        private String numDownloads_ = "";
        private String packageName_ = "";
        private String recentChangesHtml_ = "";
        private String uploadDate_ = "";
        private int cachedSize = -1;

        public String getDeveloperName() {
            return this.developerName_;
        }

        public boolean hasDeveloperName() {
            return this.hasDeveloperName;
        }

        public AppDetails setDeveloperName(String value) {
            this.hasDeveloperName = true;
            this.developerName_ = value;
            return this;
        }

        public int getMajorVersionNumber() {
            return this.majorVersionNumber_;
        }

        public boolean hasMajorVersionNumber() {
            return this.hasMajorVersionNumber;
        }

        public AppDetails setMajorVersionNumber(int value) {
            this.hasMajorVersionNumber = true;
            this.majorVersionNumber_ = value;
            return this;
        }

        public int getVersionCode() {
            return this.versionCode_;
        }

        public boolean hasVersionCode() {
            return this.hasVersionCode;
        }

        public AppDetails setVersionCode(int value) {
            this.hasVersionCode = true;
            this.versionCode_ = value;
            return this;
        }

        public String getVersionString() {
            return this.versionString_;
        }

        public boolean hasVersionString() {
            return this.hasVersionString;
        }

        public AppDetails setVersionString(String value) {
            this.hasVersionString = true;
            this.versionString_ = value;
            return this;
        }

        public String getTitle() {
            return this.title_;
        }

        public boolean hasTitle() {
            return this.hasTitle;
        }

        public AppDetails setTitle(String value) {
            this.hasTitle = true;
            this.title_ = value;
            return this;
        }

        public List<String> getAppCategoryList() {
            return this.appCategory_;
        }

        public AppDetails addAppCategory(String value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.appCategory_.isEmpty()) {
                this.appCategory_ = new ArrayList();
            }
            this.appCategory_.add(value);
            return this;
        }

        public int getContentRating() {
            return this.contentRating_;
        }

        public boolean hasContentRating() {
            return this.hasContentRating;
        }

        public AppDetails setContentRating(int value) {
            this.hasContentRating = true;
            this.contentRating_ = value;
            return this;
        }

        public long getInstallationSize() {
            return this.installationSize_;
        }

        public boolean hasInstallationSize() {
            return this.hasInstallationSize;
        }

        public AppDetails setInstallationSize(long value) {
            this.hasInstallationSize = true;
            this.installationSize_ = value;
            return this;
        }

        public List<String> getPermissionList() {
            return this.permission_;
        }

        public AppDetails addPermission(String value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.permission_.isEmpty()) {
                this.permission_ = new ArrayList();
            }
            this.permission_.add(value);
            return this;
        }

        public String getDeveloperEmail() {
            return this.developerEmail_;
        }

        public boolean hasDeveloperEmail() {
            return this.hasDeveloperEmail;
        }

        public AppDetails setDeveloperEmail(String value) {
            this.hasDeveloperEmail = true;
            this.developerEmail_ = value;
            return this;
        }

        public String getDeveloperWebsite() {
            return this.developerWebsite_;
        }

        public boolean hasDeveloperWebsite() {
            return this.hasDeveloperWebsite;
        }

        public AppDetails setDeveloperWebsite(String value) {
            this.hasDeveloperWebsite = true;
            this.developerWebsite_ = value;
            return this;
        }

        public String getNumDownloads() {
            return this.numDownloads_;
        }

        public boolean hasNumDownloads() {
            return this.hasNumDownloads;
        }

        public AppDetails setNumDownloads(String value) {
            this.hasNumDownloads = true;
            this.numDownloads_ = value;
            return this;
        }

        public String getPackageName() {
            return this.packageName_;
        }

        public boolean hasPackageName() {
            return this.hasPackageName;
        }

        public AppDetails setPackageName(String value) {
            this.hasPackageName = true;
            this.packageName_ = value;
            return this;
        }

        public String getRecentChangesHtml() {
            return this.recentChangesHtml_;
        }

        public boolean hasRecentChangesHtml() {
            return this.hasRecentChangesHtml;
        }

        public AppDetails setRecentChangesHtml(String value) {
            this.hasRecentChangesHtml = true;
            this.recentChangesHtml_ = value;
            return this;
        }

        public String getUploadDate() {
            return this.uploadDate_;
        }

        public boolean hasUploadDate() {
            return this.hasUploadDate;
        }

        public AppDetails setUploadDate(String value) {
            this.hasUploadDate = true;
            this.uploadDate_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasDeveloperName()) {
                output.writeString(1, getDeveloperName());
            }
            if (hasMajorVersionNumber()) {
                output.writeInt32(2, getMajorVersionNumber());
            }
            if (hasVersionCode()) {
                output.writeInt32(3, getVersionCode());
            }
            if (hasVersionString()) {
                output.writeString(4, getVersionString());
            }
            if (hasTitle()) {
                output.writeString(5, getTitle());
            }
            for (String element : getAppCategoryList()) {
                output.writeString(7, element);
            }
            if (hasContentRating()) {
                output.writeInt32(8, getContentRating());
            }
            if (hasInstallationSize()) {
                output.writeInt64(9, getInstallationSize());
            }
            for (String element2 : getPermissionList()) {
                output.writeString(10, element2);
            }
            if (hasDeveloperEmail()) {
                output.writeString(11, getDeveloperEmail());
            }
            if (hasDeveloperWebsite()) {
                output.writeString(12, getDeveloperWebsite());
            }
            if (hasNumDownloads()) {
                output.writeString(13, getNumDownloads());
            }
            if (hasPackageName()) {
                output.writeString(14, getPackageName());
            }
            if (hasRecentChangesHtml()) {
                output.writeString(15, getRecentChangesHtml());
            }
            if (hasUploadDate()) {
                output.writeString(16, getUploadDate());
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
            int size = hasDeveloperName() ? 0 + CodedOutputStreamMicro.computeStringSize(1, getDeveloperName()) : 0;
            if (hasMajorVersionNumber()) {
                size += CodedOutputStreamMicro.computeInt32Size(2, getMajorVersionNumber());
            }
            if (hasVersionCode()) {
                size += CodedOutputStreamMicro.computeInt32Size(3, getVersionCode());
            }
            if (hasVersionString()) {
                size += CodedOutputStreamMicro.computeStringSize(4, getVersionString());
            }
            if (hasTitle()) {
                size += CodedOutputStreamMicro.computeStringSize(5, getTitle());
            }
            int dataSize = 0;
            for (String element : getAppCategoryList()) {
                dataSize += CodedOutputStreamMicro.computeStringSizeNoTag(element);
            }
            int size2 = size + dataSize + (getAppCategoryList().size() * 1);
            if (hasContentRating()) {
                size2 += CodedOutputStreamMicro.computeInt32Size(8, getContentRating());
            }
            if (hasInstallationSize()) {
                size2 += CodedOutputStreamMicro.computeInt64Size(9, getInstallationSize());
            }
            int dataSize2 = 0;
            for (String element2 : getPermissionList()) {
                dataSize2 += CodedOutputStreamMicro.computeStringSizeNoTag(element2);
            }
            int size3 = size2 + dataSize2 + (getPermissionList().size() * 1);
            if (hasDeveloperEmail()) {
                size3 += CodedOutputStreamMicro.computeStringSize(11, getDeveloperEmail());
            }
            if (hasDeveloperWebsite()) {
                size3 += CodedOutputStreamMicro.computeStringSize(12, getDeveloperWebsite());
            }
            if (hasNumDownloads()) {
                size3 += CodedOutputStreamMicro.computeStringSize(13, getNumDownloads());
            }
            if (hasPackageName()) {
                size3 += CodedOutputStreamMicro.computeStringSize(14, getPackageName());
            }
            if (hasRecentChangesHtml()) {
                size3 += CodedOutputStreamMicro.computeStringSize(15, getRecentChangesHtml());
            }
            if (hasUploadDate()) {
                size3 += CodedOutputStreamMicro.computeStringSize(16, getUploadDate());
            }
            this.cachedSize = size3;
            return size3;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public AppDetails mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        setDeveloperName(input.readString());
                        break;
                    case 16:
                        setMajorVersionNumber(input.readInt32());
                        break;
                    case 24:
                        setVersionCode(input.readInt32());
                        break;
                    case 34:
                        setVersionString(input.readString());
                        break;
                    case 42:
                        setTitle(input.readString());
                        break;
                    case 58:
                        addAppCategory(input.readString());
                        break;
                    case 64:
                        setContentRating(input.readInt32());
                        break;
                    case 72:
                        setInstallationSize(input.readInt64());
                        break;
                    case 82:
                        addPermission(input.readString());
                        break;
                    case 90:
                        setDeveloperEmail(input.readString());
                        break;
                    case 98:
                        setDeveloperWebsite(input.readString());
                        break;
                    case 106:
                        setNumDownloads(input.readString());
                        break;
                    case 114:
                        setPackageName(input.readString());
                        break;
                    case 122:
                        setRecentChangesHtml(input.readString());
                        break;
                    case 130:
                        setUploadDate(input.readString());
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

    public static final class AlbumDetails extends MessageMicro {
        private boolean hasAlbum;
        private MusicInfo.Album album_ = null;
        private List<SongDetails> song_ = Collections.emptyList();
        private int cachedSize = -1;

        public boolean hasAlbum() {
            return this.hasAlbum;
        }

        public MusicInfo.Album getAlbum() {
            return this.album_;
        }

        public AlbumDetails setAlbum(MusicInfo.Album value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasAlbum = true;
            this.album_ = value;
            return this;
        }

        public List<SongDetails> getSongList() {
            return this.song_;
        }

        public AlbumDetails addSong(SongDetails value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.song_.isEmpty()) {
                this.song_ = new ArrayList();
            }
            this.song_.add(value);
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasAlbum()) {
                output.writeMessage(1, getAlbum());
            }
            for (SongDetails element : getSongList()) {
                output.writeMessage(2, element);
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
            int size = hasAlbum() ? 0 + CodedOutputStreamMicro.computeMessageSize(1, getAlbum()) : 0;
            for (SongDetails element : getSongList()) {
                size += CodedOutputStreamMicro.computeMessageSize(2, element);
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public AlbumDetails mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        MusicInfo.Album value = new MusicInfo.Album();
                        input.readMessage(value);
                        setAlbum(value);
                        break;
                    case 18:
                        SongDetails value2 = new SongDetails();
                        input.readMessage(value2);
                        addSong(value2);
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

    public static final class ArtistDetails extends MessageMicro {
        private boolean hasArtist;
        private MusicInfo.Artist artist_ = null;
        private List<MusicInfo.Album> album_ = Collections.emptyList();
        private int cachedSize = -1;

        public boolean hasArtist() {
            return this.hasArtist;
        }

        public MusicInfo.Artist getArtist() {
            return this.artist_;
        }

        public ArtistDetails setArtist(MusicInfo.Artist value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasArtist = true;
            this.artist_ = value;
            return this;
        }

        public List<MusicInfo.Album> getAlbumList() {
            return this.album_;
        }

        public ArtistDetails addAlbum(MusicInfo.Album value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.album_.isEmpty()) {
                this.album_ = new ArrayList();
            }
            this.album_.add(value);
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasArtist()) {
                output.writeMessage(1, getArtist());
            }
            for (MusicInfo.Album element : getAlbumList()) {
                output.writeMessage(2, element);
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
            int size = hasArtist() ? 0 + CodedOutputStreamMicro.computeMessageSize(1, getArtist()) : 0;
            for (MusicInfo.Album element : getAlbumList()) {
                size += CodedOutputStreamMicro.computeMessageSize(2, element);
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public ArtistDetails mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        MusicInfo.Artist value = new MusicInfo.Artist();
                        input.readMessage(value);
                        setArtist(value);
                        break;
                    case 18:
                        MusicInfo.Album value2 = new MusicInfo.Album();
                        input.readMessage(value2);
                        addAlbum(value2);
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

    public static final class SongDetails extends MessageMicro {
        private boolean hasPrice;
        private boolean hasTrack;
        private MusicInfo.Track track_ = null;
        private Common.Offer price_ = null;
        private int cachedSize = -1;

        public boolean hasTrack() {
            return this.hasTrack;
        }

        public MusicInfo.Track getTrack() {
            return this.track_;
        }

        public SongDetails setTrack(MusicInfo.Track value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasTrack = true;
            this.track_ = value;
            return this;
        }

        public boolean hasPrice() {
            return this.hasPrice;
        }

        public Common.Offer getPrice() {
            return this.price_;
        }

        public SongDetails setPrice(Common.Offer value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasPrice = true;
            this.price_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasTrack()) {
                output.writeMessage(1, getTrack());
            }
            if (hasPrice()) {
                output.writeMessage(2, getPrice());
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
            int size = hasTrack() ? 0 + CodedOutputStreamMicro.computeMessageSize(1, getTrack()) : 0;
            if (hasPrice()) {
                size += CodedOutputStreamMicro.computeMessageSize(2, getPrice());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public SongDetails mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        MusicInfo.Track value = new MusicInfo.Track();
                        input.readMessage(value);
                        setTrack(value);
                        break;
                    case 18:
                        Common.Offer value2 = new Common.Offer();
                        input.readMessage(value2);
                        setPrice(value2);
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

    public static final class VideoDetails extends MessageMicro {
        private boolean hasContentRating;
        private boolean hasDislikes;
        private boolean hasDuration;
        private boolean hasLikes;
        private boolean hasReleaseDate;
        private List<VideoCredit> credit_ = Collections.emptyList();
        private String duration_ = "";
        private String releaseDate_ = "";
        private String contentRating_ = "";
        private long likes_ = 0;
        private long dislikes_ = 0;
        private List<String> genre_ = Collections.emptyList();
        private List<Trailer> trailer_ = Collections.emptyList();
        private List<VideoRentalTerm> rentalTerm_ = Collections.emptyList();
        private int cachedSize = -1;

        public List<VideoCredit> getCreditList() {
            return this.credit_;
        }

        public VideoDetails addCredit(VideoCredit value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.credit_.isEmpty()) {
                this.credit_ = new ArrayList();
            }
            this.credit_.add(value);
            return this;
        }

        public String getDuration() {
            return this.duration_;
        }

        public boolean hasDuration() {
            return this.hasDuration;
        }

        public VideoDetails setDuration(String value) {
            this.hasDuration = true;
            this.duration_ = value;
            return this;
        }

        public String getReleaseDate() {
            return this.releaseDate_;
        }

        public boolean hasReleaseDate() {
            return this.hasReleaseDate;
        }

        public VideoDetails setReleaseDate(String value) {
            this.hasReleaseDate = true;
            this.releaseDate_ = value;
            return this;
        }

        public String getContentRating() {
            return this.contentRating_;
        }

        public boolean hasContentRating() {
            return this.hasContentRating;
        }

        public VideoDetails setContentRating(String value) {
            this.hasContentRating = true;
            this.contentRating_ = value;
            return this;
        }

        public long getLikes() {
            return this.likes_;
        }

        public boolean hasLikes() {
            return this.hasLikes;
        }

        public VideoDetails setLikes(long value) {
            this.hasLikes = true;
            this.likes_ = value;
            return this;
        }

        public long getDislikes() {
            return this.dislikes_;
        }

        public boolean hasDislikes() {
            return this.hasDislikes;
        }

        public VideoDetails setDislikes(long value) {
            this.hasDislikes = true;
            this.dislikes_ = value;
            return this;
        }

        public List<String> getGenreList() {
            return this.genre_;
        }

        public VideoDetails addGenre(String value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.genre_.isEmpty()) {
                this.genre_ = new ArrayList();
            }
            this.genre_.add(value);
            return this;
        }

        public List<Trailer> getTrailerList() {
            return this.trailer_;
        }

        public VideoDetails addTrailer(Trailer value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.trailer_.isEmpty()) {
                this.trailer_ = new ArrayList();
            }
            this.trailer_.add(value);
            return this;
        }

        public List<VideoRentalTerm> getRentalTermList() {
            return this.rentalTerm_;
        }

        public VideoDetails addRentalTerm(VideoRentalTerm value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.rentalTerm_.isEmpty()) {
                this.rentalTerm_ = new ArrayList();
            }
            this.rentalTerm_.add(value);
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            for (VideoCredit element : getCreditList()) {
                output.writeMessage(1, element);
            }
            if (hasDuration()) {
                output.writeString(2, getDuration());
            }
            if (hasReleaseDate()) {
                output.writeString(3, getReleaseDate());
            }
            if (hasContentRating()) {
                output.writeString(4, getContentRating());
            }
            if (hasLikes()) {
                output.writeInt64(5, getLikes());
            }
            if (hasDislikes()) {
                output.writeInt64(6, getDislikes());
            }
            for (String element2 : getGenreList()) {
                output.writeString(7, element2);
            }
            for (Trailer element3 : getTrailerList()) {
                output.writeMessage(8, element3);
            }
            for (VideoRentalTerm element4 : getRentalTermList()) {
                output.writeMessage(9, element4);
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
            for (VideoCredit element : getCreditList()) {
                size += CodedOutputStreamMicro.computeMessageSize(1, element);
            }
            if (hasDuration()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getDuration());
            }
            if (hasReleaseDate()) {
                size += CodedOutputStreamMicro.computeStringSize(3, getReleaseDate());
            }
            if (hasContentRating()) {
                size += CodedOutputStreamMicro.computeStringSize(4, getContentRating());
            }
            if (hasLikes()) {
                size += CodedOutputStreamMicro.computeInt64Size(5, getLikes());
            }
            if (hasDislikes()) {
                size += CodedOutputStreamMicro.computeInt64Size(6, getDislikes());
            }
            int dataSize = 0;
            for (String element2 : getGenreList()) {
                dataSize += CodedOutputStreamMicro.computeStringSizeNoTag(element2);
            }
            int size2 = size + dataSize + (getGenreList().size() * 1);
            for (Trailer element3 : getTrailerList()) {
                size2 += CodedOutputStreamMicro.computeMessageSize(8, element3);
            }
            for (VideoRentalTerm element4 : getRentalTermList()) {
                size2 += CodedOutputStreamMicro.computeMessageSize(9, element4);
            }
            this.cachedSize = size2;
            return size2;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public VideoDetails mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        VideoCredit value = new VideoCredit();
                        input.readMessage(value);
                        addCredit(value);
                        break;
                    case 18:
                        setDuration(input.readString());
                        break;
                    case 26:
                        setReleaseDate(input.readString());
                        break;
                    case 34:
                        setContentRating(input.readString());
                        break;
                    case 40:
                        setLikes(input.readInt64());
                        break;
                    case 48:
                        setDislikes(input.readInt64());
                        break;
                    case 58:
                        addGenre(input.readString());
                        break;
                    case 66:
                        Trailer value2 = new Trailer();
                        input.readMessage(value2);
                        addTrailer(value2);
                        break;
                    case 74:
                        VideoRentalTerm value3 = new VideoRentalTerm();
                        input.readMessage(value3);
                        addRentalTerm(value3);
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

    public static final class VideoCredit extends MessageMicro {
        private boolean hasCredit;
        private boolean hasCreditType;
        private int creditType_ = 0;
        private String credit_ = "";
        private List<String> name_ = Collections.emptyList();
        private int cachedSize = -1;

        public boolean hasCreditType() {
            return this.hasCreditType;
        }

        public int getCreditType() {
            return this.creditType_;
        }

        public VideoCredit setCreditType(int value) {
            this.hasCreditType = true;
            this.creditType_ = value;
            return this;
        }

        public String getCredit() {
            return this.credit_;
        }

        public boolean hasCredit() {
            return this.hasCredit;
        }

        public VideoCredit setCredit(String value) {
            this.hasCredit = true;
            this.credit_ = value;
            return this;
        }

        public List<String> getNameList() {
            return this.name_;
        }

        public VideoCredit addName(String value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.name_.isEmpty()) {
                this.name_ = new ArrayList();
            }
            this.name_.add(value);
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasCreditType()) {
                output.writeInt32(1, getCreditType());
            }
            if (hasCredit()) {
                output.writeString(2, getCredit());
            }
            for (String element : getNameList()) {
                output.writeString(3, element);
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
            int size = hasCreditType() ? 0 + CodedOutputStreamMicro.computeInt32Size(1, getCreditType()) : 0;
            if (hasCredit()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getCredit());
            }
            int dataSize = 0;
            for (String element : getNameList()) {
                dataSize += CodedOutputStreamMicro.computeStringSizeNoTag(element);
            }
            int size2 = size + dataSize + (getNameList().size() * 1);
            this.cachedSize = size2;
            return size2;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public VideoCredit mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 8:
                        setCreditType(input.readInt32());
                        break;
                    case 18:
                        setCredit(input.readString());
                        break;
                    case 26:
                        addName(input.readString());
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

    public static final class Trailer extends MessageMicro {
        private boolean hasDuration;
        private boolean hasThumbnailUrl;
        private boolean hasTitle;
        private boolean hasTrailerId;
        private boolean hasWatchUrl;
        private String trailerId_ = "";
        private String title_ = "";
        private String thumbnailUrl_ = "";
        private String watchUrl_ = "";
        private String duration_ = "";
        private int cachedSize = -1;

        public String getTrailerId() {
            return this.trailerId_;
        }

        public boolean hasTrailerId() {
            return this.hasTrailerId;
        }

        public Trailer setTrailerId(String value) {
            this.hasTrailerId = true;
            this.trailerId_ = value;
            return this;
        }

        public String getTitle() {
            return this.title_;
        }

        public boolean hasTitle() {
            return this.hasTitle;
        }

        public Trailer setTitle(String value) {
            this.hasTitle = true;
            this.title_ = value;
            return this;
        }

        public String getThumbnailUrl() {
            return this.thumbnailUrl_;
        }

        public boolean hasThumbnailUrl() {
            return this.hasThumbnailUrl;
        }

        public Trailer setThumbnailUrl(String value) {
            this.hasThumbnailUrl = true;
            this.thumbnailUrl_ = value;
            return this;
        }

        public String getWatchUrl() {
            return this.watchUrl_;
        }

        public boolean hasWatchUrl() {
            return this.hasWatchUrl;
        }

        public Trailer setWatchUrl(String value) {
            this.hasWatchUrl = true;
            this.watchUrl_ = value;
            return this;
        }

        public String getDuration() {
            return this.duration_;
        }

        public boolean hasDuration() {
            return this.hasDuration;
        }

        public Trailer setDuration(String value) {
            this.hasDuration = true;
            this.duration_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasTrailerId()) {
                output.writeString(1, getTrailerId());
            }
            if (hasTitle()) {
                output.writeString(2, getTitle());
            }
            if (hasThumbnailUrl()) {
                output.writeString(3, getThumbnailUrl());
            }
            if (hasWatchUrl()) {
                output.writeString(4, getWatchUrl());
            }
            if (hasDuration()) {
                output.writeString(5, getDuration());
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
            int size = hasTrailerId() ? 0 + CodedOutputStreamMicro.computeStringSize(1, getTrailerId()) : 0;
            if (hasTitle()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getTitle());
            }
            if (hasThumbnailUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(3, getThumbnailUrl());
            }
            if (hasWatchUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(4, getWatchUrl());
            }
            if (hasDuration()) {
                size += CodedOutputStreamMicro.computeStringSize(5, getDuration());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public Trailer mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        setTrailerId(input.readString());
                        break;
                    case 18:
                        setTitle(input.readString());
                        break;
                    case 26:
                        setThumbnailUrl(input.readString());
                        break;
                    case 34:
                        setWatchUrl(input.readString());
                        break;
                    case 42:
                        setDuration(input.readString());
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

    public static final class VideoRentalTerm extends MessageMicro {
        private boolean hasOfferAbbreviation;
        private boolean hasOfferType;
        private boolean hasRentalHeader;
        private int offerType_ = 1;
        private String offerAbbreviation_ = "";
        private String rentalHeader_ = "";
        private List<Term> term_ = Collections.emptyList();
        private int cachedSize = -1;

        public static final class Term extends MessageMicro {
            private boolean hasBody;
            private boolean hasHeader;
            private String header_ = "";
            private String body_ = "";
            private int cachedSize = -1;

            public String getHeader() {
                return this.header_;
            }

            public boolean hasHeader() {
                return this.hasHeader;
            }

            public Term setHeader(String value) {
                this.hasHeader = true;
                this.header_ = value;
                return this;
            }

            public String getBody() {
                return this.body_;
            }

            public boolean hasBody() {
                return this.hasBody;
            }

            public Term setBody(String value) {
                this.hasBody = true;
                this.body_ = value;
                return this;
            }

            @Override // com.google.protobuf.micro.MessageMicro
            public void writeTo(CodedOutputStreamMicro output) throws IOException {
                if (hasHeader()) {
                    output.writeString(5, getHeader());
                }
                if (hasBody()) {
                    output.writeString(6, getBody());
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
                int size = hasHeader() ? 0 + CodedOutputStreamMicro.computeStringSize(5, getHeader()) : 0;
                if (hasBody()) {
                    size += CodedOutputStreamMicro.computeStringSize(6, getBody());
                }
                this.cachedSize = size;
                return size;
            }

            @Override // com.google.protobuf.micro.MessageMicro
            public Term mergeFrom(CodedInputStreamMicro input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0:
                            break;
                        case 42:
                            setHeader(input.readString());
                            break;
                        case 50:
                            setBody(input.readString());
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

        public boolean hasOfferType() {
            return this.hasOfferType;
        }

        public int getOfferType() {
            return this.offerType_;
        }

        public VideoRentalTerm setOfferType(int value) {
            this.hasOfferType = true;
            this.offerType_ = value;
            return this;
        }

        public String getOfferAbbreviation() {
            return this.offerAbbreviation_;
        }

        public boolean hasOfferAbbreviation() {
            return this.hasOfferAbbreviation;
        }

        public VideoRentalTerm setOfferAbbreviation(String value) {
            this.hasOfferAbbreviation = true;
            this.offerAbbreviation_ = value;
            return this;
        }

        public String getRentalHeader() {
            return this.rentalHeader_;
        }

        public boolean hasRentalHeader() {
            return this.hasRentalHeader;
        }

        public VideoRentalTerm setRentalHeader(String value) {
            this.hasRentalHeader = true;
            this.rentalHeader_ = value;
            return this;
        }

        public List<Term> getTermList() {
            return this.term_;
        }

        public VideoRentalTerm addTerm(Term value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.term_.isEmpty()) {
                this.term_ = new ArrayList();
            }
            this.term_.add(value);
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasOfferType()) {
                output.writeInt32(1, getOfferType());
            }
            if (hasOfferAbbreviation()) {
                output.writeString(2, getOfferAbbreviation());
            }
            if (hasRentalHeader()) {
                output.writeString(3, getRentalHeader());
            }
            for (Term element : getTermList()) {
                output.writeGroup(4, element);
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
            int size = hasOfferType() ? 0 + CodedOutputStreamMicro.computeInt32Size(1, getOfferType()) : 0;
            if (hasOfferAbbreviation()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getOfferAbbreviation());
            }
            if (hasRentalHeader()) {
                size += CodedOutputStreamMicro.computeStringSize(3, getRentalHeader());
            }
            for (Term element : getTermList()) {
                size += CodedOutputStreamMicro.computeGroupSize(4, element);
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public VideoRentalTerm mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 8:
                        setOfferType(input.readInt32());
                        break;
                    case 18:
                        setOfferAbbreviation(input.readString());
                        break;
                    case 26:
                        setRentalHeader(input.readString());
                        break;
                    case 35:
                        Term value = new Term();
                        input.readGroup(value, 4);
                        addTerm(value);
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
