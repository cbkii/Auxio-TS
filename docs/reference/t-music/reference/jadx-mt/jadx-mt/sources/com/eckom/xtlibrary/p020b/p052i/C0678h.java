package com.eckom.xtlibrary.p020b.p052i;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import java.io.File;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.i.h */
/* JADX INFO: compiled from: ThemeHelper.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0678h {
    /* JADX INFO: renamed from: a */
    public static C0683m m961a(String str, C0683m c0683m) {
        C0682l c0682lM974a = C0681k.get().m974a(new File(str));
        C0681k.get().m976a(c0682lM974a);
        c0683m.m990b(c0682lM974a);
        c0683m.mo989Pc();
        return c0683m;
    }

    /* JADX INFO: renamed from: c */
    public static int m962c(Context context, String str) {
        int iM963d = m963d(context, str, "color");
        return iM963d == 0 ? iM963d : context.getResources().getColor(iM963d);
    }

    /* JADX INFO: renamed from: d */
    public static Drawable m964d(Context context, String str) {
        int iM963d = m963d(context, str, "drawable");
        if (iM963d == 0) {
            return null;
        }
        return context.getResources().getDrawable(iM963d);
    }

    /* JADX INFO: renamed from: d */
    private static int m963d(Context context, String str, String str2) {
        try {
            int identifier = context.getResources().getIdentifier(str, str2, context.getPackageName());
            if (identifier == 0) {
                Log.e("ATBluetooth.ThemeHelper", "getIdentifier: no such resource was found. resName = " + str + ", type = " + str2 + ", pkgName = " + context.getPackageName());
            }
            return identifier;
        } catch (Exception e) {
            Log.e("ATBluetooth.ThemeHelper", "getIdentifier error. resName = " + str + ", type = " + str2 + ", pkgName = " + context.getPackageName());
            e.printStackTrace();
            return 0;
        }
    }
}
