package com.eckom.xtlibrary.p066b.p052i;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import java.io.File;

/* compiled from: ThemeHelper.java */
/* renamed from: com.eckom.xtlibrary.b.i.h */
/* loaded from: classes3.dex */
public class C0678h {
    /* renamed from: a */
    public static C0683m m961a(String str, C0683m c0683m) {
        C0682l m974a = C0681k.get().m974a(new File(str));
        C0681k.get().m976a(m974a);
        c0683m.m990b(m974a);
        c0683m.mo989Pc();
        return c0683m;
    }

    /* renamed from: c */
    public static int m962c(Context context, String str) {
        int m963d = m963d(context, str, "color");
        return m963d == 0 ? m963d : context.getResources().getColor(m963d);
    }

    /* renamed from: d */
    public static Drawable m964d(Context context, String str) {
        int m963d = m963d(context, str, "drawable");
        if (m963d == 0) {
            return null;
        }
        return context.getResources().getDrawable(m963d);
    }

    /* renamed from: d */
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
