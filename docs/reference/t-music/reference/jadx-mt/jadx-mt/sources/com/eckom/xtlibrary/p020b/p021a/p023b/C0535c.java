package com.eckom.xtlibrary.p020b.p021a.p023b;

import android.tw.john.TWUtil;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.a.b.c */
/* JADX INFO: compiled from: TWAT.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0535c extends TWUtil {

    /* JADX INFO: renamed from: jd */
    private static C0535c f412jd = new C0535c(8);
    private static int mCount = 0;
    private int mService;
    public int mTime;

    public C0535c(int i) {
        super(i);
        this.mService = 0;
        this.mTime = 0;
    }

    /* JADX INFO: renamed from: da */
    private void m204da(int i) {
        write(40465, 192, i);
    }

    public static C0535c open() {
        int i = mCount;
        mCount = i + 1;
        if (i == 0) {
            if (f412jd.open(new short[]{267, 274, 513, 515, 769, 1296, -25080, -25059}, 115200) != 0) {
                mCount--;
                return null;
            }
            f412jd.start();
        }
        return f412jd;
    }

    /* JADX INFO: renamed from: ca */
    public void m205ca(int i) {
        this.mService = i;
        write(40448, i);
    }

    public void close() {
        int i = mCount;
        if (i > 0) {
            int i2 = i - 1;
            mCount = i2;
            if (i2 == 0) {
                stop();
                super.close();
            }
        }
    }

    /* JADX INFO: renamed from: w */
    public void m206w(boolean z) {
        m204da(z ? 8 : 136);
    }

    public int write(int i, String str) {
        return write(i, 0, 0, str, (Object) null);
    }

    public int write(int i, int i2, String str) {
        return write(i, i2, 0, str, (Object) null);
    }
}
