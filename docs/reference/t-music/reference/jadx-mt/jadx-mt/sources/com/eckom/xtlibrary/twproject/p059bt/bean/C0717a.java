package com.eckom.xtlibrary.twproject.p059bt.bean;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.bt.bean.a */
/* JADX INFO: compiled from: TWContact.java */
/* JADX INFO: loaded from: classes3.dex */
class C0717a implements Parcelable.Creator<TWContact> {
    C0717a() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public TWContact createFromParcel(Parcel parcel) {
        return new TWContact(parcel);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public TWContact[] newArray(int i) {
        return new TWContact[i];
    }
}
