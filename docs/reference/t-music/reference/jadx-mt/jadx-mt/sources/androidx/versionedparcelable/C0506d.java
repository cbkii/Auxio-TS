package androidx.versionedparcelable;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RestrictTo;
import android.util.SparseIntArray;

/* JADX INFO: renamed from: androidx.versionedparcelable.d */
/* JADX INFO: compiled from: VersionedParcelParcel.java */
/* JADX INFO: loaded from: classes3.dex */
@RestrictTo({RestrictTo.Scope.LIBRARY})
class C0506d extends AbstractC0505c {

    /* JADX INFO: renamed from: Af */
    private final SparseIntArray f293Af;

    /* JADX INFO: renamed from: Bf */
    private final String f294Bf;

    /* JADX INFO: renamed from: Cf */
    private int f295Cf;

    /* JADX INFO: renamed from: Df */
    private int f296Df;
    private final int mEnd;
    private final int mOffset;
    private final Parcel mParcel;

    C0506d(Parcel parcel) {
        this(parcel, parcel.dataPosition(), parcel.dataSize(), "");
    }

    /* JADX INFO: renamed from: Ma */
    private int m103Ma(int i) {
        int i2;
        do {
            int i3 = this.f296Df;
            if (i3 >= this.mEnd) {
                return -1;
            }
            this.mParcel.setDataPosition(i3);
            int i4 = this.mParcel.readInt();
            i2 = this.mParcel.readInt();
            this.f296Df += i4;
        } while (i2 != i);
        return this.mParcel.dataPosition();
    }

    @Override // androidx.versionedparcelable.AbstractC0505c
    /* JADX INFO: renamed from: Ya */
    public void mo87Ya() {
        int i = this.f295Cf;
        if (i >= 0) {
            int i2 = this.f293Af.get(i);
            int iDataPosition = this.mParcel.dataPosition();
            this.mParcel.setDataPosition(i2);
            this.mParcel.writeInt(iDataPosition - i2);
            this.mParcel.setDataPosition(iDataPosition);
        }
    }

    @Override // androidx.versionedparcelable.AbstractC0505c
    /* JADX INFO: renamed from: Za */
    protected AbstractC0505c mo88Za() {
        Parcel parcel = this.mParcel;
        int iDataPosition = parcel.dataPosition();
        int i = this.f296Df;
        if (i == this.mOffset) {
            i = this.mEnd;
        }
        return new C0506d(parcel, iDataPosition, i, this.f294Bf + "  ");
    }

    @Override // androidx.versionedparcelable.AbstractC0505c
    /* JADX INFO: renamed from: a */
    public void mo91a(Parcelable parcelable) {
        this.mParcel.writeParcelable(parcelable, 0);
    }

    @Override // androidx.versionedparcelable.AbstractC0505c
    /* JADX INFO: renamed from: ab */
    public <T extends Parcelable> T mo94ab() {
        return (T) this.mParcel.readParcelable(C0506d.class.getClassLoader());
    }

    @Override // androidx.versionedparcelable.AbstractC0505c
    /* JADX INFO: renamed from: ia */
    public boolean mo99ia(int i) {
        int iM103Ma = m103Ma(i);
        if (iM103Ma == -1) {
            return false;
        }
        this.mParcel.setDataPosition(iM103Ma);
        return true;
    }

    @Override // androidx.versionedparcelable.AbstractC0505c
    /* JADX INFO: renamed from: ja */
    public void mo101ja(int i) {
        mo87Ya();
        this.f295Cf = i;
        this.f293Af.put(i, this.mParcel.dataPosition());
        writeInt(0);
        writeInt(i);
    }

    @Override // androidx.versionedparcelable.AbstractC0505c
    public byte[] readByteArray() {
        int i = this.mParcel.readInt();
        if (i < 0) {
            return null;
        }
        byte[] bArr = new byte[i];
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
