package com.eckom.xtlibrary.p020b.p037f.p043f;

import android.support.v4.view.MotionEventCompat;

/* compiled from: PinyinConv.java */
/* renamed from: com.eckom.xtlibrary.b.f.f.k */
/* loaded from: classes3.dex */
public class C0646k {

    /* renamed from: Dk */
    private static final char[] f698Dk = {21834, 33453, 25830, 25645, 34558, 21457, 22134, 21704, 21704, 20987, 21888, 22403, 22920, 25343, 21734, 21866, 26399, 28982, 25746, 22604, 22604, 22604, 25366, 26132, 21387, 21277};
    private static final int[] table = new int[27];

    /* renamed from: Ek */
    private static final char[] f699Ek = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 't', 't', 'w', 'x', 'y', 'z'};

    static {
        for (int i = 0; i < 26; i++) {
            table[i] = m770c(f698Dk[i]);
        }
        table[26] = 63486;
    }

    /* renamed from: b */
    private static char m769b(char c2) {
        int m770c;
        if (c2 >= 'a' && c2 <= 'z') {
            return (char) ((c2 - 'a') + 65);
        }
        if ((c2 >= 'A' && c2 <= 'Z') || (m770c = m770c(c2)) < 45217 || m770c > 63486) {
            return c2;
        }
        int i = 0;
        while (i < 26) {
            int[] iArr = table;
            if (m770c >= iArr[i] && m770c < iArr[i + 1]) {
                break;
            }
            i++;
        }
        if (m770c == 63486) {
            i = 25;
        }
        char c3 = f699Ek[i];
        if (i == 25) {
            if (m770c == 59075) {
                c3 = 't';
            }
        } else if (m770c == 56806) {
            c3 = 'x';
        }
        return (char) ((c3 - 'a') + 65);
    }

    /* renamed from: c */
    private static int m770c(char c2) {
        try {
            byte[] bytes = (new String() + c2).getBytes("GB2312");
            if (bytes.length < 2) {
                return 0;
            }
            return ((bytes[0] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) + (bytes[1] & 255);
        } catch (Exception unused) {
            return 0;
        }
    }

    public static String cn2py(String str) {
        try {
            int length = str.length();
            String str2 = "";
            for (int i = 0; i < length; i++) {
                str2 = str2 + m769b(str.charAt(i));
            }
            return str2;
        } catch (Exception unused) {
            return "";
        }
    }
}
