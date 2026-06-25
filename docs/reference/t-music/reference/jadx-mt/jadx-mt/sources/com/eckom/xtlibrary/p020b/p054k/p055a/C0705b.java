package com.eckom.xtlibrary.p020b.p054k.p055a;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.k.a.b */
/* JADX INFO: compiled from: Record.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0705b {

    /* JADX INFO: renamed from: ik */
    public int f849ik;

    /* JADX INFO: renamed from: jk */
    public C0704a[] f850jk;

    /* JADX INFO: renamed from: kk */
    public int f851kk;

    /* JADX INFO: renamed from: lk */
    C0705b f852lk;
    public int mIndex;
    public int mLength;
    public String mName;

    /* JADX INFO: renamed from: mk */
    C0705b f853mk;

    /* JADX INFO: renamed from: nk */
    public C0705b f854nk;

    public C0705b(String str, int i, int i2) {
        this.mName = str;
        this.mIndex = i;
        this.f849ik = i2;
    }

    /* JADX INFO: renamed from: a */
    public void m1048a(C0704a c0704a) {
        int i = this.f851kk;
        if (i < this.mLength) {
            C0704a[] c0704aArr = this.f850jk;
            this.f851kk = i + 1;
            c0704aArr[i] = c0704a;
        }
    }

    public void setLength(int i) {
        if (this.mLength != i) {
            m1050wc();
            if (i > 0) {
                this.f850jk = new C0704a[i];
                this.mLength = i;
            }
        }
    }

    /* JADX INFO: renamed from: wc */
    public void m1050wc() {
        for (int i = 0; i < this.f851kk; i++) {
            this.f850jk[i] = null;
        }
        this.f851kk = 0;
        this.f850jk = null;
        this.mLength = 0;
        C0705b c0705b = this.f852lk;
        if (c0705b != null) {
            c0705b.m1050wc();
            this.f852lk = null;
        }
        C0705b c0705b2 = this.f853mk;
        if (c0705b2 != null) {
            c0705b2.m1050wc();
            this.f853mk = null;
        }
    }

    /* JADX INFO: renamed from: a */
    public void m1049a(String str, String str2, boolean z) {
        int i = this.f851kk;
        if (i < this.mLength) {
            C0704a[] c0704aArr = this.f850jk;
            this.f851kk = i + 1;
            c0704aArr[i] = new C0704a(str, str2, z);
        }
    }
}
