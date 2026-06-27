package com.p060tw.music.utils;

import android.content.Context;
import android.content.SharedPreferences;

/* compiled from: SharedPreferencesUtils.java */
/* renamed from: com.tw.music.utils.c */
/* loaded from: classes3.dex */
public class C0794c {
    /* renamed from: a */
    public static void m1515a(Context context, String str, String str2, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(str, 0).edit();
        edit.putBoolean(str2, z);
        edit.commit();
    }

    /* renamed from: b */
    public static boolean m1516b(Context context, String str, String str2) {
        return context.getSharedPreferences(str, 0).getBoolean(str2, false);
    }

    /* renamed from: a */
    public static void m1514a(Context context, String str, String str2, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(str, 0).edit();
        edit.putInt(str2, i);
        edit.commit();
    }
}
