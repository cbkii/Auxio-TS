package com.eckom.xtlibrary.twproject.activity;

import android.support.v7.graphics.Palette;

/* compiled from: XTActivity.java */
/* renamed from: com.eckom.xtlibrary.twproject.activity.c */
/* loaded from: classes3.dex */
class C0714c implements Palette.Filter {
    C0714c() {
    }

    /* renamed from: a */
    private boolean m1127a(float[] fArr) {
        return fArr[0] >= 15.0f && fArr[0] <= 345.0f;
    }

    /* renamed from: b */
    private boolean m1128b(float[] fArr) {
        return fArr[1] >= 0.2f && fArr[1] <= 0.62f;
    }

    /* renamed from: c */
    private boolean m1129c(float[] fArr) {
        return fArr[2] >= 0.05f && fArr[2] <= 0.95f;
    }

    @Override // android.support.v7.graphics.Palette.Filter
    public boolean isAllowed(int i, float[] fArr) {
        return m1127a(fArr) && m1128b(fArr) && m1129c(fArr);
    }
}
