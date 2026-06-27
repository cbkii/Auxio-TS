package com.p073tw.music.utils;

import android.util.Log;

/* compiled from: LogUtil.java */
/* renamed from: com.tw.music.utils.a */
/* loaded from: classes3.dex */
public class C0792a {

    /* renamed from: An */
    public static boolean f1176An = true;
    private static String TAG = "LogUtils";

    /* renamed from: d */
    public static void m1512d(String str) {
        if (f1176An) {
            Log.d(TAG, str);
        }
    }

    /* renamed from: e */
    public static void m1513e(String str) {
        if (f1176An) {
            Log.e(TAG, str);
        }
    }
}
