package com.google.android.finsky.remoting.protos;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class Common {
    private Common() {
    }

    public static final class Offer extends MessageMicro {
        private boolean hasCheckoutFlowRequired;
        private boolean hasCurrencyCode;
        private boolean hasFormattedAmount;
        private boolean hasFormattedFullAmount;
        private boolean hasFullPriceMicros;
        private boolean hasMicros;
        private boolean hasOfferType;
        private boolean hasRentalTerms;
        private long micros_ = 0;
        private String currencyCode_ = "";
        private String formattedAmount_ = "";
        private long fullPriceMicros_ = 0;
        private String formattedFullAmount_ = "";
        private List<Offer> convertedPrice_ = Collections.emptyList();
        private boolean checkoutFlowRequired_ = false;
        private int offerType_ = 1;
        private RentalTerms rentalTerms_ = null;
        private int cachedSize = -1;

        public long getMicros() {
            return this.micros_;
        }

        public boolean hasMicros() {
            return this.hasMicros;
        }

        public Offer setMicros(long value) {
            this.hasMicros = true;
            this.micros_ = value;
            return this;
        }

        public String getCurrencyCode() {
            return this.currencyCode_;
        }

        public boolean hasCurrencyCode() {
            return this.hasCurrencyCode;
        }

        public Offer setCurrencyCode(String value) {
            this.hasCurrencyCode = true;
            this.currencyCode_ = value;
            return this;
        }

        public String getFormattedAmount() {
            return this.formattedAmount_;
        }

        public boolean hasFormattedAmount() {
            return this.hasFormattedAmount;
        }

        public Offer setFormattedAmount(String value) {
            this.hasFormattedAmount = true;
            this.formattedAmount_ = value;
            return this;
        }

        public long getFullPriceMicros() {
            return this.fullPriceMicros_;
        }

        public boolean hasFullPriceMicros() {
            return this.hasFullPriceMicros;
        }

        public Offer setFullPriceMicros(long value) {
            this.hasFullPriceMicros = true;
            this.fullPriceMicros_ = value;
            return this;
        }

        public String getFormattedFullAmount() {
            return this.formattedFullAmount_;
        }

        public boolean hasFormattedFullAmount() {
            return this.hasFormattedFullAmount;
        }

        public Offer setFormattedFullAmount(String value) {
            this.hasFormattedFullAmount = true;
            this.formattedFullAmount_ = value;
            return this;
        }

        public List<Offer> getConvertedPriceList() {
            return this.convertedPrice_;
        }

        public Offer addConvertedPrice(Offer value) {
            if (value == null) {
                throw new NullPointerException();
            }
            if (this.convertedPrice_.isEmpty()) {
                this.convertedPrice_ = new ArrayList();
            }
            this.convertedPrice_.add(value);
            return this;
        }

        public boolean getCheckoutFlowRequired() {
            return this.checkoutFlowRequired_;
        }

        public boolean hasCheckoutFlowRequired() {
            return this.hasCheckoutFlowRequired;
        }

        public Offer setCheckoutFlowRequired(boolean value) {
            this.hasCheckoutFlowRequired = true;
            this.checkoutFlowRequired_ = value;
            return this;
        }

        public boolean hasOfferType() {
            return this.hasOfferType;
        }

        public int getOfferType() {
            return this.offerType_;
        }

        public Offer setOfferType(int value) {
            this.hasOfferType = true;
            this.offerType_ = value;
            return this;
        }

        public boolean hasRentalTerms() {
            return this.hasRentalTerms;
        }

        public RentalTerms getRentalTerms() {
            return this.rentalTerms_;
        }

        public Offer setRentalTerms(RentalTerms value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.hasRentalTerms = true;
            this.rentalTerms_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasMicros()) {
                output.writeInt64(1, getMicros());
            }
            if (hasCurrencyCode()) {
                output.writeString(2, getCurrencyCode());
            }
            if (hasFormattedAmount()) {
                output.writeString(3, getFormattedAmount());
            }
            for (Offer element : getConvertedPriceList()) {
                output.writeMessage(4, element);
            }
            if (hasCheckoutFlowRequired()) {
                output.writeBool(5, getCheckoutFlowRequired());
            }
            if (hasFullPriceMicros()) {
                output.writeInt64(6, getFullPriceMicros());
            }
            if (hasFormattedFullAmount()) {
                output.writeString(7, getFormattedFullAmount());
            }
            if (hasOfferType()) {
                output.writeInt32(8, getOfferType());
            }
            if (hasRentalTerms()) {
                output.writeMessage(9, getRentalTerms());
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
            int size = hasMicros() ? 0 + CodedOutputStreamMicro.computeInt64Size(1, getMicros()) : 0;
            if (hasCurrencyCode()) {
                size += CodedOutputStreamMicro.computeStringSize(2, getCurrencyCode());
            }
            if (hasFormattedAmount()) {
                size += CodedOutputStreamMicro.computeStringSize(3, getFormattedAmount());
            }
            for (Offer element : getConvertedPriceList()) {
                size += CodedOutputStreamMicro.computeMessageSize(4, element);
            }
            if (hasCheckoutFlowRequired()) {
                size += CodedOutputStreamMicro.computeBoolSize(5, getCheckoutFlowRequired());
            }
            if (hasFullPriceMicros()) {
                size += CodedOutputStreamMicro.computeInt64Size(6, getFullPriceMicros());
            }
            if (hasFormattedFullAmount()) {
                size += CodedOutputStreamMicro.computeStringSize(7, getFormattedFullAmount());
            }
            if (hasOfferType()) {
                size += CodedOutputStreamMicro.computeInt32Size(8, getOfferType());
            }
            if (hasRentalTerms()) {
                size += CodedOutputStreamMicro.computeMessageSize(9, getRentalTerms());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public Offer mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 8:
                        setMicros(input.readInt64());
                        break;
                    case 18:
                        setCurrencyCode(input.readString());
                        break;
                    case 26:
                        setFormattedAmount(input.readString());
                        break;
                    case 34:
                        Offer value = new Offer();
                        input.readMessage(value);
                        addConvertedPrice(value);
                        break;
                    case 40:
                        setCheckoutFlowRequired(input.readBool());
                        break;
                    case 48:
                        setFullPriceMicros(input.readInt64());
                        break;
                    case 58:
                        setFormattedFullAmount(input.readString());
                        break;
                    case 64:
                        setOfferType(input.readInt32());
                        break;
                    case 74:
                        RentalTerms value2 = new RentalTerms();
                        input.readMessage(value2);
                        setRentalTerms(value2);
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

    public static final class RentalTerms extends MessageMicro {
        private boolean hasActivatePeriodSeconds;
        private boolean hasGrantPeriodSeconds;
        private int grantPeriodSeconds_ = 0;
        private int activatePeriodSeconds_ = 0;
        private int cachedSize = -1;

        public int getGrantPeriodSeconds() {
            return this.grantPeriodSeconds_;
        }

        public boolean hasGrantPeriodSeconds() {
            return this.hasGrantPeriodSeconds;
        }

        public RentalTerms setGrantPeriodSeconds(int value) {
            this.hasGrantPeriodSeconds = true;
            this.grantPeriodSeconds_ = value;
            return this;
        }

        public int getActivatePeriodSeconds() {
            return this.activatePeriodSeconds_;
        }

        public boolean hasActivatePeriodSeconds() {
            return this.hasActivatePeriodSeconds;
        }

        public RentalTerms setActivatePeriodSeconds(int value) {
            this.hasActivatePeriodSeconds = true;
            this.activatePeriodSeconds_ = value;
            return this;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public void writeTo(CodedOutputStreamMicro output) throws IOException {
            if (hasGrantPeriodSeconds()) {
                output.writeInt32(1, getGrantPeriodSeconds());
            }
            if (hasActivatePeriodSeconds()) {
                output.writeInt32(2, getActivatePeriodSeconds());
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
            int size = hasGrantPeriodSeconds() ? 0 + CodedOutputStreamMicro.computeInt32Size(1, getGrantPeriodSeconds()) : 0;
            if (hasActivatePeriodSeconds()) {
                size += CodedOutputStreamMicro.computeInt32Size(2, getActivatePeriodSeconds());
            }
            this.cachedSize = size;
            return size;
        }

        @Override // com.google.protobuf.micro.MessageMicro
        public RentalTerms mergeFrom(CodedInputStreamMicro input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 8:
                        setGrantPeriodSeconds(input.readInt32());
                        break;
                    case 16:
                        setActivatePeriodSeconds(input.readInt32());
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
