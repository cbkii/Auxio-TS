package com.eckom.xtlibrary.p019a;

import android.text.TextUtils;

/* compiled from: XTLog.java */
/* renamed from: com.eckom.xtlibrary.a.b */
/* loaded from: classes3.dex */
public class C0529b {

    /* renamed from: Mf */
    private static String f354Mf;
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /* renamed from: Nf */
    private static boolean f355Nf = true;

    /* renamed from: Of */
    private static boolean f356Of = true;

    /* renamed from: a */
    public static void m178a(Object obj) {
        m177a(6, null, obj);
    }

    /* renamed from: b */
    private static String[] m179b(int i, String str, Object... objArr) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[i];
        String className = stackTraceElement.getClassName();
        String[] split = className.split("\\.");
        if (split.length > 0) {
            className = split[split.length - 1] + ".java";
        }
        if (className.contains("$")) {
            className = className.split("\\$")[0] + ".java";
        }
        String methodName = stackTraceElement.getMethodName();
        int lineNumber = stackTraceElement.getLineNumber();
        if (lineNumber < 0) {
            lineNumber = 0;
        }
        if (str == null) {
            str = className;
        }
        if (f355Nf && TextUtils.isEmpty(str)) {
            str = "XTLog";
        } else if (!f355Nf) {
            str = f354Mf;
        }
        return new String[]{str, objArr == null ? "Log with null object" : m176a(objArr), "[ (" + className + ":" + lineNumber + ")#" + methodName + " ] "};
    }

    /* renamed from: d */
    public static void m180d(Object obj) {
        m177a(2, null, obj);
    }

    /* renamed from: e */
    public static void m181e(Object obj) {
        m177a(3, null, obj);
    }

    /* renamed from: a */
    private static void m177a(int i, String str, Object... objArr) {
        if (f356Of) {
            String[] m179b = m179b(5, str, objArr);
            String str2 = m179b[0];
            String str3 = m179b[1];
            String str4 = m179b[2];
            switch (i) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    C0528a.m174g(i, str2, str4 + str3);
                    break;
            }
        }
    }

    /* renamed from: a */
    private static String m176a(Object... objArr) {
        Object obj;
        if (objArr.length <= 1) {
            return (objArr.length != 1 || (obj = objArr[0]) == null) ? "null" : obj.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (int i = 0; i < objArr.length; i++) {
            Object obj2 = objArr[i];
            if (obj2 == null) {
                sb.append("Param");
                sb.append("[");
                sb.append(i);
                sb.append("]");
                sb.append(" = ");
                sb.append("null");
                sb.append("\n");
            } else {
                sb.append("Param");
                sb.append("[");
                sb.append(i);
                sb.append("]");
                sb.append(" = ");
                sb.append(obj2.toString());
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
