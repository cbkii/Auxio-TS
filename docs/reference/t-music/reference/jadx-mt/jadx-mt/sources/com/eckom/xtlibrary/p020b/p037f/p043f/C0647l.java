package com.eckom.xtlibrary.p020b.p037f.p043f;

import android.content.Context;
import android.content.SharedPreferences;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.f.l */
/* JADX INFO: compiled from: SPUtils.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0647l {

    /* JADX INFO: renamed from: Fk */
    public static String f700Fk = "CLISTINDEX";

    /* JADX INFO: renamed from: a */
    public static void m772a(Context context, String str, String str2, int i) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences(str, 0).edit();
        editorEdit.putInt(str2, i);
        editorEdit.commit();
    }

    /* JADX INFO: renamed from: a */
    public static int m771a(Context context, String str, String str2) {
        return context.getSharedPreferences(str, 0).getInt(str2, 0);
    }
}
