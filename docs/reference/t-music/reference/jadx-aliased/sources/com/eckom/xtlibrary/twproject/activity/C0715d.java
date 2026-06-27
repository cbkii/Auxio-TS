package com.eckom.xtlibrary.twproject.activity;

import android.support.v7.graphics.Palette;
import android.util.Log;
import com.android.internal.graphics.ColorUtils;

/* compiled from: XTActivity.java */
/* renamed from: com.eckom.xtlibrary.twproject.activity.d */
/* loaded from: classes3.dex */
class C0715d implements Palette.PaletteAsyncListener {
    final /* synthetic */ XTActivity this$0;

    C0715d(XTActivity xTActivity) {
        this.this$0 = xTActivity;
    }

    @Override // android.support.v7.graphics.Palette.PaletteAsyncListener
    public void onGenerated(Palette palette) {
        int i;
        if (palette != null) {
            palette.getDominantSwatch();
            i = palette.getDominantColor(0);
        } else {
            i = 0;
        }
        float calculateLuminance = (float) ColorUtils.calculateLuminance(i);
        Log.d("XTActivity", "onGenerated: dominantColor:" + Integer.toHexString(i) + " lum:" + calculateLuminance);
        this.this$0.m1110I(calculateLuminance < 0.2f);
    }
}
