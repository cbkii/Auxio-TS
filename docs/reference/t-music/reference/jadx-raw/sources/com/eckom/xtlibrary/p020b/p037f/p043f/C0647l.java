package com.eckom.xtlibrary.p020b.p037f.p043f;

import android.content.Context;
import android.content.SharedPreferences;

/* compiled from: SPUtils.java */
/* renamed from: com.eckom.xtlibrary.b.f.f.l */
/* loaded from: classes3.dex */
public class C0647l {

    /* renamed from: Fk */
    public static String f700Fk = "CLISTINDEX";

    /* renamed from: a */
    public static void m772a(Context context, String str, String str2, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(str, 0).edit();
        edit.putInt(str2, i);
        edit.commit();
    }

    /* renamed from: a */
    public static int m771a(Context context, String str, String str2) {
        return context.getSharedPreferences(str, 0).getInt(str2, 0);
    }
}
