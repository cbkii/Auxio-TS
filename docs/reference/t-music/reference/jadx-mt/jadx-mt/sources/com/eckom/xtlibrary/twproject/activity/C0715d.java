package com.eckom.xtlibrary.twproject.activity;

import android.support.v7.graphics.Palette;
import android.util.Log;
import com.android.internal.graphics.ColorUtils;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.activity.d */
/* JADX INFO: compiled from: XTActivity.java */
/* JADX INFO: loaded from: classes3.dex */
class C0715d implements Palette.PaletteAsyncListener {
    final /* synthetic */ XTActivity this$0;

    C0715d(XTActivity xTActivity) {
        this.this$0 = xTActivity;
    }

    @Override // android.support.v7.graphics.Palette.PaletteAsyncListener
    public void onGenerated(Palette palette) {
        int dominantColor;
        if (palette != null) {
            palette.getDominantSwatch();
            dominantColor = palette.getDominantColor(0);
        } else {
            dominantColor = 0;
        }
        float fCalculateLuminance = (float) ColorUtils.calculateLuminance(dominantColor);
        Log.d("XTActivity", "onGenerated: dominantColor:" + Integer.toHexString(dominantColor) + " lum:" + fCalculateLuminance);
        this.this$0.m1110I(fCalculateLuminance < 0.2f);
    }
}
