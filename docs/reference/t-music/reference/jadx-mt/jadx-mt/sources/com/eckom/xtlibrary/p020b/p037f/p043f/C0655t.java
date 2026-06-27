package com.eckom.xtlibrary.p020b.p037f.p043f;

import android.support.v4.internal.view.SupportMenu;
import android.support.v4.media.subtitle.Cea708CCParser;
import android.tw.john.TWUtil;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.f.t */
/* JADX INFO: compiled from: TWMusicIikID3.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0655t extends TWUtil {

    /* JADX INFO: renamed from: jd */
    private static C0655t f732jd = new C0655t();
    private static int mCount = 0;
    private int mService = 0;

    public static C0655t open() {
        int i = mCount;
        mCount = i + 1;
        if (i == 0) {
            if (f732jd.open(new short[]{513, 514, 515, 524, 769, 770, 772, 1296, -25085, -25057, -24804}) != 0) {
                mCount--;
                return null;
            }
            f732jd.start();
        }
        return f732jd;
    }

    /* JADX INFO: renamed from: b */
    public void m791b(int i, int i2, int i3, int i4, int i5) {
        write(1282, (i2 & SupportMenu.USER_MASK) | (i3 << 16), (i << 31) | ((i5 & 127) << 24) | (16777215 & i4));
    }

    /* JADX INFO: renamed from: ca */
    public void m792ca(int i) {
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

    /* JADX INFO: renamed from: da */
    public void m793da(int i) {
        write(40465, 192, i);
    }

    public int getService() {
        return this.mService;
    }

    /* JADX INFO: renamed from: w */
    public void m794w(boolean z) {
        m793da(z ? 3 : Cea708CCParser.Const.CODE_C1_CW3);
    }
}
