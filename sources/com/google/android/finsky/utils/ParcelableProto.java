package com.google.android.finsky.utils;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.protobuf.micro.MessageMicro;

/* JADX INFO: loaded from: classes.dex */
public class ParcelableProto<T extends MessageMicro> implements Parcelable {
    public static Parcelable.Creator<ParcelableProto> CREATOR = new Parcelable.Creator<ParcelableProto>() { // from class: com.google.android.finsky.utils.ParcelableProto.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ParcelableProto createFromParcel(Parcel source) {
            int size = source.readInt();
            byte[] payload = new byte[size];
            source.readByteArray(payload);
            String className = source.readString();
            try {
                MessageMicro proto = (MessageMicro) Class.forName(className).getConstructor((Class[]) null).newInstance((Object[]) null);
                proto.mergeFrom(payload);
                return new ParcelableProto(proto);
            } catch (Exception e) {
                FinskyLog.e("Could not properly unmarshal %s", className);
                throw new IllegalArgumentException("Exception when unmarshalling: " + className, e);
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ParcelableProto[] newArray(int size) {
            return new ParcelableProto[size];
        }
    };
    private final T mPayload;
    private byte[] mSerialized = null;

    public ParcelableProto(T payload) {
        this.mPayload = payload;
    }

    public static <T extends MessageMicro> ParcelableProto<T> forProto(T payload) {
        return new ParcelableProto<>(payload);
    }

    public static <T extends MessageMicro> T getProtoFromParcel(Parcel parcel, ClassLoader classLoader) {
        return (T) ((ParcelableProto) parcel.readParcelable(classLoader)).getPayload();
    }

    public T getPayload() {
        return this.mPayload;
    }

    private void serializePayload() {
        this.mSerialized = this.mPayload.toByteArray();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        if (this.mSerialized == null) {
            serializePayload();
        }
        dest.writeInt(this.mSerialized.length);
        dest.writeByteArray(this.mSerialized);
        dest.writeString(this.mPayload.getClass().getName());
    }
}
