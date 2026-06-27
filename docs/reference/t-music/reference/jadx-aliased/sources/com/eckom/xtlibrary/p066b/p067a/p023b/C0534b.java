package com.eckom.xtlibrary.p066b.p067a.p023b;

import android.util.Log;
import java.util.Arrays;
import java.util.List;

/* compiled from: LogUtil.java */
/* renamed from: com.eckom.xtlibrary.b.a.b.b */
/* loaded from: classes3.dex */
public class C0534b {
    private static boolean debug = true;

    /* renamed from: lh */
    private static List<String> f411lh = Arrays.asList("BasePresenter", "SettingFragment", "ServicePresenter");

    /* renamed from: d */
    public static void m201d(String str, String str2) {
        if (m203xb(str)) {
            Log.d(str, str2);
        }
    }

    /* renamed from: e */
    public static void m202e(String str, String str2) {
        if (m203xb(str)) {
            Log.e(str, str2);
        }
    }

    /* renamed from: xb */
    private static boolean m203xb(String str) {
        if (debug) {
            return f411lh.isEmpty() || f411lh.contains(str);
        }
        return false;
    }
}
