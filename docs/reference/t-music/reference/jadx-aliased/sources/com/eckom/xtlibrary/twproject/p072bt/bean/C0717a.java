package com.eckom.xtlibrary.twproject.p072bt.bean;

import android.os.Parcel;
import android.os.Parcelable;

/* compiled from: TWContact.java */
/* renamed from: com.eckom.xtlibrary.twproject.bt.bean.a */
/* loaded from: classes3.dex */
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
