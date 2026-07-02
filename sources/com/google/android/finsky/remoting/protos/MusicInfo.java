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
public final class MusicInfo {
    private MusicInfo() {
    }

    public static final class Audio extends MessageMicro {
        private boolean hasAudioType;
        private boolean hasDurationMsec;
        private boolean hasUrl;
        private int audioType_ = 0;
        private String url_ = "";
        private int durationMsec_ = 0;
        private int cachedSize = -1;

        public boolean hasAudioType() {
            return this.hasAudioType;
        }

        public int getAudioType() {
            return this.audioType_;
        }

        public Audio setAudioType(int value) {
            this.hasAudioType = true;
            this.audioType_ = value;
            return this;
        }

        public String getUrl() {
            return this.url_;
        }

        public boolean hasUrl() {
            return this.hasUrl;
        }

        public Audio setUrl(String value) {
            this.hasUrl = true;
            this.url_ = value;
            return this;
        }

        public int getDurationMsec() {
            return this.durationMsec_;
        }

        public boolean hasDurationMsec() {
            return this.hasDurationMsec;
        }

        public Audio setDurationMsec(int value) {
            this.hasDurationMsec = true;
            this.durationMsec_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasAudioType()) {
                output.writeInt32(1, getAudioType());
            }
            if (hasUrl()) {
                output.writeString(2, getUrl());
            }
            if (hasDurationMsec()) {
                output.writeInt32(3, getDurationMsec());
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
            int size = hasAudioType() ? 0 + CodedOutputStreamMicro.computeInt32Size(1, getAudioType()) : 0;
            if (hasUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getUrl());
            }
            if (hasDurationMsec()) {
                size += CodedOutputStreamMicro.computeInt32Size(3, getDurationMsec());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public Audio mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 8:
                        setAudioType(input.readInt32());
                        break;
                    case 18:
                        setUrl(input.readString());
                        break;
                    case 24:
                        setDurationMsec(input.readInt32());
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

    public static final class Detail extends MessageMicro {
        private boolean hasCensoring;
        private boolean hasDeprecatedGenre;
        private boolean hasDescription;
        private boolean hasDurationSec;
        private boolean hasLabel;
        private boolean hasMusicLanguage;
        private boolean hasOriginalReleaseDate;
        private List<String> artistName_ = Collections.emptyList();
        private String deprecatedGenre_ = "";
        private int censoring_ = 0;
        private int durationSec_ = 0;
        private String description_ = "";
        private String originalReleaseDate_ = "";
        private List<Doc.Image> artwork_ = Collections.emptyList();
        private String musicLanguage_ = "";
        private String label_ = "";
        private List<Artist> artist_ = Collections.emptyList();
        private List<String> genre_ = Collections.emptyList();
        private int cachedSize = -1;

        public List<String> getArtistNameList() {
            return this.artistName_;
        }

        public Detail addArtistName(String value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.artistName_.isEmpty()) {
                this.artistName_ = new ArrayList();
            }
            this.artistName_.add(value);
            return this;
        }

        public String getDeprecatedGenre() {
            return this.deprecatedGenre_;
        }

        public boolean hasDeprecatedGenre() {
            return this.hasDeprecatedGenre;
        }

        public Detail setDeprecatedGenre(String value) {
            this.hasDeprecatedGenre = true;
            this.deprecatedGenre_ = value;
            return this;
        }

        public boolean hasCensoring() {
            return this.hasCensoring;
        }

        public int getCensoring() {
            return this.censoring_;
        }

        public Detail setCensoring(int value) {
            this.hasCensoring = true;
            this.censoring_ = value;
            return this;
        }

        public int getDurationSec() {
            return this.durationSec_;
        }

        public boolean hasDurationSec() {
            return this.hasDurationSec;
        }

        public Detail setDurationSec(int value) {
            this.hasDurationSec = true;
            this.durationSec_ = value;
            return this;
        }

        public String getDescription() {
            return this.description_;
        }

        public boolean hasDescription() {
            return this.hasDescription;
        }

        public Detail setDescription(String value) {
            this.hasDescription = true;
            this.description_ = value;
            return this;
        }

        public String getOriginalReleaseDate() {
            return this.originalReleaseDate_;
        }

        public boolean hasOriginalReleaseDate() {
            return this.hasOriginalReleaseDate;
        }

        public Detail setOriginalReleaseDate(String value) {
            this.hasOriginalReleaseDate = true;
            this.originalReleaseDate_ = value;
            return this;
        }

        public List<Doc.Image> getArtworkList() {
            return this.artwork_;
        }

        public Detail addArtwork(Doc.Image value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.artwork_.isEmpty()) {
                this.artwork_ = new ArrayList();
            }
            this.artwork_.add(value);
            return this;
        }

