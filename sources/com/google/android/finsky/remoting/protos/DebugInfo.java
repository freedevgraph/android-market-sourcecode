package com.google.android.finsky.remoting.protos;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class DebugInfo extends MessageMicro {
    private List<String> message_ = Collections.emptyList();
    private List<Timing> timing_ = Collections.emptyList();
    private int cachedSize = -1;

    public static final class Timing extends MessageMicro {
        private boolean hasName;
        private boolean hasTimeInMs;
        private String name_ = "";
        private double timeInMs_ = 0.0d;
        private int cachedSize = -1;

        public String getName() {
            return this.name_;
        }

        public boolean hasName() {
            return this.hasName;
        }

        public Timing setName(String value) {
            this.hasName = true;
            this.name_ = value;
            return this;
        }

        public double getTimeInMs() {
            return this.timeInMs_;
        }

        public boolean hasTimeInMs() {
            return this.hasTimeInMs;
        }

        public Timing setTimeInMs(double value) {
            this.hasTimeInMs = true;
            this.timeInMs_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasName()) {
                output.writeString(3, getName());
            }
            if (hasTimeInMs()) {
                output.writeDouble(4, getTimeInMs());
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
            int size = hasName() ? 0 + CodedOutputStreamMicro.computeStringSize(3, getName()) : 0;
            if (hasTimeInMs()) {
                size += CodedOutputStreamMicro.computeDoubleSize(4, getTimeInMs());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public Timing mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 26:
                        setName(input.readString());
                        break;
                    case 33:
                        setTimeInMs(input.readDouble());
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

    public List<String> getMessageList() {
        return this.message_;
    }

    public DebugInfo addMessage(String value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (this.message_.isEmpty()) {
            this.message_ = new ArrayList();
        }
        this.message_.add(value);
        return this;
    }

    public List<Timing> getTimingList() {
        return this.timing_;
    }

    public DebugInfo addTiming(Timing value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (this.timing_.isEmpty()) {
            this.timing_ = new ArrayList();
        }
        this.timing_.add(value);
        return this;
    }

    @Override // com.google.protobuf.micro.MessageMicro
    public void writeTo(CodedOutputStreamMicro output) throws IOException {
        for (String element : getMessageList()) {
            output.writeString(1, element);
        }
        for (Timing element2 : getTimingList()) {
            output.writeGroup(2, element2);
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
        for (String element : getMessageList()) {
            dataSize += CodedOutputStreamMicro.computeStringSizeNoTag(element);
        }
        int size = 0 + dataSize;
        int size2 = size + (getMessageList().size() * 1);
        for (Timing element2 : getTimingList()) {
            size2 += CodedOutputStreamMicro.computeGroupSize(2, element2);
        }
        this.cachedSize = size2;
        return size2;
    }

    @Override // com.google.protobuf.micro.MessageMicro
    public DebugInfo mergeFrom(CodedInputStreamMicro input) throws IOException {
        while (true) {
            int tag = input.readTag();
            switch (tag) {
                case 0:
                    break;
                case 10:
                    addMessage(input.readString());
                    break;
                case 19:
                    Timing value = new Timing();
                    input.readGroup(value, 2);
                    addTiming(value);
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
