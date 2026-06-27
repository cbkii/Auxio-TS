package com.p060tw.music.utils;

import android.content.Context;
import android.content.SharedPreferences;

/* JADX INFO: renamed from: com.tw.music.utils.c */
/* JADX INFO: compiled from: SharedPreferencesUtils.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0794c {
    /* JADX INFO: renamed from: a */
    public static void m1515a(Context context, String str, String str2, boolean z) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences(str, 0).edit();
        editorEdit.putBoolean(str2, z);
        editorEdit.commit();
    }

    /* JADX INFO: renamed from: b */
    public static boolean m1516b(Context context, String str, String str2) {
        return context.getSharedPreferences(str, 0).getBoolean(str2, false);
    }

    /* JADX INFO: renamed from: a */
    public static void m1514a(Context context, String str, String str2, int i) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences(str, 0).edit();
        editorEdit.putInt(str2, i);
        editorEdit.commit();
    }
}