        public String getMusicLanguage() {
            return this.musicLanguage_;
        }

        public boolean hasMusicLanguage() {
            return this.hasMusicLanguage;
        }

        public Detail setMusicLanguage(String value) {
            this.hasMusicLanguage = true;
            this.musicLanguage_ = value;
            return this;
        }

        public String getLabel() {
            return this.label_;
        }

        public boolean hasLabel() {
            return this.hasLabel;
        }

        public Detail setLabel(String value) {
            this.hasLabel = true;
            this.label_ = value;
            return this;
        }

        public List<Artist> getArtistList() {
            return this.artist_;
        }

        public Detail addArtist(Artist value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.artist_.isEmpty()) {
                this.artist_ = new ArrayList();
            }
            this.artist_.add(value);
            return this;
        }

        public List<String> getGenreList() {
            return this.genre_;
        }

        public Detail addGenre(String value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.genre_.isEmpty()) {
                this.genre_ = new ArrayList();
            }
            this.genre_.add(value);
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            for (String element : getArtistNameList()) {
                output.writeString(1, element);
            }
            if (hasDeprecatedGenre()) {
                output.writeString(2, getDeprecatedGenre());
            }
            if (hasCensoring()) {
                output.writeInt32(3, getCensoring());
            }
            if (hasDurationSec()) {
                output.writeInt32(4, getDurationSec());
            }
            if (hasDescription()) {
                output.writeString(5, getDescription());
            }
            if (hasOriginalReleaseDate()) {
                output.writeString(6, getOriginalReleaseDate());
            }
            for (Doc.Image element2 : getArtworkList()) {
                output.writeMessage(7, element2);
            }
            if (hasMusicLanguage()) {
                output.writeString(8, getMusicLanguage());
            }
            if (hasLabel()) {
                output.writeString(9, getLabel());
            }
            for (Artist element3 : getArtistList()) {
                output.writeMessage(10, element3);
            }
            for (String element4 : getGenreList()) {
                output.writeString(11, element4);
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
            int dataSize = 0;
            for (String element : getArtistNameList()) {
                dataSize += CodedOutputStreamMicro.computeStringSizeNoTag(element);
            }
            int size = 0 + dataSize + (getArtistNameList().size() * 1);
            if (hasDeprecatedGenre()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getDeprecatedGenre());
            }
            if (hasCensoring()) {
                size += CodedOutputStreamMicro.computeInt32Size(3, getCensoring());
            }
            if (hasDurationSec()) {
                size += CodedOutputStreamMicro.computeInt32Size(4, getDurationSec());
            }
            if (hasDescription()) {
                size += CodedOutputStreamMicro.computeStringSize(5, getDescription());
            }
            if (hasOriginalReleaseDate()) {
                size += CodedOutputStreamMicro.computeStringSize(6, getOriginalReleaseDate());
            }
            for (Doc.Image element2 : getArtworkList()) {
                size += CodedOutputStreamMicro.computeMessageSize(7, element2);
            }
            if (hasMusicLanguage()) {
                size += CodedOutputStreamMicro.computeStringSize(8, getMusicLanguage());
            }
            if (hasLabel()) {
                size += CodedOutputStreamMicro.computeStringSize(9, getLabel());
            }
            for (Artist element3 : getArtistList()) {
                size += CodedOutputStreamMicro.computeMessageSize(10, element3);
            }
            int dataSize2 = 0;
            for (String element4 : getGenreList()) {
                dataSize2 += CodedOutputStreamMicro.computeStringSizeNoTag(element4);
            }
            int size2 = size + dataSize2 + (getGenreList().size() * 1);
            this.cachedSize = size2;
            return size2;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public Detail mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        addArtistName(input.readString());
                        break;
                    case 18:
                        setDeprecatedGenre(input.readString());
                        break;
                    case 24:
                        setCensoring(input.readInt32());
                        break;
                    case 32:
                        setDurationSec(input.readInt32());
                        break;
                    case 42:
                        setDescription(input.readString());
                        break;
                    case 50:
                        setOriginalReleaseDate(input.readString());
                        break;
                    case 58:
                        Doc.Image value = new Doc.Image();
                        input.readMessage(value);
                        addArtwork(value);
                        break;
                    case 66:
                        setMusicLanguage(input.readString());
                        break;
                    case 74:
                        setLabel(input.readString());
                        break;
                    case 82:
                        Artist value2 = new Artist();
                        input.readMessage(value2);
                        addArtist(value2);
                        break;
                    case 90:
                        addGenre(input.readString());
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

