package com.google.protobuf.micro;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/* JADX INFO: loaded from: classes.dex */
public final class CodedInputStreamMicro {
    private final byte[] buffer;
    private int bufferPos;
    private int bufferSize;
    private int bufferSizeAfterLimit;
    private int lastTag;
    private int recursionDepth;
    private int totalBytesRetired;
    private int currentLimit = Integer.MAX_VALUE;
    private int recursionLimit = 64;
    private int sizeLimit = 67108864;
    private final InputStream input = null;

    public static CodedInputStreamMicro newInstance(byte[] buf, int off, int len) {
        return new CodedInputStreamMicro(buf, off, len);
    }

    public int readTag() throws IOException {
        if (isAtEnd()) {
            this.lastTag = 0;
            return 0;
        }
        this.lastTag = readRawVarint32();
        if (this.lastTag == 0) {
            throw InvalidProtocolBufferMicroException.invalidTag();
        }
        return this.lastTag;
    }

    public void checkLastTagWas(int value) throws InvalidProtocolBufferMicroException {
        if (this.lastTag != value) {
            throw InvalidProtocolBufferMicroException.invalidEndTag();
        }
    }

    public boolean skipField(int tag) throws IOException {
        switch (WireFormatMicro.getTagWireType(tag)) {
            case 0:
                readInt32();
                return true;
            case 1:
                readRawLittleEndian64();
                return true;
            case 2:
                skipRawBytes(readRawVarint32());
                return true;
            case 3:
                skipMessage();
                checkLastTagWas(WireFormatMicro.makeTag(WireFormatMicro.getTagFieldNumber(tag), 4));
                return true;
            case 4:
                return false;
            case 5:
                readRawLittleEndian32();
                return true;
            default:
                throw InvalidProtocolBufferMicroException.invalidWireType();
        }
    }

