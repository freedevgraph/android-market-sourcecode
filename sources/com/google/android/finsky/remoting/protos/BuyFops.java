package com.google.android.finsky.remoting.protos;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class BuyFops {
    private BuyFops() {
    }

    public static final class UpdateFopResponse extends MessageMicro {
        private boolean hasInstrumentId;
        private boolean hasResult;
        private boolean hasUserMessageHtml;
        private int result_ = 0;
        private String instrumentId_ = "";
        private String userMessageHtml_ = "";
        private List<InputValidationError> errorInputField_ = Collections.emptyList();
        private int cachedSize = -1;

        public boolean hasResult() {
            return this.hasResult;
        }

        public int getResult() {
            return this.result_;
        }

        public UpdateFopResponse setResult(int value) {
            this.hasResult = true;
            this.result_ = value;
            return this;
        }

        public String getInstrumentId() {
            return this.instrumentId_;
        }

        public boolean hasInstrumentId() {
            return this.hasInstrumentId;
        }

        public UpdateFopResponse setInstrumentId(String value) {
            this.hasInstrumentId = true;
            this.instrumentId_ = value;
            return this;
        }

        public String getUserMessageHtml() {
            return this.userMessageHtml_;
        }

        public boolean hasUserMessageHtml() {
            return this.hasUserMessageHtml;
        }

        public UpdateFopResponse setUserMessageHtml(String value) {
            this.hasUserMessageHtml = true;
            this.userMessageHtml_ = value;
            return this;
        }

        public List<InputValidationError> getErrorInputFieldList() {
            return this.errorInputField_;
        }

        public UpdateFopResponse addErrorInputField(InputValidationError value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.errorInputField_.isEmpty()) {
                this.errorInputField_ = new ArrayList();
            }
            this.errorInputField_.add(value);
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasResult()) {
                output.writeInt32(1, getResult());
            }
            if (hasInstrumentId()) {
                output.writeString(2, getInstrumentId());
            }
            if (hasUserMessageHtml()) {
                output.writeString(3, getUserMessageHtml());
            }
            for (InputValidationError element : getErrorInputFieldList()) {
                output.writeMessage(4, element);
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
            int size = hasResult() ? 0 + CodedOutputStreamMicro.computeInt32Size(1, getResult()) : 0;
            if (hasInstrumentId()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getInstrumentId());
            }
            if (hasUserMessageHtml()) {
                size += CodedOutputStreamMicro.computeStringSize(3, getUserMessageHtml());
            }
            for (InputValidationError element : getErrorInputFieldList()) {
                size += CodedOutputStreamMicro.computeMessageSize(4, element);
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public UpdateFopResponse mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 8:
                        setResult(input.readInt32());
                        break;
                    case 18:
                        setInstrumentId(input.readString());
                        break;
                    case 26:
                        setUserMessageHtml(input.readString());
                        break;
                    case 34:
                        InputValidationError value = new InputValidationError();
                        input.readMessage(value);
                        addErrorInputField(value);
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

    public static final class InputValidationError extends MessageMicro {
        private boolean hasErrorMessage;
        private boolean hasInputField;
        private int inputField_ = 0;
        private String errorMessage_ = "";
        private int cachedSize = -1;

        public boolean hasInputField() {
            return this.hasInputField;
        }

        public int getInputField() {
            return this.inputField_;
        }

        public InputValidationError setInputField(int value) {
            this.hasInputField = true;
            this.inputField_ = value;
            return this;
        }

        public String getErrorMessage() {
            return this.errorMessage_;
        }

        public boolean hasErrorMessage() {
            return this.hasErrorMessage;
        }

        public InputValidationError setErrorMessage(String value) {
            this.hasErrorMessage = true;
            this.errorMessage_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasInputField()) {
                output.writeInt32(1, getInputField());
            }
            if (hasErrorMessage()) {
                output.writeString(2, getErrorMessage());
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
            int size = hasInputField() ? 0 + CodedOutputStreamMicro.computeInt32Size(1, getInputField()) : 0;
            if (hasErrorMessage()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getErrorMessage());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public InputValidationError mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 8:
                        setInputField(input.readInt32());
                        break;
                    case 18:
                        setErrorMessage(input.readString());
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
