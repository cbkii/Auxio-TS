package com.eckom.xtlibrary.twproject.p072bt.bean;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes3.dex */
public class TWContact implements Parcelable {
    public static final Parcelable.Creator<TWContact> CREATOR = new C0717a();

    /* renamed from: Yf */
    protected String f883Yf;

    /* renamed from: Zf */
    protected String f884Zf;

    /* renamed from: _f */
    protected String f885_f;

    /* renamed from: cg */
    protected boolean f886cg;

    /* renamed from: id */
    protected int f887id;

    public TWContact() {
    }

    /* renamed from: A */
    public void m1130A(boolean z) {
        this.f886cg = z;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* renamed from: eb */
    public String m1131eb() {
        return this.f884Zf;
    }

    public boolean equals(Object obj) {
        try {
            if (obj instanceof TWContact) {
                return this.f884Zf.equals(((TWContact) obj).f884Zf);
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    public String getContactName() {
        return this.f883Yf;
    }

    public void setId(int i) {
        this.f887id = i;
    }

    public String toString() {
        return "TWContact{id=" + this.f887id + ", contactName='" + this.f883Yf + "', contactNumber='" + this.f884Zf + "', contactPin='" + this.f885_f + "', favorite=" + this.f886cg + '}';
    }

    /* renamed from: wa */
    public void m1132wa(String str) {
        this.f883Yf = str;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.f887id);
        parcel.writeString(this.f883Yf);
        parcel.writeString(this.f884Zf);
        parcel.writeString(this.f885_f);
        parcel.writeByte(this.f886cg ? (byte) 1 : (byte) 0);
    }

    /* renamed from: xa */
    public void m1133xa(String str) {
        this.f884Zf = str;
    }

    /* renamed from: ya */
    public void m1134ya(String str) {
        this.f885_f = str;
    }

    public TWContact(String str, String str2, String str3) {
        this.f883Yf = str;
        this.f884Zf = str2;
        this.f885_f = str3;
    }

    protected TWContact(Parcel parcel) {
        this.f887id = parcel.readInt();
        this.f883Yf = parcel.readString();
        this.f884Zf = parcel.readString();
        this.f885_f = parcel.readString();
        this.f886cg = parcel.readByte() != 0;
    }
}
