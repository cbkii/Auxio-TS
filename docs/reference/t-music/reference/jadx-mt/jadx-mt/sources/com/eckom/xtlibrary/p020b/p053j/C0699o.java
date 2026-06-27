package com.eckom.xtlibrary.p020b.p053j;

import android.content.Context;
import android.content.SharedPreferences;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.j.o */
/* JADX INFO: compiled from: SPUtils.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0699o {

    /* JADX INFO: renamed from: pm */
    public static String f844pm = "PATHINDEX";

    /* JADX INFO: renamed from: a */
    public static void m1027a(Context context, String str, String str2, String str3) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences(str, 0).edit();
        editorEdit.putString(str2, str3);
        editorEdit.commit();
    }

    /* JADX INFO: renamed from: b */
    public static boolean m1031b(Context context, String str, String str2) {
        return context.getSharedPreferences(str, 0).getBoolean(str2, true);
    }

    /* JADX INFO: renamed from: c */
    public static String m1032c(Context context, String str, String str2) {
        return context.getSharedPreferences(str, 0).getString(str2, null);
    }

    /* JADX INFO: renamed from: b */
    public static int m1029b(Context context, String str, String str2, int i) {
        return context.getSharedPreferences(str, 0).getInt(str2, i);
    }

    /* JADX INFO: renamed from: a */
    public static void m1028a(Context context, String str, String str2, boolean z) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences(str, 0).edit();
        editorEdit.putBoolean(str2, z);
        editorEdit.commit();
    }

    /* JADX INFO: renamed from: b */
    public static void m1030b(Context context, String str, String str2, long j) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences(str, 0).edit();
        editorEdit.putLong(str2, j);
        editorEdit.commit();
    }

    /* JADX INFO: renamed from: a */
    public static void m1026a(Context context, String str, String str2, int i) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences(str, 0).edit();
        editorEdit.putInt(str2, i);
        editorEdit.commit();
    }

    /* JADX INFO: renamed from: a */
    public static Long m1025a(Context context, String str, String str2, long j) {
        return Long.valueOf(context.getSharedPreferences(str, 0).getLong(str2, j));
    }
}
