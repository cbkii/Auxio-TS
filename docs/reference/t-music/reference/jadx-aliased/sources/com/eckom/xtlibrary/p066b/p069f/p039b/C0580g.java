package com.eckom.xtlibrary.p066b.p069f.p039b;

/* compiled from: Record.java */
/* renamed from: com.eckom.xtlibrary.b.f.b.g */
/* loaded from: classes3.dex */
public class C0580g {

    /* renamed from: ik */
    public int f543ik;

    /* renamed from: jk */
    public C0579f[] f544jk;

    /* renamed from: kk */
    public int f545kk;

    /* renamed from: lk */
    public C0580g f546lk;
    public int mIndex;
    public String mKey;
    public int mLength;
    public String mName;

    /* renamed from: mk */
    public C0580g f547mk;

    /* renamed from: nk */
    public C0580g f548nk;

    /* renamed from: qk */
    public int f549qk;

    public C0580g() {
        this.mIndex = 0;
        this.mKey = "";
        this.f549qk = 0;
    }

    /* renamed from: d */
    public static C0580g m448d(C0580g c0580g) {
        C0580g c0580g2 = new C0580g();
        c0580g2.mName = c0580g.mName;
        c0580g2.mIndex = c0580g.mIndex;
        c0580g2.f549qk = c0580g.f549qk;
        c0580g2.mKey = c0580g.mKey;
        c0580g2.f543ik = c0580g.f543ik;
        c0580g2.m450c(c0580g);
        c0580g2.f548nk = c0580g.f548nk;
        return c0580g2;
    }

    /* renamed from: a */
    public void m449a(C0579f c0579f) {
        int i = this.f545kk;
        if (i < this.mLength) {
            C0579f[] c0579fArr = this.f544jk;
            this.f545kk = i + 1;
            c0579fArr[i] = c0579f;
        }
    }

    /* renamed from: c */
    public void m450c(C0580g c0580g) {
        setLength(c0580g.mLength);
        this.f545kk = 0;
        C0579f[] c0579fArr = c0580g.f544jk;
        if (c0579fArr == null || c0580g.mLength <= 0) {
            return;
        }
        for (C0579f c0579f : c0579fArr) {
            if (c0579f != null) {
                m449a(new C0579f(c0579f));
            }
        }
    }

    /* renamed from: e */
    public void m451e(C0580g c0580g) {
        if (this.f546lk != c0580g) {
            C0580g c0580g2 = this.f547mk;
            if (c0580g2 != null && c0580g2 != c0580g) {
                c0580g2.m453wc();
                this.f547mk = null;
            }
            this.f547mk = this.f546lk;
            this.f546lk = c0580g;
        }
    }

    /* renamed from: oa */
    public C0580g m452oa(int i) {
        C0580g c0580g = this.f546lk;
        if (c0580g != null && c0580g.mIndex == i) {
            return c0580g;
        }
        C0580g c0580g2 = this.f547mk;
        if (c0580g2 == null || c0580g2.mIndex != i) {
            return null;
        }
        return c0580g2;
    }

    public void setLength(int i) {
        if (this.mLength != i) {
            m453wc();
            if (i > 0) {
                this.f544jk = new C0579f[i];
                this.mLength = i;
            }
        }
    }

    /* renamed from: wc */
    public void m453wc() {
        for (int i = 0; i < this.f545kk; i++) {
            this.f544jk[i] = null;
        }
        this.f545kk = 0;
        this.f544jk = null;
        this.mLength = 0;
        C0580g c0580g = this.f546lk;
        if (c0580g != null) {
            c0580g.m453wc();
            this.f546lk = null;
        }
        C0580g c0580g2 = this.f547mk;
        if (c0580g2 != null) {
            c0580g2.m453wc();
            this.f547mk = null;
        }
    }

    public C0580g(String str, int i, int i2) {
        this.mIndex = 0;
        this.mKey = "";
        this.f549qk = 0;
        this.mName = str;
        this.mIndex = i;
        this.f543ik = i2;
    }

    public C0580g(String str, int i, int i2, int i3) {
        this.mIndex = 0;
        this.mKey = "";
        this.f549qk = 0;
        this.mName = str;
        this.mIndex = i;
        this.f549qk = i2;
        this.f543ik = i3;
    }

    public C0580g(String str, int i, int i2, C0580g c0580g) {
        this.mIndex = 0;
        this.mKey = "";
        this.f549qk = 0;
        this.mName = str;
        this.mIndex = i;
        this.f543ik = i2;
        this.f548nk = c0580g;
    }

    public C0580g(String str, int i, int i2, int i3, C0580g c0580g) {
        this.mIndex = 0;
        this.mKey = "";
        this.f549qk = 0;
        this.mName = str;
        this.mIndex = i;
        this.f549qk = i2;
        this.f543ik = i3;
        this.f548nk = c0580g;
    }
}