    public void skipMessage() throws IOException {
        int tag;
        do {
            tag = readTag();
            if (tag == 0) {
                return;
            }
        } while (skipField(tag));
    }

    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readRawLittleEndian64());
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readRawLittleEndian32());
    }

    public long readUInt64() throws IOException {
        return readRawVarint64();
    }

    public long readInt64() throws IOException {
        return readRawVarint64();
    }

    public int readInt32() throws IOException {
        return readRawVarint32();
    }

    public long readFixed64() throws IOException {
        return readRawLittleEndian64();
    }

    public boolean readBool() throws IOException {
        return readRawVarint32() != 0;
    }

    public String readString() throws IOException {
        int size = readRawVarint32();
        if (size > this.bufferSize - this.bufferPos || size <= 0) {
            return new String(readRawBytes(size), "UTF-8");
        }
        String result = new String(this.buffer, this.bufferPos, size, "UTF-8");
        this.bufferPos += size;
        return result;
    }

    public void readGroup(MessageMicro msg, int fieldNumber) throws IOException {
        if (this.recursionDepth >= this.recursionLimit) {
            throw InvalidProtocolBufferMicroException.recursionLimitExceeded();
        }
        this.recursionDepth++;
        msg.mergeFrom(this);
        checkLastTagWas(WireFormatMicro.makeTag(fieldNumber, 4));
        this.recursionDepth--;
    }

    public void readMessage(MessageMicro msg) throws IOException {
        int length = readRawVarint32();
        if (this.recursionDepth >= this.recursionLimit) {
            throw InvalidProtocolBufferMicroException.recursionLimitExceeded();
        }
        int oldLimit = pushLimit(length);
        this.recursionDepth++;
        msg.mergeFrom(this);
        checkLastTagWas(0);
        this.recursionDepth--;
        popLimit(oldLimit);
    }

    public int readRawVarint32() throws IOException {
        int result;
        byte tmp = readRawByte();
        if (tmp >= 0) {
            return tmp;
        }
        int result2 = tmp & 127;
        byte tmp2 = readRawByte();
        if (tmp2 >= 0) {
            result = result2 | (tmp2 << 7);
        } else {
            int result3 = result2 | ((tmp2 & 127) << 7);
            byte tmp3 = readRawByte();
            if (tmp3 >= 0) {
                result = result3 | (tmp3 << 14);
            } else {
                int result4 = result3 | ((tmp3 & 127) << 14);
                byte tmp4 = readRawByte();
                if (tmp4 >= 0) {
                    result = result4 | (tmp4 << 21);
                } else {
                    int result5 = result4 | ((tmp4 & 127) << 21);
                    byte tmp5 = readRawByte();
                    result = result5 | (tmp5 << 28);
                    if (tmp5 < 0) {
                        for (int i = 0; i < 5; i++) {
                            if (readRawByte() >= 0) {
                                return result;
                            }
                        }
                        throw InvalidProtocolBufferMicroException.malformedVarint();
                    }
                }
            }
        }
        return result;
    }

    public long readRawVarint64() throws IOException {
        long result = 0;
        for (int shift = 0; shift < 64; shift += 7) {
            byte b = readRawByte();
            result |= ((long) (b & 127)) << shift;
            if ((b & 128) == 0) {
                return result;
            }
        }
        throw InvalidProtocolBufferMicroException.malformedVarint();
    }

    public int readRawLittleEndian32() throws IOException {
        byte b1 = readRawByte();
        byte b2 = readRawByte();
        byte b3 = readRawByte();
        byte b4 = readRawByte();
        return (b1 & 255) | ((b2 & 255) << 8) | ((b3 & 255) << 16) | ((b4 & 255) << 24);
    }

    public long readRawLittleEndian64() throws IOException {
        byte b1 = readRawByte();
        byte b2 = readRawByte();
        byte b3 = readRawByte();
        byte b4 = readRawByte();
        byte b5 = readRawByte();
        byte b6 = readRawByte();
        byte b7 = readRawByte();
        byte b8 = readRawByte();
        return (((long) b1) & 255) | ((((long) b2) & 255) << 8) | ((((long) b3) & 255) << 16) | ((((long) b4) & 255) << 24) | ((((long) b5) & 255) << 32) | ((((long) b6) & 255) << 40) | ((((long) b7) & 255) << 48) | ((((long) b8) & 255) << 56);
    }

    private CodedInputStreamMicro(byte[] buffer, int off, int len) {
        this.buffer = buffer;
        this.bufferSize = off + len;
        this.bufferPos = off;
    }

    public int pushLimit(int byteLimit) throws InvalidProtocolBufferMicroException {
        if (byteLimit < 0) {
            throw InvalidProtocolBufferMicroException.negativeSize();
        }
        int byteLimit2 = byteLimit + this.totalBytesRetired + this.bufferPos;
        int oldLimit = this.currentLimit;
        if (byteLimit2 > oldLimit) {
            throw InvalidProtocolBufferMicroException.truncatedMessage();
        }
        this.currentLimit = byteLimit2;
        recomputeBufferSizeAfterLimit();
        return oldLimit;
    }

    private void recomputeBufferSizeAfterLimit() {
        this.bufferSize += this.bufferSizeAfterLimit;
        int bufferEnd = this.totalBytesRetired + this.bufferSize;
        if (bufferEnd > this.currentLimit) {
            this.bufferSizeAfterLimit = bufferEnd - this.currentLimit;
            this.bufferSize -= this.bufferSizeAfterLimit;
        } else {
            this.bufferSizeAfterLimit = 0;
        }
    }

    public void popLimit(int oldLimit) {
        this.currentLimit = oldLimit;
        recomputeBufferSizeAfterLimit();
    }

    public boolean isAtEnd() throws IOException {
        return this.bufferPos == this.bufferSize && !refillBuffer(false);
    }

    private boolean refillBuffer(boolean mustSucceed) throws IOException {
        if (this.bufferPos < this.bufferSize) {
            throw new IllegalStateException("refillBuffer() called when buffer wasn't empty.");
        }
        if (this.totalBytesRetired + this.bufferSize == this.currentLimit) {
            if (mustSucceed) {
                throw InvalidProtocolBufferMicroException.truncatedMessage();
            }
            return false;
        }
        this.totalBytesRetired += this.bufferSize;
        this.bufferPos = 0;
        this.bufferSize = this.input == null ? -1 : this.input.read(this.buffer);
        if (this.bufferSize == 0 || this.bufferSize < -1) {
            throw new IllegalStateException("InputStream#read(byte[]) returned invalid result: " + this.bufferSize + "\nThe InputStream implementation is buggy.");
        }
        if (this.bufferSize == -1) {
            this.bufferSize = 0;
            if (mustSucceed) {
                throw InvalidProtocolBufferMicroException.truncatedMessage();
            }
            return false;
        }
        recomputeBufferSizeAfterLimit();
        int totalBytesRead = this.totalBytesRetired + this.bufferSize + this.bufferSizeAfterLimit;
        if (totalBytesRead > this.sizeLimit || totalBytesRead < 0) {
            throw InvalidProtocolBufferMicroException.sizeLimitExceeded();
        }
        return true;
    }

    public byte readRawByte() throws IOException {
        if (this.bufferPos == this.bufferSize) {
            refillBuffer(true);
        }
        byte[] bArr = this.buffer;
        int i = this.bufferPos;
        this.bufferPos = i + 1;
        return bArr[i];
    }

    public byte[] readRawBytes(int size) throws IOException {
        if (size < 0) {
            throw InvalidProtocolBufferMicroException.negativeSize();
        }
        if (this.totalBytesRetired + this.bufferPos + size > this.currentLimit) {
            skipRawBytes((this.currentLimit - this.totalBytesRetired) - this.bufferPos);
            throw InvalidProtocolBufferMicroException.truncatedMessage();
        }
        if (size <= this.bufferSize - this.bufferPos) {
            byte[] bytes = new byte[size];
            System.arraycopy(this.buffer, this.bufferPos, bytes, 0, size);
            this.bufferPos += size;
            return bytes;
        }
        if (size < 4096) {
            byte[] bytes2 = new byte[size];
            int pos = this.bufferSize - this.bufferPos;
            System.arraycopy(this.buffer, this.bufferPos, bytes2, 0, pos);
            this.bufferPos = this.bufferSize;
            refillBuffer(true);
            while (size - pos > this.bufferSize) {
                System.arraycopy(this.buffer, 0, bytes2, pos, this.bufferSize);
                pos += this.bufferSize;
                this.bufferPos = this.bufferSize;
                refillBuffer(true);
            }
            System.arraycopy(this.buffer, 0, bytes2, pos, size - pos);
            this.bufferPos = size - pos;
            return bytes2;
        }
        int originalBufferPos = this.bufferPos;
        int originalBufferSize = this.bufferSize;
        this.totalBytesRetired += this.bufferSize;
        this.bufferPos = 0;
        this.bufferSize = 0;
        int sizeLeft = size - (originalBufferSize - originalBufferPos);
        Vector chunks = new Vector();
        while (sizeLeft > 0) {
            byte[] chunk = new byte[Math.min(sizeLeft, 4096)];
            int pos2 = 0;
            while (pos2 < chunk.length) {
                int n = this.input == null ? -1 : this.input.read(chunk, pos2, chunk.length - pos2);
                if (n == -1) {
                    throw InvalidProtocolBufferMicroException.truncatedMessage();
                }
                this.totalBytesRetired += n;
                pos2 += n;
            }
            sizeLeft -= chunk.length;
            chunks.addElement(chunk);
        }
        byte[] bytes3 = new byte[size];
        int pos3 = originalBufferSize - originalBufferPos;
        System.arraycopy(this.buffer, originalBufferPos, bytes3, 0, pos3);
        for (int i = 0; i < chunks.size(); i++) {
            byte[] chunk2 = (byte[]) chunks.elementAt(i);
            System.arraycopy(chunk2, 0, bytes3, pos3, chunk2.length);
            pos3 += chunk2.length;
        }
        return bytes3;
    }

    public void skipRawBytes(int size) throws IOException {
        if (size < 0) {
            throw InvalidProtocolBufferMicroException.negativeSize();
        }
        if (this.totalBytesRetired + this.bufferPos + size > this.currentLimit) {
            skipRawBytes((this.currentLimit - this.totalBytesRetired) - this.bufferPos);
            throw InvalidProtocolBufferMicroException.truncatedMessage();
        }
        if (size <= this.bufferSize - this.bufferPos) {
            this.bufferPos += size;
            return;
        }
        int pos = this.bufferSize - this.bufferPos;
        this.totalBytesRetired += pos;
        this.bufferPos = 0;
        this.bufferSize = 0;
        while (pos < size) {
            int n = this.input == null ? -1 : (int) this.input.skip(size - pos);
            if (n <= 0) {
                throw InvalidProtocolBufferMicroException.truncatedMessage();
            }
            pos += n;
            this.totalBytesRetired += n;
        }
    }
}
