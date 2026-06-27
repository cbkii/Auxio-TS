package com.eckom.xtlibrary.p020b.p052i;

import android.util.Log;
import java.util.Locale;

/* compiled from: DFLog.java */
/* renamed from: com.eckom.xtlibrary.b.i.a */
/* loaded from: classes3.dex */
public class C0671a {
    private static final String CLASS_NAME = "com.eckom.xtlibrary.b.i.a";
    public static boolean DEBUG = true;

    /* renamed from: a */
    public static void m923a(String str, String str2, Object... objArr) {
        if (DEBUG) {
            Log.e(str, m922a(str2, objArr));
        }
    }

    /* renamed from: a */
    private static String m922a(String str, Object... objArr) {
        String str2;
        if (objArr != null) {
            str = String.format(Locale.US, str, objArr);
        }
        StackTraceElement[] stackTrace = new Throwable().fillInStackTrace().getStackTrace();
        int i = 2;
        while (true) {
            if (i >= stackTrace.length) {
                str2 = "<unknown>";
                break;
            }
            StackTraceElement stackTraceElement = stackTrace[i];
            if (!stackTraceElement.getClassName().equals(CLASS_NAME)) {
                str2 = "(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ")";
                break;
            }
            i++;
        }
        return String.format(Locale.US, "thread[%s, %d] %s: %s", Thread.currentThread().getName(), Long.valueOf(Thread.currentThread().getId()), str2, str);
    }
}
