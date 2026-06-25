package com.eckom.xtlibrary.p020b.p046h.p050d;

import android.tw.john.TWUtil;

/* compiled from: TWRadio.java */
/* renamed from: com.eckom.xtlibrary.b.h.d.b */
/* loaded from: classes3.dex */
public class C0669b extends TWUtil {
    private static int mCount;

    /* compiled from: TWRadio.java */
    /* renamed from: com.eckom.xtlibrary.b.h.d.b$a */
    private static class a {

        /* renamed from: jd */
        private static final C0669b f790jd = new C0669b(1);
    }

    public static C0669b open() {
        int i = mCount;
        mCount = i + 1;
        if (i == 0) {
            if (a.f790jd.open(new short[]{265, 266, 274, 513, 515, 769, 1025, 1026, 1028, 1029, 1030, -25088, -25087}) != 0) {
                mCount--;
                return null;
            }
            a.f790jd.start();
        }
        return a.f790jd;
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

    private C0669b(int i) {
        super(i);
    }
}
