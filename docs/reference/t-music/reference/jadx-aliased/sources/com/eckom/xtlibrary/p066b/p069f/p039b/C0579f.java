package com.eckom.xtlibrary.p066b.p069f.p039b;

import android.graphics.Bitmap;

/* compiled from: MusicName.java */
/* renamed from: com.eckom.xtlibrary.b.f.b.f */
/* loaded from: classes3.dex */
public class C0579f {

    /* renamed from: ek */
    public boolean f539ek;

    /* renamed from: fk */
    public String f540fk;

    /* renamed from: gk */
    public String f541gk;

    /* renamed from: hk */
    public Bitmap f542hk;
    public int mLength;
    public String mName;
    public String mPath;

    public C0579f(String str, String str2, boolean z) {
        this.f539ek = false;
        this.f540fk = "";
        this.f541gk = "";
        this.f542hk = null;
        this.mLength = 0;
        this.mName = str;
        this.mPath = str2;
        this.f539ek = z;
    }

    public C0579f(String str, int i) {
        this.f539ek = false;
        this.f540fk = "";
        this.f541gk = "";
        this.f542hk = null;
        this.mLength = 0;
        this.mName = str;
        this.mLength = i;
    }

    public C0579f(String str, String str2) {
        this.f539ek = false;
        this.f540fk = "";
        this.f541gk = "";
        this.f542hk = null;
        this.mLength = 0;
        this.mName = str;
        this.mPath = str2;
    }

    public C0579f(String str, String str2, String str3, String str4, Bitmap bitmap) {
        this.f539ek = false;
        this.f540fk = "";
        this.f541gk = "";
        this.f542hk = null;
        this.mLength = 0;
        this.mName = str;
        this.mPath = str2;
        this.f540fk = str3;
        this.f541gk = str4;
        this.f542hk = bitmap;
    }

    public C0579f(C0579f c0579f) {
        this.f539ek = false;
        this.f540fk = "";
        this.f541gk = "";
        this.f542hk = null;
        this.mLength = 0;
        this.mName = c0579f.mName;
        this.mPath = c0579f.mPath;
        this.f540fk = c0579f.f540fk;
        this.f541gk = c0579f.f541gk;
        this.f539ek = c0579f.f539ek;
        this.mLength = c0579f.mLength;
        this.f542hk = c0579f.f542hk;
    }
}
