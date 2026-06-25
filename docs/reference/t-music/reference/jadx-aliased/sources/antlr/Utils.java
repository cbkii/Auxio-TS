package antlr;

import p054a.p055a.p056a.p003a.C0000a;

/* loaded from: classes3.dex */
public class Utils {
    public static boolean useDirectClassLoading = false;
    public static boolean useSystemExit = true;

    static {
        if ("true".equalsIgnoreCase(System.getProperty("ANTLR_DO_NOT_EXIT", "false"))) {
            useSystemExit = false;
        }
        if ("true".equalsIgnoreCase(System.getProperty("ANTLR_USE_DIRECT_CLASS_LOADING", "false"))) {
            useDirectClassLoading = true;
        }
    }

    public static Object createInstanceOf(String str) {
        return loadClass(str).newInstance();
    }

    public static void error(String str) {
        if (useSystemExit) {
            System.exit(1);
        }
        throw new RuntimeException(C0000a.m1a("ANTLR Panic: ", str));
    }

    public static void error(String str, Throwable th) {
        if (useSystemExit) {
            System.exit(1);
        }
        throw new RuntimeException("ANTLR Panic", th);
    }

    public static Class loadClass(String str) {
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            return (useDirectClassLoading || contextClassLoader == null) ? Class.forName(str) : contextClassLoader.loadClass(str);
        } catch (Exception unused) {
            return Class.forName(str);
        }
    }
}
