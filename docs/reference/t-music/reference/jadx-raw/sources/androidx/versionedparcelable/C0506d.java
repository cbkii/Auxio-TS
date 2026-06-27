package androidx.versionedparcelable;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RestrictTo;
import android.util.SparseIntArray;

/* compiled from: VersionedParcelParcel.java */
@RestrictTo({RestrictTo.Scope.LIBRARY})
/* renamed from: androidx.versionedparcelable.d */
/* loaded from: classes3.dex */
class C0506d extends AbstractC0505c {

    /* renamed from: Af */
    private final SparseIntArray f293Af;

    /* renamed from: Bf */
    private final String f294Bf;

    /* renamed from: Cf */
    private int f295Cf;

    /* renamed from: Df */
    private int f296Df;
    private final int mEnd;
    private final int mOffset;
    private final Parcel mParcel;

    C0506d(Parcel parcel) {
        this(parcel, parcel.dataPosition(), parcel.dataSize(), "");
    }

    /* renamed from: Ma */
    private int m103Ma(int i) {
        int readInt;
        do {
            int i2 = this.f296Df;
            if (i2 >= this.mEnd) {
                return -1;
            }
            this.mParcel.setDataPosition(i2);
            int readInt2 = this.mParcel.readInt();
            readInt = this.mParcel.readInt();
            this.f296Df += readInt2;
        } while (readInt != i);
        return this.mParcel.dataPosition();
    }

    @Override // androidx.versionedparcelable.AbstractC0505c
    /* renamed from: Ya */
    public void mo87Ya() {
        int i = this.f295Cf;
        if (i >= 0) {
            int i2 = this.f293Af.get(i);
            int dataPosition = this.mParcel.dataPosition();
            this.mParcel.setDataPosition(i2);
            this.mParcel.writeInt(dataPosition - i2);
            this.mParcel.setDataPosition(dataPosition);
        }
    }

    @Override // androidx.versionedparcelable.AbstractC0505c
    /* renamed from: Za */
    protected AbstractC0505c mo88Za() {
        Parcel parcel = this.mParcel;
        int dataPosition = parcel.dataPosition();
        int i = this.f296Df;
        if (i == this.mOffset) {
            i = this.mEnd;
        }
        return new C0506d(parcel, dataPosition, i, this.f294Bf + "  ");
    }

    @Override // androidx.versionedparcelable.AbstractC0505c
    /* renamed from: a */
    public void mo91a(Parcelable parcelable) {
        this.mParcel.writeParcelable(parcelable, 0);
    }

    @Override // androidx.versionedparcelable.AbstractC0505c
    /* renamed from: ab */
    public <T extends Parcelable> T mo94ab() {
        return (T) this.mParcel.readParcelable(C0506d.class.getClassLoader());
    }

    @Override // androidx.versionedparcelable.AbstractC0505c
    /* renamed from: ia */
    public boolean mo99ia(int i) {
        int m103Ma = m103Ma(i);
        if (m103Ma == -1) {
            return false;
        }
        this.mParcel.setDataPosition(m103Ma);
        return true;
    }

    @Override // androidx.versionedparcelable.AbstractC0505c
    /* renamed from: ja */
    public void mo101ja(int i) {
        mo87Ya();
        this.f295Cf = i;
        this.f293Af.put(i, this.mParcel.dataPosition());
        writeInt(0);
        writeInt(i);
    }

    @Override // androidx.versionedparcelable.AbstractC0505c
    public byte[] readByteArray() {
        int readInt = this.mParcel.readInt();
        if (readInt < 0) {
            return null;
        }
        byte[] bArr = new byte[readInt];
        this.mParcel.readByteArray(bArr);
        return bArr;
    }

    @Override // androidx.versionedparcelable.AbstractC0505c
    public int readInt() {
        return this.mParcel.readInt();
    }

    @Override // androidx.versionedparcelable.AbstractC0505c
    public String readString() {
        return this.mParcel.readString();
    }

    @Override // androidx.versionedparcelable.AbstractC0505c
    public void writeByteArray(byte[] bArr) {
        if (bArr == null) {
            this.mParcel.writeInt(-1);
        } else {
            this.mParcel.writeInt(bArr.length);
            this.mParcel.writeByteArray(bArr);
        }
    }

    @Override // androidx.versionedparcelable.AbstractC0505c
    public void writeInt(int i) {
        this.mParcel.writeInt(i);
    }

    @Override // androidx.versionedparcelable.AbstractC0505c
    public void writeString(String str) {
        this.mParcel.writeString(str);
    }

    C0506d(Parcel parcel, int i, int i2, String str) {
        this.f293Af = new SparseIntArray();
        this.f295Cf = -1;
        this.f296Df = 0;
        this.mParcel = parcel;
        this.mOffset = i;
        this.mEnd = i2;
        this.f296Df = this.mOffset;
        this.f294Bf = str;
    }
}
