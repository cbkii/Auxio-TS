package androidx.versionedparcelable;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: renamed from: androidx.versionedparcelable.b */
/* JADX INFO: compiled from: ParcelImpl.java */
/* JADX INFO: loaded from: classes3.dex */
class C0504b implements Parcelable.Creator<ParcelImpl> {
    C0504b() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public ParcelImpl createFromParcel(Parcel parcel) {
        return new ParcelImpl(parcel);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public ParcelImpl[] newArray(int i) {
        return new ParcelImpl[i];
    }
}
