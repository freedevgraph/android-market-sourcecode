package com.google.android.finsky.remoting.protos;

import com.google.android.finsky.remoting.protos.Common;
import com.google.android.finsky.remoting.protos.Purchase;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class Buy {
    private Buy() {
    }

    public static final class BuyResponse extends MessageMicro {
        private boolean hasBaseCheckoutUrl;
        private boolean hasCheckoutInfo;
        private boolean hasCheckoutServiceId;
        private boolean hasCheckoutTokenRequired;
        private boolean hasContinueViaUrl;
        private boolean hasPurchaseResponse;
        private boolean hasPurchaseStatusUrl;
        private Purchase.PurchaseNotificationResponse purchaseResponse_ = null;
        private CheckoutInfo checkoutInfo_ = null;
        private String continueViaUrl_ = "";
        private boolean checkoutTokenRequired_ = false;
        private String checkoutServiceId_ = "";
        private String baseCheckoutUrl_ = "";
        private String purchaseStatusUrl_ = "";
        private int cachedSize = -1;

        public static final class CheckoutInfo extends MessageMicro {
            private boolean hasAddInstrumentUrl;
            private boolean hasDeprecatedCheckoutUrl;
            private boolean hasItem;
            private LineItem item_ = null;
            private List<LineItem> subItem_ = Collections.emptyList();
            private List<CheckoutOption> checkoutOption_ = Collections.emptyList();
            private String deprecatedCheckoutUrl_ = "";
            private String addInstrumentUrl_ = "";
            private int cachedSize = -1;

            public static final class CheckoutOption extends MessageMicro {
                private boolean hasAdjustedCart;
                private boolean hasFormOfPayment;
                private boolean hasInstrumentId;
                private String formOfPayment_ = "";
                private String instrumentId_ = "";
                private String adjustedCart_ = "";
                private int cachedSize = -1;

                public String getFormOfPayment() {
                    return this.formOfPayment_;
                }

                public boolean hasFormOfPayment() {
                    return this.hasFormOfPayment;
                }

                public CheckoutOption setFormOfPayment(String value) {
                    this.hasFormOfPayment = true;
                    this.formOfPayment_ = value;
                    return this;
                }

                public String getInstrumentId() {
                    return this.instrumentId_;
                }

                public boolean hasInstrumentId() {
                    return this.hasInstrumentId;
                }

                public CheckoutOption setInstrumentId(String value) {
                    this.hasInstrumentId = true;
                    this.instrumentId_ = value;
                    return this;
                }

                public String getAdjustedCart() {
                    return this.adjustedCart_;
                }

                public boolean hasAdjustedCart() {
                    return this.hasAdjustedCart;
                }

                public CheckoutOption setAdjustedCart(String value) {
                    this.hasAdjustedCart = true;
                    this.adjustedCart_ = value;
                    return this;
                }

                @Override // com.google.protobuf.micro.MessageMicro
                public void writeTo(CodedOutputStreamMicro output) throws IOException {
                    if (hasFormOfPayment()) {
                        output.writeString(6, getFormOfPayment());
                    }
                    if (hasAdjustedCart()) {
                        output.writeString(7, getAdjustedCart());
                    }
                    if (hasInstrumentId()) {
                        output.writeString(15, getInstrumentId());
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
                    int size = hasFormOfPayment() ? 0 + CodedOutputStreamMicro.computeStringSize(6, getFormOfPayment()) : 0;
                    if (hasAdjustedCart()) {
                        size += CodedOutputStreamMicro.computeStringSize(7, getAdjustedCart());
                    }
                    if (hasInstrumentId()) {
                        size += CodedOutputStreamMicro.computeStringSize(15, getInstrumentId());
                    }
                    this.cachedSize = size;
                    return size;
                }

                @Override // com.google.protobuf.micro.MessageMicro
                public CheckoutOption mergeFrom(CodedInputStreamMicro input) throws IOException {
                    while (true) {
                        int tag = input.readTag();
                        switch (tag) {
                            case 0:
                                break;
                            case 50:
                                setFormOfPayment(input.readString());
                                break;
                            case 58:
                                setAdjustedCart(input.readString());
                                break;
                            case 122:
                                setInstrumentId(input.readString());
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

            public boolean hasItem() {
                return this.hasItem;
            }

            public LineItem getItem() {
                return this.item_;
            }

            public CheckoutInfo setItem(LineItem value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.hasItem = true;
                this.item_ = value;
                return this;
            }

            public List<LineItem> getSubItemList() {
                return this.subItem_;
            }

            public CheckoutInfo addSubItem(LineItem value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                if (this.subItem_.isEmpty()) {
                    this.subItem_ = new ArrayList();
                }
                this.subItem_.add(value);
                return this;
            }

            public List<CheckoutOption> getCheckoutOptionList() {
                return this.checkoutOption_;
            }

            public CheckoutInfo addCheckoutOption(CheckoutOption value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                if (this.checkoutOption_.isEmpty()) {
                    this.checkoutOption_ = new ArrayList();
                }
                this.checkoutOption_.add(value);
                return this;
            }

            public String getDeprecatedCheckoutUrl() {
                return this.deprecatedCheckoutUrl_;
            }

            public boolean hasDeprecatedCheckoutUrl() {
                return this.hasDeprecatedCheckoutUrl;
            }

            public CheckoutInfo setDeprecatedCheckoutUrl(String value) {
                this.hasDeprecatedCheckoutUrl = true;
                this.deprecatedCheckoutUrl_ = value;
                return this;
            }

            public String getAddInstrumentUrl() {
                return this.addInstrumentUrl_;
            }

            public boolean hasAddInstrumentUrl() {
                return this.hasAddInstrumentUrl;
            }

            public CheckoutInfo setAddInstrumentUrl(String value) {
                this.hasAddInstrumentUrl = true;
                this.addInstrumentUrl_ = value;
                return this;
            }

            @Override // com.google.protobuf.micro.MessageMicro
            public void writeTo(CodedOutputStreamMicro output) throws IOException {
                if (hasItem()) {
                    output.writeMessage(3, getItem());
                }
                for (LineItem element : getSubItemList()) {
                    output.writeMessage(4, element);
                }
                for (CheckoutOption element2 : getCheckoutOptionList()) {
                    output.writeGroup(5, element2);
                }
                if (hasDeprecatedCheckoutUrl()) {
                    output.writeString(10, getDeprecatedCheckoutUrl());
                }
                if (hasAddInstrumentUrl()) {
                    output.writeString(11, getAddInstrumentUrl());
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
                int size = hasItem() ? 0 + CodedOutputStreamMicro.computeMessageSize(3, getItem()) : 0;
                for (LineItem element : getSubItemList()) {
                    size += CodedOutputStreamMicro.computeMessageSize(4, element);
                }
                for (CheckoutOption element2 : getCheckoutOptionList()) {
                    size += CodedOutputStreamMicro.computeGroupSize(5, element2);
                }
                if (hasDeprecatedCheckoutUrl()) {
                    size += CodedOutputStreamMicro.computeStringSize(10, getDeprecatedCheckoutUrl());
                }
                if (hasAddInstrumentUrl()) {
                    size += CodedOutputStreamMicro.computeStringSize(11, getAddInstrumentUrl());
                }
                this.cachedSize = size;
                return size;
            }

            @Override // com.google.protobuf.micro.MessageMicro
            public CheckoutInfo mergeFrom(CodedInputStreamMicro input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0:
                            break;
                        case 26:
                            LineItem value = new LineItem();
                            input.readMessage(value);
                            setItem(value);
                            break;
                        case 34:
                            LineItem value2 = new LineItem();
                            input.readMessage(value2);
                            addSubItem(value2);
                            break;
                        case 43:
                            CheckoutOption value3 = new CheckoutOption();
                            input.readGroup(value3, 5);
                            addCheckoutOption(value3);
                            break;
                        case 82:
                            setDeprecatedCheckoutUrl(input.readString());
                            break;
                        case 90:
                            setAddInstrumentUrl(input.readString());
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

        public boolean hasPurchaseResponse() {
            return this.hasPurchaseResponse;
        }

        public Purchase.PurchaseNotificationResponse getPurchaseResponse() {
            return this.purchaseResponse_;
        }

        public BuyResponse setPurchaseResponse(Purchase.PurchaseNotificationResponse value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasPurchaseResponse = true;
            this.purchaseResponse_ = value;
            return this;
        }

        public boolean hasCheckoutInfo() {
            return this.hasCheckoutInfo;
        }

        public CheckoutInfo getCheckoutInfo() {
            return this.checkoutInfo_;
        }

        public BuyResponse setCheckoutInfo(CheckoutInfo value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasCheckoutInfo = true;
            this.checkoutInfo_ = value;
            return this;
        }

        public String getContinueViaUrl() {
            return this.continueViaUrl_;
        }

        public boolean hasContinueViaUrl() {
            return this.hasContinueViaUrl;
        }

        public BuyResponse setContinueViaUrl(String value) {
            this.hasContinueViaUrl = true;
            this.continueViaUrl_ = value;
            return this;
        }

        public boolean getCheckoutTokenRequired() {
            return this.checkoutTokenRequired_;
        }

        public boolean hasCheckoutTokenRequired() {
            return this.hasCheckoutTokenRequired;
        }

        public BuyResponse setCheckoutTokenRequired(boolean value) {
            this.hasCheckoutTokenRequired = true;
            this.checkoutTokenRequired_ = value;
            return this;
        }

        public String getCheckoutServiceId() {
            return this.checkoutServiceId_;
        }

        public boolean hasCheckoutServiceId() {
            return this.hasCheckoutServiceId;
        }

        public BuyResponse setCheckoutServiceId(String value) {
            this.hasCheckoutServiceId = true;
            this.checkoutServiceId_ = value;
            return this;
        }

        public String getBaseCheckoutUrl() {
            return this.baseCheckoutUrl_;
        }

        public boolean hasBaseCheckoutUrl() {
            return this.hasBaseCheckoutUrl;
        }

        public BuyResponse setBaseCheckoutUrl(String value) {
            this.hasBaseCheckoutUrl = true;
            this.baseCheckoutUrl_ = value;
            return this;
        }

        public String getPurchaseStatusUrl() {
            return this.purchaseStatusUrl_;
        }

        public boolean hasPurchaseStatusUrl() {
            return this.hasPurchaseStatusUrl;
        }

        public BuyResponse setPurchaseStatusUrl(String value) {
            this.hasPurchaseStatusUrl = true;
            this.purchaseStatusUrl_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasPurchaseResponse()) {
                output.writeMessage(1, getPurchaseResponse());
            }
            if (hasCheckoutInfo()) {
                output.writeGroup(2, getCheckoutInfo());
            }
            if (hasContinueViaUrl()) {
                output.writeString(8, getContinueViaUrl());
            }
            if (hasPurchaseStatusUrl()) {
                output.writeString(9, getPurchaseStatusUrl());
            }
            if (hasCheckoutServiceId()) {
                output.writeString(12, getCheckoutServiceId());
            }
            if (hasCheckoutTokenRequired()) {
                output.writeBool(13, getCheckoutTokenRequired());
            }
            if (hasBaseCheckoutUrl()) {
                output.writeString(14, getBaseCheckoutUrl());
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
            int size = hasPurchaseResponse() ? 0 + CodedOutputStreamMicro.computeMessageSize(1, getPurchaseResponse()) : 0;
            if (hasCheckoutInfo()) {
                size += CodedOutputStreamMicro.computeGroupSize(2, getCheckoutInfo());
            }
            if (hasContinueViaUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(8, getContinueViaUrl());
            }
            if (hasPurchaseStatusUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(9, getPurchaseStatusUrl());
            }
            if (hasCheckoutServiceId()) {
                size += CodedOutputStreamMicro.computeStringSize(12, getCheckoutServiceId());
            }
            if (hasCheckoutTokenRequired()) {
                size += CodedOutputStreamMicro.computeBoolSize(13, getCheckoutTokenRequired());
            }
            if (hasBaseCheckoutUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(14, getBaseCheckoutUrl());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public BuyResponse mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        Purchase.PurchaseNotificationResponse value = new Purchase.PurchaseNotificationResponse();
                        input.readMessage(value);
                        setPurchaseResponse(value);
                        break;
                    case 19:
                        CheckoutInfo value2 = new CheckoutInfo();
                        input.readGroup(value2, 2);
                        setCheckoutInfo(value2);
                        break;
                    case 66:
                        setContinueViaUrl(input.readString());
                        break;
                    case 74:
                        setPurchaseStatusUrl(input.readString());
                        break;
                    case 98:
                        setCheckoutServiceId(input.readString());
                        break;
                    case 104:
                        setCheckoutTokenRequired(input.readBool());
                        break;
                    case 114:
                        setBaseCheckoutUrl(input.readString());
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

    public static final class LineItem extends MessageMicro {
        private boolean hasDescription;
        private boolean hasName;
        private boolean hasPrice;
        private String name_ = "";
        private String description_ = "";
        private Common.Offer price_ = null;
        private int cachedSize = -1;

        public String getName() {
            return this.name_;
        }

        public boolean hasName() {
            return this.hasName;
        }

        public LineItem setName(String value) {
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

        public LineItem setDescription(String value) {
            this.hasDescription = true;
            this.description_ = value;
            return this;
        }

        public boolean hasPrice() {
            return this.hasPrice;
        }

        public Common.Offer getPrice() {
            return this.price_;
        }

        public LineItem setPrice(Common.Offer value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasPrice = true;
            this.price_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasName()) {
                output.writeString(1, getName());
            }
            if (hasDescription()) {
                output.writeString(2, getDescription());
            }
            if (hasPrice()) {
                output.writeMessage(3, getPrice());
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
            if (hasDescription()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getDescription());
            }
            if (hasPrice()) {
                size += CodedOutputStreamMicro.computeMessageSize(3, getPrice());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public LineItem mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 10:
                        setName(input.readString());
                        break;
                    case 18:
                        setDescription(input.readString());
                        break;
                    case 26:
                        Common.Offer value = new Common.Offer();
                        input.readMessage(value);
                        setPrice(value);
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

    public static final class PurchaseStatusResponse extends MessageMicro {
        private boolean hasBriefMessage;
        private boolean hasInfoUrl;
        private boolean hasStatus;
        private boolean hasStatusMsg;
        private boolean hasStatusTitle;
        private int status_ = 1;
        private String statusMsg_ = "";
        private String statusTitle_ = "";
        private String briefMessage_ = "";
        private String infoUrl_ = "";
        private int cachedSize = -1;

        public boolean hasStatus() {
            return this.hasStatus;
        }

        public int getStatus() {
            return this.status_;
        }

        public PurchaseStatusResponse setStatus(int value) {
            this.hasStatus = true;
            this.status_ = value;
            return this;
        }

        public String getStatusMsg() {
            return this.statusMsg_;
        }

        public boolean hasStatusMsg() {
            return this.hasStatusMsg;
        }

        public PurchaseStatusResponse setStatusMsg(String value) {
            this.hasStatusMsg = true;
            this.statusMsg_ = value;
            return this;
        }

        public String getStatusTitle() {
            return this.statusTitle_;
        }

        public boolean hasStatusTitle() {
            return this.hasStatusTitle;
        }

        public PurchaseStatusResponse setStatusTitle(String value) {
            this.hasStatusTitle = true;
            this.statusTitle_ = value;
            return this;
        }

        public String getBriefMessage() {
            return this.briefMessage_;
        }

        public boolean hasBriefMessage() {
            return this.hasBriefMessage;
        }

        public PurchaseStatusResponse setBriefMessage(String value) {
            this.hasBriefMessage = true;
            this.briefMessage_ = value;
            return this;
        }

        public String getInfoUrl() {
            return this.infoUrl_;
        }

        public boolean hasInfoUrl() {
            return this.hasInfoUrl;
        }

        public PurchaseStatusResponse setInfoUrl(String value) {
            this.hasInfoUrl = true;
            this.infoUrl_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasStatus()) {
                output.writeInt32(1, getStatus());
            }
            if (hasStatusMsg()) {
                output.writeString(2, getStatusMsg());
            }
            if (hasStatusTitle()) {
                output.writeString(3, getStatusTitle());
            }
            if (hasBriefMessage()) {
                output.writeString(4, getBriefMessage());
            }
            if (hasInfoUrl()) {
                output.writeString(5, getInfoUrl());
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
            int size = hasStatus() ? 0 + CodedOutputStreamMicro.computeInt32Size(1, getStatus()) : 0;
            if (hasStatusMsg()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getStatusMsg());
            }
            if (hasStatusTitle()) {
                size += CodedOutputStreamMicro.computeStringSize(3, getStatusTitle());
            }
            if (hasBriefMessage()) {
                size += CodedOutputStreamMicro.computeStringSize(4, getBriefMessage());
            }
            if (hasInfoUrl()) {
                size += CodedOutputStreamMicro.computeStringSize(5, getInfoUrl());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public PurchaseStatusResponse mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 8:
                        setStatus(input.readInt32());
                        break;
                    case 18:
                        setStatusMsg(input.readString());
                        break;
                    case 26:
                        setStatusTitle(input.readString());
                        break;
                    case 34:
                        setBriefMessage(input.readString());
                        break;
                    case 42:
                        setInfoUrl(input.readString());
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