    public static final class Album extends MessageMicro {
        private boolean hasDetail;
        private boolean hasId;
        private boolean hasName;
        private boolean hasNumDiscs;
        private String id_ = "";
        private String name_ = "";
        private Detail detail_ = null;
        private int numDiscs_ = 0;
        private int cachedSize = -1;

        public String getId() {
            return this.id_;
        }

        public boolean hasId() {
            return this.hasId;
        }

        public Album setId(String value) {
            this.hasId = true;
            this.id_ = value;
            return this;
        }

        public String getName() {
            return this.name_;
        }

        public boolean hasName() {
            return this.hasName;
        }

        public Album setName(String value) {
            this.hasName = true;
            this.name_ = value;
            return this;
        }

        public boolean hasDetail() {
            return this.hasDetail;
        }

        public Detail getDetail() {
            return this.detail_;
        }

        public Album setDetail(Detail value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasDetail = true;
            this.detail_ = value;
            return this;
        }

        public int getNumDiscs() {
            return this.numDiscs_;
        }

        public boolean hasNumDiscs() {
            return this.hasNumDiscs;
        }

        public Album setNumDiscs(int value) {
            this.hasNumDiscs = true;
            this.numDiscs_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasId()) {
                output.writeString(1, getId());
            }
            if (hasName()) {
                output.writeString(2, getName());
            }
            if (hasDetail()) {
                output.writeMessage(3, getDetail());
            }
            if (hasNumDiscs()) {
                output.writeInt32(4, getNumDiscs());
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
            int size = hasId() ? 0 + CodedOutputStreamMicro.computeStringSize(1, getId()) : 0;
            if (hasName()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getName());
            }
            if (hasDetail()) {
                size += CodedOutputStreamMicro.computeMessageSize(3, getDetail());
            }
            if (hasNumDiscs()) {
                size += CodedOutputStreamMicro.computeInt32Size(4, getNumDiscs());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public Album mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        setId(input.readString());
                        break;
                    case 18:
                        setName(input.readString());
                        break;
                    case 26:
                        Detail value = new Detail();
                        input.readMessage(value);
                        setDetail(value);
                        break;
                    case 32:
                        setNumDiscs(input.readInt32());
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

    public static final class Track extends MessageMicro {
        private boolean hasAlbumId;
        private boolean hasAlbumName;
        private boolean hasDetail;
        private boolean hasDiscNumber;
        private boolean hasId;
        private boolean hasName;
        private boolean hasTrackNumber;
        private String id_ = "";
        private String name_ = "";
        private Detail detail_ = null;
        private String albumId_ = "";
        private String albumName_ = "";
        private int discNumber_ = 0;
        private int trackNumber_ = 0;
        private List<Audio> audio_ = Collections.emptyList();
        private int cachedSize = -1;

        public String getId() {
            return this.id_;
        }

        public boolean hasId() {
            return this.hasId;
        }

        public Track setId(String value) {
            this.hasId = true;
            this.id_ = value;
            return this;
        }

        public String getName() {
            return this.name_;
        }

        public boolean hasName() {
            return this.hasName;
        }

        public Track setName(String value) {
            this.hasName = true;
            this.name_ = value;
            return this;
        }

        public boolean hasDetail() {
            return this.hasDetail;
        }

        public Detail getDetail() {
            return this.detail_;
        }

        public Track setDetail(Detail value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasDetail = true;
            this.detail_ = value;
            return this;
        }

        public String getAlbumId() {
            return this.albumId_;
        }

        public boolean hasAlbumId() {
            return this.hasAlbumId;
        }

        public Track setAlbumId(String value) {
            this.hasAlbumId = true;
            this.albumId_ = value;
            return this;
        }

        public String getAlbumName() {
            return this.albumName_;
        }

        public boolean hasAlbumName() {
            return this.hasAlbumName;
        }

        public Track setAlbumName(String value) {
            this.hasAlbumName = true;
            this.albumName_ = value;
            return this;
        }

        public int getDiscNumber() {
            return this.discNumber_;
        }

        public boolean hasDiscNumber() {
            return this.hasDiscNumber;
        }

        public Track setDiscNumber(int value) {
            this.hasDiscNumber = true;
            this.discNumber_ = value;
            return this;
        }

        public int getTrackNumber() {
            return this.trackNumber_;
        }

        public boolean hasTrackNumber() {
            return this.hasTrackNumber;
        }

        public Track setTrackNumber(int value) {
            this.hasTrackNumber = true;
            this.trackNumber_ = value;
            return this;
        }

        public List<Audio> getAudioList() {
            return this.audio_;
        }

        public Track addAudio(Audio value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.audio_.isEmpty()) {
                this.audio_ = new ArrayList();
            }
            this.audio_.add(value);
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasId()) {
                output.writeString(1, getId());
            }
            if (hasName()) {
                output.writeString(2, getName());
            }
            if (hasDetail()) {
                output.writeMessage(3, getDetail());
            }
            if (hasAlbumId()) {
                output.writeString(4, getAlbumId());
            }
            if (hasAlbumName()) {
                output.writeString(5, getAlbumName());
            }
            if (hasTrackNumber()) {
                output.writeInt32(6, getTrackNumber());
            }
            for (Audio element : getAudioList()) {
                output.writeMessage(7, element);
            }
            if (hasDiscNumber()) {
                output.writeInt32(8, getDiscNumber());
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
            int size = hasId() ? 0 + CodedOutputStreamMicro.computeStringSize(1, getId()) : 0;
            if (hasName()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getName());
            }
            if (hasDetail()) {
                size += CodedOutputStreamMicro.computeMessageSize(3, getDetail());
            }
            if (hasAlbumId()) {
                size += CodedOutputStreamMicro.computeStringSize(4, getAlbumId());
            }
            if (hasAlbumName()) {
                size += CodedOutputStreamMicro.computeStringSize(5, getAlbumName());
            }
            if (hasTrackNumber()) {
                size += CodedOutputStreamMicro.computeInt32Size(6, getTrackNumber());
            }
            for (Audio element : getAudioList()) {
                size += CodedOutputStreamMicro.computeMessageSize(7, element);
            }
            if (hasDiscNumber()) {
                size += CodedOutputStreamMicro.computeInt32Size(8, getDiscNumber());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public Track mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        setId(input.readString());
                        break;
                    case 18:
                        setName(input.readString());
                        break;
                    case 26:
                        Detail value = new Detail();
                        input.readMessage(value);
                        setDetail(value);
                        break;
                    case 34:
                        setAlbumId(input.readString());
                        break;
                    case 42:
                        setAlbumName(input.readString());
                        break;
                    case 48:
                        setTrackNumber(input.readInt32());
                        break;
                    case 58:
                        Audio value2 = new Audio();
                        input.readMessage(value2);
                        addAudio(value2);
                        break;
                    case 64:
                        setDiscNumber(input.readInt32());
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

    public static final class Artist extends MessageMicro {
        private boolean hasDescription;
        private boolean hasId;
        private boolean hasName;
        private String id_ = "";
        private String name_ = "";
        private String description_ = "";
        private int cachedSize = -1;

        public String getId() {
            return this.id_;
        }

        public boolean hasId() {
            return this.hasId;
        }

        public Artist setId(String value) {
            this.hasId = true;
            this.id_ = value;
            return this;
        }

        public String getName() {
            return this.name_;
        }

        public boolean hasName() {
            return this.hasName;
        }

        public Artist setName(String value) {
            this.hasName = true;
            this.name_ = value;
            return this;
        }

        public String getDescription() {
            return this.description_;
        }

        public boolean hasDescription() {
            return this.hasDescription;
        }

        public Artist setDescription(String value) {
            this.hasDescription = true;
            this.description_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasId()) {
                output.writeString(1, getId());
            }
            if (hasName()) {
                output.writeString(2, getName());
            }
            if (hasDescription()) {
                output.writeString(3, getDescription());
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
            int size = hasId() ? 0 + CodedOutputStreamMicro.computeStringSize(1, getId()) : 0;
            if (hasName()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getName());
            }
            if (hasDescription()) {
                size += CodedOutputStreamMicro.computeStringSize(3, getDescription());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public Artist mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        setId(input.readString());
                        break;
                    case 18:
                        setName(input.readString());
                        break;
                    case 26:
                        setDescription(input.readString());
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
