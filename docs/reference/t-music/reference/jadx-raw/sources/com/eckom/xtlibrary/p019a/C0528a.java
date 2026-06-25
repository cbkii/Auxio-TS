package com.eckom.xtlibrary.p019a;

import android.util.Log;

/* compiled from: BaseLog.java */
/* renamed from: com.eckom.xtlibrary.a.a */
/* loaded from: classes3.dex */
public class C0528a {
    /* renamed from: g */
    public static void m174g(int i, String str, String str2) {
        int length = str2.length();
        int i2 = length / 4000;
        if (i2 <= 0) {
            m175j(i, str, str2);
            return;
        }
        int i3 = 0;
        int i4 = 0;
        while (i3 < i2) {
            int i5 = i4 + 4000;
            m175j(i, str, str2.substring(i4, i5));
            i3++;
            i4 = i5;
        }
        m175j(i, str, str2.substring(i4, length));
    }

    /* renamed from: j */
    private static void m175j(int i, String str, String str2) {
        switch (i) {
            case 1:
                Log.v(str, str2);
                break;
            case 2:
                Log.d(str, str2);
                break;
            case 3:
                Log.i(str, str2);
                break;
            case 4:
                Log.w(str, str2);
                break;
            case 5:
                Log.e(str, str2);
                break;
            case 6:
                Log.wtf(str, str2);
                break;
        }
    }
}
