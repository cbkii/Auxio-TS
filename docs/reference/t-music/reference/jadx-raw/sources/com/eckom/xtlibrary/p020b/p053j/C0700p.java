package com.eckom.xtlibrary.p020b.p053j;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.WindowManager;

/* compiled from: ScreenUtils.java */
/* renamed from: com.eckom.xtlibrary.b.j.p */
/* loaded from: classes3.dex */
public class C0700p {
    /* renamed from: b */
    public static int[] m1033b(Context context) {
        if (context == null) {
            return new int[2];
        }
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        if (windowManager == null) {
            return new int[2];
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= 17) {
            windowManager.getDefaultDisplay().getRealSize(point);
        } else {
            windowManager.getDefaultDisplay().getSize(point);
        }
        return new int[]{point.x, point.y};
    }

    /* renamed from: c */
    public static int m1034c(Context context) {
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            return context.getResources().getDimensionPixelSize(Integer.parseInt(cls.getField("status_bar_height").get(cls.newInstance()).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
