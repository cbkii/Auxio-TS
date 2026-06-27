package com.eckom.xtlibrary.p020b.p053j;

import android.content.Context;
import android.content.SharedPreferences;

/* compiled from: SPUtils.java */
/* renamed from: com.eckom.xtlibrary.b.j.o */
/* loaded from: classes3.dex */
public class C0699o {

    /* renamed from: pm */
    public static String f844pm = "PATHINDEX";

    /* renamed from: a */
    public static void m1027a(Context context, String str, String str2, String str3) {
        SharedPreferences.Editor edit = context.getSharedPreferences(str, 0).edit();
        edit.putString(str2, str3);
        edit.commit();
    }

    /* renamed from: b */
    public static boolean m1031b(Context context, String str, String str2) {
        return context.getSharedPreferences(str, 0).getBoolean(str2, true);
    }

    /* renamed from: c */
    public static String m1032c(Context context, String str, String str2) {
        return context.getSharedPreferences(str, 0).getString(str2, null);
    }

    /* renamed from: b */
    public static int m1029b(Context context, String str, String str2, int i) {
        return context.getSharedPreferences(str, 0).getInt(str2, i);
    }

    /* renamed from: a */
    public static void m1028a(Context context, String str, String str2, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(str, 0).edit();
        edit.putBoolean(str2, z);
        edit.commit();
    }

    /* renamed from: b */
    public static void m1030b(Context context, String str, String str2, long j) {
        SharedPreferences.Editor edit = context.getSharedPreferences(str, 0).edit();
        edit.putLong(str2, j);
        edit.commit();
    }

    /* renamed from: a */
    public static void m1026a(Context context, String str, String str2, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(str, 0).edit();
        edit.putInt(str2, i);
        edit.commit();
    }

    /* renamed from: a */
    public static Long m1025a(Context context, String str, String str2, long j) {
        return Long.valueOf(context.getSharedPreferences(str, 0).getLong(str2, j));
    }
}
