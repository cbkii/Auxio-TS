package com.p060tw.music.utils;

import android.util.Log;

/* JADX INFO: renamed from: com.tw.music.utils.a */
/* JADX INFO: compiled from: LogUtil.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0792a {

    /* JADX INFO: renamed from: An */
    public static boolean f1176An = true;
    private static String TAG = "LogUtils";

    /* JADX INFO: renamed from: d */
    public static void m1512d(String str) {
        if (f1176An) {
            Log.d(TAG, str);
        }
    }

    /* JADX INFO: renamed from: e */
    public static void m1513e(String str) {
        if (f1176An) {
            Log.e(TAG, str);
        }
    }
}
